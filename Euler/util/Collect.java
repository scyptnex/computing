package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Collect {

    public static void main(String[] args){
        Arrays.asList(
                forLoopStream(0, 10, 1),
                forLoopStream(0, 10, 2),
                forLoopStream(0, 10, 3),
                forLoopStream(0, 0, 3),
                forLoopStream(5, 0, 3),
                forLoopStream(-3, -1, 1),
                forLoopStream(-3, 1, 1),
                forLoopStream(10, 1, -1),
                forLoopStream(10, 1, -2),
                forLoopStream(-3, -10, -1)
        ).forEach(
                i -> System.out.println(i.boxed().map(Object::toString).collect(Collectors.joining(",")))
        );
    }

    public static class Pair<A, B>{
        public final A first;
        public final B second;
        public Pair(A a, B b){
            first = a;
            second = b;
        }
        @Override
        public String toString(){
            return String.format("<%s,%s>", first.toString(), second.toString());
        }
        @Override
        public int hashCode(){
            return first.hashCode() ^ second.hashCode() << 1;
        }
    }

    /**
     * <p>Returns an int-stream modelled after a for loop:</p>
     * <p>incr > 0 : for(int i=start; i < end; i+=incr)</p>
     * <p>incr < 0 : for(int i=start; i > end; i+=incr)</p>
     *
     * @param start The first integer to return
     * @param end The integer which, if we reach or pass it, stop returning
     * @param incr The difference between this int and the next
     * @return the integer range
     */
    public static IntStream forLoopStream(int start, int end, int incr){
        if(start != 0) return forLoopStream(0, end - start, incr).map(i -> i+start);
        if(incr < 0) return forLoopStream(0, -end, -incr).map(i -> -i);
        return IntStream.range(0, end).filter(i -> i%incr == 0);
    }

    /**
     * Sorts the input list according to its natural order,
     * maintaining positional information
     *
     * @param in The input list
     * @param <T> The type of the elements in the input list
     * @return A list of pairs, where each pair houses the element of the input list and the index that it was originally at
     */
    public static <T extends Comparable<? super T>> List<Pair<T, Integer>> dualSort(final List<T> in){
        return IntStream
                .range(0, in.size())
                .mapToObj( i -> new Pair<>(in.get(i), i))
                .sorted((a, b) -> a.first.compareTo(b.first))
                .collect(Collectors.toList());
    }

    public static <T extends Comparable<? super T>, U> List<Pair<T, U>> dualSort(final List<T> main, final List<U> dependant){
        return dualSort(main).stream()
                .map((Pair<T, Integer> ip) -> new Pair<T, U>(ip.first, dependant.get(ip.second)))
                .collect(Collectors.toList());
    }

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

    public static <T> String stringify(List<T> items){
        return items.stream().map(T::toString).collect(Collectors.joining(","));
    }

}
