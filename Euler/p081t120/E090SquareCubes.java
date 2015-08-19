package p081t120;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class E090SquareCubes {

    public static final int B_0 = 1;
    public static final int B_1 = B_0*2;
    public static final int B_2 = B_1*2;
    public static final int B_3 = B_2*2;
    public static final int B_4 = B_3*2;
    public static final int B_5 = B_4*2;
    public static final int B_6 = B_5*2;
    public static final int B_7 = B_6*2;
    public static final int B_8 = B_7*2;
    public static final int B_9 = B_8*2;

    public static final int SQ_1 = B_0 | B_1;
    public static final int SQ_2 = B_0 | B_4;
    public static final int SQ_3 = B_0 | B_9;
    public static final int SQ_4 = B_1 | B_6;
    public static final int SQ_5 = B_2 | B_5;
    public static final int SQ_6 = B_3 | B_6;
    public static final int SQ_7 = B_4 | B_9;
    public static final int SQ_8 = B_6 | B_4;
    public static final int SQ_9 = B_8 | B_1;

    public static void main(String[] args) {
        List<Cub> lst = cfgs();
        long num = lst.stream()
                .flatMap(ca -> lst.stream().map(cb -> new Cub[]{ca, cb}))
                .filter(cs -> canSq(cs[0], cs[1]))
                .map(cs -> cs[0] + "-" + cs[1])
                .peek(System.out::println)
                .distinct()
                .count();
        System.out.println(num/2);//divide by 2 because we count each one twice (a-b and b-a)
    }

    public static boolean canSq(Cub a, Cub b){
        final int ad = a.desc;
        final int bd = b.desc;
        return (((ad & B_0) | (bd & B_1)) == SQ_1 || ((bd & B_0) | (ad & B_1)) == SQ_1) &&
                (((ad & B_0) | (bd & B_4)) == SQ_2 || ((bd & B_0) | (ad & B_4)) == SQ_2) &&
                (((ad & B_0) | (bd & B_9)) == SQ_3 || ((bd & B_0) | (ad & B_9)) == SQ_3) &&
                (((ad & B_1) | (bd & B_6)) == SQ_4 || ((bd & B_1) | (ad & B_6)) == SQ_4) &&
                (((ad & B_2) | (bd & B_5)) == SQ_5 || ((bd & B_2) | (ad & B_5)) == SQ_5) &&
                (((ad & B_3) | (bd & B_6)) == SQ_6 || ((bd & B_3) | (ad & B_6)) == SQ_6) &&
                (((ad & B_4) | (bd & B_9)) == SQ_7 || ((bd & B_4) | (ad & B_9)) == SQ_7) &&
                (((ad & B_6) | (bd & B_4)) == SQ_8 || ((bd & B_6) | (ad & B_4)) == SQ_8) &&
                (((ad & B_8) | (bd & B_1)) == SQ_9 || ((bd & B_8) | (ad & B_1)) == SQ_9);
    }

    public static List<Cub> cfgs(){
        List<Cub> lst = new ArrayList<>();
        for(int a=0; a<=9; a++){
            for(int b=0; b<a; b++){
                for(int c=0; c<b; c++){
                    for(int d=0; d<c; d++){
                        for(int e=0; e<d; e++){
                            for(int f=0; f<e; f++){
                                lst.add(new Cub(a, b, c, d, e, f));
                            }
                        }
                    }
                }
            }
        }
        return lst;
    }

    public static class Cub {
        public final int a, b, c, d, e, f;
        public final int desc;
        public Cub(int a_, int b_, int c_, int d_, int e_, int f_){
            List<Integer> fcs = IntStream.of(a_, b_, c_, d_, e_, f_).sorted().boxed().collect(Collectors.toList());
            a = fcs.get(0);
            b = fcs.get(1);
            c = fcs.get(2);
            d = fcs.get(3);
            e = fcs.get(4);
            f = fcs.get(5);
            int tDesc = (1 << a) | (1 << b) | (1 << c) | (1 << d) | (1 << e) | (1 << f);
            tDesc |= (tDesc & B_6) << 3;
            tDesc |= (tDesc & B_9) >> 3;
            desc = tDesc;
        }
        @Override
        public String toString(){
            return String.format("%d%d%d%d%d%d", a, b, c, d, e, f);
        }
    }
}

