package p081t120;

import java.math.BigInteger;

public class E097NonMerssenne {
	
	public static void main(String[] args){
		System.out.println(BigInteger.valueOf(2).pow(7830457).multiply(BigInteger.valueOf(28433)).add(BigInteger.ONE).mod(new BigInteger("10000000000")));
	}

}
