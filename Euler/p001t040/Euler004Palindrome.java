package p001t040;

import java.util.ArrayList;

public class Euler004Palindrome {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Integer> palins = new ArrayList<Integer>();
		for(int i=999; i>=100; i--){
			for(int j=999; j>=100; j--){
				String chk = (i*j) + "";
				boolean pal = true;
				for(int c=0; c<chk.length()/2; c++){
					if(chk.charAt(c) != chk.charAt(chk.length()-(c+1))){
						pal = false;
						break;
					}
				}
				if(pal) palins.add(i*j);
			}
		}
		int lrgest = -1;
		for(Integer in : palins){
			lrgest = Math.max(lrgest, in);
		}
		System.out.println(lrgest);
	}

}
