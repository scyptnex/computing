package util;

import java.util.HashSet;
import java.util.Set;

public class Tri implements Comparable<Tri>{

    public final int s;
    public final int l;
    public final int h;

    public Tri(int m, int n){
        s = Math.min(m*m - n*n, 2*m*n);
        l = Math.max(m * m - n * n, 2 * m * n);
        h = m*m + n*n;
    }

    public Tri(int a, int b, int c){
        s = Math.min(a, Math.min(b, c));
        h = Math.max(a, Math.max(b, c));
        l = a + b + c - s - h;
        if(h*h != s*s + l*l) throw new RuntimeException("Not a right triangle");
    }

    @Override
    public String toString(){
        return String.format("[%d %d - %d]", s, l, h);
    }

    @Override
    public int compareTo(Tri o) {
        if(h == o.h){
            if(l == o.l){
                return s - o.s;
            }
            return l - o.l;
        }
        return h - o.h;
    }

    public static Set<Tri> getPrimsFor(int maxM){
        Set<Tri> ret = new HashSet<>();
        for(int m=2; m < maxM; m++){
            for(int n=1; n < m; n++) if((m+n) % 2 == 1 && Numeral.gcd(m, n) == 1) {
                ret.add(new Tri(m, n));
            }
        }
        return ret;
    }

}
