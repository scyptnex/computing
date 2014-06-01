package p001t040;

import java.math.BigInteger;
import java.util.ArrayList;

public class Euler003LargePrime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long num = 600851475143L;
		int lrgst = (int)Math.floor(Math.sqrt(num));
		ArrayList<Long> sieve = util.Primer.primeSieve(lrgst+1);
		System.out.println(sieve.size());
		System.out.println(lrgst);
		for(int i=sieve.size()-1; i>=0; i--){
			if(num%sieve.get(i) == 0){
				System.out.println(sieve.get(i));
			}
		}
		System.out.println("---------");
		int cur = 0;
		while(num > 1){
			if(num % sieve.get(cur) == 0){
				System.out.println(sieve.get(cur));
				num = num/sieve.get(cur);
			}
			cur++;
		}
	}

}
