package drwho.gallifreyan.application;

import drwho.gallifreyan.glyph.Glyph;
import drwho.gallifreyan.visual.Plan;

import java.util.stream.Stream;

/**
 * Determine how a whole body of text will be drawn.
 */
public interface Arranger {

    /**
     * Decide how a body of Gallifreyan text will be rendered.  For computability's sake the arranger might decide
     * that it (for exmaple) shouldnt arrange texts by having rays cast between letters in completely different
     * chapters of a book.  Alternatively, if there are few enough sentences in the text, it might try to arrange them
     * as a single large drawing, or it can break them up any way it wants.  Each Plan in the stream of returned plans
     * is expected to be a different image (screen on the computer, mural on the wall, etc)
     * @param in    The text, as a stream of sentences (a stream of words [a stream of glyphs])
     * @return A series of plans for rendering the text.
     */
    Stream<Plan> arrange(Stream<Stream<Stream<Glyph>>> in);

}
