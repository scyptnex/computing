package blob;

import org.junit.Assert;
import org.junit.Test;

public class SimpleAisTest {

    User uNic = new User("nic");
    User uJim = new User("jim");
    AuthorizedInfoStore ais = new SimpleAIS();

    @Test
    public void nicIsAuthorised(){
        Assert.assertTrue(ais.isAuthorized(uNic));
    }

    @Test
    public void notNicIsNotAuthorised(){
        Assert.assertFalse(ais.isAuthorized(uJim));
    }

    @Test(expected = AuthorityException.class)
    public void unauthorisedUserCannotGetKeys() throws AuthorityException {
        ais.getInfo(uJim, "key");
    }

    @Test(expected = AuthorityException.class)
    public void unauthorisedUserCannotSetKeys() throws AuthorityException{
        ais.addInfo(uJim, "key", "value");
    }

    @Test
    public void authorisedUserCanSetKey() throws AuthorityException {
        ais.addInfo(uNic, "Key", "value");
    }

    @Test
    public void authorisedUserCanGetKey() throws AuthorityException {
        ais.getInfo(uNic, "Key");
    }

    @Test
    public void nonExistantInfoIsNull() throws AuthorityException {
        Assert.assertNull(ais.getInfo(uNic, "nonExistantKey"));
    }

    @Test
    public void existantInfoIsNotNull() throws AuthorityException {
        ais.addInfo(uNic, "existantKey", "someValue");
        Assert.assertNotNull(ais.getInfo(uNic, "existantKey"));
    }

    @Test
    public void addInfoChangesState() throws AuthorityException {
        ais.addInfo(uNic, "keyA", "valueA");
        Assert.assertEquals(ais.getInfo(uNic, "keyA"), "valueA");
        ais.addInfo(uNic, "keyA", "valueB");
        Assert.assertNotEquals(ais.getInfo(uNic, "keyA"), "valueA");
        Assert.assertEquals(ais.getInfo(uNic, "keyA"), "valueB");
    }

}
