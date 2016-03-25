import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.*;

public class J8 {

    public static void main(String[] args){
        System.out.println("Hello world");
        //a little lambda
        List<Integer> l = new ArrayList<>();
        l.add(1);
        l.add(2);
        l.add(3);
        l.forEach(i -> System.out.println(i * 2));
        l.forEach(new Con());
        System.out.println();

        //predicate lambda
        List<Integer> hs = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println(hs);
        System.out.println(hs.removeIf(i -> i % 2 == 0));
        System.out.println(hs.removeIf(new Pred()));
        System.out.println(hs.removeIf(new Pred()));
        System.out.println(hs);
        System.out.println();

        //default, static and functional interfaces
        Arrays.asList(new BIA(), new BIB(), new BIC(), () -> System.out.println("barrr I")).forEach((BI bi) -> {
            bi.bar();
            bi.baz();
        });
        BI.foo();
        BI exp = () -> System.out.println("bar EXP");// Functional interfaces
        exp.bar();
        System.out.println();

        //function
        Function<String, Integer> stoi = s -> Integer.parseInt(s);
        System.out.println(stoi.apply("6") * stoi.apply("7"));
        Function<char[], Integer> ctoi= stoi.compose((char[] ch) -> new String(ch));
        System.out.println(ctoi.apply(new char[]{'1', '3', '3', '6'}) + 1);
        System.out.println();

        //method references
        List<Double> mr = Arrays.asList(3.14, 2.60, 4.0);
        mr.replaceAll(J8::methRef2);
        mr.forEach(J8::methRef);
        System.out.println();

        //optionals
        Arrays.asList(Optional.ofNullable(null), Optional.ofNullable("Nic")).forEach(op -> {
                    Optional<String> o = (Optional<String>)op;
                    System.out.println(o.isPresent());
                    System.out.println("hi " + o.orElse("unnamed"));
                    System.out.println(o.map(s -> s.toUpperCase()).orElseGet(() -> "anon."));
                }
        );
        System.out.println();

        //streams
        System.out.println(DoubleStream.of(3.14, 2.68, 9.99).reduce(0.0, (double a, double b) -> a + b));
        DoubleStream.iterate(1.0, (double s) -> s+0.1).limit(10).forEach(d -> System.out.print(d));
        System.out.println();
        System.out.println(IntStream.range(1, 5).sum());
        System.out.println(IntStream.rangeClosed(1, 5).sum());
        System.out.println(IntStream.iterate(0, i -> i + 2).mapToObj(i -> (10.0 + (i)) / 10.0).limit(10).reduce("", (s, d) -> s.concat(d.toString() + ", "), (s1, s2) -> s1 + ", " + s2));
        System.out.println(IntStream.range(0, 10).mapToDouble(i -> i / 2.0).boxed().map(Object::toString).collect(Collectors.joining(", ")));
        System.out.println(IntStream.range(0, 10).boxed().collect(Collectors.groupingBy(i -> i % 3)));
        List<String> inL = Arrays.asList("a bX", "c", "def");
        System.out.println(inL.stream().map(s -> s.split("")).flatMap(Arrays::stream).collect(Collectors.joining(",")));
        System.out.println(inL.stream().flatMap(s -> s.chars().mapToObj(i -> (char) i)).map(Object::toString).collect(Collectors.joining("-")));
        System.out.println();

        //Base 64
        String in = "Hello World, i am a long string which will hopefully generate URLDECODE chars032897490";
        System.out.println(in);
        String b64 = Base64.getUrlEncoder().encodeToString(in.getBytes(StandardCharsets.UTF_8));
        System.out.println(b64);
        String d64 = new String(Base64.getUrlDecoder().decode(b64), StandardCharsets.UTF_8);
        System.out.println(d64);
        System.out.println();

        //Parallel Arrays
        int[] arr = new int[20000];
        Arrays.parallelSetAll(arr, idx -> ThreadLocalRandom.current().nextInt(1000000));
        System.out.println(Arrays.stream(arr).limit(10).boxed().map(Object::toString).collect(Collectors.joining(" ")));
        Arrays.parallelSort(arr);
        System.out.println(Arrays.stream(arr).limit(10).boxed().map(Object::toString).collect(Collectors.joining(" ")));
        System.out.println();

        // File reading
        try(Stream<String> lns = Files.lines(new File("Playground/fi.txt").toPath())){
            System.out.println("beginning streaming");
            lns.onClose(() -> System.out.println("Closed :)"));
            String res = lns.
                    //peek(System.out::println).
                    map(s -> s.split(" ")).
                    flatMap(Arrays::stream).
                    sorted().
                    peek(System.out::println).
                    reduce((a, b) -> Integer.parseInt(a.substring(a.indexOf("=") + 1)) > Integer.parseInt(b.substring(b.indexOf("=") + 1)) ? a : b).
                    orElse("failed");
            System.out.println("Greatest: " + res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static class Pred implements Predicate<Integer>{

        @Override
        public boolean test(Integer integer) {
            return integer%3 == 0;
        }
    }

    public static class Con implements Consumer<Integer>{

        @Override
        public void accept(Integer integer) {
            System.out.println(integer);
        }
    }

    public static void methRef(Double d){
        System.out.println((int) Math.floor(d.doubleValue()));
    }

    public static Double methRef2(Double d){
        return d*2.0;
    }

    @FunctionalInterface
    public interface BI{
        static void foo(){
            System.out.println("foo");
        }
        void bar();
        default void baz(){
            System.out.println("baz");
        }
    }

    public interface BII extends BI{
        @Override
        default void bar(){
            System.out.println("bAr");
        }
        @Override
        default void baz(){
            System.out.println("bazzaaaa");
        }
    }

    public static class BIA implements BI{
        @Override
        public void bar() {
            System.out.println("bar A");
        }
    }

    public static class BIB implements BI{
        @Override
        public void bar() {
            System.out.println("bar B");
        }
        @Override
        public void baz(){
            System.out.println("baz B");
        }
    }
    public static class BIC implements BII{
    }

}
