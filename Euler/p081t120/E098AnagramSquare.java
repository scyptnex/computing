package p081t120;

import util.Collect;
import util.Read;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        anagramPairs.stream().flatMap(E098AnagramSquare::conv).forEach(System.out::println);
//        int largestDigits=anagramPairs.stream()
//        		.mapToInt(p -> p.first.length())
//        		.max().orElse(0);
//        int smallestDigits=anagramPairs.stream()
//        		.mapToInt(p -> p.first.length())
//        		.min().orElse(-1);
//        anagramPairs.stream().parallel()
//                .map(p -> new Collect.Pair<>((p.first+p.second).chars().sorted().distinct().mapToObj(i -> "" + (char)i).collect(Collectors.joining()), p))
//                .forEach(System.out::println);
//        Map<Integer, Set<Integer>> grps = IntStream.rangeClosed(smallestDigits, largestDigits).collect(() -> new HashMap<Integer, Set<Integer>>(), (m,i)->m.put(i, new HashSet<Integer>()), (a,b) -> a.putAll(b));
    }
    
    public static HashMap<Integer, Set<Integer>> convCache = new HashMap<>();
    public static Stream<Collect.Pair<Integer, Integer>> conv(final Collect.Pair<String, String> in){
    	int l = in.first.length();
    	if(!convCache.containsKey(l)){
    		convCache.put(l, IntStream
    				.rangeClosed((int)Math.ceil(Math.sqrt(Math.pow(10, l-1))), (int)Math.floor(Math.sqrt(Math.pow(10, l))))
    				.map(i -> i*i).boxed().collect(Collectors.toSet()));
    	}
    	return convCache.get(l).stream().parallel()
    			.map(i -> );
    }
    
    public static char[] digitMap(String wrd, int num, int len){
    	char[] ret = {' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
    	int i=1;
    	while(num > 0){
    		int idx = num%10;
    		char c = wrd.charAt(len-i);
    		if(ret[idx] == ' ')
    		num = num/10;
    		i++;
    	}
    	return ret;
    }
}
