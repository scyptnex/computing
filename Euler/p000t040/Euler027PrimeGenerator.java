package p000t040;

import java.util.ArrayList;

public class Euler027PrimeGenerator {

	public static void main(String[] args) {
		ArrayList<Long> primes = util.Primer.primeSieve(10000000);
		genCheck(1, 41, primes);
		genCheck(-79, 1601, primes);
		System.exit(0);
		int max = 0;
		int ma = 0;
		int mb = 0;
		for (int a = -999; a < 1000; a++) {
			for (int b = -999; b < 1000; b++) {
				int cur = genCheck(a, b, primes);
			}
		}
	}

	public static int genCheck(long a, long b, ArrayList<Long> primes) {
		int cur = 0;
		int pos = -1;
		boolean going = true;
		while (going) {
			long p = Math.abs(cur * cur + cur * a + b);
			if (pos == -1) {
				pos = primes.indexOf(p);
				if(pos == -1) break;
			} else {
				if(primes.get(pos+1) == p){
					pos++;
				} else if(primes.get(pos-1) == p){
					pos--;
				} else{
					System.out.println(p + " - " + primes.get(pos-1) + " - " + primes.get(pos+1));
					break;
				}
			}
			System.out.println(p);
			cur++;
		}
		return cur;
	}

}
