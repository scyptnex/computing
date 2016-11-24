package rmq;

/**
 * Care of:
 * https://www.topcoder.com/community/data-science/data-science-tutorials/range-minimum-query-and-lowest-common-ancestor/
 */

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Super implements RMQ{

    @Override
    public String getName() {
        return "Super";
    }

    public static int eulerWalk(int[][] tree, int[] walk, int[] height, int[] occ, int idx, int cur, int curH){
        if(occ[cur] == -1) occ[cur] = idx;
        walk[idx] = cur;
        height[idx++] = curH;
        if(tree[1][cur] != -1){
            idx = eulerWalk(tree, walk, height, occ, idx, tree[1][cur], curH+1);
            walk[idx] = cur;
            height[idx++] = curH;
        }
        if(tree[2][cur] != -1){
            idx = eulerWalk(tree, walk, height, occ, idx, tree[2][cur], curH+1);
            walk[idx] = cur;
            height[idx++] = curH;
        }
        return idx;
    }

    @Override
    public void preprocess(ArrayList<Integer> cur) {
        final int n = cur.size();
        // build the tree version
        int[][] t = new int[3][n]; // the tree is 3 arrays: parent, left and right
        int[] st = new int[n];
        int top = -1;
        for(int i=0; i<n; i++){
            t[1][i] = -1;
            t[2][i] = -1;
            int k = top;
            while(k >= 0 && cur.get(st[k]) > cur.get(i)) k--;
            if(k != -1) t[0][i] = st[k];
            if(k < top) t[0][st[k+1]] = i;
            top = ++k;
            st[top] = i;
        }
        t[0][st[0]] = -1;
        // determine the left/right children of the tree
        int root = 0;
        for(int i=0; i<n; i++){
            if(t[0][i] == -1) root = i;
            else if(i < t[0][i]) t[1][t[0][i]] = i;
            else t[2][t[0][i]] = i;
        }
        System.out.println(cur + "\n" + show(t[0]) + "\n" + show(t[1]) + "\n" + show(t[2]));
        // construct the euler walk
        int[] walk = new int[2*n-1];
        int[] height = new int [2*n-1];
        int[] occ = new int[n];
        for(int i=0; i<n; i++) occ[i] = -1;
        eulerWalk(t, walk, height, occ, 0, root, 1);
        System.out.println(show(walk) + "\n" + show(height) + "\n" + show(occ));
        //convert height to bin
        int[] bin = new int[2*n-2];
        for(int i=0; i<2*n-2; i++){
            bin[i] = height[i]-height[i+1];
        }
        System.out.println(show(bin));
    }

    @Override
    public int query(int low, int high) {
        return 0;
    }

    public static void main(String[] args){
        ArrayList<Integer> arr = Stream.of(2, 4, 3, 1, 6, 7, 8, 9, 1, 7).collect(Collectors.toCollection(ArrayList<Integer>::new));
        RMQ q = new Super();
        q.preprocess(arr);
    }
}
