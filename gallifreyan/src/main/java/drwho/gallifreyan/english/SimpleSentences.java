package drwho.gallifreyan.english;

import drwho.gallifreyan.application.SentenceSplitter;

import java.util.stream.Stream;

/**
 * splits sentences based on full stops.
 * Author: nic
 *   Date: 15/09/16
 */
public class SimpleSentences implements SentenceSplitter{
    @Override
    public Stream<String> split(Stream<String> in) {
        final StringBuffer sb = new StringBuffer();
        final Stream.Builder<String> bld = Stream.builder();
        in.flatMapToInt(String::chars).sequential().forEach(i -> {
            if(i == '.'){
                bld.accept(sb.toString());
                sb.delete(0, sb.length());
            } else {
                sb.append((char)i);
            }
        });
        return bld.add(sb.toString()).build().map(String::trim).filter(s -> s.length() > 0);
    }
}
