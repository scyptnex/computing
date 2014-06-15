package p041t080;

import java.util.HashSet;

public class Euler049PermutingPrimes {
	
	public static void main(String[]  args){
		HashSet<Long> primes = new HashSet<Long>(util.Primer.primeSieve(10000));
		for(long sp : primes) if (sp > 1000){
			for(long i=1; i<(10000-sp)/2; i++){
				if(primes.contains(i + sp) && primes.contains(2*i + sp) && isPermutation(sp, i+sp, 2*i+sp)){
					System.out.println(sp + "" + (sp+i) + (sp+i*2));
				}
			}
		}
	}
	
	public static boolean isPermutation(long a, long b, long c){
		int[] sa = new int[10];
		stack(sa, a);
		int[] sb = new int[10];
		stack(sb, b);
		int[] sc = new int[10];
		stack(sc, c);
		for(int i=0; i<10; i++){
			if(sa[i] != sb[i] || sa[i] != sc[i]) return false;
		}
		return true;
	}
	
	private static void stack(int[] s, long l){
		if(l < 10){
			s[(int)l]++;
		}
		else{
			s[(int)(l%10)]++;
			stack(s, l/10);
		}
	}
	
}
