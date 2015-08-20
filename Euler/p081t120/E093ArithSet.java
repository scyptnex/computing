package p081t120;

import util.Numeral;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class E093ArithSet {

    public static final int ADD = 0;
    public static final int SUB = 1;
    public static final int MUL = 2;
    public static final int DIV = 3;

    public enum OPS {ADD, SUB, MUL, DIV, RSUB, RDIV};

    public static void main(String[] args){
        int[] seed = new int[]{1, 2, 3, 4};
        int max = 0;
        while(seed[3] < 10){
            int nw = cvg(seed);
            if(nw > max){
                System.out.println(String.format("\n%d\n%d%d%d%d", nw, seed[0], seed[1], seed[2], seed[3]));
                max = nw;
            }
            shufl(seed, 0);
        }
    }

    public static void shufl(int[] sd, int idx){
        sd[idx] += 1;
        if(idx < 3 && sd[idx] == sd[idx+1]){
            sd[idx] = idx == 0 ? 1 : sd[idx-1]+1;
            shufl(sd, idx+1);
        }
    }

    public static int cvg(int[] opts){
        Set<Integer> sols = new HashSet<>();
        for(int a=0; a<4; a++){
            for(int b=0; b<4; b++) if (b!= a){
                for(int c=0; c<4; c++) if (c != b && c != a){
                    for(int d=0; d<4; d++) if(d != c && d != b && d != a){
                        try{
                            for(OPS o1 : OPS.values()){
                                Numeral.Fraction r1 = op(o1, opts[a], opts[b]);
                                for(OPS o2 : OPS.values()){
                                    Numeral.Fraction r2 = op(o2, r1, opts[c]);
                                    for(OPS o3 : OPS.values()){
                                        Numeral.Fraction r3 = op(o3, r2, opts[d]);
                                        if(r3.denominator.equals(BigInteger.ONE)){
                                            sols.add(r3.numerator.intValue());
                                        }
                                    }
                                }
                            }
                        }
                        catch(Exception e){
                            //do nothing
                        }
                    }
                }
            }
        }
        for(int i=0; i<sols.size(); i++){
            if(!sols.contains(i+1)) return i;
        }
        return sols.size();
    }

    public static void str(int a, OPS o1, int b, OPS o2, int c, OPS o3, int d, int r){
        System.out.println(String.format("((%d %s %d) %s %d) %s %d = %d", a, o1, b, o2, c, o3, d, r));
    }

    public static Numeral.Fraction op(OPS o, int x, int y) throws Exception{
        return op(o, new Numeral.Fraction(x), y);
    }

    public static Numeral.Fraction op(OPS o, Numeral.Fraction x, int y) throws Exception{
        return op(o, x, new Numeral.Fraction(y));
    }

    public static Numeral.Fraction op(OPS o, Numeral.Fraction x, Numeral.Fraction y) throws Exception{
        switch(o){
            case ADD : return x.add(y);
            case SUB : return x.subtract(y);
            case MUL : return x.multiply(y);
            case RSUB : return op(OPS.SUB, y, x);
            case RDIV : return op(OPS.DIV, y, x);
            default : {
                if(y.numerator.equals(BigInteger.ZERO)) throw new Exception("Bad division");
                return x.multiply(new Numeral.Fraction(y.denominator, y.numerator));
            }
        }
    }

}
