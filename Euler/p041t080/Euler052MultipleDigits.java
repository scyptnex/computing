package p041t080;

import java.util.ArrayList;
import java.util.List;

public class Euler052MultipleDigits {

    public static void main(String[] args){
        System.out.println(multiDigits(125874, 2));
        long cur=1;
        while(true){
            if(multiDigits(cur, 2, 3, 4, 5, 6)) {
                break;
            } else {
                cur++;
            }
        }
        System.out.println(cur);
        System.out.println(cur*2);
        System.out.println(cur*3);
        System.out.println(cur*4);
        System.out.println(cur*5);
        System.out.println(cur*6);
    }

    public static boolean multiDigits(long val, int ... multipliers){
        int[] duv = digitsUsed(val);
        for(int m : multipliers){
            int[] chk = digitsUsed(m*val);
            if(!sameList(duv, chk)) return false;
        }
        return true;
    }

    public static int[] digitsUsed(long l) {
        int[] ret = new int[10];
        do {
            ret[(int)(l % 10)]++;
            l /= 10;
        } while (l > 0);
        return ret;
    }

    public static boolean sameList(int[] a, int[] b){
        if(a.length != b.length) return false;
        else{
            for(int i=0; i<a.length; i++){
                if(a[i] != b[i]) return false;
            }
        }
        return true;
    }

}
