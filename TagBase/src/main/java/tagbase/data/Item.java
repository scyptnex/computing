package tagbase.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Item {

    private String name;
    private Path path;

    private Set<String> tags;
    private Long sizeKB;
    private Date dateAdded;

    public Item(Path pa) throws IOException{
        this.path = pa;
        this.name = path.getFileName().toString();
        this.tags = new HashSet<>();
        this.sizeKB = Files.size(path);
        this.dateAdded = new Date();
    }

    @Override
    public String toString(){
        //TODO json string
        return String.format("{name=\"%s\", path=\"%s\", size=\"%d\", date=\"%s\", tags=%s}", name, path.toString(), sizeKB, dateAdded, tags.toString());
    }

    public static void main(String[] args) throws IOException{
        Item itm = new Item(new File(".").getAbsoluteFile().toPath());
        itm.setTags(Arrays.asList("a", "b"));
        System.out.println(itm);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        //TODO sanity check
        this.path = path;
    }

    public Collection<String> getTags(){
        return new ArrayList<>(tags); // avoid handing out the real set
    }

    public void setTags(Collection<String> newTags){
        tags.clear();
        tags.addAll(newTags); // avoid taking in the external tags
    }

    public Long getSizeKB() {
        return sizeKB;
    }

    public void setSizeKB(Long sizeKB) {
        this.sizeKB = sizeKB;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
