package p041t080;

import java.util.ArrayList;

public class Euler046Goldbach {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Long> primes = util.Primer.primeSieve(100000000);
		ArrayList<Long> dsq = new ArrayList<Long>();
		long cur = 1;
		while(dsq.size() < primes.size()){
			dsq.add(2*cur*cur);
			cur++;
		}
	}

}
