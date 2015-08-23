package p081t120;

import java.util.HashSet;
import java.util.Set;

import util.Numeral;
import util.PrimeFactory;

public class E095AmicableChain {

    public static final int[] cs = new int[100];

    public static void main(String[] args){
    	int max = 0;
    	int maxNum = -1;
    	for(int i=3; i<1000001; i++){
    		//System.out.println(Numeral.divisors(i));
    		int cur = clen(i);
    		if(cur > max){
    			max = cur;
    			maxNum = i;
    		}
    	}
    	System.out.println(maxNum);
    }


    public static int clen(long num){
        int count = 0;
        long cur = num;
        Set<Long> seen = new HashSet<>();
        do{
        	seen.add(cur);
        	if(cur > 1000000){
        		count = 0;
        		break;
        	}
            cur = Numeral.divisors(cur).stream().mapToLong(Long::longValue).sum() - cur;
            count++;
        } while(!seen.contains(cur));
        if(cur == num){
        	System.out.println(num + " " + count + " - " + seen);
        	return count;
        }
        return 0;
    }

}
