package p041t080;

import util.Primer;

public class Euler060PrimeCat {

    public static Primer.Sieve sv = new Primer.Sieve(2000000000);//initial sieve size

    public static void main(String[] args){
        int[] cyc = new int[]{2, 2, 2, 2, 2};//2 appended to anything is not prime :)
        omniShuffle(cyc, 0);
        int sum = 0;
        for(int i=0; i<cyc.length; i++){
            System.out.print(cyc[i] + " ");
            sum += cyc[i];
        }
        System.out.println();
        System.out.println(sum);
    }

    public static void omniShuffle(int[] cyc, int idx){
        // i assume everything above me is fixed
        // only if i must exceed next does it prove next must be upshuffled
        if(idx+1 == cyc.length){
            cyc[idx] = sv.nextPrime(cyc[idx]);
            return;
        }
        do {
            if(cyc[idx] >= cyc[idx+1]){
                cyc[idx] = (idx == 0 ? 2 : sv.nextPrime(cyc[idx-1]));//first, make me as small as i can be
                omniShuffle(cyc, idx+1);//omniShuffle those above me
            } else {
                cyc[idx] = sv.nextPrime(cyc[idx]);
            }
        } while(!pfix(cyc, idx));
    }

    public static boolean pfix(int[] cyc, int checkFromIdx){
        for(int i=checkFromIdx+1; i<cyc.length; i++){
            long l = Long.parseLong(cyc[checkFromIdx] + "" + cyc[i]);
            if (l > sv.max_size) return false;
            if(!sv.isPrime((int)l)) return false;
            l = Long.parseLong(cyc[i] + "" + cyc[checkFromIdx]);
            if (l > sv.max_size) return false;
            if(!sv.isPrime((int)l)) return false;
        }
        return true;
    }

}
