package p041t080;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class Euler047FourPrimes {

	public static ArrayList<Long> primes;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		primes = util.Primer.primeSieve(100000000);
		System.out.println(primes.size());
		long cur = 3;
		long count = 0;
		while(true){
			if(residue(cur).size() == 4){
				count++;
				System.out.println(cur);
				if(count == 4) break;
			}
			else count = 0;
			
			if(cur%100000 == 0) System.out.println(cur);
			cur++;
		}
		System.out.println(cur-3);
	}
	
	public static HashSet<Long> residue(long l){
		HashSet<Long> ret = new HashSet<Long>();
		for(int i=0; primes.get(i) <= l; i++){
			while(l%primes.get(i) == 0){
				l = l/primes.get(i);
				ret.add(primes.get(i));
			}
		}
		return ret;
	}

}
