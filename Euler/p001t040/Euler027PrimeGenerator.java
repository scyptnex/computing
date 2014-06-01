package p001t040;

import java.util.HashSet;
import java.util.Set;

public class Euler027PrimeGenerator {

	public static void main(String[] args) {
		Set<Long> primes = new HashSet<Long>(util.Primer.primeSieve(10000000));
		long max = 0;
		int ma = 0;
		int mb = 0;
		for (int a = -999; a < 1000; a++) {
			for (int b = -999; b < 1000; b++) {
				long cur = genCheck(a, b, primes);
				if(cur > max){
					max = cur;
					ma = a;
					mb = b;
				}
			}
		}
		System.out.println(ma*mb);
	}
	
	public static long genCheck(long a, long b, Set<Long> primes) {
		long cur = 0;
		boolean going = true;
		while (going) {
			long p = Math.abs(cur * cur + cur * a + b);
			if(!primes.contains(p)){
				break;
			}
			cur++;
		}
		return cur;
	}

}
