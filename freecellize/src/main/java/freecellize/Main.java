/************************************************************************* 
 *                               Main.java                               *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Feb-26                                                     *
 *************************************************************************/

package freecellize;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        Screenterface screen = new Screenterface();
        BufferedImage fci = Misc.getFreecellIcon();
        BufferedImage begin = screen.screenGrab();
        Rectangle rct = getBestLocationOfSubimage(begin, fci);
        screen.lclick(rct.x + rct.width/2, rct.y + rct.height/2);
        Thread.sleep(500);
        BufferedImage fin = screen.screenGrab();
        Misc.logImage(fin, "FINAL");
    }

    public static Rectangle getBestLocationOfSubimage(BufferedImage large, BufferedImage small){
        int sw = small.getWidth();
        int sh = small.getHeight();
        long s = System.currentTimeMillis();
        double[] smallHist = calcHist(small, 0, 0, sw, sh);
        double[] loc = IntStream.range(0, large.getWidth()-sw).boxed()
                .flatMap(x -> IntStream.range(0, large.getHeight() - sh).mapToObj(y -> new int[]{x, y}))
                .parallel()
                .map(a -> new double[]{a[0], a[1], histSimilarity(calcHist(large, a[0], a[1], sw, sh), smallHist)})
                .filter(d -> d[2] < 0.3)
                //.sorted((d1, d2) -> Double.compare(d1[2], d2[2]))
                //.peek(d -> System.out.println(d[0] + ", " + d[1] + " = " + d[2]))
                .min((d1, d2) -> Double.compare(d1[2], d2[2])).get();
        logTime(s, "Finding the freecell window in the task bar");
        return new Rectangle((int)loc[0], (int)loc[1], sw, sh);
    }

    /**
     * Returns a histogram in the RGB colour space for a subimage with granularity of 4
     * i.e. each r g b is divided into 4 groups (bright, visible, dull, dark) and the proportion of each is
     * recorded in the output histogram
     * @param img the full image
     * @return the histogram
     */
    public static double[] calcHist(BufferedImage img, int xStart, int yStart, int w, int h){
        Map<Integer, Long> counts = IntStream.range(xStart, xStart+w).boxed()
                .flatMap(x -> IntStream.range(yStart, yStart+w).mapToObj(y -> {
                    int[] px = new int[4];
                    img.getRaster().getPixel(x, y, px);
                    return px;
                })).map(px -> px[0]/64 + (px[1]/64)*4 + (px[2]/64)*16)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        double[] ret = new double[64];
        Arrays.parallelSetAll(ret, i -> counts.getOrDefault(i, 0l)/256.0);
        return ret;
    }

    /**
     * zero is identical
     */
    public static double histSimilarity(double[] h1, double[] h2){
        return IntStream.range(0, h1.length).mapToDouble(i -> Math.abs(h1[i] - h2[i])).sum();
    }

    public static void logTime(long start, String msg){
        System.err.println(msg + " : " + (System.currentTimeMillis() - start) + "ms");
    }

}
