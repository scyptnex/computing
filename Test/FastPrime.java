import java.util.*;

public class FastPrime {
	
	public static final int NUM_THREADS = 4;
	public static final int JOB_THREAD_INCR = 10;
	
	public static final int[] seedPrimes = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};
	
	public ArrayList<Long> curPrimes;
	public Long highest;
	public int jobProgress;
	
	public Queue<Integer> jobs;
	
	public static void main(String[] args){
		new FastPrime(seedPrimes);
	}
	
	public FastPrime(int[] firstPrimes){
		jobs = new LinkedList<Integer>();
		curPrimes = new ArrayList<Long>();
		for(int i=0; i<seedPrimes.length; i++){
			curPrimes.add((long)seedPrimes[i]);
		}
		highest = (long)firstPrimes[firstPrimes.length-1];
		jobProgress = 0;
		while(curPrimes.get(jobProgress+1)*curPrimes.get(jobProgress+1) < highest) jobProgress++;
		//do jobs
		while(true){
			tryJobMake();
			tryJobDispatch();
		}
	}
	
	public void tryJobMake(){
		synchronized(seedPrimes){
			while(jobProgress < curPrimes.size()-1 && jobs.size() < NUM_THREADS*JOB_THREAD_INCR){
				jobs.add(jobProgress);
				jobProgress++;
			}
		}
	}
	
	public void tryJobDispatch(){
		synchronized(seedPrimes){
			for(int i=0; i<NUM_THREADS; i++){
				Integer job = jobs.poll();
				ArrayList<Long> newPrimes = primeRange(curPrimes, Math.max(highest, sqa(job)), sqa(job+1));
				highest = sqa(job+1);
				curPrimes.addAll(newPrimes);
			}
		}
	}
	
	public Long sqa(int idx){
		return curPrimes.get(idx)*curPrimes.get(idx);
	}
	
	public static ArrayList<Long> primeRange(ArrayList<Long> primes, Long start, Long end){
		ArrayList<Long> ret = new ArrayList<Long>();
		Long chk = start+1;
		while(chk < end){
			boolean isprime = true;
			for(int i=0; primes.get(i)*primes.get(i) < chk; i++){
				if(chk%primes.get(i) == 0){
					isprime = false;
					break;
				}
			}
			if(isprime){
				System.out.println(chk);
				ret.add(chk);
			}
			chk++;
		}
		return ret;
	}
	
}