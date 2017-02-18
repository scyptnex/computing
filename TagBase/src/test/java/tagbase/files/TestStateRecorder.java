package tagbase.files;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestStateRecorder {

    @Test
    public void stringifiesEmptyMap(){
        assertThat(StateRecorder.itemToString(Collections.emptyMap()), is("{}"));
    }

    @Test
    public void stringifiesUnitMap(){
        assertThat(StateRecorder.itemToString(Collections.singletonMap("a", "b")), is("{\"a\":\"b\"}"));
    }

//    @Test
//    public void stringifiesMultiMap(){
//        // TODO maps dont have to obey your primitive human notions of order
//        Map<String, String> m = Stream.of(
//                new String[]{"a", "b"},
//                new String[]{"c", "d"})
//                .collect(Collectors.toMap(sa -> sa[0], sa->sa[1]));
//        assertThat(StateRecorder.itemToString(m), is("{\"a\":\"b\",\"c\":\"d\"}"));
//    }

    @Test
    public void mapifiesEmptyBraces(){
        Map<String, String> m = StateRecorder.stringToItem("{}");
        assertThat(m, is(notNullValue()));
        assertThat(m.isEmpty(), is(true));
    }

    @Test
    public void mapifiesNormalCases(){
        Map<String, String> m = StateRecorder.stringToItem("{\"a\":\"b\",\"x\":\"y\"}");
        assertThat(m.get("a"), is("b"));
        assertThat(m.get("x"), is("y"));
    }

    @Test
    public void mapifyInvalidReturnsNull(){
        Map<String, String> m = StateRecorder.stringToItem("");
        assertThat(m, is(nullValue()));
    }

}
