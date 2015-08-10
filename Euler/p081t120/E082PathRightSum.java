package p081t120;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class E082PathRightSum {

    public static final String[] matIn = {"131,673,234,103,18",
            "201,96,342,965,150",
            "630,803,746,422,111",
            "537,699,497,121,956",
            "805,732,524,37,331"};

    public static ArrayList<ArrayList<Long>> mat = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        int wid = 0;
        for(String s : util.Read.fRead("Euler/82.in")){
        //for(String s : matIn){
            mat.add(util.Read.listOfNumbers(s, ","));
            wid = mat.get(0).size();
        }
        int hei = mat.size();
        System.out.println(wid + ", " + hei);
        long min = Long.MAX_VALUE;
        for(int i=0; i<hei; i++){
            min = Math.min(min, mrps(i, 0, 'r'));
        }
        System.out.println(cache);
        System.out.println(min);
    }

    static HashMap<String, Long> cache = new HashMap<>();
    public static long mrps(int h, int w, char head){
        if(w == mat.get(h).size()-1) return mat.get(h).get(w);
        String k = h + "" + head + "" + w;
        if(!cache.containsKey(k)){
            long me = mat.get(h).get(w);
            long up = h > 0 && head != 'd' ? me + mrps(h-1, w, 'u') : Long.MAX_VALUE;
            long right = me + mrps(h, w + 1, 'r');
            long down = h < mat.size()-1 && head != 'u' ? me + mrps(h+1, w, 'd') : Long.MAX_VALUE;
            cache.put(k, Math.min(up, Math.min(right, down)));
        }
        return cache.get(k);
    }

}
