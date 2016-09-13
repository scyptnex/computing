package drwho.gallifreyan.application;

import drwho.gallifreyan.glyph.Glyph;

import java.util.stream.Stream;

/**
 * Turns a word in the normal language into a Gallifreyan "word" (minor-circle)
 */
public interface GlyphConverter {

    /**
     * converts the word (as a string without spaces) into the correct series of glyphs
     * @param s    the word (with no spaces), either a single punctuation character or several letters
     * @return a stream of Gallifreyan glyphs
     */
    Stream<Glyph> convert(String s);

}
