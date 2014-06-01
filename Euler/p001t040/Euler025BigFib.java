package p001t040;

import java.math.BigInteger;

public class Euler025BigFib {
	
	public static void main(String[] args){
		BigInteger a = BigInteger.ONE;
		BigInteger b = BigInteger.ONE;
		long ctr = 2;
		while(b.toString().length()<1000){
			if(ctr%1000 == 0){
				System.out.println(ctr + " , " + b);
			}
			BigInteger temp = b;
			b = a.add(b);
			a = temp;
			ctr++;
		}
		System.out.println(ctr);
	}
	
}
