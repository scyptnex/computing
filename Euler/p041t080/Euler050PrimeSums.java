package p041t080;

import java.util.ArrayList;

public class Euler050PrimeSums {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Long> primes = util.Primer.primeSieve(1000000);
		int s = 0;
		int l = 0;
		long p = -1;
		for(int pi = 0; pi < primes.size(); pi++){
			long curP = primes.get(pi);
			int curS = 0;
			int curL = 0;
			long curSum = 0;
			boolean going = true;
			while(going){
				if(curSum < curP){
					curSum += primes.get(curS + curL);
					curL++;
				}
				else if(curSum > curP){
					curSum = curSum - primes.get(curS);
					curS++;
					curL--;
				}
				else{
					if(curL > l){
						s = curS;
						l = curL;
						p = curP;
					}
					going = false;
				}
			}
		}
		System.out.print(p + " =");
		for(int i=0; i<l; i++){
			System.out.print(" " + primes.get(s+i));
		}
		System.out.println();
	}
	
	public static long sum(ArrayList<Long> base, int s, int l){
		long ret = 0;
		for(int i=0; i<l; i++){
			ret += base.get(s+l);
		}
		return ret;
	}

}
