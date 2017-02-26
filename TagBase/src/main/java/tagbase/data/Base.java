package tagbase.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Base {

    private Path tagBaseDirectory;
    private ArrayList<Record> data;

    public Base(Path _tagBaseDirectory){
        this.tagBaseDirectory = _tagBaseDirectory;
    }

    private static Stream<Map<String, String>> loadStateLegacy(Path p) throws IOException {
        Scanner sca = new Scanner(Files.newInputStream(p));
        List<Map<String, String>> buffer = new ArrayList<>();
        int index = 0;
        while(sca.hasNextLine()){
            Map<String, String> addition = new HashMap<>();
            addition.put("index", index + "");
            addition.put("name", sca.nextLine());
            addition.put("tags", sca.nextLine());
            addition.put("date", sca.nextLine());
            addition.put("size", sca.nextLine());
            addition.put("path", sca.nextLine());
            buffer.add(addition);
        }
        sca.close();
        return buffer.stream();
    }

    private static class Record implements Comparable<Record>{

        public int index;
        public String name;
        public String tags;
        public String date;
        public Map<String, String> meta;
        public long size;
        public Path path;

        public static Record buildOrNull(Map<String, String> values){
            try{
                return new Record(values);
            } catch(IOException e){
                return null;
            }
        }

        private Record(Map<String, String> values) throws IOException{
            this.index = Integer.parseInt(values.get("index"));
            this.name = values.get("name");
            this.tags = values.get("tags");
            this.date = values.get("date");
            this.size = Long.parseLong(values.get("size"));
            this.path = Paths.get(values.get("path"));
        }

        @Override
        public int compareTo(Record o) {
            return this.index - o.index;
        }
    }

}
