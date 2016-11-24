package rmq;

import java.util.ArrayList;

public interface RMQ {

    default String show(int[] arr){
        StringBuilder ret = new StringBuilder("[");
        for(int i=0; i<arr.length; i++){
            if(i != 0) ret.append(", ");
            ret.append(arr[i]);
        }
        return ret.append("]").toString();
    }

    String getName();

    void preprocess(ArrayList<Integer> cur);

    int query(int low, int high);

}
