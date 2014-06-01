package p001t040;

import java.math.BigInteger;
import java.util.ArrayList;

public class Euler013Sum50 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> nums = util.Read.fRead("13.in");
		System.out.println(nums.size());
		BigInteger sum = BigInteger.ZERO;
		for(String s : nums){
			sum = sum.add(new BigInteger(s));
		}
		System.out.println(sum);
	}
}