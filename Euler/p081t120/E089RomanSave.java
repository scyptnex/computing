package p081t120;

import util.Numeral;
import util.Read;

import java.io.IOException;

public class E089RomanSave {

    public static void main(String[] args) throws IOException {
        int tot = Read.streamLines(E089RomanSave.class)
                .mapToInt(s -> s.length() - Numeral.toRoman(Numeral.fromRoman(s)).length())
                .sum();
        System.out.println(tot);
    }

}
