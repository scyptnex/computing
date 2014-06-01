package p001t040;

import java.util.HashSet;

import util.Primer;

public class Euler037TruncatablePrimes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashSet<Long> primes = new HashSet<Long>(Primer.primeSieve(100000000));
		long count = 0;
		for(long prime : primes) if(prime > 10){
			String pl = prime + "";
			boolean isTrunc = true;
			for(int i=1; i<pl.length(); i++){
				if(!primes.contains(Long.parseLong(pl.substring(i)))){
					isTrunc = false;
					break;
				}
				if(!primes.contains(Long.parseLong(pl.substring(0, i)))){
					isTrunc = false;
					break;
				}
			}
			if(isTrunc){
				count += prime;
			}
		}
		System.out.println(count);
	}

}
