package foo.bar.canva;

/**
 * Up to the spec exactly what these queues are supposed to hold, throwing in a generic class for now
 */
public class QueueData {

    private final long id;
    private final String name;
    private final byte[] data;

    public QueueData(long id, String name, byte[] data){
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }
}
