package p000t040;

import java.util.*;

public class Euler021AmicableSum {
	
	public static void main(String[] args){
		Set<Long> amics = new HashSet<Long>();
		for(int i=1; i<=10000; i++){
			if(!amics.contains(i)){
				long mine = sumd(i);
				long yours = sumd(mine);
				if(yours == i && mine != i){
					amics.add(mine);
					amics.add(yours);
				}
			}
		}
		System.out.println(amics);
		long sum = 0;
		for(Long l : amics){
			sum += l;
		}
		System.out.println(sum);
	}
	
	public static long sumd(long n){
		Set<Long> divis = util.Numeral.divisors(n);
		int ret = 0;
		for(Long l : divis){
			if(l != n) ret += l;
		}
		return ret;
	}
	
}
