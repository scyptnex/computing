package p041t080;

import util.NumberTheory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//1322
public class Euler064RootPeriodicity {

    static Set<Integer> squares = new HashSet<Integer>();

    public static void main(String[] args){
        int s = 0;
        while(s*s <= 10000){
            squares.add(s*s);
            s++;
        }
        int count = 0;
        for(int i=1; i<10000; i++) if(!squares.contains(i)){
            if (NumberTheory.surdCF(i).size() % 2 == 0) count ++;
        }
        System.out.println(count);
    }

}
