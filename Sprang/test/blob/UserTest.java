package blob;

import org.junit.*;

public class UserTest {

    @Test
    public void checkUserHasName(){
        User u = new User("abc");
        String nam = u.getUname();
        Assert.assertNotNull(nam);
        Assert.assertEquals(nam, "abc");
    }

    @Test
    public void checkUserNull(){
        User u = new User(null);
        String nam = u.getUname();
        Assert.assertNull(nam);
        Assert.assertNotEquals(nam, "abc");
    }

    @Test
    public void impossible(){
        Assert.assertEquals(1, 2);
    }

}
