package util;

import java.util.Arrays;

public class Vect {

    public final int dim;
    private final Numeral.Fraction[] mags;
    public final Numeral.Fraction scalar;

    public Vect(int ... elems){
        dim = elems.length;
        mags = new Numeral.Fraction[dim];
        Arrays.setAll(mags, i -> new Numeral.Fraction(elems[i]));
        scalar = new Numeral.Fraction(1);
    }
}
