package p081t120;

import util.Numeral;

public class E091TriGrid {

    public static final int SIZ = 50;

    public static void main(String[] args){
        long cou = 0;
        for(int x1=0; x1 <= SIZ; x1++){
            for(int y1=0; y1 <= SIZ; y1++){
                for(int x2=x1; x2 <= SIZ; x2++){
                    for(int y2=(x2 == x1 ? y1 : 0); y2 <= SIZ; y2++) if (isRat(x1, y1, x2, y2)){
                        //System.out.println(String.format("(%d,%d) (%d,%d)", x1, y1, x2, y2));
                        cou++;
                    }
                }
            }
        }
        System.out.println(cou);
    }

    public static boolean isRat(int xa, int ya, int xb, int yb){
        if(xa == xb && ya == yb) return false;
        if((xa == 0 && ya == 0) || (xb == 0 && yb == 0)) return false;
        return isRightAngled(xa, ya, xb, yb) ||
                isRightAngled(xa, ya, xa-xb, ya-yb) ||
                isRightAngled(xa-xb, ya-yb, xb, yb);
    }

    public static boolean isRightAngled(int xa, int ya, int xb, int yb){
        int x1 = xa / (int)Numeral.gcd(xa, ya);
        int y1 = ya / (int)Numeral.gcd(xa, ya);
        int x2 = xb / (int)Numeral.gcd(xb, yb);
        int y2 = yb / (int)Numeral.gcd(xb, yb);
        return (x1 == y2 && y1 == -x2) || (x1 == -y2 && y1 == x2);
    }

}
