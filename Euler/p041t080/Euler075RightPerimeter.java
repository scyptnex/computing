package p041t080;

import util.Numeral;

import java.util.HashSet;
import java.util.Set;

public class Euler075RightPerimeter {
    public static void main(String[] args){
        final int MAX_PERIM = 1500000;
        //first find all the primitive pythagorean roots
        Set<Integer> pprs = new HashSet<>();
        for(long m=2; perimOfPPT(m, 1)<=MAX_PERIM; m++){
            for(long n = 1; n<m && perimOfPPT(m, n) <= MAX_PERIM; n++) if((m-n)%2 == 1 && Numeral.gcd(m, n) == 1){
                pprs.add((int)perimOfPPT(m, n));
            }
        }
        int count = 0;
        for(int i=10; i<=MAX_PERIM; i++){
            int tot = pprs.contains(i) ? 1 : 0;
            for(int j=2; j*j<=i; j++) if(i%j == 0 && (pprs.contains(i/j) || pprs.contains(j))){
                tot++;
                if(tot > 1) break;
            }
            if(tot==1) count++;
        }
        System.out.println(count);
    }

    public static long perimOfPPT(long m, long n){
        return 2*m*m + 2*m*n;
    }
}
