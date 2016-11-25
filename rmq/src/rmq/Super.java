package rmq;

/**
 * Care of:
 * https://www.topcoder.com/community/data-science/data-science-tutorials/range-minimum-query-and-lowest-common-ancestor/
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Super implements RMQ{

    Log blockQueryIndex = new Log();

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

    public void blockerize(int[] bin, int begin, int blockLen, int[][][] lookups){
        int desc = 0;
        int[][] mins = new int[blockLen][blockLen];
        int[][] vals = new int[blockLen][blockLen];
        int[][] tots = new int[blockLen][blockLen];
        for(int i=0; i<blockLen && i+begin < bin.length; i++){
            if(bin[begin+i] == 1) desc += (1<<i);
            if(i == 0) for(int j=0; j<blockLen; j++){
                mins[j][0] = j;
                vals[j][0] = bin[begin+i];
                tots[j][0] = bin[begin+i];
            }
            else for(int j=0; j+i<blockLen; j++){
                tots[j][i] = tots[j][i-1] + tots[j+i][0];
                if(vals[j][i-1] > tots[j][i]){
                    vals[j][i] = tots[j][i];
                    mins[j][i] = j+i;
                } else {
                    vals[j][i] = vals[j][i-1];
                    mins[j][i] = mins[j][i-1];
                }
            }
        }
        lookups[desc] = mins;
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
        // break the walk into blocks
        int blockLen = -1;
        while((1<<(blockLen+1)) <= n) blockLen++;
        int[] blockIdxs = new int[height.length/blockLen + 1];
        ArrayList<Integer> blockMins = new ArrayList<>(blockIdxs.length);
        for(int i=0; i<height.length; i++){
            if(i%blockLen == 0){
                blockMins.add(height[i]);
                blockIdxs[i/blockLen] = i;
            } else if(height[i] < blockMins.get(i/blockLen)){
                blockMins.set(i/blockLen, height[i]);
                blockIdxs[i/blockLen] = i;
            }
        }
        blockQueryIndex.preprocess(blockMins);
        // determine the minimum for any binary block

        System.out.println(blockLen + ", " + blockMins + " - " + show(blockIdxs));
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
