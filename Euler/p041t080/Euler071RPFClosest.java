package p041t080;

import util.Numeral;

import java.util.stream.IntStream;

public class Euler071RPFClosest {

    public static void main(String[] args){
        IntStream.rangeClosed(2, 1000000)
                .filter(i -> i != 7)
                .mapToObj(denom -> {
                    int ub = (int) Math.floor((denom * 3.0) / 7.0);
                    while (ub > 0 && Numeral.gcd(ub, denom) != 1) ub--;
                    return new Numeral.Fraction(ub, denom);
                })
                .max(Numeral.Fraction::compareTo)
                .ifPresent(f -> System.out.println(f + "\n" + f.numerator));
    }

}
