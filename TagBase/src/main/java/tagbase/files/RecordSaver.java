package tagbase.files;

import tagbase.data.RecordKeeper;

import java.io.File;
import java.io.IOException;

public interface RecordSaver {

    void save(File mainDir, RecordKeeper rk) throws IOException;

}
