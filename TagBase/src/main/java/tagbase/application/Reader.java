package tagbase.application;

import tagbase.data.Base;

import java.io.InputStream;
import java.io.OutputStream;

public interface Reader {

    String getListName();

    Base readIn(InputStream is);

    void writeOut(OutputStream os, Base b);

}
