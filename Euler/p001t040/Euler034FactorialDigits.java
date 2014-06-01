package p001t040;

import java.math.BigInteger;

public class Euler034FactorialDigits {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BigInteger[] factors = new BigInteger[10];
		for(int i=0; i<10; i++){
			factors[i] = BigInteger.ONE;
			for(int j=1; j<=i; j++){
				factors[i] = factors[i].multiply(BigInteger.valueOf(j));
			}
			System.out.println(factors[i]);
		}
		BigInteger i = BigInteger.valueOf(3);
		BigInteger tot = BigInteger.ZERO;
		while(true){
			String rep = i.toString();
			BigInteger cur = BigInteger.ZERO;
			for(char c : rep.toCharArray()){
				cur = cur.add(factors[c - '0']);
			}
			if(i.equals(cur)){
				tot = tot.add(cur);
				System.out.println(cur + " - " + tot);
			}
			i = i.add(BigInteger.ONE);
		}
	}

}
