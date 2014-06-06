package p041t080;

import java.util.ArrayList;
import java.util.HashSet;

public class Euler046GoldFail {
	
	public static void main(String[] args){
		HashSet<Long> primes = new HashSet<Long>(util.Primer.primeSieve(100000000));
		ArrayList<Long> dbs = new ArrayList<Long>();
		long cur = 1;
		while(dbs.size() < primes.size()){
			dbs.add(cur*cur*2);
			cur++;
		}
		
		for(long i=3; i>0; i+=2) if(!primes.contains(i)){
			boolean canGB = false;
			for(int d=0; dbs.get(d) < cur; d++){
				if(primes.contains(i-dbs.get(d))){
					canGB = true;
					break;
				}
			}
			if(!canGB){
				System.out.println(i);
				break;
			}
		}
	}
	
}
