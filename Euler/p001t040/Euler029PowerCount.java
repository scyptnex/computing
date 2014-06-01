package p001t040;

import java.math.BigInteger;
import java.util.HashSet;

public class Euler029PowerCount {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashSet<BigInteger> atob = new HashSet<BigInteger>();
		for(int a=2; a<=100; a++){
			for(int b=2; b<=100; b++){
				BigInteger bi = new BigInteger(a + "").pow(b);
				atob.add(bi);
			}
		}
		System.out.println(atob.size());
	}

}
