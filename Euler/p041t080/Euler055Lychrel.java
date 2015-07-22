package p041t080;

import util.Stringy;

import java.math.BigInteger;

public class Euler055Lychrel {

    public static void main(String[] args){
        int count = 0;
        for(int i=1; i<10000; i++){
            if(isLychrel(BigInteger.valueOf(i))) count++;
        }
        System.out.println(count);
    }

    public static boolean isLychrel(BigInteger l){
        for(int i=0; i<49; i++){
            System.out.print(l.toString() + " ");
            l = new BigInteger(Stringy.reverse(l.toString())).add(l);
            if (Stringy.isPallindrome(l.toString())){
                System.out.println(l.toString());
                return false;
            }
        }
        System.out.println("...");
        return true;
    }

}
