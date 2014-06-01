package p001t040;

import java.math.BigInteger;

public class Euler020DigitFact {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger mult = BigInteger.ONE;
		for(int i=1; i<101; i++){
			mult = mult.multiply(new BigInteger(i + ""));
		}
		System.out.println(mult);
		int sum = 0;
		for(char c : mult.toString().toCharArray()){
			sum += (c - '0');
		}
		System.out.println(sum);
	}

}
