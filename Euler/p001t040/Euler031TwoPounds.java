package p001t040;

public class Euler031TwoPounds {

	public static final int[] denoms = {200, 100, 50, 20, 10, 5, 2, 1};
	
	public static int[][] memo = new int[201][denoms.length];
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int a=0; a<memo.length; a++){
			for(int c=0; c<denoms.length; c++){
				memo[a][c] = -1;
			}
		}
		make(200, 0);
		System.out.println(memo[200][0]);
	}
	
	public static int make(int amt, int fromCoin){
		if(fromCoin >= denoms.length){
			if(amt == 0) return 1;
			return 0;
		}
		if(memo[amt][fromCoin] != -1) return memo[amt][fromCoin];
		int made = 0;
		for(int cur=0; cur*denoms[fromCoin]<=amt; cur++){
			made += make(amt-(cur*denoms[fromCoin]), fromCoin+1);
		}
		memo[amt][fromCoin] = made;
		return made;
	}

}
