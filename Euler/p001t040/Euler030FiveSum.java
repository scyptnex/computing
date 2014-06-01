package p001t040;

public class Euler030FiveSum {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long tot = 0;
		for(long i=2; i>1; i++){//to infinity and beyond
			long sm = 0;
			for(char c : (i + "").toCharArray()){
				sm += raise((c-'0'), 5);
			}
			if(sm == i){
				tot += i;
				System.out.println(i + " - " + tot);
			}
		}
	}
	
	public static long raise(int n, int x){
		if(x <= 0) return 1;
		return n*raise(n, x-1);
	}

}
