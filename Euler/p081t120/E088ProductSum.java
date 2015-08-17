package p081t120;

import util.Collect;
import util.PrimeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class E088ProductSum {

    public static void main(String[] args){

        List<Integer> mins = IntStream.generate(() -> -1).limit(10).boxed().collect(Collectors.toList());
        mins.set(0, 0);
        mins.set(1, 0);
        int solved = 2;
        int cur = 2;
        while(solved < mins.size()){
            if(PrimeFactory.isPrime(cur)) continue;
            List<Collect.Pair<Integer, Integer>> pfacs = PrimeFactory.getPrimeFactors(cur);

            cur++;
        }
    }

    public static int minimal(List<Collect.Pair<Integer, Integer>> fac, int n){
        return n - (fac.get(0).first) - (n/fac.get(0).first);
    }

    public static int maximal(List<Collect.Pair<Integer, Integer>> fac, int n){
        return n - (fac.stream().mapToInt(p -> p.first*p.second).sum());
    }

}
