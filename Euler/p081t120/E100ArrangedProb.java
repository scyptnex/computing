package p081t120;


import util.NumberTheory;

import java.util.function.LongBinaryOperator;

public class E100ArrangedProb {

    public static void main(String[] args){
        long sq0 = 0;
        long sq1 = 1;
        long tr0 = 0;
        long tr1 = 1;
        LongBinaryOperator blu = (t,s)->((2*t + 1)+(2*s+1))/2;
        while(sq1 + blu.applyAsLong(sq1, tr1) < 1000000000000l){
            long newS = 6*sq1 - sq0;
            long newT = 6*tr1 - tr0 + 2;
            sq0 = sq1;
            sq1 = newS;
            tr0 = tr1;
            tr1 = newT;
            long rm = sq1;
            long bl = blu.applyAsLong(tr1, sq1);
            System.out.println((bl + rm) + " - " + rm + " - " + bl);
        }
        System.out.println(blu.applyAsLong(tr1, sq1));
    }

}
