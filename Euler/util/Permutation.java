package util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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

    /**
     * @param draw The set of elements to permute
     * @param perm The callback handler for permutations
     * @param <T> The type of elements to permute
     */
	public static <T> void permuteMinimal(List<T> draw, Permutatio<T> perm){
		boolean[] used = new boolean[draw.size()];
		for(int i=0; i<used.length; i++) used[i] = false;
		for(int len = 1; len <= draw.size(); len++){
			if( !permuteMinimalInternal(draw, used, new ArrayList<T>(), len, perm)) return;
		}
	}
	private static <T> boolean permuteMinimalInternal(List<T> draw, boolean[] used, ArrayList<T> cur, int len, Permutatio<T> perm){
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


	public interface Permutatio<T>{
        /**
         * @param config The current permutation configuration (including non-choices of the draw)
         * @return True if you wish to continue the sequence, false otherwise
         */
		boolean permutation(List<T> config);
	}
	
}
