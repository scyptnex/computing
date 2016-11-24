package rmq;

import java.util.ArrayList;

public interface RMQ {

    String getName();

    void preprocess(ArrayList<Integer> cur);

    int query(int low, int high);

}
