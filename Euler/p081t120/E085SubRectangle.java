package p081t120;

import util.Collect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class E085SubRectangle {

    public static final int TARGET = 2000000;

    public static void main(String[] args){
        List<Collect.Pair<Integer, Integer>> arr = new ArrayList<>(2000);
        arr.add(new Collect.Pair<>(0, 0));
        for(int i=1; i<2002; i++){
            arr.add(best(i, arr.get(i-1).first));
        }
        IntStream.range(0, arr.size())
                .mapToObj(i -> new Collect.Pair<>(i*arr.get(i).first, Math.abs(TARGET - arr.get(i).second)))
                .min((a, b) -> a.second.compareTo(b.second))
                .map(p -> p.first)
                .ifPresent(System.out::println);
    }

    public static Collect.Pair<Integer, Integer> best(int w, int hint){
        int cur = hint;
        int cs = subs(w, cur);
        int direction = (TARGET-cs)/Math.abs(TARGET-cs);//relying on the fact that none are 2000000
        while(true){
            int best = cs;
            cs = subs(w, cur + direction);
            //System.out.println(w + " x " + cur + " = " + cs);
            if(Math.abs(TARGET-cs) > Math.abs(TARGET-best) && best <2*TARGET){
                return new Collect.Pair<>(cur, best);
            }
            cur += direction;

        }
    }

    public static int subs(int w, int h){
        int total = 0;
        for(int ws=0; ws<w; ws++){
            for(int hs=0; hs<h; hs++){
                total += (w-ws)*(h-hs);
                if(total > 2*TARGET) break;//then we know subs(1, 1) is closer to 2 million;
            }
        }
        return total;
    }

}
