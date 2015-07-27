package p041t080;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Euler066MinDiophantine {

    public static void main(String[] args){
        Set<Long> unknowns = new HashSet<>();
        for(long i=2; i<=1000; i++) if(!isSquare(i)) unknowns.add(i);
        System.out.println(unknowns.size());
        long i=1;
        while(unknowns.size()>1){
            Set<Long> rems = new HashSet<>();
            for(long l : unknowns){
                long res = (i*i*l)+1;
                if(isSquare(res)) rems.add(l);
            }
            if(rems.size() > 0){
                unknowns.removeAll(rems);
                System.out.println(i + " - " + unknowns.size());
            }
            i++;
        }
    }

    public static long minXDio(long coeff){
        long cur = 1;
        while(true){
            if(isSquare(cur*cur*coeff+1)) return (long)Math.floor(Math.sqrt(cur * cur * coeff + 1));
            cur++;
        }
    }

    public static boolean isSquare(long num){
        long n = (long)Math.floor(Math.sqrt(num));
        return n*n == num;
    }

}
