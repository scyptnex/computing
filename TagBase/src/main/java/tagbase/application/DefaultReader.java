package tagbase.application;

import tagbase.data.Base;
import tagbase.files.MainDirRecordSaverLoader;

import java.io.InputStream;
import java.io.OutputStream;

public class DefaultReader implements Reader{

    @Override
    public String getListName() {
        return MainDirRecordSaverLoader.LIST_NAME;
    }

    @Override
    public Base readIn(InputStream is) {
        return null;
    }

    @Override
    public void writeOut(OutputStream os, Base b) {

    }
}
