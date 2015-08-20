package p081t120;

import java.util.stream.IntStream;

public class E092SumChain {

    public static void main(String[] args){
        System.out.println(IntStream.range(1, 10000000)
                .map(E092SumChain::chainEnd)
                .filter(i -> i == 89)
                .count());
    }

    public static int chainEnd(int ch){
        while (ch != 1 && ch != 89){
            ch = (ch + "").chars()
                    .map(i -> i - '0')
                    .map(i -> i*i)
                    .sum();
        }
        return ch;
    }

}
