import java.io.IOException;
import java.net.Socket;

/**
 * Created by nic on 3/07/15.
 */
public class RequestParser {

    public RequestParser(Socket sock){
        System.out.println("lol i killed it");
        try {
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
