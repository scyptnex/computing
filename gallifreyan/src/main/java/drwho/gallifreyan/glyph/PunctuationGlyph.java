package drwho.gallifreyan.glyph;

/**
 * PunctuationGlyph
 */
public class PunctuationGlyph implements Glyph{

    public enum Style {
        DOTS, LINES, CIRCS, FILLED_CIRC;
    }

    public final Style style;
    public final int count;

    public PunctuationGlyph(Style style, int count){
        this.style = style;
        this.count = count;
    }

    @Override
    public void visitMe(Glyph.Visitor vis) {
        vis.visitPunctuation(this);
    }

}
