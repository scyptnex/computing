package p081t120;

import util.Tri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class E086ShortCuboid {

    public static final int MAX_M = 100;

    public static void main(String[] args){
        System.out.println(shortLen(6, 5, 3));
        List<Tri> itris = Tri.getPrimsFor(2*MAX_M).stream()
                .filter( t -> t.s < MAX_M && t.l < 2*MAX_M)
                .flatMap( t -> IntStream
                        .rangeClosed(1, Math.min(MAX_M/t.s, (2*MAX_M)/t.l))
                        .mapToObj(i -> new Tri(t.s*i, t.l*i, t.h*i)))
                .sorted().collect(Collectors.toList());

        itris.stream().sorted().mapToInt(t -> t.s + t.l).sorted().forEach(System.out::println);
    }

    // x > y & x > z => x^2 + (y+z)^2 is shortest
    public static long shortLen(long longer, long s1, long s2){
        return sqr(longer*longer + (s1+s2)*(s1+s2));
    }

    private static long max = -1;
    private static Map<Long, Long> lngs = new HashMap<>();
    public static long sqr(long candidate){
        while(max*max <= candidate){
            max++;
            lngs.put(max*max, max);
        }
        if(lngs.containsKey(candidate)) return lngs.get(candidate);
        return -1;
    }

}
