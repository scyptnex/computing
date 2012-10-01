import java.util.*;

public class RIncr {
	
	public static final int RANGE = 100;
	
	public static void main(String[] args){
		new RIncr();
	}
	
	public RIncr(){
		//listPrimes();
		System.out.println();
		RangeIncr i = new RangeIncr();
		i.range= RANGE;
		i.incr = getBestIncr(RANGE);
		DRI x = new DRI();
		x.sub = i;
		x.range = RANGE;
		x.incr = 7;
		printSwaps(x, RANGE);
	}
	
	public void printSwaps(ItoI swap, int range){
		Set<Integer> taken = new HashSet<Integer>();
		Set<Integer> available = new HashSet<Integer>();
		for(int i=0; i<range; i++) available.add(i);
		for(int i=0; i<range; i++){
			if(!taken.contains(i) && available.contains(i)){
				System.out.println(i + "\t" + swap.get(i));
			}
			else{
				System.err.println(i + "\t to " + swap.get(i) + " TAKEN");
			}
			taken.add(i);
			available.remove(i);
		}
		if(available.size() == 0) System.out.println("All numbers accounted for");
		else System.err.println("Numbers missing");
	}
	
	public abstract class ItoI{
		public abstract int get(int in);
	}
	
	public class DRI extends ItoI{
		public int range, incr;
		public RangeIncr sub;
		public int get(int idx){
			return (sub.get(idx)*incr)%range;
		}
	}
	
	public class RangeIncr extends ItoI{
		public int range, incr;
		public int get(int idx){
			return (idx*incr)%range;
		}
	}
	
	public static int gcd(int a, int b){
		if(b == 0) return a;
		return gcd(b, a % b);
	}
	
	public static int getBestIncr(int range){
		int best = -1;
		for(int i=1; i<range/2; i++){
			if(best == -1 || score(best, range) < score(i, range)){
				best = i;
			}
		}
		return best;
	}
	
	public static int score(int incr, int rng){
		int gcd = gcd(rng, incr);
		if(gcd != 1) return -1;
		int lp = rng%incr;
		return(Math.min(incr - lp, lp));
	}
	
	public static void listPrimes(){
		ArrayList<Long> primes = new ArrayList<Long>();
		long cur = 2;
		while(true){
			boolean isprime = true;
			double lim = Math.sqrt(cur);
			for(Long l : primes){
				if(l > lim){
					break;
				}
				if(cur % l == 0){
					isprime = false;
					break;
				}
			}
			if(isprime){
				primes.add(cur);
				System.out.println(primes.size() + "\t" + cur);
			}
			cur++;
		}
	}
	
	public static long calcNGP(long in){
		long ret = in+1;
		while(true){
			if(ret%2 != 0){
				double mf = Math.sqrt(ret);
				boolean found = false;
				for(long fac=3; fac<mf; fac+=2){
					if(ret % fac == 0){
						found = true;
						break;
					}
				}
				if(!found) return ret;
			}
			ret++;
		}
	}
	
}