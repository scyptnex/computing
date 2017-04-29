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
import java.io.PrintStream;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        int maxGames = 1;
        if(args.length>0) maxGames = Integer.parseInt(args[0]);
        Screenterface screen = new Screenterface();

        selectFreecellWindow(screen); //find the location of the freecell window

        BufferedImage kingPic = Misc.getKingPic();
        BufferedImage board = screen.screenGrab();
        Rectangle kng = getBestLocationOfSubimage(board, kingPic);

        int games = 0;
        while(games < maxGames){
            System.out.println("========================================================================");
            System.out.printf( "| Game %13d                                                   |\n", games);
            System.out.println("========================================================================");
            if(games != 0){
                screen.returnKey();
                Thread.sleep(1000);
            }
            double t = System.currentTimeMillis();
            solve(kng, screen);
            System.out.printf("Time: %.2f\n", (System.currentTimeMillis()-t)/1000.0d);
            System.out.print("Press tab to exit in");
            for(int tim=5; tim>=0; tim--){
                System.out.print(" " + tim);
                Thread.sleep(1000);
            }
            System.out.println();
            games++;
        }
    }

    public static void selectFreecellWindow(Screenterface screen) throws IOException, InterruptedException {
        try {
            Process p = new ProcessBuilder().command("wmctrl", "-a", "Freecell Game #").start();
            if (p.waitFor() != 0){
                System.err.println("wmctrl failed, using screengrab instead");
                throw new Exception();
            }
        } catch(Exception exc) {
            Rectangle rct = getBestLocationOfSubimage(screen.screenGrab(), Misc.getFreecellIcon());
            screen.lclick(rct.x + rct.width / 2, rct.y + rct.height / 2);
            Thread.sleep(500);
        }
    }

    public static void solve(Rectangle kng, Screenterface screen) throws IOException, InterruptedException {
        BufferedImage board = screen.screenGrab();
        String[][] codes = readStartState(screen, board, kng);
        State initial = new State(codes);
        System.out.println("Initial state");
        System.out.println(initial.dump());

        Process p = new ProcessBuilder()
                .command("fc-solve", "-m", "-snx").start();
        initial.printForSolver(new PrintStream(p.getOutputStream()));
        Scanner sca = new Scanner(p.getInputStream());
        p.getOutputStream().close();
        sca.nextLine();
        sca.nextLine();
        String ln = sca.nextLine();
        ArrayList<String> moves = new ArrayList<>();
        while(ln.length() > 0){
            String[] ms = ln.split(" ");
            for(String s : ms) if(s.length() > 0){
                moves.add(s);
            }
            ln = sca.nextLine();
        }
        sca.close();
        int ret = p.waitFor();
        if(ret != 0){
            throw new RuntimeException("Couldnt find a solution");
        }
        /*
         * Normalise the moves into a list of 1-card movements
         */
        State actual = new State(initial);
        List<Character> tmpClicks = new ArrayList<>();
        for(String mv : moves){
            tmpClicks.addAll(initial.makeMove(mv));
        }
        initial = actual;
        moves = new ArrayList<>(tmpClicks.size()/2);
        Character cur = null;
        for(Character c : tmpClicks){
            if(cur == null){
                cur = c;
            } else {
                if(moves.size()%24 == 0) System.out.println();
                moves.add("" + cur + (c >= 'h' && c <= 'k' ? "h" : c));
                System.out.print(moves.get(moves.size()-1) + " ");
                cur = null;
            }
        }
        System.out.println();
        System.out.println("Moves: " + moves.size());
        /*
         * play the actual game
         */
        //List<String> autos = new ArrayList<>();
        int delay = 120;
        char lastClick = 'x'; // just something that isnt a real click
        int[] ghostPieces = new int[12];//0-7 are columns, 8-11 are freecells
        Arrays.setAll(ghostPieces, i->0);
        for(int i=0; i<moves.size(); i++){

            // the move
            String m = moves.get(i);
            char ms = m.charAt(0);
            char md = m.charAt(1);
            int ghostIndex = (ms <= '8' && ms >= '1' ? ms-'1' : ms-'a'+8);


//            System.out.println("===========================");
//            System.out.println(String.format("%3d %3s %s", i, m, ""));
//            System.out.println();
//            System.out.println(initial.dump());
//            System.out.println();
//            System.out.println("Ghosting: " + (ghostPieces[ghostIndex] == 0));

            // make the move internally and find out its clicks
            List<Character> clicks = initial.makeMove(m);

            // check if we are trying to move a ghost piece
            if(ghostPieces[ghostIndex] == 0){
                for (Character c : clicks) {
                    if (c == lastClick) {
                        // prevent the OS from interpreting a double click
                        screen.lclick(kng.x + kng.width / 2, kng.y + kng.height / 2);
                        Thread.sleep(delay);
                    }
                    lastClick = c;
                    Point loc = Misc.locate(c, kng);
                    screen.lclick(loc.x + Similariser.SMALL_WIDTH / 2, loc.y + Similariser.SMALL_HEIGHT / 2);
                    Thread.sleep(delay);
                }
            }
            ghostPieces = initial.autoMoveGhosts();
            // Make a big stack move then some of its cards ghost home before the move completes
            //if(i > 95) break;
        }
    }

    public static String[][] readStartState(Screenterface screen, BufferedImage board, Rectangle kng){
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
        return codes;
    }

    public static Rectangle getBestLocationOfSubimage(BufferedImage large, BufferedImage small){
        int sw = small.getWidth();
        int sh = small.getHeight();
        long s = System.currentTimeMillis();
        double[] smallHist = calcHist(small, 0, 0, sw, sh);
        int[] topLeftOfSmall = new int[4];
        small.getRaster().getPixel(0, 0, topLeftOfSmall);
        int[] centerOfSmall = new int[4];
        small.getRaster().getPixel(sw/2, sh/2, centerOfSmall);
        double[] loc = IntStream.range(0, large.getWidth()-sw).boxed()
                .flatMap(x -> IntStream.range(0, large.getHeight() - sh).mapToObj(y -> new int[]{x, y}))
                .parallel()
                .filter(a -> similarPixel(a[0], a[1], large, topLeftOfSmall))
                .filter(a -> similarPixel(a[0]+sw/2, a[1]+sh/2, large, centerOfSmall))
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
