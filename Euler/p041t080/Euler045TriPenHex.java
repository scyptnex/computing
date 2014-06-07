package p041t080;

import java.util.ArrayList;
import java.util.HashSet;

public class Euler045TriPenHex {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SequenceGen tris = new SequenceGen(1, 2, 1);
		SequenceGen pents = new SequenceGen(1, 4, 3);
		SequenceGen hexs = new SequenceGen(1, 5, 4);
		int found = 0;
		int cur = 0;
		while(found < 2){
			if(pents.contains(tris.get(cur)) && hexs.contains(tris.get(cur))){
				System.out.println(cur + " - " + tris.get(cur));
			}
			cur++;
		}
	}
	
	public static class SequenceGen{
		long cur, diff, dd;
		final ArrayList<Long> seq;
		final HashSet<Long> col;
		private long greatest = 0;
		public SequenceGen(long start, long startDiff, long diffDiff){
			cur = start;
			diff = startDiff;
			dd = diffDiff;
			seq = new ArrayList<Long>();
			col = new HashSet<Long>();
		}
		
		private void generate(){
			seq.add(cur);
			col.add(cur);
			greatest = cur;
			cur += diff;
			diff += dd;
		}
		
		public boolean contains(long n){
			while(greatest < n) generate();
			return col.contains(n);
		}
		
		public long get(int i){
			while(seq.size() <= i) generate();
			return seq.get(i);
		}
	}

}
