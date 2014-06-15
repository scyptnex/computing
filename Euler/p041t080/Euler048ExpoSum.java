package p041t080;

import java.math.BigInteger;

public class Euler048ExpoSum {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger bi = BigInteger.ZERO;
		for(int i=1; i<1000; i++){
			bi = bi.add(BigInteger.valueOf(i).pow(i));
		}
		String bs = bi.toString();
		System.out.println(bs.substring(bs.length() - 10));
	}

}
