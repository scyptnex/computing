package p041t080;

import util.Primer;

import java.util.HashSet;
import java.util.Set;

public class Euler058PrimeSpiral {

    public static int MAX = 1000000000;//five hundred million

    public static void main(String[]args){
        if(args.length == 1){
            MAX = Integer.parseInt(args[0]);
        }
        Primer.Sieve primeSet = new Primer.Sieve(MAX);
        int sl = 1;
        int nonPrimes = 1;
        int primes = 0;
        int cur = 1;
        while(true){
            sl += 2;
            for(int i=0; i<4; i++){
                cur += sl - 1;
                if(primeSet.isPrime(cur)) primes++;
                else nonPrimes++;
            }
            double rat = (double)primes/(double)(primes + nonPrimes);
            System.out.println(sl + ", " + primes + "/" + (nonPrimes+primes) + " = " + rat);
            if(rat <= 0.1) break;
        }
    }

}
