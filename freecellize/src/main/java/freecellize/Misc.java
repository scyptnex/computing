package freecellize;

import javax.imageio.ImageIO;
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

    public static void logImage(BufferedImage img, String logName) throws IOException {
        ImageIO.write(img, "png", new File(logName + ".png"));
    }

    public static final int RED   = 0; // 255   0   0
    public static final int BLACK = 1; //   0   0   0
    public static final int WHITE = 2; // 255 255 255
    public static final int GREEN = 3; //   0 127   0
    public static int closestColour(int[] cvals){
        int[] mins = new int[4];
        mins[RED  ] = Math.abs(255 - cvals[0]) + Math.abs(  0 - cvals[1]) + Math.abs(  0 - cvals[2]);
        mins[BLACK] = Math.abs(  0 - cvals[0]) + Math.abs(  0 - cvals[1]) + Math.abs(  0 - cvals[2]);
        mins[WHITE] = Math.abs(255 - cvals[0]) + Math.abs(255 - cvals[1]) + Math.abs(255 - cvals[2]);
        mins[GREEN] = Math.abs(  0 - cvals[0]) + Math.abs(127 - cvals[1]) + Math.abs(  0 - cvals[2]);
        int mn = 0;
        for(int i=1; i<mins.length; i++){
            if(mins[i] < mins[mn]) mn = i; // bias the earlier colours, so that magenta looks red not white
        }
        return mn;
    }

    public static int[] getPx(BufferedImage img, int x, int y){
        int[] ret = new int[4];
        img.getRaster().getPixel(x, y, ret);
        return ret;
    }

}
