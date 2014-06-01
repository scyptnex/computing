package p001t040;

import java.util.ArrayList;

public class Euler010SumPrimesBelow {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Long> sieve = util.Primer.primeSieve(2000000);
		long sm = 0;
		for(Long l : sieve){
			sm += l;
		}
		System.out.println(sm);
	}

}
