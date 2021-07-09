package tagbase.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleRecordKeeper implements RecordKeeper, RecordKeeperBuilder{

    private ArrayList<SimpleRecord> records = new ArrayList<>();
    private Map<String, Long> cachedTagsHistogram = null;
    private long totalSizeBytes = 0;

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public long getTotalSizeBytes() {
        return totalSizeBytes;
    }

    @Override
    public Map<String, Long> getTagHistogram() {
        if(cachedTagsHistogram == null){
            cachedTagsHistogram = streamOfTagsToHistogram(records.stream().map(SimpleRecord::getTags));
        }
        return cachedTagsHistogram;
    }

    @Override
    public Record getRecord(int i) {
        return records.get(i);
    }

    @Override
    public void retag(int i, String newTags) {
        cachedTagsHistogram = null;
        records.get(i).setTags(newTags);
    }

    @Override
    public void addRecord(Record r) {
        totalSizeBytes += r.getSizeBytes();
        records.add(new SimpleRecord(r.getName(), r.getPath(), r.getTags(), r.getSizeBytes(), r.getDateAdded()));
    }

    @Override
    public RecordKeeper build() {
        return this;
    }

    public static Map<String, Long> streamOfTagsToHistogram(Stream<String> streamOfTags){
        return streamOfTags
                .flatMap(t -> Arrays.stream(t.split(" ")))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}
