package p041t080;

import util.Collect;
import util.Permutation;

import java.util.List;

public class Euler068MagicPent implements Permutation.Permutatio<Integer>{

    public static final int[] DRAW = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static final int SIDES = DRAW.length/2;

    public static void main(String[] args){
        Permutation.permuteMinimal(Collect.fromArray(DRAW), new Euler068MagicPent());
        //System.out.println(permSize(Collect.fromArray(new int[]{3, 2, 1, 4, 6, 5})));
    }

    private long greatest = -1;

    @Override
    public boolean permutation(List<Integer> config) {
        //only configurations with 10 in the outer ring can be 16-digit
        if(config.size() == DRAW.length && config.get(SIDES) == DRAW.length){
            long pl = permSize(config);
            if(pl != -1 && pl > greatest){
                System.out.println(pl);
                greatest = pl;
            }
        }
        return true;
    }

    public static long permSize(List<Integer> config){
        String rep = "";
        // calculate the lowest outer index
        int minOuterIdx = SIDES;
        for(int i=SIDES+1; i<DRAW.length; i++) if(config.get(i) < config.get(minOuterIdx)) minOuterIdx = i;
        // proceeding clockwise from the midx
        int mGonSum = -1;
        for(int r=0; r<SIDES; r++){
            int curRunIdx = SIDES + (r+(minOuterIdx-SIDES))%SIDES;
            int curRunSum = config.get(curRunIdx) + config.get(curRunIdx%SIDES) + config.get((curRunIdx+1)%SIDES);
            if(mGonSum == -1) mGonSum = curRunSum;
            if(curRunSum != mGonSum) return -1;
            rep += config.get(curRunIdx) + "" + config.get(curRunIdx%SIDES) + "" + config.get((curRunIdx+1)%SIDES);
        }
        return Long.parseLong(rep);
    }
}
