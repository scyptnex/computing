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
        int sum = 0;
        for(char c : (getFrac(0, 99).numerator + "").toCharArray()){
            System.out.print(c);
            sum += (c-'0');
        }
        System.out.println( " = " + sum);
    }

    public static Numeral.Fraction getFrac(int targ, int convergent){
        Numeral.Fraction ret = new Numeral.Fraction(getEConvergent(targ), 1);
        if(targ != convergent) {
            Numeral.Fraction sub = getFrac(targ + 1, convergent);
            ret =  new Numeral.Fraction(sub.denominator, sub.numerator).add(ret);
        }
        return ret;
    }

    public static int getEConvergent(int idx){
        if(idx == 0) return 2;
        idx += 2;
        if(idx % 3 == 1) return (idx/3)*2;
        return 1;
    }

}
