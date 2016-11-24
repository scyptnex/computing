package rmq;

import java.util.ArrayList;

public class Super implements RMQ{
    @Override
    public String getName() {
        return "Super";
    }

    @Override
    public void preprocess(ArrayList<Integer> cur) {

    }

    @Override
    public int query(int low, int high) {
        return 0;
    }
}
