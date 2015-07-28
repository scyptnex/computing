package p001t040;

import java.util.ArrayList;
import java.util.List;

public class Euler032Pandigital {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Character> chrs = new ArrayList<Character>();
		for(char c = '1'; c<= '9'; c++){
			chrs.add(c);
		}
		ResultPerm rp = new ResultPerm();
		util.Permutation.permuteMinimal(chrs, rp);
		System.out.println(rp.rpSum);
	}
	
	public static class ResultPerm implements util.Permutation.Permutatio<Character>{

		public int rpSum = 0;
		
		@Override
		public boolean permutation(List<Character> config) {
			ArrayList<Character> subl = new ArrayList<Character>();
			for(char c = '1'; c<= '9'; c++){
				if(!config.contains(c)) subl.add(c);
			}
			long amt = 0;
			for(char c : config){
				amt = amt*10 + c - '0';
			}
			util.Permutation.permuteMinimal(subl, new PermChecker(this, amt));
			return true;
		}
	}
	
	public static class PermChecker implements util.Permutation.Permutatio<Character>{
		ResultPerm res;
		long trg;
		public PermChecker(ResultPerm rp, long target){
			trg = target;
			res = rp;
		}
		@Override
		public boolean permutation(List<Character> config) {
			long amt = 0;
			for(char c : config){
				amt = amt*10 + c - '0';
			}
			if(amt*amt > trg) return false;
			if(trg%amt == 0 && isPan(amt, trg)){
				System.out.println(amt + " * " + (trg/amt) + " = " + trg);
				res.rpSum += trg;
				return false;
			}
			return true;
		}
	}
	
	public static boolean isPan(String s){
		if (s.length() != 9)
			return false;
		boolean[] taken = new boolean[9];
		for (char ch : s.toCharArray()) {
			if (ch == '0' || taken[ch - '1'])
				return false;
			taken[ch - '1'] = true;
		}
		return true;
	}

	public static boolean isPan(long a, long c) {
		long b = c / a;
		String ccat = a + "" + b + "" + c;
		return isPan(ccat);
	}

}
