package rmq;

import java.util.ArrayList;

public class Naive implements RMQ{

    private ArrayList<Integer> list;

    @Override
    public String getName() {
        return "Naive";
    }

    @Override
    public void preprocess(ArrayList<Integer> cur) {
        this.list = cur;
    }

    @Override
    public int query(int low, int high) {
        if(high <= low) return -1;
        int min = list.get(low);
        for(int i=low+1; i<high; i++){
            min = Math.min(min, list.get(i));
        }
        return min;
    }
}
