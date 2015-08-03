package p041t080;

import util.NumberTheory;

import java.math.BigInteger;

public class Euler078SumGroups {

    public static void main(String[] args){
        int i = 0;
        for(i=1; !NumberTheory.partitionsOf(i).mod(BigInteger.valueOf(1000000)).equals(BigInteger.ZERO); i++);
        System.out.println(NumberTheory.partitionsOf(i));
        System.out.println(i);
    }

}
