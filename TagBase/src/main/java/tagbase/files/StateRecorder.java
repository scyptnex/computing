package tagbase.files;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class StateRecorder {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String itemToString(Map<String, String> i){
        try {
            return mapper.writeValueAsString(i);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Code unreachable unless Jackson is broken");
        }
    }

    public static Map<String, String> stringToItem(String s){
        try {
            return mapper.readValue(s, Map.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static Stream<Map<String, String>> loadState(Path p) throws IOException {
        return Files.lines(p).map(StateRecorder::stringToItem);
    }

    /**
     * @param state the maps you want to save
     * @param os The output stream you want to print to (CLOSE THIS YOURSELF)
     */
    public static void saveState(Stream<Map<String, String>> state, OutputStream os){
        PrintStream ps = new PrintStream(os);
        state.parallel().map(StateRecorder::itemToString).sequential().forEach(ps::println);
    }

    /**
     * @param state the maps you want to save
     * @param p the path to the file you want to save over
     * @throws IOException for file errors
     */
    public static void saveState(Stream<Map<String, String>> state, Path p) throws IOException {
        OutputStream os = Files.newOutputStream(p);
        saveState(state, os);
        os.close();
    }
}
