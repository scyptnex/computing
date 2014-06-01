package p001t040;

import java.util.HashSet;

public class Euler035CircularPrime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int count = 0;
		HashSet<Long> primes = new HashSet<Long>(util.Primer.primeSieve(1000000));
		for(long l : primes){
			String pri = l + "";
			boolean isCirc = true;
			for(int i=0; i<pri.length(); i++){
				String chk = pri.substring(i) + pri.substring(0, i);
				long p2 = Long.parseLong(chk);
				if(!primes.contains(p2)){
					isCirc = false;
					break;
				}
			}
			if(isCirc){
				count++;
				System.out.println(pri);
			}
		}
		System.out.println(count);
	}

}
