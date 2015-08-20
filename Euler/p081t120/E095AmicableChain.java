package p081t120;

import util.Numeral;
import util.PrimeFactory;
import util.Primer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class E095AmicableChain {

    public static final int[] cs = new int[100];

    public static void main(String[] args){
        LongStream.range(3, 100)
                .mapToInt(E095AmicableChain::clen)
                .peek(System.out::println)
                .boxed()
                .collect(Collectors.toList());
    }


    public static int clen(long num){
        int count = 0;
        long cur = num;
        while(true){
            cur = Numeral.divisors(cur).stream().mapToLong(Long::longValue).sum() - cur;
            if(cur == num) return count;
            if(cur > 1000000) return 0;
            count++;
        }
    }

}
