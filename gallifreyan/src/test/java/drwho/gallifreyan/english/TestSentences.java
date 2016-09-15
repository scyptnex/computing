package drwho.gallifreyan.english;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestSentences {

    @Test
    public void simpleSplitsOnFullStop(){
        List<String> split = new SimpleSentences().split(Stream.of("a.b")).collect(Collectors.toList());
        assertThat(split, is(Arrays.asList("a", "b")));
    }

    @Test
    public void simpleNoStopNoSplit(){
        List<String> split = new SimpleSentences().split(Stream.of("a","b","c")).collect(Collectors.toList());
        assertThat(split, is(Collections.singletonList("abc")));
    }

    @Test
    public void simpleWrapsSplitAcrossLines(){
        List<String> split1 = new SimpleSentences().split(Stream.of("a","b.c","d")).collect(Collectors.toList());
        assertThat(split1, is(Arrays.asList("ab","cd")));
        List<String> split2 = new SimpleSentences().split(Stream.of("a",".bc","d")).collect(Collectors.toList());
        assertThat(split2, is(Arrays.asList("a","bcd")));
        List<String> split3 = new SimpleSentences().split(Stream.of("a","bc.","d")).collect(Collectors.toList());
        assertThat(split3, is(Arrays.asList("abc","d")));
    }

}
