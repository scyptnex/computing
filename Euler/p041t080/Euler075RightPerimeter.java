package p041t080;

import util.Numeral;

import java.util.ArrayList;
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
        //remove the non primitive perimeters
//        Set<Integer> newPprs = new HashSet<>();
//        for(int p : pprs){
//            boolean add = true;
//            for(int j=2; j*j<=p; j++) if(p%j == 0 && pprs.contains(p/j)){
//                add = false;
//                System.out.println(p + ", " + (p/j));
//                break;
//            }
//            if(add) newPprs.add(p);
//        }
//        System.out.println(pprs.contains(84));
//        System.out.println(pprs.size());
//        pprs = newPprs;
//        System.out.println(pprs.size());
//        System.out.println(pprs.contains(84));
        //System.exit(0);
        //count the results
        int count = 0;
        for(int i=10; i<=MAX_PERIM; i++){
            int tot = pprs.contains(i) ? 1 : 0;
            for(int j=2; j*j<=i; j++) if(i%j == 0 && pprs.contains(i/j)){
                tot++;
                if(tot > 1) break;
            }
            Set<String> irs = iRATs(i);
            long ir = irs.size();
            if(tot == 1){
                if(ir != 1) throw new RuntimeException("WRONG " + i + ": tot=" + tot + " irat=" + irs);
                count++;
            } else if (tot == 0){
                if(ir != 0) throw new RuntimeException("WRONG " + i + ": tot=" + tot + " irat=" + irs);
            } else {
                if(ir < 2) throw new RuntimeException("WRONG " + i + ": tot=" + tot + " irat=" + irs);
            }
        }
        System.out.println(count);
    }

    public static long perimOfPPT(long m, long n){
        return 2*m*m + 2*m*n;
    }

    public static Set<String> iRATs(final long p){
        Set<String> shorts = new HashSet<>();
        for(long s=1; s<=Math.floor(p/3.0); s++){
            long hn = p*p - 2*p*s + 2*s*s;
            long hd = 2*p - 2*s;
            if(hn%hd == 0){
                long trues = Math.min(s, p-s-(hn/hd));
                shorts.add(trues + "h" + (hn/hd));
            }
        }
        return shorts;
//        System.out.println(shorts);
//        Set<String> uniques = new HashSet<>();
//        for(long shr : shorts){
//            long h = ((p*p - 2*p*shr + 2*shr*shr)/(2*p - 2*shr));
//            boolean add = true;
//            for(long l=2; l*l<=shr; l++) if(shr%l == 0 && shorts.contains(shr/l)){
//                add = false;
//            }
//            if(add) uniques.add(shr  + "h" + h);
//        }
//        return uniques;
    }
}
