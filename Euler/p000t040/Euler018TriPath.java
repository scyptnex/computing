package p000t040;

import java.util.ArrayList;

public class Euler018TriPath {
	
	public static ArrayList<ArrayList<Long>> memo = new ArrayList<ArrayList<Long>>();
	public static ArrayList<ArrayList<Long>> tri;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> triS = util.Read.fRead("18.in");
		tri = new ArrayList<ArrayList<Long>>();
		for(String s : triS){
			tri.add(util.Read.listOfNumbers(s));
			memo.add(new ArrayList<Long>());
			for(int i=0; i<=memo.size(); i++){
				memo.get(memo.size()-1).add(null);
			}
		}
		System.out.println(best(0, 0));
	}
	
	public static long best(int row, int col){
		if(memo.get(row).get(col) == null){
			long bst = tri.get(row).get(col);
			if(row < tri.size()-1){
				bst += Math.max(best(row+1, col), best(row+1, col+1));
			}
			memo.get(row).set(col, bst);
		}
		return memo.get(row).get(col);
	}

}
