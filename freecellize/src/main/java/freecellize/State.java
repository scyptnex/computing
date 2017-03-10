package freecellize;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class State {

    final String[] freecells;
    final String[] homes;
    final ArrayList<Stack<String>> columns;

    public State(String[][] beginState){
        freecells = new String[]{null, null, null, null};
        homes = new String[]{null, null, null, null};
        columns = new ArrayList<>();
        for(int col=0; col<8; col++){
            columns.add(new Stack<>());
            for(int row=0; row<7; row++) if (row < 6 || col < 4) {
                columns.get(col).push(beginState[col][row]);
            }
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
        sb = new StringBuilder(sb.toString().trim());
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

    public void makeMove(String move){
        // first check for a stack swap
        Stack<String> hand = new Stack<>();
        if(move.length() > 2){
            for(int i=1; i<Long.parseLong(move.substring(3), 16); i++) hand.push(retrieve(move.charAt(0)));
        } else if(move.matches("[0-9]*")){
            while(!columns.get(move.charAt(0)-'1').empty() &&
                    !columns.get(move.charAt(1)-'1').empty() &&
                    faceVal(move.charAt(0)-'1') < faceVal(move.charAt(1)-'1') - 1){
                //System.out.print(faceVal(move.charAt(0)-'1') + " - " + faceVal(move.charAt(1)-'1') + " , ");
                hand.push(retrieve(move.charAt(0)));
                //System.out.println(hand.peek());
            }
        }
        // otherwise retrieve and emplace
        hand.push(retrieve(move.charAt(0)));
        while(!hand.empty()){
            emplace(move.charAt(1), hand.pop());
        }
    }

    //uses triangle logic, 2c->h is an auto move if ah and ad are home already
    public List<String> getAutoMoves(){
        int[] bottoms = new int[]{0,0,0,0};
        for(String c : homes) if(c != null){
            bottoms[getSuit(c)] = faceVal(c);
        }
        List<String> ret = new ArrayList<>();
        for(int src=0; src<12; src++){
            String check = src < 8 ? (columns.get(src).size() > 0 ? columns.get(src).peek() : null) : freecells[src-8];
            if(check != null){
                int mysuit = getSuit(check);
                int myval = faceVal(check);
                if(bottoms[mysuit] == myval-1 && bottoms[mysuit^0b10] >= myval-1 && bottoms[mysuit^0b11] >= myval-1){
                    ret.add( (char)(src < 8 ? src + '1' : src - 8 + 'a') + "h");
                }
            }
        }
        return ret;
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
