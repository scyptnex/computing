package p041t080;

import util.PrimeFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Euler077SumPrimes {

    public static final long DIVIS = Integer.MAX_VALUE/2;
    public static ArrayList<Integer> primeBank = new ArrayList<>();

    public static void main(String[] args){
        primeBank.add(2);
        primeBank.add(3);
        primeBank.add(5);
        primeBank.add(7);
        primeBank.add(11);
        int cur = 10;
        while(true){
            if(cur >= primeBank.get(primeBank.size()-1)){
                primeBank.add(PrimeFactory.getNextHigherPrime(primeBank.get(primeBank.size()-1)));
                System.out.println("Pb now " + primeBank.get(primeBank.size()-1) + ", due to " + cur);
            }
            if(waysToMake(cur, primeBank.size()-2) > 5000) break;
            cur++;
        }
        System.out.println(cur);
    }

    public static final HashMap<Long, Long> cache = new HashMap<>();
    public static long waysToMake(int sum, int usingOnlyPrimesBelowIndex){
        long k = sum*DIVIS + usingOnlyPrimesBelowIndex;
        if(!cache.containsKey(k)){
            cache.put(k, waysToMakeRecursive(sum, usingOnlyPrimesBelowIndex));
        }
        return cache.get(k);
    }

    public static long waysToMakeRecursive(int sum, int usingOnlyPrimesBelowIndex){
        if(usingOnlyPrimesBelowIndex < 0) return 0;
        long count = 0;
        for(int i=0; i<=sum; i+=primeBank.get(usingOnlyPrimesBelowIndex)){
            if( i == sum) count++;
            else count += waysToMake(sum-i, usingOnlyPrimesBelowIndex-1);
        }
        return count;
    }

}
