package p081t120;

import util.Collect;
import util.Numeral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class E084Monopoly {

    public static final List<String> board = Arrays.asList((
            "GO   A1 CC1 A2  T1 R1 B1  CH1 B2 B3 " +
                    "JAIL c1 u1  c2  c3 r2 d1  cc2 d2 d3 " +
                    "fp   e1 ch2 e2  e3 r3 f1  f2  u2 f3 " +
                    "g2j  g1 g2  cc3 g3 r4 ch3 h1  t2 h2").toUpperCase().split(" +"));

    public static final int S_GO = board.indexOf("GO");
    public static final int S_C1 = board.indexOf("C1");
    public static final int S_E3 = board.indexOf("E3");
    public static final int S_H2 = board.indexOf("H2");
    public static final int S_R1 = board.indexOf("R1");
    public static final int S_G2J = board.indexOf("G2J");
    public static final int S_JAIL = board.indexOf("JAIL");

    public static void main(String[] args){
        List<Numeral.Fraction> turn = null;
        for(int i=0; i<100; i++) turn = takeTurn(turn);
        System.out.println(turn);
        List<Collect.Pair<Numeral.Fraction, Integer>> sorts = Collect.dualSort(turn);
        sorts.stream()
                .map( i -> new Collect.Pair<>(i.first.doubleApprox(10), board.get(i.second)))
                .forEach(System.out::println);
        Arrays.asList(1, 2, 3).stream()
                .map(i -> "00" + sorts.get(board.size()-i).second)
                .map(s -> s.substring(s.length()-2))
                .forEach(System.out::print);
        System.out.println();
    }

    public static List<Numeral.Fraction> takeTurn(List<Numeral.Fraction> lastState){
        List<Numeral.Fraction> ret = Stream.generate(() -> new Numeral.Fraction(0)).limit(board.size()).collect(Collectors.toList());
        if(lastState == null){
            ret.set(0, new Numeral.Fraction(1));
            return ret;
        }
        //make moves
        int DSIDE = 4;
        int dprob = DSIDE*DSIDE;
        for(int d1 = 1; d1<=DSIDE; d1++){
            for(int d2=1; d2<=DSIDE; d2++){
                for(int s=0; s<board.size(); s++){
                    int newIdx = (s + d1 + d2)%board.size();
                    ret.set(newIdx, lastState.get(s).multiply(new Numeral.Fraction(1, dprob)).add(ret.get(newIdx)));
                }
            }
        }
        // triple double roll goes to jail (approx)
        for(int s=0; s<board.size(); s++){
            Numeral.Fraction jprob = ret.get(s).multiply(new Numeral.Fraction(1, DSIDE*DSIDE*DSIDE));
            ret.set(s, ret.get(s).subtract(jprob));
            ret.set(S_JAIL, ret.get(S_JAIL).add(jprob));
        }
        //chance first
        for(int s=0; s<board.size(); s++) if(board.get(s).startsWith("CH")){
            //backwards 3 and next railway, utility
            int nextR = s+1;
            while(!board.get(nextR).startsWith("R")) nextR = (nextR+1)%board.size();
            int nextU = s+1;
            while(!board.get(nextU).startsWith("U")) nextU = (nextU+1)%board.size();
            int back3 = (s + board.size() - 3)%board.size();
            //change the probability
            ret.set(S_JAIL, ret.get(S_JAIL).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(S_GO, ret.get(S_GO).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(S_C1, ret.get(S_C1).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(S_E3, ret.get(S_E3).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(S_H2, ret.get(S_H2).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(S_R1, ret.get(S_R1).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(nextR, ret.get(nextR).add(ret.get(s).multiply(new Numeral.Fraction(2, 16))));
            ret.set(nextU, ret.get(nextU).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(back3, ret.get(back3).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(s, ret.get(s).multiply(new Numeral.Fraction(6, 16)));
        }
        //community chest
        for(int s=0; s<board.size(); s++) if(board.get(s).startsWith("CC")){
            ret.set(S_JAIL, ret.get(S_JAIL).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(S_GO, ret.get(S_GO).add(ret.get(s).multiply(new Numeral.Fraction(1, 16))));
            ret.set(s, ret.get(s).multiply(new Numeral.Fraction(14, 16)));
        }
        //finally, go to jail
        ret.set(S_JAIL, ret.get(S_JAIL).add(ret.get(S_G2J)));
        ret.set(S_G2J, new Numeral.Fraction(0, 1));
        return ret;
    }

}
