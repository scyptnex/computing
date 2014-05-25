package util;

import java.util.ArrayList;

public class Primer {
	
	public static void main(String[] args){
		//for testing
	}
	
	public static ArrayList<Long> primeSieve(int below){
		boolean[] notPrimes = new boolean[below];
		notPrimes[0] = true;
		notPrimes[1] = true;
		ArrayList<Long> ret = new ArrayList<Long>();
		long cur = 2;
		while(cur < below){
			if(!notPrimes[(int)cur]){
				ret.add((long)cur);
				long seq = cur*cur;
				while(seq < below){
					notPrimes[(int)seq] = true;
					seq = seq + cur;
				}
			}
			cur++;
		}
		return ret;
	}
	
	public static void addNextPrime(ArrayList<Long> curPrimes){
		if(curPrimes.isEmpty()){
			curPrimes.add(2L);
			return;
		}
		long cur = curPrimes.get(curPrimes.size()-1);
		boolean isPrime = false;
		while(!isPrime){
			isPrime = true;
			cur++;
			for(Long p : curPrimes){
				if(cur%p == 0){
					isPrime = false;
					break;
				}
				if(cur*cur > p){
					break;
				}
			}
		}
		System.out.println(cur);
		curPrimes.add(cur);
	}

	public static ArrayList<Long> makePrimes(Long upto){
		ArrayList<Long> ret = new ArrayList<Long>();
		for(long l=2; l<upto; l++){
			boolean isPrime = true;
			for(Long p : ret){
				if(l%p == 0){
					isPrime = false;
					break;
				}
			}
			if(isPrime){
				ret.add(l);
			}
		}
		return ret;
	}

}
