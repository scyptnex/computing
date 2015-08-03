package p041t080;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Euler076SumList {

    public static void main(String[] args){
        System.out.println(cns(100, 99));
    }

    private static final Map<Long, BigInteger> cache = new HashMap<>();
    public static BigInteger cns(int summingTo, int digi){
        Long k = (long)summingTo*((long)Integer.MAX_VALUE) + (long)digi;
        if(!cache.containsKey(k)){
            cache.put(k, cnsUnCached(summingTo, digi));
        }
        return cache.get(k);
    }

    public static BigInteger cnsUnCached(int summingTo, int digi){
        if(digi == 1) return BigInteger.ONE;//there is 1 way to make summingto out of 1s
        BigInteger count = BigInteger.ZERO;
        for(int i=0; i<=summingTo; i+=digi){
            if(i == summingTo) count = count.add(BigInteger.ONE);
            else { //i < summingto
                count = count.add(cns(summingTo-i, digi-1));
            }
        }
        return count;
    }

}
