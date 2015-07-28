package p041t080;

import util.Permutation;

import java.util.ArrayList;
import java.util.List;

public class Euler061ShapedCycle implements Permutation.Permutatio<Integer>{

    public static final int[] coeffs = {0, 0, 0, 1, 1, 3, 2, 5, 3};
    public static final int[] addits = {0, 0, 0, 1, 0, -1, -1, -3, -2};
    public static final int[] diviss = {0, 0, 0, 2, 1, 2, 1, 2, 1};

    public static void main(String[] args){
        for(int s=3; s<=8; s++){
            for(int i=1; i<10; i++) {
                int g = gen(s, i);
                System.out.print(g + " " + revGen(s, g) + ", ");
            }
            System.out.println();
        }
        ArrayList<Integer> perms = new ArrayList<>();
        perms.add(4);
        perms.add(5);
        perms.add(6);
        perms.add(7);
        perms.add(8);
        Euler061ShapedCycle e = new Euler061ShapedCycle();
        e.size = perms.size();
        Permutation.permuteMinimal(perms, e);
        //find(new int[3], 0);
    }

    public int size = -1;

    @Override
    public boolean permutation(List<Integer> config) {

        if(config.size() != size) return true;
        int[] seqs = new int[size+1];
        int[] chks = new int[size+1];
        seqs[0] = 3;
        for(int i=1; i<seqs.length; i++) seqs[i] = config.get(i-1);
        System.out.println(config);
        find(chks, seqs, 0);
        return true;
    }


    public static void find(int[] chks, int[] seqs, int cur){
        if(cur >= chks.length){
            if(chks[0]/100 == chks[chks.length-1]%100){
                int sum = 0;
                for(int i=0; i<chks.length; i++){
                    sum += chks[i];
                    System.out.println(chks[i] + " - " + revGen(seqs[i], chks[i]) + "," + seqs[i]);
                }
                System.out.println(sum);
                System.exit(0);
            }
            else return;
        }
        int lb = (cur == 0 ? 1000 : (chks[cur-1]%100)*100);
        if(lb < 1000) return;
        int ub = (cur == 0 ? 10000 : lb + 100);
        for(int c=(int)Math.ceil(revGen(seqs[cur], lb)); gen(seqs[cur], c) < ub; c++){
            chks[cur] = gen(seqs[cur], c);
            find(chks, seqs, cur+1);
        }
    }

    public static int gen(int seq, int n){
        return (n*(coeffs[seq]*n + addits[seq]))/diviss[seq];
    }

    public static double revGen(int seq, int res){
        return poly(coeffs[seq], addits[seq], -res*diviss[seq]);
    }

    public static double poly(int a, int b, int c){
        double srd = Math.sqrt(b*b - 4*a*c);
        return (-b+srd)/(double)(2*a);
    }
}
