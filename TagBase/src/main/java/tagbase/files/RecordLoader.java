package tagbase.files;

import tagbase.data.RecordKeeper;
import tagbase.data.RecordKeeperBuilder;

import java.io.IOException;

public interface RecordLoader {

    RecordKeeper load(RecordKeeperBuilder builder) throws IOException;
}
