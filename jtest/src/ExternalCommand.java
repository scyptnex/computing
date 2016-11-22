import java.io.File;

public class ExternalCommand {

    public static void main(String[] args) throws Exception{
        ProcessBuilder pb = new ProcessBuilder("env");
        pb.directory(new File("jTest"));
        pb.environment().put("FOO", "zomgg");
        pb.redirectOutput(new File("OUT"));
        pb.redirectError(new File("ERR"));
        Process p = pb.start();
        p.waitFor();
    }

}
