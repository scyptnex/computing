import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by nic on 3/07/15.
 */
public class Weblet {

    public static final int DEFAULT_PORT = 12345;// TODO 80;

    public final File htmlFolder;
    public final int serverPort;
    private final ServerSocket server;

    public Weblet(File htmlDir, int port) throws IOException {
        htmlFolder = htmlDir;
        serverPort = port;
        if(! htmlFolder.isDirectory()) throw new SocketException("You must serve a directory");
        server = new ServerSocket(serverPort);
        while(true){
            Socket request = server.accept();
//            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
//            String line;
//            while((line = br.readLine()) != null){
//                System.out.println(line);
//            }
//            System.out.println("done");
            PrintWriter pw = new PrintWriter(request.getOutputStream());
            pw.println("hi");
            pw.close();
            request.close();
        }
    }

    // Main method to run the server as a standalone
    public static void main(String[] args){
        try {
            int port = DEFAULT_PORT;
            if(args.length > 1){
                port = Integer.parseInt(args[1]);
            }
            File fold = new File("/home/nic/general/usydWeb/out");// TODO args[0]);
            new Weblet(fold, port);
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println("Usage: Weblet <html folder> [port]");
            System.exit(1);
        }
    }

}
