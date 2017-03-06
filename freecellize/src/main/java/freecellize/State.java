package freecellize;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
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

    public void printForSolver(PrintStream os){
        columns.stream()
                .map(s -> s.stream().collect(Collectors.joining(" ")))
                .forEachOrdered(os::println);
    }

}
