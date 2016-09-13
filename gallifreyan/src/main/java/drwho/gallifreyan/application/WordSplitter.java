package drwho.gallifreyan.application;

import java.util.stream.Stream;

/**
 * For identifying individual Gallifreyan words in the sentence
 */
public interface WordSplitter {

    /**
     * Splits a major-circle's sentence into individual words
     * @param in    A sentence which will become a major-circle
     * @return the stream of words (and punctuation) in the sentence
     */
    Stream<String> split(String in);

}
