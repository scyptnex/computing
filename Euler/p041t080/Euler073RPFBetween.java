package p041t080;

import util.Numeral;

import java.util.stream.IntStream;

public class Euler073RPFBetween {

    public static void main(String[] args){
        System.out.println(IntStream.rangeClosed(4, 12000)
                .map(denom -> {
                    int count = 0;
                    for (int i = (int) Math.ceil(denom / 3.0); i < denom / 2.0; i++) if (Numeral.gcd(i, denom) == 1){
                        System.out.println(i + "/" + denom);
                        count++;
                    }
                    return count;
                })
                .sum());
    }

}
