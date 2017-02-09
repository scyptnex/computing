package foo.bar.canva;

/**
 * Similar notion to an exception but people arent supposed to catch these so not extending that class
 */
public abstract class QueueResponse {

    private final String genericMessage;

    public QueueResponse(String genericMessage){
        this.genericMessage = genericMessage;
    }

}
