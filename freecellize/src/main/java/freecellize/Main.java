/************************************************************************* 
 *                               Main.java                               *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Feb-26                                                     *
 *************************************************************************/

package freecellize;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        Screenterface screen = new Screenterface();
        //find the window with freecell
        Rectangle rct = getBestLocationOfSubimage(screen.screenGrab(), Misc.getFreecellIcon());
        //Rectangle rct = new Rectangle(101, 1058, 16, 16);
        screen.lclick(rct.x + rct.width/2, rct.y + rct.height/2);
        Thread.sleep(500);
        //find the location of the freecell window
        BufferedImage kingPic = Misc.getKingPic();
        int[] boardGreen = Misc.getPx(kingPic, 0, 0); // board colour might come in handy
        BufferedImage board = screen.screenGrab();
        Rectangle kng = getBestLocationOfSubimage(board, kingPic);
        //Rectangle kng = new Rectangle(1475, 196, 32, 32);
        String[][] codes = new String[8][7];
        for(int x=0; x<52; x++){
            Point l = Misc.locate(x/8, x%8, kng);
            Similariser oo = new Similariser(x + "", board, l.x, l.y, Similariser.SMALL_WIDTH, Similariser.SMALL_HEIGHT);
            String code = Similariser.mostSimilarSmall(oo);
            if(code.startsWith("a")){
                BufferedImage grab = screen.holdRightScreenGrab(l.x + (Similariser.SMALL_WIDTH/2), l.y + (Similariser.SMALL_HEIGHT)/2);
                Similariser big = new Similariser(x + "", grab, l.x, l.y, Similariser.SMALL_WIDTH, Similariser.LARGE_HEIGHT);
                code = Similariser.mostSimilarLarge(big);
            }
            codes[x%8][x/8] = code;
        }
        Process p = new ProcessBuilder()
                .command("fc-solve", "-m", "-snx").start();
        PrintWriter pw = new PrintWriter(p.getOutputStream());
        for(int col=0; col<8; col++){
            for(int row=0; row<7; row++) if (row < 6 || col < 4) {
                pw.print(String.format("%s ", codes[col][row].toUpperCase()));
            }
            pw.println();
        }
        Scanner sca = new Scanner(p.getInputStream());
        pw.close();
        sca.nextLine();
        sca.nextLine();
        String ln = sca.nextLine();
        while(ln.length() > 0){
            System.out.println(ln);
            ln = sca.nextLine();
        }
        sca.close();
        int ret = p.waitFor();
        if(ret != 0){
            throw new RuntimeException("Couldnt find a solution");
        }
    }

    public static Rectangle getBestLocationOfSubimage(BufferedImage large, BufferedImage small){
        int sw = small.getWidth();
        int sh = small.getHeight();
        long s = System.currentTimeMillis();
        double[] smallHist = calcHist(small, 0, 0, sw, sh);
        int[] topLeftOfSmall = new int[4];
        small.getRaster().getPixel(0, 0, topLeftOfSmall);
        double[] loc = IntStream.range(0, large.getWidth()-sw).boxed()
                .flatMap(x -> IntStream.range(0, large.getHeight() - sh).mapToObj(y -> new int[]{x, y}))
                .parallel()
                .filter(a -> similarPixel(a[0], a[1], large, topLeftOfSmall))
                .map(a -> new double[]{a[0], a[1], histSimilarity(calcHist(large, a[0], a[1], sw, sh), smallHist)})
                .filter(d -> d[2] < 0.3)
                //.sorted((d1, d2) -> Double.compare(d1[2], d2[2]))
                //.peek(d -> System.out.println(d[0] + ", " + d[1] + " = " + d[2]))
                .min((d1, d2) -> Double.compare(d1[2], d2[2])).get();
        Misc.logTime(s, "Finding the subimage");
        return new Rectangle((int)loc[0], (int)loc[1], sw, sh);
    }

    /**
     * checks the pixel in the image against a given value, returning true if every colour channel is within 16
     */
    public static boolean similarPixel(int x, int y, BufferedImage im, int[] px){
        int[] chk = Misc.getPx(im, x, y);
//        boolean ret = true;
        for(int i=0; i<3; i++){
            if(Math.abs(chk[i] - px[i]) > 16) return false;
        }
//        for(int i=0; i<3; i++){
//            System.out.print(px[i] + "-" + chk[i] + "  ");
//        }
//        System.out.println(ret);
        return true;
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
                .flatMap(x -> IntStream.range(yStart, yStart+w).mapToObj(y -> Misc.getPx(img, x, y)))
                .map(px -> px[0]/64 + (px[1]/64)*4 + (px[2]/64)*16)
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

}
