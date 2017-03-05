package freecellize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Author: nic
 * Date: 4/03/17
 */
public class CardMaker {

    public static void main(String[] args) throws IOException {
        BufferedImage smp = Misc.getSamplePic();
        BufferedImage kng = Misc.getKingPic();
        Rectangle kl = Main.getBestLocationOfSubimage(smp, kng);
        for(int r=0; r<7; r++){
            for(int c=0; c<8; c++){
                Point oo = locate(r, c, kl);
                BufferedImage sub = smp.getSubimage(oo.x + 1, oo.y + 1, 69, 17);
                ImageIO.write(sub, "png", new File("PIC." + r + "." + c + ".png"));
            }
        }
    }

    // A full card is 70x95 (71x95)
    // A card tab is 70x18 (71x18
    // left of card to left of card is 78


    /**
     * finds the offset vs the supposed location of the king
     */
    public static Point locate(int row, int col){
        return new Point(78*col - 293, 18*row + 85);
    }

    public static Point locate(int row, int col, Rectangle king){
        Point l = locate(row, col);
        return new Point(l.x + king.x, l.y + king.y);
    }

}
