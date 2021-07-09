package tagbase.data;

import java.util.Iterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RecordCheck {

    public static boolean isSame(RecordKeeper r1, RecordKeeper r2){
        if(r1.getCount() != r2.getCount())
            return false;
        if(r1.getTotalSizeBytes() != r2.getTotalSizeBytes())
            return false;
        Iterator<Record> s1 = toSortedStream(r1).iterator();
        Iterator<Record> s2 = toSortedStream(r2).iterator();
        while(s1.hasNext()){
            if(!isSame(s1.next(), s2.next()))
                return false;
        }
        return true;
    }

    static boolean isSame(Record r1, Record r2){
        return r1.getName().equals(r2.getName())
                && r1.getPath().equals(r2.getPath())
                && r1.getTags().equals(r2.getTags())
                && r1.getSizeBytes() == r2.getSizeBytes()
                && r1.getDateAdded().equals(r2.getDateAdded());
    }

    private static Stream<Record> toSortedStream(RecordKeeper rk){
        return IntStream.range(0, rk.getCount()).mapToObj(rk::getRecord).sorted();
    }

}
