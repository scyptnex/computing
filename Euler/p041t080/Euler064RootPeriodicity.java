package p041t080;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Euler064RootPeriodicity {

    static Set<Integer> squares = new HashSet<Integer>();

    public static void main(String[] args){
        int s = 0;
        while(s*s <= 10000){
            squares.add(s*s);
            s++;
        }
        int count = 0;
        for(int i=1; i<10000; i++) if(!squares.contains(i)){
            System.out.println("=== " + i + " ===");
            if (getPeriodicity(i) % 2 == 1) count ++;
        }
        System.out.println(count);
    }

    public static int getPeriodicity(int i){
        ArrayList<RT> lst = new ArrayList<RT>();
        lst.add(new RT(i));
        System.out.println(lst.get(0));
        while(true){
            RT nxt = new RT(lst.get(lst.size() - 1));
            for(int j=0; j<lst.size(); j++){
                if(lst.get(j).equals(nxt)){
                    return lst.size() - j;
                }
            }
            lst.add(nxt);
            System.out.println(nxt);
        }
    }

    public static class RT{
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
