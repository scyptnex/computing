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
        anagramPairs.stream().flatMap(E098AnagramSquare::conv).sorted((p1, p2)-> p1.second.second - p2.second.second).forEach(System.out::println);
    }
    
    public static HashMap<Integer, Set<Integer>> convCache = new HashMap<>();
    public static Stream<Collect.Pair<Collect.Pair<String, String>, Collect.Pair<Integer, Integer>>> conv(final Collect.Pair<String, String> in){
    	int l = in.first.length();
    	if(!convCache.containsKey(l)){
    		convCache.put(l, IntStream
    				.rangeClosed((int)Math.ceil(Math.sqrt(Math.pow(10, l-1))), (int)Math.floor(Math.sqrt(Math.pow(10, l))))
    				.map(i -> i*i).boxed().collect(Collectors.toSet()));
    	}
    	return convCache.get(l).stream()
    			.map(i -> new Collect.Pair<>(i, digitMap(in.first, in.second, i)))
    			.filter(p -> p.second != -1 && convCache.get(l).contains(p.second))
    			.map(p -> new Collect.Pair<>(in, p));
    }
    
    public static int digitMap(String wrd1, String wrd2, int num1){
    	char[] n1= (num1 + "").toCharArray();
    	char[] w1 = wrd1.toCharArray();
    	char[] w2 = wrd2.toCharArray();
    	char[] n2 = new char[n1.length];
    	for(char c : wrd2.chars().mapToObj(c -> (char)c).collect(Collectors.toSet())){
    		char mc = '\0';
    		//check all in w1 are same
    		for(int i=0; i<w1.length; i++){
    			if(c == w1[i]){
    				if(mc != '\0' && mc != n1[i]) return -1;
    				mc = n1[i];
    			}
    		}
    		for(int i=0; i<n2.length; i++) if(n2[i] == mc) return -1;
    		for(int i=0; i<w2.length; i++){
    			if(c == w2[i]) n2[i] = mc;
    		}
    	}
    	if(n2[0] == '0') return -1;
    	int ret = 0;
    	for(int i=0; i<n2.length; i++){
    		ret = ret*10 + (n2[i]-'0');
    	}
    	return ret;
    }
}
