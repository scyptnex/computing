package p041t080;

import util.NumberTheory;
import util.Numeral;

import java.math.BigInteger;
import java.util.List;

public class Euler066MinDiophantine {

    public static void main(String[] args){
        BigInteger max = BigInteger.ZERO;
        int g = -1;
        for(int i=2; i<=1000; i++) if(Math.sqrt(i)*Math.floor(Math.sqrt(i))!= (double)i){
            BigInteger xd = getXDio(i);
            System.out.println(i + " - " + xd);
            if(xd.compareTo(max) > 0){
                max = xd;
                g = i;
            }
        }
        System.out.println(g);
    }

    public static BigInteger getXDio(int d){
        List<Integer> contfrac = NumberTheory.surdCF(d);
        int r = contfrac.size()-2;
        Numeral.Fraction fr = r%2 == 0 ? getFrac(0, 1+2*r, contfrac) : getFrac(0, r, contfrac);
        return fr.numerator;
    }

    public static Numeral.Fraction getFrac(int targ, int convergent, List<Integer> cfe){
        int cft = cfe.get(targ < cfe.size() ? targ : 1+(targ - cfe.size())%(cfe.size()-1));
        Numeral.Fraction ret = new Numeral.Fraction(cft, 1);
        if(targ != convergent) {
            Numeral.Fraction sub = getFrac(targ + 1, convergent, cfe);
            ret =  new Numeral.Fraction(sub.denominator, sub.numerator).add(ret);
        }
        return ret;
    }
}
