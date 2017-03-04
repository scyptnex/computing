package freecellize;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Author: nic
 * Date: 4/03/17
 */
public class CardMaker {

    public static void main(String[] args) throws IOException {
        BufferedImage smp = Misc.getSamplePic();
        //BufferedImage kng = Misc.getKingPic();
        //Rectangle r = Main.getBestLocationOfSubimage(smp, kng);
        Rectangle r = new Rectangle(237, 82, 32, 32);
        System.out.println(r);
    }

    /**
     * finds the offset vs the supposed location of the king
     */
    public static Point locate(int row, int col){
        return new Point(0,0); // TODO
    }

    public static Point locate(int row, int col, Rectangle king){
        Point l = locate(row, col);
        return new Point(l.x + king.x, l.y + king.y);
    }

}
