package p041t080;

import util.Numeral;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Euler080RootDigits {

    public static void main(String[] args){
        System.out.println(IntStream.rangeClosed(1, 100)
                .filter(i -> i != (int) Math.floor(Math.sqrt(i)) * (int) Math.floor(Math.sqrt(i)))
                .map(i -> rationalApprox(i, 101)
                        .toString()
                        .chars()
                        .limit(100)
                        .map(c -> c-'0')
                        .sum())
                .sum());
    }

    public static BigInteger rationalApprox(int squa, int accuracy){
        if(accuracy <= 0) return BigInteger.valueOf((int)Math.floor(Math.sqrt(squa)));
        BigInteger cur = rationalApprox(squa, accuracy - 1);
        cur = cur.multiply(BigInteger.TEN);
        BigInteger denom = BigInteger.TEN.pow(2*accuracy);
        while(cur.multiply(cur).compareTo(BigInteger.valueOf(squa).multiply(denom)) < 0){
            //System.out.println(cur.multiply(cur) + " vs " + BigInteger.valueOf(squa).multiply(denom));
            cur = cur.add(BigInteger.ONE);
        }
        cur = cur.subtract(BigInteger.ONE);
        return cur;
    }

}
