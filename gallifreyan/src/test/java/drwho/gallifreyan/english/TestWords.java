package drwho.gallifreyan.english;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestWords {

    @Test
    public void simpleSpacesMakeWords(){
        List<String> words = new SimpleWords().split("aa b ccc").collect(Collectors.toList());
        assertThat(words, is(Arrays.asList("aa", "b", "ccc")));
    }

    @Test
    public void simpleWordsCanBeAlphaNum(){
        List<String> words = new SimpleWords().split("11141 b432cdz23 1ccc08").collect(Collectors.toList());
        assertThat(words, is(Arrays.asList("11141", "b432cdz23", "1ccc08")));
    }

    @Test
    public void simplePunctuationSplits(){
        List<String> words = new SimpleWords().split("if \"i\" we're a, rich-man").collect(Collectors.toList());
        assertThat(words, is(Arrays.asList("if", "\"", "i", "\"", "we", "'", "re", "a", ",", "rich", "-", "man")));
    }

}
