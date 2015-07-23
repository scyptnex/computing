package p041t080;

import util.Numeral;

public class Euler057SurdFraction {

    public static void main(String[] args){
        Numeral.Fraction cur = new Numeral.Fraction(1, 1);
        int count = 0;
        for(int i=0; i<1000; i++){
            cur = cur.add(new Numeral.Fraction(1, 1));
            cur = new Numeral.Fraction(cur.denominator, cur.numerator).add(new Numeral.Fraction(1, 1));
            System.out.println(cur);
            if(cur.numerator.toString().length() > cur.denominator.toString().length()){
                count++;
            }
        }
        System.out.println(count);
    }

}
