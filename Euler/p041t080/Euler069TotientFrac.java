package p041t080;

import util.NumberTheory;

public class Euler069TotientFrac {

    public static void main(String[] args){
        double mxt = 0;
        int mxn = 0;
        for(int n=1000000; n>=2; n--){
            double tot = NumberTheory.totient(n);
            if(n%10000 == 0) System.out.println(n);
            if(n/tot > mxt){
                mxn = n;
                mxt = n/tot;
            }
        }
        System.out.println();
        System.out.println(mxt);
        System.out.println(mxn);
    }

}
