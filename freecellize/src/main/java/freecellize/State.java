package freecellize;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;
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

    public void makeMove(String move){
        // first check for a stack swap
        if(move.matches("[0-9]*")){
            throw new RuntimeException("TODO");
        }
        // otherwise retrieve and emplace
        emplace(move.charAt(1), retrieve(move.charAt(0)));
    }

    //uses triangle logic, 2c->h is an auto move if ah and ad are home already
    public String[] getAutoMoves(){
        return null;
    }

    public void printForSolver(PrintStream os){
        columns.stream()
                .map(s -> s.stream().collect(Collectors.joining(" ")))
                .forEachOrdered(os::println);
    }

}
