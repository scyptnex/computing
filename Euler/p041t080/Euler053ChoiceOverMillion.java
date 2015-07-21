package p041t080;

import util.Permutation;

import java.math.BigInteger;

public class Euler053ChoiceOverMillion {

    public static void main(String[] args){
        int c = 0;
        BigInteger val = BigInteger.valueOf(1000000);
        for(int n=1; n<=100; n++){
            for(int r=1; r<=n; r++){
                if(Permutation.countChoices(n, r).compareTo(val) > 0) c++;
            }
        }
        System.out.println(c);
    }

}
