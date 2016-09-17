package drwho.gallifreyan.glyph;

/**
 * This is the single pronouncable (or entonable...because punctuation) unit of Gallifreyan
 */
public class Glyph {

    public enum BodyStyle {
        NONE,
        VOWEL,
        TOUCHING,
        SEMI,
        CROSSING,
        SEPARATE,
        PUNCTUATION;
    }

    public enum DetailStyle {
        LINES,
        DOTS,
        VOWELOID; // used for floating vowels (a and o) and punctuation "." "," and ":"
    }

    public final BodyStyle body;
    public final DetailStyle detail;
    public final int detailCount;
    public final boolean detailAbove;

    private Glyph(BodyStyle b, DetailStyle d, int dc, boolean da){
        body = b;
        detail = d;
        detailCount = dc;
        detailAbove = da;
    }

    /**
     * @param pattern the string to convert to a glyph
     * @return a glyph for this tring, or null if no glyph is recognised
     */
    public static Glyph get(String pattern){
        String s = pattern.toLowerCase() + " ";
        switch(s.charAt(0)){
            case 'a' : return new Glyph(BodyStyle.NONE, DetailStyle.VOWELOID, 1, false);
            case 'b' : return new Glyph(BodyStyle.TOUCHING, DetailStyle.VOWELOID, 0, false);
            case 'c' : {
                switch(s.charAt(1)){
                    case 'h' : return new Glyph(BodyStyle.TOUCHING, DetailStyle.DOTS, 2, false);
                    // c is not allowed, should be K or S based on phonetics
                }
                break;
            }
            case 'd' : return new Glyph(BodyStyle.TOUCHING, DetailStyle.DOTS, 3, false);
            case 'e' : return new Glyph(BodyStyle.VOWEL, DetailStyle.VOWELOID, 0, false);
            case 'f' : return new Glyph(BodyStyle.TOUCHING, DetailStyle.LINES, 3, true);
            case 'g' : return new Glyph(BodyStyle.TOUCHING, DetailStyle.LINES, 1, true);
            case 'h' : return new Glyph(BodyStyle.TOUCHING, DetailStyle.LINES, 2, true);
            case 'i' : return new Glyph(BodyStyle.VOWEL, DetailStyle.LINES, 1, true);
            case 'j' : return new Glyph(BodyStyle.SEPARATE, DetailStyle.VOWELOID, 0, false);
            case 'k' : return new Glyph(BodyStyle.SEPARATE, DetailStyle.DOTS, 2, false);
            case 'l' : return new Glyph(BodyStyle.SEPARATE, DetailStyle.DOTS, 3, false);
            case 'm' : return new Glyph(BodyStyle.TOUCHING, DetailStyle.LINES, 3, true);
            case 'n' : {
                switch(s.charAt(1)){
                    case 'h' : return new Glyph(BodyStyle.CROSSING, DetailStyle.LINES, 3, true);
                    case ' ' : return new Glyph(BodyStyle.SEPARATE, DetailStyle.LINES, 1, true);
                }
                break;
            }
            case 'o' : return new Glyph(BodyStyle.NONE, DetailStyle.VOWELOID, 1, true);
            case 'p' : return new Glyph(BodyStyle.SEPARATE, DetailStyle.LINES, 2, true);
            case 'q' : {
                switch(s.charAt(1)){
                    case 'u' : return new Glyph(BodyStyle.CROSSING, DetailStyle.LINES, 1, true);
                    //Supposedly not allowed, i expect this to be phonetically converted to QU
                }
                break;
            }
            case 'r' : return new Glyph(BodyStyle.SEMI, DetailStyle.DOTS, 3, false);
            case 's' : {
                switch(s.charAt(1)){
                    case 'h' : return new Glyph(BodyStyle.SEMI, DetailStyle.DOTS, 2, false);
                    case ' ' : return new Glyph(BodyStyle.SEMI, DetailStyle.LINES, 3, true);
                }
                break;
            }
            case 't' : {
                switch(s.charAt(1)){
                    case 'h' : return new Glyph(BodyStyle.CROSSING, DetailStyle.VOWELOID, 0, false);
                    case ' ' : return new Glyph(BodyStyle.SEMI, DetailStyle.VOWELOID, 0, false);
                }
                break;
            }
            case 'u' : return new Glyph(BodyStyle.VOWEL, DetailStyle.LINES, 1, false);
            case 'v' : return new Glyph(BodyStyle.SEMI, DetailStyle.LINES, 1, true);
            case 'w' : return new Glyph(BodyStyle.SEMI, DetailStyle.LINES, 2, true);
            case 'x' : return new Glyph(BodyStyle.CROSSING, DetailStyle.LINES, 2, true);
            case 'y' : return new Glyph(BodyStyle.CROSSING, DetailStyle.DOTS, 2, false);
            case 'z' : return new Glyph(BodyStyle.CROSSING, DetailStyle.DOTS, 3, false);
        }
        return null;
    }

}
