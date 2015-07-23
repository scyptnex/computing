package p041t080;

import java.math.BigInteger;

public class Euler056PowerDigitSum {

    public static void main(String[] args){
        int max = 0;
        for (int i=1; i<100; i++){
            for(int j=1; j<100; j++){
                int sum = 0;
                for(char c : BigInteger.valueOf(i).pow(j).toString().toCharArray()){
                    sum += (c - '0');
                }
                max = Math.max(max, sum);
            }
        }
        System.out.println(max);
    }
}
