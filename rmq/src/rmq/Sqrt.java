package rmq;

import java.util.ArrayList;

public class Sqrt implements RMQ{

    ArrayList<Integer> list;
    int[] mins;
    int sqrt;

    @Override
    public String getName() {
        return "Sqrt";
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append(sqrt).append(":[");
        for(int i=0; i<mins.length; i++){
            if(i != 0) ret.append(", ");
            ret.append(mins[i]);
        }
        return ret.append("]").toString();
    }

    @Override
    public void preprocess(ArrayList<Integer> currentTestCase) {
        list = currentTestCase;
        sqrt = (int)Math.ceil(Math.sqrt(list.size()));
        mins = new int[sqrt];
        //System.out.println(sqrt);
        int i = 0;
        for(int val : list){
            if(i%sqrt == 0) mins[i/sqrt] = val;
            else mins[i/sqrt] = Math.min(mins[i/sqrt], val);
            i++;
        }
    }

    @Override
    public int query(int low, int high) {
        if(high <= low) return -1;
        int lsq = low/sqrt;
        int hsq = high/sqrt;
        int min = list.get(low);
        //System.out.println(low + ", " + high + ", " + lsq + ", " + hsq + ", " + min);
        for (int i = low + 1; i < Math.min(lsq + 1, hsq) * sqrt; i++) min = Math.min(min, list.get(i));
        for (int i = Math.max(hsq * sqrt, low+1); i < high; i++) min = Math.min(min, list.get(i));
        for (int i = lsq + 1; i < hsq; i++) min = Math.min(min, mins[i]);
        return min;
    }
}
