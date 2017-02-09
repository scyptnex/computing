package foo.bar.canva;

import foo.bar.canva.inmemory.MemoryQueue;
import foo.bar.canva.util.ReceiveNone;
import foo.bar.canva.util.ReceiveSuccess;
import foo.bar.canva.util.SendSuccess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class TestQueueInterface {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        MemoryQueue mc = new MemoryQueue();
        return Arrays.asList(new Object[][] {
                 { mc, mc }
           });
    }

    @Parameterized.Parameter
    public QueueSender sender;

    @Parameterized.Parameter(1)
    public QueueReceiver receiver;

    @Test
    public void canSend(){
        QueueData qd = new MockQueueData(0, "", 0);
        assertThat(sender.send(qd), instanceOf(QueueSendResponse.class));
    }

    @Test
    public void canReceive(){
        assertThat(receiver.dequeue(), instanceOf(QueueReceiveResponse.class));
    }

    @Test
    public void queueIsInitiallyEmpty(){
        assertThat(receiver.dequeue(), instanceOf(ReceiveNone.class));
    }

    @Test
    public void nonEmptyQueueCanBeDequeued(){
        QueueData qd = new MockQueueData(0, "", 0);
        assertThat(sender.send(qd), instanceOf(SendSuccess.class));
        assertThat(receiver.dequeue(), instanceOf(ReceiveSuccess.class));
    }

    @Test
    public void whatGoesInMustComeOut(){
        QueueData qd = new MockQueueData(0, "", 1000);
        assertThat(sender.send(qd), instanceOf(SendSuccess.class));
        QueueReceiveResponse rsp = receiver.dequeue();
        for(int i=0; i<1000; i++){
            assertThat("Index match for " + i, rsp.getResponse().getData()[i], is(qd.getData()[i]));
        }
    }

}
