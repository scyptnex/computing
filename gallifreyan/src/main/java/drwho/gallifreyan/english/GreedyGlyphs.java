package drwho.gallifreyan.english;

import drwho.gallifreyan.application.GlyphConverter;
import drwho.gallifreyan.glyph.Glyph;

import java.util.stream.Stream;

/**
 * Splits out glyphs using a greedy search
 *
 * Author: nic
 * Date: 17/09/16
 */
public class GreedyGlyphs implements GlyphConverter{

    @Override
    public Stream<Glyph> convert(String s) {
        Stream.Builder<Glyph> ret = Stream.builder();
        s = s.toLowerCase();
        int idx = 0;
        while(idx < s.length()){
            switch (s.charAt(idx)){
                case 'c' :{
                    break;
                }
                case 'n' :{
                    break;
                }
                case 'q' :{
                    break;
                }
                case 's' :{
                    break;
                }
                case 't' :{
                    break;
                }
            }
        }
        return ret.build();
    }

}
