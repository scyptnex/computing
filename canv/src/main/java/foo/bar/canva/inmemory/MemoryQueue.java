package foo.bar.canva.inmemory;

import foo.bar.canva.*;
import foo.bar.canva.util.ReceiveNone;
import foo.bar.canva.util.ReceiveSuccess;
import foo.bar.canva.util.SendFull;
import foo.bar.canva.util.SendSuccess;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MemoryQueue implements QueueSender, QueueReceiver{

    private final Queue<QueueData> internal;

    public MemoryQueue(){
        this.internal = new LinkedList<QueueData>();
    }

    public QueueReceiveResponse dequeue() {
        try{
            return new ReceiveSuccess(internal.remove());
        } catch (NoSuchElementException exc){
            return new ReceiveNone();
        }
    }

    public QueueSendResponse send(QueueData data) {
        try {
            internal.add(data);
            return new SendSuccess();
        } catch(IllegalStateException exc){
            return new SendFull();
        }
    }
}
