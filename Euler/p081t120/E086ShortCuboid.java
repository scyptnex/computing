package p081t120;

import util.Tri;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class E086ShortCuboid {

    public static int TOP_M = 2000;//got here by trial and error
    public static int MAX_M = -1;
    public static int BOT_M = 1000;

    public static void main(String[] args){
        //binary search for the first over 1000000
        while(true){
            MAX_M = (TOP_M + BOT_M)/2;
            long cbds = Tri.getPrimsFor(3 * MAX_M).stream()
                    .filter(t -> t.s < MAX_M && t.l < 2 * MAX_M)
                    .flatMap(t -> IntStream
                                    .rangeClosed(1, Math.min(MAX_M / t.s, (2 * MAX_M) / t.l))
                                    .mapToObj(i -> new Tri(t.s * i, t.l * i, t.h * i))
                    )
                    .flatMap(E086ShortCuboid::forms)
                    .count();
            System.out.println(MAX_M + " - " + cbds);
            if(cbds > 1000000){
                TOP_M = MAX_M;
            } else {
                BOT_M = MAX_M + 1;
            }
            if(TOP_M <= BOT_M) break;
        }
        System.out.println(TOP_M);
        System.out.println(BOT_M);
    }

    public static Stream<String> forms(final Tri t){
        return Stream.concat(IntStream.rangeClosed(1, t.s / 2).mapToObj(i -> new int[]{t.l, t.s - i, i}),
                IntStream.rangeClosed(1, t.l/2).mapToObj(i -> new int[]{Math.max(t.l-i, t.s), Math.min(t.l-i, t.s), i}))
                .filter(arr -> arr[0] <= MAX_M && arr[1] <= MAX_M && (arr[0] == t.s || arr[0] == t.l))
                .map(arr -> String.format("%d-%d-%d", arr[0], Math.max(arr[1], arr[2]), Math.min(arr[1], arr[2])));
    }

}
