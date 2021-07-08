package tagbase.data;

import java.util.Map;

public interface RecordKeeper {

    int getCount();

    long getTotalSizeBytes();

    Map<String, Long> getTagHistogram();

    Record getRecord(int i);

    void retag(int i, String newTags);
}
