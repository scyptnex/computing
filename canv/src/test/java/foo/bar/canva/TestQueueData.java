package foo.bar.canva;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TestQueueData {

    @Test
    public void hasID(){
        QueueData qd = new QueueData(0, "", new byte[]{-128, 0, 127});
        assertThat(qd.getId(), is(0l));
    }

    @Test
    public void hasName(){
        QueueData qd = new QueueData(0, "", new byte[]{-128, 0, 127});
        assertThat(qd.getName(), is(""));
    }

    @Test
    public void hasData(){
        QueueData qd = new QueueData(0, "", new byte[]{-128, 0, 127});
        assertThat(qd.getData(), is(new byte[]{-128, 0, 127}));
    }
}
