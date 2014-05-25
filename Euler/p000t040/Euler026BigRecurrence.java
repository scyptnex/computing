package p000t040;

import java.util.ArrayList;

public class Euler026BigRecurrence {
	
	public static void main(String[] args){
		int max = 0;
		int longest = 0;
		for(int i=1; i<1000; i++){
			int len = deciRepeat(1, i).length();
			if(len > longest){
				max = i;
				longest = len;
			}
		}
		System.out.println(max);
	}
	
	public static String deciRepeat(long numer, long divis){
		ArrayList<Long> numers = new ArrayList<>();
		ArrayList<Long> decis = new ArrayList<>();
		numers.add(numer);
		decis.add(numer/divis);
		numer = numer%divis;
		boolean going = true;
		while(going){
			numer *= 10;
			if(numers.contains(numer)){
				going = false;
			}
			else{
				numers.add(numer);
				decis.add(numer/divis);
				numer = numer%divis;
			}
		}
		String ret = "";
		for(int i=numers.indexOf(numer); i<numers.size(); i++){
			ret = ret + decis.get(i);
		}
		return ret;
	}
	
}
