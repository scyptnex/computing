package p041t080;

import java.util.ArrayList;

public class Euler051PrimeReplace {

	public static final int digis = 6;

	public static void main(String[] args) {
		int max = 10;
		for (int i = 0; i < digis; i++) {
			max *= 10;
		}
		ArrayList<Long> prm = util.Primer.primeSieve(max);
		int perms = 1 << (digis-1);
		System.out.println(perms);
		int[][] cntr = new int[max/10][perms];
		
	}

}