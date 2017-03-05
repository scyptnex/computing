package freecellize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: nic
 * Date: 4/03/17
 */
public class CardIsh {

    public static final int SMALL_HEIGHT = 17;
    public static final int SMALL_WIDTH = 69;

    public final int[] popcount;
    public final BitSet colourMap;

    public CardIsh(String code) throws IOException {
        this(Misc.getCardSmall(code));
    }

    public CardIsh(BufferedImage card){
        this(card, 0, 0);
    }

    public CardIsh(BufferedImage main, int xo, int yo){
        popcount = new int[]{0,0,0,0};
        colourMap = new BitSet(SMALL_HEIGHT*SMALL_WIDTH*2);
        int[] cols = new int[4];
        for(int xc=xo; xc<xo+SMALL_WIDTH; xc++){
            for(int yc=yo; yc<yo+SMALL_HEIGHT; yc++){
                main.getRaster().getPixel(xc, yc, cols);
                int nc = Misc.closestColour(cols);
                popcount[nc]++;
                colourMap.set(((xc-xo) + (yc-yo)*SMALL_WIDTH)*2, nc%2==1);
                colourMap.set(((xc-xo) + (yc-yo)*SMALL_WIDTH)*2 + 1, nc/2==1);
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer ret = new StringBuffer();
        for(int i=0; i<popcount.length; i++) ret.append(popcount[i] + " ");
        ret.append(colourMap.size()).append(" ").append(colourMap.cardinality()).append(" ");
        ret.append(bitsetToHex(colourMap));
        return ret.toString();
    }

    /**
     * Returns true if the pop-count histogram has at most 10 pixels in error
     */
    public boolean similarEnoughFast(CardIsh other){
        int err = 0;
        for(int i=0; i<popcount.length; i++) err += Math.abs(popcount[i] - other.popcount[i]);
        return err <= 10;
    }

    public int getError(CardIsh other) {
        BitSet chk = new BitSet(this.colourMap.size());
        chk.or(this.colourMap);
        chk.xor(other.colourMap);
        return chk.cardinality();
    }

    public static void main(String[] args) throws IOException {
        BufferedImage smp = Misc.getSamplePic();
        BufferedImage kng = Misc.getKingPic();
        Rectangle king = Main.getBestLocationOfSubimage(smp, kng);
        System.out.println(king);
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

    public static String bitsetToHex(BitSet s){
        StringBuffer sb = new StringBuffer();
        for(long l : s.toLongArray()){
            sb.append(Long.toHexString(l));
        }
        return sb.toString();
    }

    private static Map<String, CardIsh> known = null;
    public static String mostSimilar(CardIsh toMe){
        if(known == null){
            long t = System.currentTimeMillis();
            known = Stream.of("a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k")
                    .flatMap(n -> Stream.of("s", "c", "d", "h").map(s -> n + s))
                    .map(c -> {
                        try {
                            return new Object[]{c, new CardIsh(c)};
                        } catch (IOException e) {
                            return new Object[0];
                        }
                    }).collect(Collectors.toMap(oa -> (String)oa[0], oa -> (CardIsh)oa[1], (c1, c2) -> c1, HashMap::new));
            Misc.logTime(t, "Building the card-ish map");
        }
        return known.entrySet().stream()
                .filter(p -> toMe.similarEnoughFast(p.getValue()))
                //.peek(oa -> System.out.println(oa.getKey() + " - " + oa.getValue()))
                .map(p -> new Object[]{p.getKey(), toMe.getError(p.getValue())})
                .min((oa1, oa2) -> Integer.compare((Integer)oa1[1], (Integer)oa2[1]))
                .map(oa -> (String)oa[0])
                .get();
    }

}
