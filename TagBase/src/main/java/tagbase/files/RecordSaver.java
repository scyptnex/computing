package tagbase.files;

import tagbase.data.RecordKeeper;

import java.io.IOException;

public interface RecordSaver {

    void save(RecordKeeper rk) throws IOException;

}
