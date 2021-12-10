package p081t120;

import util.Collect;
import util.NumberTheory;
import util.Numeral;
import util.PrimeFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class E108DioReciprocals {

    public static void main(String[] args) {
        int i=2;
        while(true){
            int c = smartRD(i);
            System.out.println(i + " - " + c);
            if(c>1000)
                break;
            i++;
        }
//        for(int z=0; z<=3; z++) {
//            for (int y = 0; y <= 3; y++) {
//                for (int x = 0; x <= 3; x++) {
//                    System.out.print(countRD(ipow(2, x) * ipow(3, y)*ipow(5, z)) + "\t");
//                }
//                System.out.print("\t\t");
//                for (int x = 0; x <= 3; x++) {
//                    System.out.print(smartRD(ipow(2, x) * ipow(3, y)*ipow(5, z)) + "\t");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
    }

    static int ipow(int n, int p){
        if(p==0) return 1;
        int ret = n;
        for(int i=1; i<p; i++){
            ret*=n;
        }
        return ret;
    }

    static int countRD(int n){
        //System.out.println(n + PrimeFactory.getPrimeFactors(n).stream().map(p -> "(" + p.first + "x" + p.second + ")").collect(Collectors.joining(",")));
        Numeral.Fraction nf = new Numeral.Fraction(1, n);
        int count =0;
        for(int xDenom=n+1; xDenom<= 2*n; xDenom++){
            Numeral.Fraction xf = new Numeral.Fraction(1, xDenom);
            Numeral.Fraction yf = nf.subtract(xf);
            if(yf.numerator.equals(BigInteger.ONE)){
                //System.out.println(xf + " + " + yf);
                count++;
            }
        }
        //System.out.println("= " + count);
        return count;
    }

    static int smartRD(int n) {
        return smartRD( PrimeFactory.getPrimeFactors(n));
    }

    static int smartRD(List<Collect.Pair<Integer, Integer>> primeFactors){
        int count = 1;
        for(Collect.Pair<Integer, Integer> pf : primeFactors){
            count = pf.second*((2*count)-1) + count;
        }
        return count;
    }
}
