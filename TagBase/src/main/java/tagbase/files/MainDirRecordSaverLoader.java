package tagbase.files;

import tagbase.data.Record;
import tagbase.data.RecordKeeper;
import tagbase.data.RecordKeeperBuilder;
import tagbase.data.SimpleRecord;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainDirRecordSaverLoader implements RecordSaver, RecordLoader{

    public static final String LIST_NAME = "zzList.txt";
    public static final String FILE_NAME = "zzTagBase.json";

    private File mainDir;

    public MainDirRecordSaverLoader(File mainDir) {
        this.mainDir = mainDir;
    }


    @Override
    public void save(RecordKeeper rk) throws IOException {
            PrintWriter pw = new PrintWriter(new File(mainDir, LIST_NAME));
            for(int i=0; i<rk.getCount(); i++){
                Record rec = rk.getRecord(i);
                pw.println(rec.getName());
                pw.println(rec.getTags());
                pw.println(rec.getDateAdded());
                pw.println(rec.getSizeBytes());
                pw.println(rec.getPath());
            }
            pw.close();
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
    public RecordKeeper load(RecordKeeperBuilder builder) throws IOException{
        File list = new File(mainDir, LIST_NAME);
        list.createNewFile();
        Scanner sca = new Scanner(list);
        while(sca.hasNextLine()){
            String nm = sca.nextLine();
            String ta = sca.nextLine();
            String da = sca.nextLine();
            long sz = Long.parseLong(sca.nextLine());
            String pa = sca.nextLine();
            builder.addRecord(new SimpleRecord(nm, pa, ta, sz, da));
        }
        sca.close();
        return builder.build();
    }
}
