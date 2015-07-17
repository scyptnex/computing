package blob;

public interface AuthorizedInfoStore {

    boolean isAuthorized(User u);

    void addInfo(User u, String k, String v) throws AuthorityException;

    String getInfo(User u, String k) throws AuthorityException;

}
