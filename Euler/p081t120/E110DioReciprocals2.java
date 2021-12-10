package p081t120;


import util.Collect;
import util.PrimeFactory;

import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class E110DioReciprocals2 {

    static int[] primes = new int[16];

    public static void main(String[] args) {
        System.out.println("1260 " + E108DioReciprocals.smartRD(1260));
        int[] counts = new int[16];
        int i=0;
        for(int p : PrimeFactory.primeStream().limit(16).boxed().collect(Collectors.toList())){
            counts[i] = 1;
            primes[i++] = p;
        }
        counts[15] = 0;
        System.out.println(pf2long(counts) + " - " + rd(counts));
    }

    static int isBasicallyDone(int[] pf){
        for(int i=)
    }

    static int rd(int[] pf){
        int count = 1;
        for(int f : pf){
            count = f*((2*count)-1) + count;
        }
        return count;
    }

    static Optional<Long> pf2long(int[] pf){
        long cur = 1;
        for(int i=0; i<pf.length; i++){
            long mult = lpow(primes[i], pf[i]);
            if(Long.MAX_VALUE/(mult+1) < cur){
                return Optional.empty();
            }
            cur *= mult;
        }
        return Optional.of(cur);
    }

    static long lpow(long n, long p){
        long r = 1;
        for(int i=0; i<p; i++){
            r*=n;
        }
        return r;
    }

        static void old(){
//        Optional<BigInteger> minVal = IntStream.range(1, 20000000).mapToObj(i -> {
//            List<Collect.Pair<Integer, Integer>> simnum = new ArrayList<>();
//            int cur = i;
//            int digit = 0;
//            BigInteger numerval = BigInteger.ONE;
//            while(cur > 0){
//                int nval = Math.max(10-digit, 3);
//                int curdigit = cur%nval;
//                cur = cur/nval;
//                simnum.add(new Collect.Pair<>(primeList.get(digit), curdigit));
//                numerval = numerval.multiply(BigInteger.valueOf(primeList.get(digit)).pow(curdigit));
//                digit++;
//            }
//            return new Collect.Pair<BigInteger, Integer>(numerval, E108DioReciprocals.smartRD(simnum));
//        }).filter(p -> p.second > 4000000 && p.second < 8000000)
//                .map(p -> p.first)
//                .min(BigInteger::compareTo);
//        // 139885119141768000
//        // 139885119141768000
//        System.out.println(minVal);
//        List<Collect.Pair<Integer, Integer>> maxpf = Collections.emptyList();
//        for(int i=1260; true; i++){
//            List<Collect.Pair<Integer, Integer>> pfs = PrimeFactory.getPrimeFactors(i);
//            int rd = E108DioReciprocals.smartRD(pfs);
//            if(rd > max){
//                max = rd;
//                maxpf = pfs;
//                System.out.println(i + " - " + max + " - " + Collect.stringify(maxpf));
//            }
//            if(max > 4000000){
//                System.out.println(i + "<===== " + max);
//                break;
//            }
//        }
    }
}
