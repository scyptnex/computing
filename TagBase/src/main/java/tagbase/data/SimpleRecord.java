package tagbase.data;

public class SimpleRecord implements Record, Comparable<Record>{

    private String name;
    private String path;
    private String tags;
    private long sizeBytes;
    private String dateAdded;

    public SimpleRecord(String name, String path, String tags, long sizeBytes, String dateAdded){
        this.name = name;
        this.path = path;
        this.tags = tags;
        this.sizeBytes = sizeBytes;
        this.dateAdded = dateAdded;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getTags() {
        return tags;
    }

    @Override
    public long getSizeBytes() {
        return sizeBytes;
    }

    @Override
    public String getDateAdded() {
        return dateAdded;
    }

    void setTags(String tags){
        this.tags = tags;
    }

    @Override
    public int compareTo(Record o) {
        return getPath().compareTo(o.getPath());
    }
}
