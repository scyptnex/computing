package tagbase.files;

import tagbase.data.RecordCheck;
import tagbase.data.RecordKeeper;
import tagbase.data.RecordKeeperBuilder;
import tagbase.data.SimpleRecordKeeper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HyperParanoid implements RecordLoader, RecordSaver {

    private static final int MAX_BACKUPS = 8;
    private static final int MAX_RETRIES = 3;

    private LegacyFileBasedList internal = new LegacyFileBasedList(getPrimary().toString());

    static Path getMetaDir(){
        return Paths.get(".tagbase_metadata");
    }

    static Path getPrimary(){
        return getBackup(0);
    }

    static Path getBackup(int backupNumber){
        return Paths.get(getMetaDir().toString(), "data_" + backupNumber + ".txt");
    }

    static File ensureMetaDir(File mainDir) throws IOException{
        File metadir = new File(mainDir, getMetaDir().toString());
        if(!metadir.exists() && !metadir.mkdir())
            throw new IOException("couldn't make the meta dir");
        return metadir;
    }

    @Override
    public boolean isTagBaseDir(File mainDir) {
        File metadir = new File(mainDir, getMetaDir().toString());
        return metadir.exists() && metadir.isDirectory() && new File(mainDir, getPrimary().toString()).exists();
    }

    @Override
    public RecordKeeper load(File mainDir, RecordKeeperBuilder builder) throws IOException {
        if(!isTagBaseDir(mainDir)) throw new IOException("Bailing out because i'm not a tagbase");
        ensureMetaDir(mainDir);
        return internal.load(mainDir, builder);
    }

    @Override
    public void save(File mainDir, RecordKeeper rk) throws IOException {
        ensureMetaDir(mainDir);
        makeRoomForAndGetBackup(mainDir, 0);
        int retryCount = 1;
        while(retryCount <= MAX_RETRIES){
            internal.save(mainDir, rk);
            System.out.println("Confirming equality of records " + retryCount + "/" + MAX_RETRIES);
            SimpleRecordKeeper paranoidRK = new SimpleRecordKeeper();
            load(mainDir, paranoidRK);
            if( RecordCheck.isSame(rk, paranoidRK)){
                return;
            }
            System.out.println("Failed!");
        }
        throw new IOException("Failed to save");
    }

    private File makeRoomForAndGetBackup(File mainDir, int backup) throws IOException{
        File backupFile = new File(mainDir, getBackup(backup).toString());
        if(!backupFile.exists())
            return backupFile;
        if(backup >= MAX_BACKUPS){
            System.out.println("Removing " + backupFile.toString());
            if(!backupFile.delete()){
                throw new IOException("Could not delete " + backupFile);
            }
            return backupFile;
        }
        System.out.println("Shuffling " + backupFile.toString() + " to place " + (backup+1));
        File olderBackup = makeRoomForAndGetBackup(mainDir, backup+1);
        if(!backupFile.renameTo(olderBackup)){
            throw new IOException("Could not rename " + backupFile + " to " + olderBackup);
        }
        return new File(mainDir, getBackup(backup).toString());
    }
}
