package p000t040;

import java.util.HashSet;

public class Euler023AbundantSum {

	public static void main(String[] args) {
		HashSet<Long> abundants = new HashSet<Long>();
		for (int i = 0; i < 28123; i++) {
			if (Euler021AmicableSum.sumd(i) > i) {
				abundants.add((long) i);
			}
		}
		long sum = 0;
		for (long i = 1; i < 28123; i++) {
			boolean soa = false;
			for(Long l : abundants){
				if(l < i && abundants.contains(i-l)){
					soa = true;
					break;
				}
			}
			if(!soa){
				sum += i;
			}
		}
		System.out.println(sum);
	}

}
