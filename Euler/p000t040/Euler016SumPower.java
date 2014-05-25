package p000t040;

import java.math.BigInteger;

public class Euler016SumPower {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger bi = BigInteger.ONE.add(BigInteger.ONE).pow(1000);
		System.out.println(bi);
		String b = bi.toString();
		int sm = 0;
		for(char c : b.toCharArray()){
			sm += c-'0';
		}
		System.out.println(sm);
	}

}
