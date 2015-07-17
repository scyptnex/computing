package blob;

public class AuthorityException extends Exception{

    public final User involvedUser;
    public final String involvedMessage;

    public AuthorityException(User involvedUser, String involvedMessage) {
        this.involvedUser = involvedUser;
        this.involvedMessage = involvedMessage;
    }

    public String toString(){
        return "Authority exception: " + involvedMessage + " [user =  " + involvedUser.getUname() + "]";
    }
}
