package p081t120;

import util.Collect;
import util.PrimeFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class E087PowerTriplets {

    public static final int SIZE = 50000000;

    public static void main(String[] args){
        List<Integer> prms = new ArrayList<>();
        PrimeFactory.allPrimesInRange(1, (int)Math.ceil(Math.sqrt(SIZE)) + 1000).forEach(prms::add);
        List<Long> squas = prms.stream().filter(i -> i < Math.pow(SIZE, 0.5)).map(i -> (long)(i * i)).filter(i -> i < SIZE).collect(Collectors.toList());
        List<Long> cubes = prms.stream().filter(i -> i < Math.pow(SIZE, 0.35)).map(i -> (long)(i * i * i)).filter(i -> i < SIZE).collect(Collectors.toList());
        List<Long> tetrs = prms.stream().filter(i -> i < Math.pow(SIZE, 0.25)).map(i -> (long) (i * i * i * i)).filter(i -> i < SIZE).collect(Collectors.toList());
        long num = squas.stream()
                .flatMap(s -> cubes.stream().map(c -> new Collect.Pair<>(s, c)))
                .flatMap(sc -> tetrs.stream().map(t -> sc.first + sc.second + t))
                .filter(l -> l < SIZE)
                .distinct()
                .count();
        System.out.println(num);
    }

}
