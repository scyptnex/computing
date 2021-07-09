package tagbase.files;

import tagbase.data.RecordKeeper;
import tagbase.data.RecordKeeperBuilder;

import java.io.File;
import java.io.IOException;

public interface RecordLoader {

    boolean isTagBaseDir(File mainDir);

    RecordKeeper load(File mainDir, RecordKeeperBuilder builder) throws IOException;
}
