package drwho.gallifreyan.application;

import java.util.stream.Stream;

/**
 * The source of the sentence we are translating, could be a file, text input, streaming web data, etc
 */
public interface TextSource {

    /**
     * Get the text, we dont have to worry about structure yet, just get the right order or characters
     * @return the in-order stream of text
     */
    Stream<String> get();

}
