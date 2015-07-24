package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Collect {

    public static <T extends Comparable<? super T>> List<T> sort(List<T> in){
        Collections.sort(in);
        return in;
    }

    public static char[] toArrayC(List<Character> in){
        char[] ret = new char[in.size()];
        int i=0;
        for(char c : in) ret[i++] = c;
        return ret;
    }

    public static double[] toArrayD(List<Double> in){
        double[] ret = new double[in.size()];
        int i=0;
        for(double c : in) ret[i++] = c;
        return ret;
    }

    public static int[] toArrayI(List<Integer> in){
        int[] ret = new int[in.size()];
        int i=0;
        for(int c : in) ret[i++] = c;
        return ret;
    }

    public static long[] toArrayL(List<Long> in){
        long[] ret = new long[in.size()];
        int i=0;
        for(long c : in) ret[i++] = c;
        return ret;
    }

    public static List<Double> fromArray(double[] in){
        List<Double> ret = new ArrayList<>(in.length);
        for(double c : in) ret.add(c);
        return ret;
    }

    public static List<Character> fromArray(char[] in){
        List<Character> ret = new ArrayList<>(in.length);
        for(char c : in) ret.add(c);
        return ret;
    }

    public static List<Integer> fromArray(int[] in){
        List<Integer> ret = new ArrayList<>(in.length);
        for(int c : in) ret.add(c);
        return ret;
    }

    public static List<Long> fromArray(long[] in){
        List<Long> ret = new ArrayList<>(in.length);
        for(long c : in) ret.add(c);
        return ret;
    }

    public static <T> List<T> fromArray(T[] in){
        List<T> ret = new ArrayList<>();
        for(T t : in) ret.add(t);
        return ret;
    }

}
