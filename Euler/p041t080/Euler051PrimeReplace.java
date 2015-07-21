package p041t080;

import util.Numeral;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Euler051PrimeReplace {

    public static final int digis = 6;
    public static final int FAMILY_SIZE=8;

    public static void main(String[] args) {
        int max = (int)Math.pow(10, digis);
        ArrayList<Long> prm = util.Primer.primeSieve(max);
        Set<Long> prmSet = new HashSet<Long>(prm);
        for(Long cur : prm){
            if(hasFamily(cur, prmSet, false)){
                hasFamily(cur, prmSet, true);
                System.out.println(cur);
                System.exit(0);
            }
        }
        System.err.println("Cannot find");
    }

    public static boolean hasFamily(long itm, Set<Long> primes, boolean verb){
        if(verb) System.out.println(itm);
        char[] b = (itm + "").toCharArray();
        for(int perm=1; perm < (1 << b.length)-1; perm++) {
            if(verb) System.out.println("  " + perm);
            int famSize = 0;
            for(char i='0'; i<='9'; i++){
                char[] fc = new char[b.length];
                for(int repl=0; repl<b.length; repl++){
                    if(((perm >> repl) & 1) == 1) {
                        fc[repl] = i;
                    } else {
                        fc[repl] = b[repl];
                    }
                }
                String res = new String(fc);
                if(!res.startsWith("0") && primes.contains(Long.parseLong(new String(fc)))){
                    if(verb) System.out.println("    " + new String(fc));
                    famSize++;
                }
            }
            if(famSize >= FAMILY_SIZE) return true;
        }
        return false;
    }

}