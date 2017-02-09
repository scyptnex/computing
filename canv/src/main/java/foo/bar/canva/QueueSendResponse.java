package foo.bar.canva;

/**
 * Similar notion to an exception but people arent supposed to catch these so not extending that class
 */
public abstract class QueueSendResponse extends QueueResponse{

    public QueueSendResponse(String genericMessage) {
        super(genericMessage);
    }
}
