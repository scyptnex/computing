package util;

import java.math.BigInteger;
import java.util.*;

public class NumberTheory {


    private static HashMap<Integer, BigInteger> partitionsOfCache = new HashMap<>();
    public static BigInteger partitionsOf(int n){
        if(n < 0) return BigInteger.ZERO;
        if(n == 0) return BigInteger.ONE;
        if(!partitionsOfCache.containsKey(n)){
            int k = 1;
            BigInteger sm = BigInteger.ZERO;
            while((k*(3*k-1))/2 <= n){
                BigInteger add = partitionsOf(n - ((k*(3*k-1))/2));
                if(Math.abs(k-1)%2 == 1) add = add.negate();
                sm = sm.add(add);
                if(k > 0) k = -k;
                else if(k < 0) k = 1-k;
            }
            partitionsOfCache.put(n, sm);
        }
        return partitionsOfCache.get(n);
    }

    public static int totient(int n){
        //fast exit
        if(PrimeFactory.isPrime(n)) return n-1;
        //slow main path
        int ret = n;
        for(int i : PrimeFactory.getPrimeDivisors(n)) if(i != 1){
            ret = ret - (ret/i);
        }
        return ret;
    }

    public static Numeral.Fraction newtonSquareRoot(final long sqn, final int iters){
        final BigInteger s = BigInteger.valueOf(sqn);
        Numeral.Fraction ret = new Numeral.Fraction(1);
        for(int i=0; i<iters; i++){
            ret = new Numeral.Fraction(s.multiply(ret.denominator.multiply(ret.denominator)).add(ret.numerator.multiply(ret.numerator)), BigInteger.valueOf(2).multiply(ret.numerator).multiply(ret.denominator));
        }
        return ret;
    }

    /**
     * Accurately find the square root of a long number
     * @param res the number we want the square root of
     * @return the square root of res, or -1 if res is not a perfect square
     */
    public static long integralSquareRoot(long res){
        long h = res & 0xF;
        if (h > 9) return -1;
        long guess = 1;
        long tmp = res;
        while(tmp > 100){
            guess *= 10;
            tmp /= 100;
        }
        for(int i=0; i<10; i++){
            guess = (res + (guess*guess))/(2*guess);
            if(res == guess*guess) return guess;
        }
        for(long g=guess-1; g*g >= res; g--){
            if(g*g == res) return g;
        }
        for(long g=guess+1; g*g<=res; g++){
            if(g*g == res) return g;
        }
        return -1;
    }

    public static List<Integer> surdCF(int surd){
        //when the continued fraction is 2x the
        ArrayList<RT> lst = new ArrayList<RT>();
        lst.add(new RT(surd));
        while(true){
            RT nxt = new RT(lst.get(lst.size() - 1));
            lst.add(nxt);
            if(nxt.exte == 2*lst.get(0).exte){
                break;
            }
        }
        ArrayList<Integer> ret = new ArrayList<>();
        for(RT rt : lst){
            ret.add(rt.exte);
        }
        return ret;
    }

    private static class RT{
        public final int root;
        public final int exte;
        public final int inte;
        public final int divi;
        public RT(int r){
            root = r;
            exte = (int)Math.floor(Math.sqrt(r));
            inte = exte;
            divi = 1;
        }

        public RT(RT prev){
            root = prev.root;
            if((prev.root - (prev.inte*prev.inte))%prev.divi != 0) throw new RuntimeException("Wierd divisi " + prev);
            divi = (prev.root - (prev.inte*prev.inte))/prev.divi;
            if(divi < 0) throw new RuntimeException("Negative divisi " + prev);
            int tempi = prev.inte;
            int tempe = 0;
            while(prev.inte - (tempe*divi) > -Math.sqrt(root)) tempe++;
            exte = tempe-1;
            inte = (exte*divi) - prev.inte;
        }

        @Override
        public String toString(){
            return exte + "+('" + root + "'-" + inte + ")/" + divi;
        }

        @Override
        public boolean equals(Object oth){
            if(oth instanceof RT){
                //System.out.println(this + " | " + oth);
                RT o = (RT)oth;
                return root == o.root && exte == o.exte && inte == o.inte && divi == o.divi;
            }
            return false;
        }
    }

}
