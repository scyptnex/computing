package p081t120;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class E081PathSum {

    public static final String[] matIn = {"131,673,234,103,18",
            "201,96,342,965,150",
            "630,803,746,422,111",
            "537,699,497,121,956",
            "805,732,524,37,331"};

    public static List<List<Long>> best = new ArrayList<>();
    public static ArrayList<ArrayList<Long>> mat = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        int wid = 0;
        for(String s : util.Read.fRead("Euler/81.in")){
        //for(String s : matIn){
            mat.add(util.Read.listOfNumbers(s, ","));
            wid = mat.get(0).size();
            best.add(LongStream.generate(()->-1).limit(wid).boxed().collect(Collectors.toList()));
        }
        int hei = mat.size();
        System.out.println(wid + ", " + hei + " : " + best);
        System.out.println(mps(0, 0));
    }

    public static long mps(int y, int x){
        if(y >= mat.size() || x >= mat.get(y).size()) return -1;
        if(best.get(y).get(x) == -1){
            long right = mps(y, x+1);
            long down = mps(y+1, x);
            long me = mat.get(y).get(x);
            if(right < 0){
                if(down < 0){
                    //do nothing
                } else {
                    me += down;
                }
            } else {
                if(down < 0){
                    me += right;
                } else {
                    me += Math.min(down, right);
                }
            }
            best.get(y).set(x, me);
        }
        return best.get(y).get(x);
    }

}
