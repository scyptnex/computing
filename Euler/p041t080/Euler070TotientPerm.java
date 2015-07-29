package p041t080;

import util.Collect;
import util.NumberTheory;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Euler070TotientPerm {

    public static void main(String[] args){
        int MAX = 9999999;
        Collect.Pair<Integer, Integer> bestPair = IntStream.range(2, MAX)
                .mapToObj(i -> new Collect.Pair<>(i, NumberTheory.totient(i)))
                .filter(pr -> canonicalPerm(pr.first.toString()).equals(canonicalPerm(pr.second.toString())))
                .peek((pr) -> {
                    System.out.println(pr + " - " + (pr.first/(double)pr.second));
                })
                .min((pa, pb) -> {
                    double arat = pa.first/(double)pa.second;
                    double brat = pb.first/(double)pb.second;
                    if(arat > brat) return 1;
                    else if (brat > arat) return -1;
                    return 0;
                })
                .orElse(null);
        System.out.println();
        System.out.println(bestPair);
    }

    public static Collect.Pair<Integer, Integer> betterPair(Collect.Pair<Integer, Integer> a, Collect.Pair<Integer, Integer> b){
        if(a == null) return b;
        if(b == null) return a;
        return a.first/(double)a.second < b.first/(double)b.second? a : b;
    }

    public static String canonicalPerm(String in){
        char[] ret = in.toCharArray();
        Arrays.sort(ret);
        return new String(ret);
    }

}
