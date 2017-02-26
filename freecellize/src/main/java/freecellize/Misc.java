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

    public static void logImage(BufferedImage img, String logName) throws IOException {
        ImageIO.write(img, "png", new File(logName + ".png"));
    }

}
