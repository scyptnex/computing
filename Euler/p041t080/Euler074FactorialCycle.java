package p041t080;


import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Euler074FactorialCycle {

    public static long[] facto = {
            1,
            1,
            2,
            2*3,
            2*3*4,
            2*3*4*5,
            2*3*4*5*6,
            2*3*4*5*6*7,
            2*3*4*5*6*7*8,
            2*3*4*5*6*7*8*9
    };

    public static void main(String[] args){
        int count = 0;
        for(long l=1; l<1000000; l++){
            Set<Long> seen = new HashSet<>();
            long cur = l;
            while(!seen.contains(cur)){
                seen.add(cur);
                cur = sumOfDigitFactorials(cur);
            }
            count += seen.size() == 60 ? 1 : 0;
        }
        System.out.println(count);
    }

    public static long sumOfDigitFactorials(long l){
        return (l+"").chars()
                .map(i -> i-'0')
                .mapToLong(i -> facto[i])
                .sum();
    }

}
