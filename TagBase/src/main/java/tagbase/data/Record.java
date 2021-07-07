package tagbase.data;

public interface Record {
    String getName();

    String getPath();

    String getTags();

    long getSizeBytes();

    String getDateAdded();
}
