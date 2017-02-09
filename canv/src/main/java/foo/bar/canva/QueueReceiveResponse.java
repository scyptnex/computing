package foo.bar.canva;

/**
 * Similar notion to an exception but people arent supposed to catch these so not extending that class
 */
public abstract class QueueReceiveResponse extends QueueResponse{

    private final QueueData data;

    public QueueReceiveResponse(String genericMessage, QueueData data) {
        super(genericMessage);
        this.data = data;
    }

    public QueueData getResponse() {
        return data;
    }
}
