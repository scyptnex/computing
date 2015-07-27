package util;

import java.util.ArrayList;
import java.util.List;

public class NumberTheory {

    public static List<Integer> surdCF(int surd){
        //when the continued fraction is 2x the
        ArrayList<RT> lst = new ArrayList<RT>();
        lst.add(new RT(surd));
        while(true){
            RT nxt = new RT(lst.get(lst.size() - 1));
            lst.add(nxt);
            if(nxt.exte == 2*lst.get(0).exte){
                break;
            }
        }
        ArrayList<Integer> ret = new ArrayList<>();
        for(RT rt : lst){
            ret.add(rt.exte);
        }
        return ret;
    }

    private static class RT{
        public final int root;
        public final int exte;
        public final int inte;
        public final int divi;
        public RT(int r){
            root = r;
            exte = (int)Math.floor(Math.sqrt(r));
            inte = exte;
            divi = 1;
        }

        public RT(RT prev){
            root = prev.root;
            if((prev.root - (prev.inte*prev.inte))%prev.divi != 0) throw new RuntimeException("Wierd divisi " + prev);
            divi = (prev.root - (prev.inte*prev.inte))/prev.divi;
            if(divi < 0) throw new RuntimeException("Negative divisi " + prev);
            int tempi = prev.inte;
            int tempe = 0;
            while(prev.inte - (tempe*divi) > -Math.sqrt(root)) tempe++;
            exte = tempe-1;
            inte = (exte*divi) - prev.inte;
        }

        @Override
        public String toString(){
            return exte + "+('" + root + "'-" + inte + ")/" + divi;
        }

        @Override
        public boolean equals(Object oth){
            if(oth instanceof RT){
                //System.out.println(this + " | " + oth);
                RT o = (RT)oth;
                return root == o.root && exte == o.exte && inte == o.inte && divi == o.divi;
            }
            return false;
        }
    }

}
