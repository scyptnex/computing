package drwho.gallifreyan.english;

import drwho.gallifreyan.application.WordSplitter;

import java.util.stream.Stream;

/**
 * Splits on words, which are sequences of alphanums, or single punctuation characters
 * Author: nic
 * Date: 15/09/16
 */
public class SimpleWords implements WordSplitter {
    @Override
    public Stream<String> split(String in) {
        StringBuffer sb = new StringBuffer();
        Stream.Builder<String> bld = Stream.builder();
        for (char c : in.toCharArray()) {
            if (c == ' ') {
                bld.accept(sb.toString());
                sb = new StringBuffer();
            } else if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            } else {
                bld.accept(sb.toString());
                bld.accept("" + c);
                sb = new StringBuffer();
            }
        }
        return bld.add(sb.toString()).build().map(String::trim).filter(s -> s.length() > 0);
    }
}
