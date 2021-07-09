package tagbase.files;

import tagbase.data.Record;
import tagbase.data.RecordKeeper;
import tagbase.data.RecordKeeperBuilder;
import tagbase.data.SimpleRecord;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class LegacyFileBasedList implements RecordSaver, RecordLoader {

    private static final String LIST_NAME = ".List.txt";

    private String subpath;

    LegacyFileBasedList(){
        this(LIST_NAME);
    }

    LegacyFileBasedList(String customSubpath){
        subpath = customSubpath;
    }

    @Override
    public void save(File mainDir, RecordKeeper rk) throws IOException {
        File listFile = new File(mainDir, subpath);
        PrintWriter pw = new PrintWriter(listFile);
        for(int i=0; i<rk.getCount(); i++){
            Record rec = rk.getRecord(i);
            pw.println(rec.getName());
            pw.println(rec.getTags());
            pw.println(rec.getDateAdded());
            pw.println(rec.getSizeBytes());
            pw.println(rec.getPath());
        }
        pw.close();
    }

    @Override
    public boolean isTagBaseDir(File mainDir) {
        return new File(mainDir, subpath).exists();
    }

    @Override
    public RecordKeeper load(File mainDir, RecordKeeperBuilder builder) throws IOException{
        File listFile = new File(mainDir, subpath);
        listFile.createNewFile();
        Scanner sca = new Scanner(listFile);
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
