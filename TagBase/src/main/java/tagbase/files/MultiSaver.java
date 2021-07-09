package tagbase.files;

import tagbase.data.RecordKeeper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MultiSaver implements RecordSaver{

    private List<RecordSaver> savers;

    public MultiSaver(){
        savers = Arrays.asList(new HyperParanoid());
    }

    @Override
    public void save(File mainDir, RecordKeeper rk) throws IOException {
        IOException lastException = null;
        for(RecordSaver saver : savers){
            System.out.println("Attempting to save using: " + saver.getClass().getSimpleName());
            try{
                saver.save(mainDir, rk);
            } catch (IOException exc){
                System.out.println("Failed - " + exc.getMessage());
                lastException = exc;
            }
        }
        if(lastException != null)
            throw lastException;
    }
}
