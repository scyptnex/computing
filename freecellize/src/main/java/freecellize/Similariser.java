package freecellize;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A 'hash' of an image, used to check similarity
 */
public class Similariser {

    public static final int SMALL_HEIGHT = 17;
    public static final int SMALL_WIDTH = 69;
    public static final int LARGE_HEIGHT = 94;

    public static final int RED   = 0; // 255   0   0
    public static final int BLACK = 1; //   0   0   0
    public static final int WHITE = 2; // 255 255 255
    public static final int GREEN = 3; //   0 127   0

    private static List<Similariser> smalls = null;
    private static List<Similariser> larges = null;

    public final String id;
    public final int[] popcount;
    public final BitSet colourMap;

    public Similariser(String identifier, BufferedImage img, int xo, int yo, int w, int h){
        this.id = identifier;
        popcount = new int[]{0,0,0,0};
        colourMap = new BitSet(w*h*2);
        int[] cols = new int[4];
        for(int x=0; x<w; x++){
            for(int y=0; y<h; y++){
                img.getRaster().getPixel(xo+x, yo+y, cols);
                int nc = Similariser.closestColour(cols);
                popcount[nc]++;
                colourMap.set((x + y*w)*2, nc%2==1);
                colourMap.set((x + y*w)*2 + 1, nc/2==1);
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
    public boolean similarEnoughFast(Similariser other){
        int err = 0;
        for(int i=0; i<popcount.length; i++) err += Math.abs(popcount[i] - other.popcount[i]);
        return err <= 10;
    }

    public int getError(Similariser other) {
        BitSet chk = new BitSet(this.colourMap.size());
        chk.or(this.colourMap);
        chk.xor(other.colourMap);
        return chk.cardinality();
    }

    public static String bitsetToHex(BitSet s){
        StringBuffer sb = new StringBuffer();
        for(long l : s.toLongArray()){
            sb.append(Long.toHexString(l));
        }
        return sb.toString();
    }

    private static String mostSimilar(Similariser candidate, List<Similariser> options){
        return options.stream()
                .filter(candidate::similarEnoughFast)
                //.peek(oa -> System.out.println(oa.getKey() + " - " + oa.getValue()))
                .map(p -> new Object[]{p, candidate.getError(p)})
                .min((oa1, oa2) -> Integer.compare((Integer)oa1[1], (Integer)oa2[1]))
                .map(oa -> ((Similariser)oa[0]).id)
                .get();
    }

    public static String mostSimilarLarge(Similariser toMe){
        if(larges == null){
            long t = System.currentTimeMillis();
            larges = Stream.of("ac", "ad", "ah", "as")
                    .map(c -> {
                        try {
                            return new Similariser(c, Misc.getCardLarge(c), 0, 0, SMALL_WIDTH, LARGE_HEIGHT);
                        } catch (IOException e) {
                            return null;
                        }
                    }).collect(Collectors.toList());
            Misc.logTime(t, "Building the large map");
        }
        return mostSimilar(toMe, larges);
    }

    public static String mostSimilarSmall(Similariser toMe){
        if(smalls == null){
            long t = System.currentTimeMillis();
            smalls = Stream.of("a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k")
                    .flatMap(n -> Stream.of("s", "c", "d", "h").map(s -> n + s))
                    .map(c -> {
                        try {
                            return new Similariser(c, Misc.getCardSmall(c), 0, 0, SMALL_WIDTH, SMALL_HEIGHT);
                        } catch (IOException e) {
                            return null;
                        }
                    }).collect(Collectors.toList());
            Misc.logTime(t, "Building the small map");
        }
        return mostSimilar(toMe, smalls);
    }

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

}
