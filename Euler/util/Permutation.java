package util;

import java.math.BigInteger;
import java.util.ArrayList;

public class Permutation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(countChoices(5, 3));
	}

	public static BigInteger factorial(int f){
		BigInteger ret = BigInteger.ONE;
		for(int i=1; i<=f; i++){
			ret = ret.multiply(BigInteger.valueOf(i));
		}
		return ret;
	}

	public static BigInteger countChoices(int n, int r){
		return factorial(n).divide(factorial(r).multiply(factorial(n-r)));
	}
	
	public static <T> void permuteMinimal(ArrayList<T> draw, Permutatio<T> perm){
		boolean[] used = new boolean[draw.size()];
		for(int i=0; i<used.length; i++) used[i] = false;
		for(int len = 1; len <= draw.size(); len++){
			if( !permuteMinimalInternal(draw, used, new ArrayList<T>(), len, perm)) return;
		}
	}
	private static <T> boolean permuteMinimalInternal(ArrayList<T> draw, boolean[] used, ArrayList<T> cur, int len, Permutatio<T> perm){
		if(cur.size() == len) return perm.permutation(cur);
		for(int i=0; i<used.length; i++) if(!used[i]){
			used[i] = true;
			cur.add(draw.get(i));
			if(!permuteMinimalInternal(draw, used, cur, len, perm)){
				return false;
			}
			cur.remove(cur.size()-1);
			used[i] = false;
		}
		return true;
	}

	public static interface Permutatio<T>{
		//return false to cancel the permutations
		public boolean permutation(ArrayList<T> config);
	}
	
}
