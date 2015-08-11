package p081t120;

import util.Collect;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class E083PathDjikstra {

    public static final String[] matIn = {"131,673,234,103,18",
            "201,96,342,965,150",
            "630,803,746,422,111",
            "537,699,497,121,956",
            "805,732,524,37,331"};

    public static ArrayList<ArrayList<Long>> mat = new ArrayList<>();
    public static int wid;
    public static int hei;
    public static void main(String[] args) throws IOException {
        int wi = 0;
        for(String s : util.Read.fRead("Euler/83.in")){
        //for(String s : matIn){
            mat.add(util.Read.listOfNumbers(s, ","));
            wi = mat.get(0).size();
        }
        hei = mat.size();
        wid = wi;
        System.out.println(wid + ", " + hei);
        System.out.println(pathDjikstra(0, 0, p -> p.x == wid-1 && p.y == hei-1));
    }

    public static long pathDjikstra(int startX, int startY, Predicate<DState> finished){
        Queue<DState> priq = new PriorityQueue<>(wid*hei, (o1, o2) -> (int)(o1.cost-o2.cost));
        Set<String> seen = new HashSet<>();
        priq.add(new DState(startX, startY, 0, seen));
        while(!priq.isEmpty()){
            DState cur = priq.poll();
            if(finished.test(cur)) return cur.cost;
            if(cur.x > 0 && !seen.contains((cur.x-1) + "," + (cur.y))) priq.add(new DState(cur.x-1, cur.y, cur.cost, seen));
            if(cur.y > 0 && !seen.contains((cur.x) + "," + (cur.y-1))) priq.add(new DState(cur.x, cur.y-1, cur.cost, seen));
            if(cur.x < wid - 1 && !seen.contains((cur.x+1) + "," + (cur.y))) priq.add(new DState(cur.x+1, cur.y, cur.cost, seen));
            if(cur.y < hei - 1 && !seen.contains((cur.x) + "," + (cur.y+1))) priq.add(new DState(cur.x, cur.y+1, cur.cost, seen));
        }
        return Long.MAX_VALUE;
    }

    public static class DState{
        public final int x;
        public final int y;
        public final long cost;
        public DState(int xs, int ys, long costCur, Set<String> visited){
            x = xs;
            y = ys;
            cost = costCur + mat.get(ys).get(xs);
            visited.add(x + "," + y);
        }
    }

}
