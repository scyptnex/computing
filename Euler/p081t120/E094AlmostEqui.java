package p081t120;

import util.NumberTheory;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class E094AlmostEqui {

    public static final int SIZ = 1000000000;

    public static void main(String[] args){
        long t = System.currentTimeMillis();
        BigInteger tot = IntStream.rangeClosed(2, SIZ / 3 + 50)
                .filter(i -> i % 2 == 1)
                .boxed()
                .flatMap(i -> IntStream.of(i - 1, i + 1).mapToObj(j -> new long[]{i, j / 2}))
                .filter(ia -> NumberTheory.integralSquareRoot((ia[0] * ia[0]) - (ia[1] * ia[1])) != -1)
                .peek(ia -> System.out.println(ia[0] + "," + ia[1]))
                .mapToLong(ia -> (2 * (ia[0] + ia[1])))
                .filter(l -> l <= 1000000000)
                .boxed()
                .reduce(BigInteger.ZERO, (a, b) -> a.add(BigInteger.valueOf(b)), BigInteger::add);
        System.out.println("Time: " + (System.currentTimeMillis() - t));
        System.out.println(tot);
    }

}
