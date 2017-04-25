package freecellize;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class State {

    final String[] freecells;
    final String[] homes;
    final ArrayList<Stack<String>> columns;

    public State() {
        freecells = new String[]{null, null, null, null};
        homes = new String[]{null, null, null, null};
        columns = new ArrayList<>();
        for(int col=0; col<8; col++) {
            columns.add(new Stack<>());
        }
    }

    public State(String[][] beginState){
        this();
        for(int col=0; col<8; col++){
            for(int row=0; row<7; row++) if (row < 6 || col < 4) {
                columns.get(col).push(beginState[col][row]);
            }
        }
    }

    public State(State s){
        freecells = Arrays.copyOf(s.freecells, s.freecells.length);
        homes = Arrays.copyOf(s.homes, s.homes.length);
        columns = new ArrayList<>();
        for(Stack<String> col : s.columns){
            Stack<String> sta = new Stack<>();
            for(String card : col){
                sta.add(card);
            }
            columns.add(sta);
        }
    }

    public String dump(){
        StringBuilder sb = new StringBuilder();
        for(String[] x : Arrays.asList(freecells, homes)) {
            for (String s : x) {
                sb.append(s == null ? "---" : String.format("%3s", s));
                sb.append(" ");
            }
            sb.append("  ");
        }
        sb = new StringBuilder(sb.toString());
        int longest = columns.stream().map(Vector::size).max(Integer::compareTo).get();
        for(int r=0; r<longest; r++){
            sb.append("\n");
            for(Stack<String> s : columns){
                sb.append(" ");
                sb.append(r < s.size() ? String.format("%3s", s.get(r)) : "   ");
            }
        }
        return sb.toString();
    }

    public String retrieve(char loc){
        if(loc >= '1' && loc <= '8'){
            return columns.get(loc - '1').pop();
        } else {
            String ret = freecells[loc - 'a'];
            freecells[loc - 'a'] = null;
            return ret;
        }
    }

    public void emplace(char loc, String card){
        if(loc >= '1' && loc <= '8'){
            //TODO check the card can go there
            columns.get(loc - '1').push(card);
        } else if (loc >= 'a' && loc <= 'd'){
            //TODO assert cell is free
            freecells[loc - 'a'] = card;
        } else {
            homes[getHome(card)] = card;
        }
    }

    public int getHome(String c){
        for(int i=0; i<4; i++){
            if(homes[i] == null) return i;
            else if(homes[i].charAt(homes[i].length()-1) == c.charAt(c.length()-1)) return i;
        }
        return -1;
    }

    //returns 1-13 of the card's face value
    public int faceVal(int col){
        return faceVal(columns.get(col).peek());
    }

    public static int faceVal(String card){
        char c = card.charAt(0);
        switch (c){
            case 'a' : return 1;
            case 'j' : return 11;
            case 'q' : return 12;
            case 'k' : return 13;
            default  : return c == '1' ? 10 : c-'0';
        }
    }

    public String topCol(int col){
        return (col+1) + "" + columns.get(col).size();
    }

    // move using only freecells
    // move to a non-empty stack using empty intermediates
    // move to an empty stack using empty intermediates
    // move using compound empty stacks
    public List<String> planStackMove(char src, char dst, int amt){
        List<String> emptyStacks = IntStream.range(0, columns.size())
                .filter(i -> columns.get(i).empty())
                .filter(i -> i != dst-'1')
                .mapToObj(i -> (i+1) + "")
                .collect(Collectors.toList());
        List<String> emptyFrees = IntStream.range(0, 4)
                .filter(i -> freecells[i] == null)
                .mapToObj(i -> (char)(i+'a') + "")
                .collect(Collectors.toList());
        List<String> moves = new ArrayList<>();
        if(emptyStacks.size() <= 1 && amt > (emptyFrees.size()+1)*(emptyStacks.size()+1)){
            System.err.println(this.dump());
            throw new RuntimeException("Impossible stack move " + src + " " + dst);
        }
        if(amt > (emptyFrees.size()+1)*(emptyStacks.size()+1)){
            long maxCards = (emptyFrees.size()+1)*emptyStacks.size();
            State copy = new State(this);
            List<Character> pre = copy.makeMove(src + emptyStacks.get(0) + "x" + Long.toHexString(maxCards));
            List<Character> mid = copy.makeMove(src + "" + dst + "x" + Long.toHexString(amt-maxCards));
            List<Character> post = copy.makeMove(emptyStacks.get(0) + dst + "x" + Long.toHexString(maxCards));
            List<Character> all = Stream.of(pre, mid, post).flatMap(Collection::stream).collect(Collectors.toList());
            String s = "";
            List<String> ret = new ArrayList<>();
            for(char c : all){
                s = s + c;
                if(s.length() >= 2){
                    ret.add(s);
                    s = "";
                }
            }
            return ret;
        } else {
            int towards = 0;
            Stack<int[]> stackMoves = new Stack<>();
            while (amt > emptyFrees.size() + 1) {
                moves.addAll(planStackMove(src, emptyStacks.get(towards).charAt(0), emptyFrees.size() + 1));
                stackMoves.push(new int[]{emptyStacks.get(towards).charAt(0), dst, emptyFrees.size() + 1});
                amt -= (emptyFrees.size() + 1);
                towards++;
            }
            Stack<String> freeLocs = new Stack<>();
            for (int i = 0; i < amt - 1; i++) {
                freeLocs.push(emptyFrees.get(i));
                moves.add(src + emptyFrees.get(i));
            }
            moves.add(src + "" + dst);
            while (!freeLocs.empty()) {
                moves.add(freeLocs.pop() + dst);
            }
            while (!stackMoves.empty()) {
                int[] sm = stackMoves.pop();
                moves.addAll(planStackMove((char) sm[0], (char) sm[1], sm[2]));
            }
            return moves;
        }
    }

    public List<Character> doMove(char from, char to){
        String card = retrieve(from);
        emplace(to, card);
        if(to == 'h'){
            to = (char)(getHome(card) + 'h');
        }
        return Arrays.asList(from, to);
    }

    /**
     * Returns the list of click-locations that are needed to perform a move
     */
    public List<Character> makeMove(String move){
        List<String> unitMoves;
        if(move.length() > 2){
            unitMoves = planStackMove(move.charAt(0), move.charAt(1), (int)Long.parseLong(move.substring(3), 16));
        } else if(move.matches("[0-9]*") &&
                !columns.get(move.charAt(1)-'1').empty() &&
                faceVal(move.charAt(0)-'1') < faceVal(move.charAt(1)-'1') - 1) {
            unitMoves = planStackMove(move.charAt(0), move.charAt(1), faceVal(move.charAt(1)-'1') - faceVal(move.charAt(0)-'1'));
        } else {
            unitMoves = Collections.singletonList(move);
        }
        List<Character> clicks = new ArrayList<>();
        for(String m : unitMoves){
            //System.out.println("__ " + m);
            int fc = -1;
            for(int i=0; i<4; i++) if (freecells[i] == null){
                fc = i;
                break;
            }
            //TODO 8559 ghost cards leave the freecells forcing this temporary to be necessary
            if(fc != -1 && m.matches("[0-9]*") && columns.get(m.charAt(1)-'1').empty()){
                //if you try to move a stack to an empty when there are freecells available, a popup will
                // appear, to avoid this when moving a single card to an empty we first put it in the freecell
                clicks.addAll(doMove(m.charAt(0), (char)(fc + 'a')));
                clicks.addAll(doMove((char)(fc + 'a'), m.charAt(1)));
            } else {
                clicks.addAll(doMove(m.charAt(0), m.charAt(1)));
            }
        }
        return clicks;
    }


    /**
     * uses triangle logic, 2c->h is an auto move if ah and ad are home already
     * ALSO 2 auto moves, even though this requires 1 induction on the triangle logic!!!
     * returns -1 if no auto move exists
     */
    public int getAutoMoveSrc(){
        int[] bottoms = new int[]{0,0,0,0};
        for(String c : homes) if(c != null){
            bottoms[getSuit(c)] = faceVal(c);
        }
        for(int src=0; src<12; src++){
            String check = src < 8 ? (columns.get(src).size() > 0 ? columns.get(src).peek() : null) : freecells[src-8];
            if(check != null){
                int mysuit = getSuit(check);
                int myval = faceVal(check);
                if(bottoms[mysuit] == myval-1 && (
                        (myval <= 2) || (bottoms[mysuit^0b10] >= myval-1 && bottoms[mysuit^0b11] >= myval-1))){
                    return src;
                }
            }
        }
        return -1;
    }
    public String getAutoMoveOrNull(){
        int src = getAutoMoveSrc();
        if(src == -1) return null;
        return (char)(src < 8 ? src + '1' : src - 8 + 'a') + "h";
    }
    /**
     * 0-7 are columns, 8-11 are freecells
     */
    public int[] autoMoveGhosts(){
        int[] ghostPieces = new int[12];
        Arrays.setAll(ghostPieces, i->0);
        new State(this).makeAutoMoveSequence().forEach(am -> {
            int idx = am.charAt(0);
            idx = idx>='1' && idx<='8' ? idx-'1' : idx+8-'a';
            ghostPieces[idx]++;
            //to ensure that a ghost auto-moved piece winds up in its real home, set the homes to have a stub
            if(idx < 8 && homes[getHome(columns.get(idx).peek())] == null){
                String c = columns.get(idx).peek();
                c = "0" + c.substring(c.length()-1);
                homes[getHome(c)] = c;
            }
        });
        return ghostPieces;
    }

    public List<String> makeAutoMoveSequence(){
        String am;
        List<String> ret = new ArrayList<>();
        while((am = getAutoMoveOrNull()) != null){
            makeMove(am);
            ret.add(am);
        }
        return ret;
    }

    public boolean hasVictoryRun(){
        State clone = new State(this);
        clone.makeAutoMoveSequence();
        return clone.columns.stream().allMatch(Vector::isEmpty);
    }

    public void printForSolver(PrintStream os){
        columns.stream()
                .map(s -> s.stream().collect(Collectors.joining(" ")))
                .forEachOrdered(os::println);
    }

    // 500 order
    public static int getSuit(String card){
        switch (card.charAt(card.length()-1)){
            case 's' : return 0;
            case 'c' : return 1;
            case 'd' : return 2;
            default  : return 3;
        }
    }

}
