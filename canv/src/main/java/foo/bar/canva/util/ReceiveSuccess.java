package foo.bar.canva.util;

import foo.bar.canva.QueueData;
import foo.bar.canva.QueueReceiveResponse;

public class ReceiveSuccess extends QueueReceiveResponse{
    public ReceiveSuccess(QueueData data) {
        super("100%", data);
    }
}
