package tagbase.data;

public interface RecordKeeperBuilder {

    void addRecord(Record r);

    RecordKeeper build();

}
