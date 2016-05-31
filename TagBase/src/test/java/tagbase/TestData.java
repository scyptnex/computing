package tagbase;

import org.junit.Test;
import tagbase.data.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestData {

    @Test
    public void newItemRetainsPath(){
        try {
            Path p = Files.createTempFile("tagbase_test", ".tmp");
            Item itm = new Item(p);
            assertEquals(p, itm.getPath());
        } catch (IOException e) {
            fail();
        }
    }

}
