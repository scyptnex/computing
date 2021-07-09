package tagbase.files;

import tagbase.data.RecordKeeper;
import tagbase.data.RecordKeeperBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainDirRecordSaverLoader implements RecordSaver, RecordLoader{

    public static final String FILE_NAME = "zzTagBase.json";

    private File mainDir;

    public MainDirRecordSaverLoader(File mainDir) {
        this.mainDir = mainDir;
    }


    @Override
    public void save(File mainDir, RecordKeeper rk) throws IOException {
            Path p = Paths.get(mainDir.getPath(), FILE_NAME);
            StateRecorder.saveState(serializeState(rk), p);
    }

    private static Stream<Map<String, String>> serializeState(RecordKeeper rk){
        return IntStream.range(0, rk.getCount())
                .mapToObj(rk::getRecord)
                .map(r -> new String[]{
                        "name", r.getName(),
                        "tags", r.getTags(),
                        "date", r.getDateAdded(),
                        "size", r.getSizeBytes() + "",
                        "path", r.getPath()})
                .map(l -> IntStream.range(0, l.length)
                        .filter(i -> i%2==0)
                        .boxed()
                        .collect(Collectors.toMap(i -> l[i], i -> l[i+1])));
    }

    @Override
    public RecordKeeper load(File mainDir, RecordKeeperBuilder builder) throws IOException{
        throw new IOException("not implemented");
    }

    @Override
    public boolean isTagBaseDir(File mainDir) {
        return false;
    }
}
