package p041t080;

import util.NumberTheory;
import util.PrimeFactory;

import java.math.BigInteger;
import java.util.stream.IntStream;

public class Euler072RPFCount {

    public static void main(String[] args){
        PrimeFactory.grow(2000000);
        BigInteger c = IntStream.rangeClosed(2, 1000000)
                .map(NumberTheory::totient)
                .boxed()
                .reduce(BigInteger.ZERO, (BigInteger bi, Integer in) -> bi.add(BigInteger.valueOf(in)), BigInteger::add);
        System.out.println(c);
    }

}
