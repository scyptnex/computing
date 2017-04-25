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
        s.makeMove("12");
        assertThat(s.columns.get(0).size(), is(1));
        assertThat(s.columns.get(1).size(), is(3));
    }

    @Test
    public void canPlanBigMove(){
        State s = new State();
        s.columns.get(0).push("kh");
        s.columns.get(0).push("qs");
        s.columns.get(0).push("jd");
        s.columns.get(0).push("10c");
        s.columns.get(0).push("9d");
        s.columns.get(0).push("8c");
        s.columns.get(0).push("7h");
        s.columns.get(1).push("kd");
        System.out.println(s.makeMove("12"));
        assertThat(s.columns.get(0).size(), is(1));
        assertThat(s.columns.get(1).size(), is(7));
    }

    @Test
    public void canPlanCompoundMove(){
        State s = new State();
        s.columns.get(0).push("kh");
        s.columns.get(0).push("qs");
        s.columns.get(0).push("jd");
        s.columns.get(0).push("10c");
        s.columns.get(0).push("9d");
        s.columns.get(0).push("8c");
        s.columns.get(0).push("7h");
        s.columns.get(0).push("6c");
        s.columns.get(0).push("5h");
        s.columns.get(1).push("kd");
        s.columns.get(4).push("4h");
        s.columns.get(5).push("4c");
        s.columns.get(6).push("4d");
        s.columns.get(7).push("4s");
        s.freecells[0] = "3c";
        s.freecells[1] = "3d";
        s.freecells[2] = "3h";
        System.out.println(s.makeMove("12"));
        assertThat(s.columns.get(0).size(), is(1));
        assertThat(s.columns.get(1).size(), is(9));
    }

}
