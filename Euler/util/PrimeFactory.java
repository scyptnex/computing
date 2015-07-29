package util;

import java.util.*;

/**
 * Global static implementation of the prime generator factory
 * will always try to answer if a given number is prime
 */
public class PrimeFactory {

    private static BitSet sieve = null;
    private static int curSize = 0;

    /**
     * Increase the size of the prime factory.
     * @param upToMax The largest number that the new factory must be able to answer without growing
     *                The new factory may be larger than upToMax.
     */
    public static void grow(int upToMax){
        if(curSize > upToMax) return;
        if(curSize<1){
            curSize = upToMax+(upToMax%2);//force an even number
        } else {
            while(curSize<upToMax) curSize*=2;//always an even number
        }
        sieve = new BitSet(curSize/2);
        for (int i = 3; i*i <= curSize; i += 2) {
            if (sieve.get((i-3)/2))
                continue;

            // We increment by 2*i to skip even multiples of i
            for (int multiple_i = i*i; multiple_i <= curSize; multiple_i += 2*i)
                sieve.set((multiple_i - 3) / 2);
        }
    }

    /**
     * @param n The number we wish to check for primality.
     * @return True if n is prime, otherwise false
     */
    public static boolean isPrime(int n){
        if(n <= 2) return n==2;
        if(n%2 == 0) return false;
        grow(n);//make sure the sieve is big enough
        return !sieve.get((n-3)/2);
    }

    /**
     * @param cur the current number (not necessarily prime)
     * @return some prime > cur
     */
    public static int getNextHigherPrime(int cur){
        if(cur<2) return 2;
        cur += 1+(cur%2);//the next higher odd number
        while(!isPrime(cur)) cur += 2;
        return cur;
    }

    /**
     * Get a set of prime divisors for a number
     * @param n The number we want the prime divisors of
     * @return a set containing all the PRIME numbers (including 1) that divide n integrally.
     */
    public static Set<Integer> getPrimeDivisors(int n){
        Set<Integer> ret = new HashSet<>();
        ret.add(1);
        for(int p : allPrimesInRange(2, 1+(int)Math.floor(Math.sqrt(n)))){
            if(n == 1) break;
            while(n%p == 0){
                n /= p;
                ret.add(p);
            }
        }
        ret.add(n);
        return ret;
    }

    /**
     * Get an iterator for in-order prime numbers
     * @param startInclusive <= the first prime
     * @param endExclusive > the last prime (strictly)
     * @return An iterator listing (in order) all the primes p: startInclusive <= p < endExclusive
     */
    public static Iterable<Integer> allPrimesInRange(final int startInclusive, final int endExclusive) {
        grow(endExclusive);
        final int first = getNextHigherPrime(Math.min(1, startInclusive-1));
        return () -> new Iterator<Integer>() {
            int cur = first;
            @Override
            public boolean hasNext() {
                return cur != -1;
            }

            @Override
            public Integer next() {
                final int limit = endExclusive;
                //start at next higher odd number
                int higher = -1;
                for(int nxt = cur + 1 +(cur%2); nxt < limit; nxt += 2){
                    if(!sieve.get((nxt-3)/2)){
                        higher = nxt;
                        break;
                    }
                }
                int ret = cur;
                cur = higher;
                return ret;
            }
        };
    }
}
