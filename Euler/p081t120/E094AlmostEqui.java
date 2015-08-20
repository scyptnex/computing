package p081t120;

import util.Collect;
import util.NumberTheory;
import util.Numeral;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class E094AlmostEqui {

    public static void main(String[] args){
        System.out.println(IntStream.rangeClosed(2, 1000000000 / 3 + 50)
                .filter(i -> i % 2 == 1)
                .boxed()
                .flatMap(i -> IntStream.of(i - 1, i + 1).mapToObj(j -> new long[]{i, j / 2}))
                        //.filter(E094AlmostEqui::integralArea)
                .map(ia -> new Collect.Pair<>(ia, ia[1] * sqrt((ia[0] * ia[0]) - (ia[1] * ia[1]))))
                .filter(p -> p.second > 0)
                        //.filter(p -> Math.floor(p.second) == Math.ceil(p.second))
                .peek(p -> System.out.println(p.first[0] + ", " + p.first[1] + " - " + p.second))
                .mapToLong(p -> (2 * (p.first[0] + p.first[1])))
                .filter(l -> l <= 1000000000)
                .boxed()
                .reduce(BigInteger.ZERO, (a, b) -> a.add(BigInteger.valueOf(b)), BigInteger::add));
    }

    public static long sqrt(long res){
        long h = res & 0xF;
        if (h > 9) return -1;
        Numeral.Fraction gues = NumberTheory.newtonSquareRoot(res, 10);
        long guess = gues.numerator.divide(gues.denominator).longValue();
        for(long g=guess; g*g >= res; g--){
            if(g*g == res){
                System.out.println(res + " : " + g + ", " + guess + " - " + gues);
                return g;
            }
        }
        for(long g=guess+1; g*g<=res; g++){
            if(g*g == res){
                System.out.println(res + " : " + g + ", " + guess + " - " + gues);
                return g;
            }
        }
        return -1;
    }

}
