package drwho.gallifreyan.glyph;

/**
 * This is the single pronouncable (or entonable...because punctuation) unit of Gallifreyan
 */
public interface Glyph {

    void visitMe(Visitor vis);

    interface Visitor {
        default void visit(Glyph g){
            g.visitMe(this);
        }
        void visitConsonant(ConsonantGlyph glyph);
        void visitVowel(VowelGlyph glyph);
        void visitPunctuation(PunctuationGlyph glyph);
    }

}
