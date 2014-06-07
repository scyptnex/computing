package p041t080;

import java.util.ArrayList;
import java.util.HashSet;

public class Euler044Pentagonal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Long> pents = new ArrayList<Long>();
		HashSet<Long> pentSet = new HashSet<Long>();
		long cur = 1;
		long diff = 4;
		while(pents.size()<10000000){
			pents.add(cur);
			pentSet.add(cur);
			cur += diff;
			diff += 3;
		}
		int curD = 0;
		while(true){
			System.out.println(pents.get(curD));
			int smaller = 0;
			boolean doingSmaller = true;
			while(doingSmaller){
				int greater = smaller+1;
				while(pents.get(greater) - pents.get(smaller) < pents.get(curD)){
					greater++;
				}
				if(pents.get(greater) - pents.get(smaller) == pents.get(curD) && pentSet.contains(pents.get(greater)+pents.get(smaller))){
					System.out.println(pents.get(curD) + " - " + pents.get(smaller) + " " + pents.get(greater));
					System.exit(0);
				}
				else{
					if(greater == smaller+1){
						doingSmaller = false;
					}
					else{
						smaller++;
					}
				}
			}
			curD++;
		}
	}

}
