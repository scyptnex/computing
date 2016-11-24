package rmq;

import java.util.ArrayList;

public class Log implements RMQ{

    int log;
    int[][] tabl;

    @Override
    public String getName() {
        return "Log";
    }

    @Override
    public void preprocess(ArrayList<Integer> cur) {
        log = 1;
        while(1<<log <= cur.size()) log++;
        tabl = new int[cur.size()][log+1];
        for(int i=0; i<cur.size(); i++) tabl[i][0] = cur.get(i);
        for(int k=1; 1<<k <= cur.size(); k++){
            for(int i=0; i + (1<<k) - 1 < cur.size(); i++){
                if(tabl[i][k-1] < tabl[i + (1<<(k-1))][k-1]){
                    tabl[i][k] = tabl[i][k-1];
                } else {
                    tabl[i][k] = tabl[i + (1<<(k-1))][k-1];
                }
            }
        }
    }

    @Override
    public int query(int low, int high) {
        if(low >= high) return -1;
        int ld = 0;
        while(1<<(ld+1) < high-low+1) ld++;
        //System.out.println(low + ", " + high + " - " + ld + " (" + log);
        if(tabl[low][ld] < tabl[high-(1<<ld)][ld]){
            return tabl[low][ld];
        } else {
            return tabl[high-(1<<ld)][ld];
        }
    }
}
