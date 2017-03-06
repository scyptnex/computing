package freecellize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Author: nic
 * Date: 26/02/17
 */
public class Misc {

    public static BufferedImage getFreecellIcon() throws IOException {
        ClassLoader cl = Misc.class.getClassLoader();
        return ImageIO.read(cl.getResourceAsStream("icon.png"));
    }

    public static BufferedImage getKingPic() throws IOException {
        ClassLoader cl = Misc.class.getClassLoader();
        return ImageIO.read(cl.getResourceAsStream("king.png"));
    }

    public static BufferedImage getSamplePic() throws IOException {
        ClassLoader cl = Misc.class.getClassLoader();
        return ImageIO.read(cl.getResourceAsStream("sample.png"));
    }

    public static BufferedImage getCardLarge(String code) throws IOException {
        ClassLoader cl = Misc.class.getClassLoader();
        return ImageIO.read(cl.getResourceAsStream("cards/" + code + ".png"));
    }

    public static BufferedImage getCardSmall(String code) throws IOException {
        if(code.equals("ac") || code.equals("as")){
            code = "black_ace";
        } else if(code.equals("ah") || code.equals("ad")){
            code = "red_ace";
        }
        return getCardLarge(code);
    }

    public static void logImage(BufferedImage img, String logName) throws IOException {
        ImageIO.write(img, "png", new File(logName + ".png"));
    }

    public static int[] getPx(BufferedImage img, int x, int y){
        int[] ret = new int[4];
        img.getRaster().getPixel(x, y, ret);
        return ret;
    }

    public static void logTime(long start, String msg){
        System.err.println(msg + " : " + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * finds the offset vs the supposed location of the king
     * A full card is 70x95 (71x95)
     * A card tab is 70x18 (71x18
     * left of card to left of card is 78
     */
    public static Point locate(int row, int col){
        return new Point(78*col - 292, 18*row + 86);
    }

    public static Point locate(int row, int col, Rectangle king){
        Point l = locate(row, col);
        return new Point(l.x + king.x, l.y + king.y);
    }

}
