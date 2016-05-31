package tagbase.application;

import tagbase.TagBaseII;
import tagbase.data.Base;

import java.io.InputStream;
import java.io.OutputStream;

public class DefaultReader implements Reader{

    @Override
    public String getListName() {
        return TagBaseII.LIST_NAME;
    }

    @Override
    public Base readIn(InputStream is) {
        return null;
    }

    @Override
    public void writeOut(OutputStream os, Base b) {

    }
}
