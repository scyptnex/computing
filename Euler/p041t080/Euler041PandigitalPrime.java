package p041t080;

import java.util.ArrayList;

public class Euler041PandigitalPrime {
	
	public static void main(String[] args) {
		ArrayList<Long> primes = util.Primer.primeSieve(1000000000);
		System.out.println(primes.size());
		for(int i=primes.size()-1; i>0; i--){
			if(isPandigital(primes.get(i))){
				System.out.println(primes.get(i));
				break;
			}
		}
	}
	
	public static boolean isPandigital(long num){
		if(num >= 1000000000) return false;
		String nm = num + "";
		boolean[] taken = new boolean[9];
		for(char c : nm.toCharArray()){
			int v = c - '1';
			if(v < 0 || taken[v]) return false;
			else if(v >= nm.length()) return false;
			taken[v] = true;
		}
		return true;
	}

}
