package freecellize;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TestState {

    @Test
    public void makeMoveChangesState(){
        State s = new State();
        s.columns.get(0).push("ah");
        s.makeMove("12");
        assertThat(s.columns.get(0).isEmpty(), is(true));
        assertThat(s.columns.get(1).size(), is(1));
    }

    @Test
    public void canMoveToFreecell(){
        State s = new State();
        s.columns.get(0).push("ah");
        s.makeMove("1b");
        assertThat(s.columns.get(0).isEmpty(), is(true));
        assertThat(s.freecells[1], is(notNullValue()));
        assertThat(s.freecells[0], is(nullValue()));
    }

    @Test
    public void canPlanMultiMove(){
        State s = new State();
        s.columns.get(0).push("kh");
        s.columns.get(0).push("qs");
        s.columns.get(0).push("jd");
        s.columns.get(1).push("kd");
        System.out.println(s.dump());
        s.makeMove("12");
        System.out.println(s.dump());
    }

}
