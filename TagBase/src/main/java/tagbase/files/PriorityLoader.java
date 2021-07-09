package tagbase.files;

import tagbase.data.RecordKeeper;
import tagbase.data.RecordKeeperBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PriorityLoader implements RecordLoader {

    private List<RecordLoader> loaders;

    public PriorityLoader(){
        loaders = Arrays.asList(new HyperParanoid(), new LegacyFileBasedList());
    }

    @Override
    public boolean isTagBaseDir(File mainDir) {
        for(RecordLoader loader : loaders){
            if(loader.isTagBaseDir(mainDir))
                return true;
        }
        return false;
    }

    @Override
    public RecordKeeper load(File mainDir, RecordKeeperBuilder builder) throws IOException {
        for(RecordLoader loader : loaders){
            System.out.println("Attempting to load using: " + loader.getClass().getSimpleName());
            try{
                return loader.load(mainDir, builder);
            } catch (IOException exc){
                System.out.println("Failed - " + exc.getMessage());
            }
        }
        throw new IOException("No loader worked");
    }
}
