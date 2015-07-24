package p041t080;

import util.Numeral;

/**
 * https://projecteuler.net/problem=65
 */
public class Euler065EConvergents {

    public static void main(String[] args){
        for(int i=0; i<10; i++){
            System.out.println(getEConvergent(i) + " - " + getFrac(0, i));
        }
    }

    public static Numeral.Fraction getFrac(int targ, int convergent){
        if(targ == convergent) return new Numeral.Fraction(getEConvergent(targ), 1);
        // targ < convergent
        Numeral.Fraction sub = getFrac(targ, convergent - 1);
        return new Numeral.Fraction(sub.denominator, sub.numerator).add(new Numeral.Fraction(getEConvergent(convergent), 1));
    }

    public static int getEConvergent(int idx){
        if(idx == 0) return 2;
        idx += 2;
        if(idx % 3 == 1) return (idx/3)*2;
        return 1;
    }

}
