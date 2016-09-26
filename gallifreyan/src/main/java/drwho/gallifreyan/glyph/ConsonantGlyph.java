package drwho.gallifreyan.glyph;

/**
 * Glyphs for minor-circles which make consonant sounds
 */
public class ConsonantGlyph implements Glyph{

    public static final ConsonantGlyph B  = new ConsonantGlyph(Style.OMEGA , Decoration.NONE , 0);
    public static final ConsonantGlyph CH = new ConsonantGlyph(Style.OMEGA , Decoration.DOTS , 2);
    public static final ConsonantGlyph D  = new ConsonantGlyph(Style.OMEGA , Decoration.DOTS , 3);
    public static final ConsonantGlyph G  = new ConsonantGlyph(Style.OMEGA , Decoration.LINES, 1);
    public static final ConsonantGlyph H  = new ConsonantGlyph(Style.OMEGA , Decoration.LINES, 2);
    public static final ConsonantGlyph F  = new ConsonantGlyph(Style.OMEGA , Decoration.LINES, 3);
    public static final ConsonantGlyph J  = new ConsonantGlyph(Style.ZENITH, Decoration.NONE , 0);
    public static final ConsonantGlyph PH = new ConsonantGlyph(Style.ZENITH, Decoration.DOTS , 1);
    public static final ConsonantGlyph K  = new ConsonantGlyph(Style.ZENITH, Decoration.DOTS , 2);
    public static final ConsonantGlyph L  = new ConsonantGlyph(Style.ZENITH, Decoration.DOTS , 3);
    public static final ConsonantGlyph N  = new ConsonantGlyph(Style.ZENITH, Decoration.LINES, 1);
    public static final ConsonantGlyph P  = new ConsonantGlyph(Style.ZENITH, Decoration.LINES, 2);
    public static final ConsonantGlyph M  = new ConsonantGlyph(Style.ZENITH, Decoration.LINES, 3);
    public static final ConsonantGlyph T  = new ConsonantGlyph(Style.BUMP  , Decoration.NONE , 0);
    public static final ConsonantGlyph WH = new ConsonantGlyph(Style.BUMP  , Decoration.DOTS , 1);
    public static final ConsonantGlyph SH = new ConsonantGlyph(Style.BUMP  , Decoration.DOTS , 2);
    public static final ConsonantGlyph R  = new ConsonantGlyph(Style.BUMP  , Decoration.DOTS , 3);
    public static final ConsonantGlyph V  = new ConsonantGlyph(Style.BUMP  , Decoration.LINES, 1);
    public static final ConsonantGlyph W  = new ConsonantGlyph(Style.BUMP  , Decoration.LINES, 2);
    public static final ConsonantGlyph S  = new ConsonantGlyph(Style.BUMP  , Decoration.LINES, 3);
    public static final ConsonantGlyph TH = new ConsonantGlyph(Style.PHI   , Decoration.NONE , 0);
    public static final ConsonantGlyph GH = new ConsonantGlyph(Style.PHI   , Decoration.DOTS , 1);
    public static final ConsonantGlyph Y  = new ConsonantGlyph(Style.PHI   , Decoration.DOTS , 2);
    public static final ConsonantGlyph Z  = new ConsonantGlyph(Style.PHI   , Decoration.DOTS , 3);
    public static final ConsonantGlyph QU = new ConsonantGlyph(Style.PHI   , Decoration.LINES, 1);
    public static final ConsonantGlyph X  = new ConsonantGlyph(Style.PHI   , Decoration.LINES, 2);
    public static final ConsonantGlyph NG = new ConsonantGlyph(Style.PHI   , Decoration.LINES, 3);

    public enum Style {
        ZENITH, PHI, OMEGA, BUMP;
    }

    public enum Decoration {
        NONE, DOTS, LINES;
    }

    public final Style style;
    public final Decoration decoration;
    public final int decoCount;

    private ConsonantGlyph(Style style, Decoration decoration, int decoCount){
        this.style = style;
        this.decoration = decoration;
        this.decoCount = decoration == Decoration.NONE ? 0 : decoCount;
    }

    @Override
    public void visitMe(Visitor vis) {
        vis.visitConsonant(this);
    }

}
