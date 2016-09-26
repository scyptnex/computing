package drwho.gallifreyan.english;

import drwho.gallifreyan.application.GlyphConverter;
import drwho.gallifreyan.glyph.ConsonantGlyph;
import drwho.gallifreyan.glyph.Glyph;
import drwho.gallifreyan.glyph.PunctuationGlyph;
import drwho.gallifreyan.glyph.VowelGlyph;

import java.util.stream.Stream;

/**
 * Splits out glyphs using a greedy search
 */
public class GreedyGlyphs implements GlyphConverter{

    @Override
    public Stream<Glyph> convert(String s) {
        Stream.Builder<Glyph> ret = Stream.builder();
        s = s.toLowerCase();
        for(int i=0; i<s.length(); i++){
            GetReturn re = get(s.charAt(i), i+1 >= s.length() ? ' ' : s.charAt(i+1));
            ret.accept(re.gly);
            if(re.twoChar) i++;
        }
        return ret.build();
    }

    public static class GetReturn{
        boolean twoChar;
        Glyph gly;
        private GetReturn(boolean two, Glyph g){
            gly = g;
            twoChar = two;
        }
    }

    /**
     * Convert two characters into a glyph
     * @param first the character to convert to a glyph
     * @param secondOrSpace
     * @return
     */
    public static GetReturn get(char first, char secondOrSpace){
        switch(first){
            // Vowels
            case 'a' : return new GetReturn(false, new VowelGlyph(VowelGlyph.Placement.BELOW, VowelGlyph.Placement.NO_ON));
            case 'e' : return new GetReturn(false, new VowelGlyph(VowelGlyph.Placement.NO_ON, VowelGlyph.Placement.NO_ON));
            case 'i' : return new GetReturn(false, new VowelGlyph(VowelGlyph.Placement.NO_ON, VowelGlyph.Placement.ABOVE));
            case 'o' : return new GetReturn(false, new VowelGlyph(VowelGlyph.Placement.ABOVE, VowelGlyph.Placement.NO_ON));
            case 'u' : return new GetReturn(false, new VowelGlyph(VowelGlyph.Placement.NO_ON, VowelGlyph.Placement.BELOW));
            //potentially double-consonants
            case 'c' : {
                switch(secondOrSpace){
                    case 'h' : return new GetReturn(true , ConsonantGlyph.CH);
                    // c is not allowed, should be K or S based on phonetics
                    // http://speakspeak.com/resources/pronunciation/when-to-pronounce-the-letter-c-as-s-or-k
                    case 'e' : return new GetReturn(false, ConsonantGlyph.S); // cents
                    case 'i' : return new GetReturn(false, ConsonantGlyph.S); // cinnamon
                    case 'y' : return new GetReturn(false, ConsonantGlyph.S); // cyan
                    default  : return new GetReturn(false, ConsonantGlyph.K);
                }
            }
            case 'g' : {
                if(secondOrSpace == 'h') return new GetReturn(true , ConsonantGlyph.GH);
                else                     return new GetReturn(false, ConsonantGlyph.G);
            }
            case 'n' : {
                if(secondOrSpace == 'g') return new GetReturn(true , ConsonantGlyph.NG);
                else                     return new GetReturn(false, ConsonantGlyph.N);
            }
            case 'p' : {
                if(secondOrSpace == 'h') return new GetReturn(true , ConsonantGlyph.PH);
                else                     return new GetReturn(false, ConsonantGlyph.P);
            }
            case 'q' : {
                if(secondOrSpace == 'u') return new GetReturn(true , ConsonantGlyph.QU);
                else                     return new GetReturn(false, ConsonantGlyph.QU); // QANTAS = "kwontehs"
            }
            case 's' : {
                if(secondOrSpace == 'h') return new GetReturn(true , ConsonantGlyph.SH);
                else                     return new GetReturn(false, ConsonantGlyph.S);
            }
            case 't' : {
                if(secondOrSpace == 'h') return new GetReturn(true , ConsonantGlyph.TH);
                else                     return new GetReturn(false, ConsonantGlyph.T);
            }
            case 'w' : {
                if(secondOrSpace == 'h') return new GetReturn(true , ConsonantGlyph.WH);
                else                     return new GetReturn(false, ConsonantGlyph.W);
            }
            //definitely single-consonants
            case 'b' : return new GetReturn(false, ConsonantGlyph.B);
            case 'd' : return new GetReturn(false, ConsonantGlyph.D);
            case 'f' : return new GetReturn(false, ConsonantGlyph.F);
            case 'h' : return new GetReturn(false, ConsonantGlyph.H);
            case 'j' : return new GetReturn(false, ConsonantGlyph.J);
            case 'k' : return new GetReturn(false, ConsonantGlyph.K);
            case 'l' : return new GetReturn(false, ConsonantGlyph.L);
            case 'm' : return new GetReturn(false, ConsonantGlyph.M);
            case 'r' : return new GetReturn(false, ConsonantGlyph.R);
            case 'v' : return new GetReturn(false, ConsonantGlyph.V);
            case 'x' : return new GetReturn(false, ConsonantGlyph.X);
            case 'y' : return new GetReturn(false, ConsonantGlyph.Y);
            case 'z' : return new GetReturn(false, ConsonantGlyph.Z);
            // punctuation
            case '.' : return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.CIRCS, 1));
            case ',' : return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.FILLED_CIRC, 1));
            case ';' : return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.DOTS , 1));
            case '?' : return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.DOTS , 2));
            case '!' : return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.DOTS , 3));
            case ':' : return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.CIRCS, 2));
            case '"' : return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.LINES, 1));
            case '\'': return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.LINES, 2));
            case '-' : return new GetReturn(false, new PunctuationGlyph(PunctuationGlyph.Style.LINES, 3));
            // we treat all unrecognisable characters as a '?'
            default  : return get('?', ' ');
        }
    }
}
