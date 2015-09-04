package p081t120;

import util.Collect;
import util.Read;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class E098AnagramSquare {
    public static void main(String[] args) throws IOException{
        Scanner sca = new Scanner(Read.locateInputFile(E098AnagramSquare.class));
        sca.useDelimiter(",");
        List<String> chrs = new ArrayList<>();
        while(sca.hasNext()) chrs.add(sca.next());
        HashMap<String, List<String>> angs = chrs.stream()
                .map(s -> s.substring(1, s.length() - 1))
                .collect(() -> new HashMap<>(), (m, s) -> {
                    String k = s.chars().sorted().mapToObj(i -> "" + (char) i).collect(Collectors.joining());
                    if (!m.containsKey(k)) m.put(k, new ArrayList<>());
                    m.get(k).add(s);
                }, (a, b) -> {
                    for (String bk : b.keySet()) {
                        if (a.containsKey(bk)) a.get(bk).addAll(b.get(bk));
                        else a.put(bk, b.get(bk));
                    }
                });
        List<Collect.Pair<String, String>> anagramPairs = angs.values().stream().parallel()
                .filter(l -> l.size()>1)
                .flatMap(l -> l.stream().flatMap(a -> l.stream().map(b -> new Collect.Pair<>(a, b)).filter(p -> p.first.compareTo(p.second) < 0)))
                .collect(Collectors.toList());
        anagramPairs.clear();
        anagramPairs.add(new Collect.Pair<>("CARE", "RACE"));
        anagramPairs.stream().parallel()
                .map(p -> new Collect.Pair<>((p.first+p.second).chars().sorted().distinct().mapToObj(i -> "" + (char)i).collect(Collectors.joining()), p))
                .forEach(System.out::println);
    }
}
