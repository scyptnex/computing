package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Numeral {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static Set<Long> divisors(long l){
		Set<Long> ret = new HashSet<Long>();
		for(long i=1; i*i<=l; i++){
			if(l%i==0){
				ret.add(i);
				ret.add(l/i);
			}
		}
		return ret;
	}

}
