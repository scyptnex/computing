package drwho.gallifreyan.glyph;

/**
 * Glyphs for little vowel circles and their lines
 */
public class VowelGlyph implements Glyph {

    public enum Placement {
        ABOVE, BELOW, NO_ON;
    }

    public final Placement circPlacement;
    public final Placement linePlacement;

    public VowelGlyph(Placement circPlacement, Placement linePlacement){
        this.circPlacement = circPlacement;
        this.linePlacement = linePlacement;
    }

    @Override
    public void visitMe(Visitor vis) {
        vis.visitVowel(this);
    }

}
