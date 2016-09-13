package drwho.gallifreyan.application;

import java.util.stream.Stream;

/**
 * Splits the text into the sentence units, which appear as large double-ringed circles in Sherman's Gallifreyan
 * This split is not as trivial as just "find the full stops", We may want to split on different things (like lines in
 * a poem).  Also who says it has to be english anyway?
 */
public interface SentenceSplitter {

    /**
     * This is basically a buffer which takes unstructured text in any form (like lines in a file) and structures it
     * into sentences.
     * @param in The unstructured stream of input text
     * @return a stream of sentence strings (each of which will become one gallifreyan major-circle)
     */
    Stream<String> split(Stream<String> in);

}
