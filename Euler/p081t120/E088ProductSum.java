package p081t120;

import util.Collect;
import util.Permutation;
import util.PrimeFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class E088ProductSum {

    public static List<Set<Integer>> solus = new ArrayList<>();
    public static void main(String[] args){

        List<Integer> mins = IntStream.generate(() -> -1).limit(12000).boxed().collect(Collectors.toList());
        mins.set(0, 0);
        mins.set(1, 0);
        solus.add(new HashSet<>());
        solus.add(new HashSet<>());
        int solved = 2;
        int cur = 2;
        while(solved < mins.size()){
            if(!PrimeFactory.isPrime(cur)){
                solus.add(explode(cur));
                boolean inRange = false;
                for(int is : solus.get(cur)){
                    if(is < mins.size()){
                        inRange = true;
                        if(mins.get(is) == -1){
                            mins.set(is, cur);
                            solved++;
                        }
                    }
                }
                if(!inRange){
                    System.err.print("Could not find " + cur);
                    break;
                }
            } else {
                solus.add(new HashSet<>());
            }
            cur++;
        }
        //IntStream.range(0,solus.size()).forEach(i -> System.out.println(i + " - " + solus.get(i)));
        System.out.println(mins);
        System.out.println(mins.stream().mapToLong(i -> i).distinct().sum());
    }

    /**
     * MATHEMATICAL INSIGHT:
     * the ways of partitioning the factors of n = ab are the a,b and a,{factors of b} and b,{factors of a} for every ab
     */
    public static Set<Integer> explode(int n){
        Set<Integer> ret = new HashSet<>();
        biFactor(n)
                .forEach(p -> {
                    ret.add(2 + (n - p.first - p.second));
                    solus.get(p.first).stream().forEach(i -> ret.add(1 + (n - p.first - p.second) + i));
                    solus.get(p.second).stream().forEach(i -> ret.add(1 + (n - p.first - p.second) + i));
                });
        System.out.println(n + " - " + ret);
        return ret;
    }

    public static Stream<Collect.Pair<Integer, Integer>> biFactor(final int n){
        return IntStream.rangeClosed(2, (int)Math.floor(Math.sqrt(n)))
                .filter(i -> n%i == 0)
                .mapToObj(i -> new Collect.Pair<>(i, n/i));
    }

}
