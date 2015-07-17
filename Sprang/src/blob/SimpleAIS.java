package blob;

import java.util.Properties;

public class SimpleAIS implements AuthorizedInfoStore{

    private Properties prop = new Properties();


    @Override
    public boolean isAuthorized(User u) {
        return u.getUname().equals("nic");
    }

    @Override
    public void addInfo(User u, String k, String v) throws AuthorityException {
        if(!isAuthorized(u)){
            throw new AuthorityException(u, "Key set " + k + "=" + v);
        } else{
            prop.setProperty(k, v);
        }
    }

    @Override
    public String getInfo(User u, String k) throws AuthorityException {
        if(!isAuthorized(u)){
            throw new AuthorityException(u, "Key get " + k);
        } else{
            return prop.getProperty(k);
        }
    }
}
