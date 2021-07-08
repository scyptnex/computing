package tagbase;

import tagbase.application.Interactor;
import tagbase.data.Record;
import tagbase.data.RecordKeeper;
import tagbase.data.RecordKeeperBuilder;
import tagbase.files.MainDirRecordSaverLoader;
import tagbase.files.RecordLoader;
import tagbase.files.RecordSaver;
import tagbase.gui.BaseChooser;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TagBaseII implements RecordKeeper, RecordKeeperBuilder, Interactor {

    public static final String IMPORT_FILENAME = "zzlib.dat";
    public static final int MAX_PROMPT_LINES = 20;

    public static final String NEW_TAG = "new";
    public static final String HIDDEN_TAG = "zh";
    public static final String BAD_TAG = "zz";

    public final File mainDir;

    private long totalSize;
    private final ArrayList<String> names;
    private final HashMap<String, Integer> indexes;
    private final HashMap<String, String> tags;
    private final HashMap<String, String> dates;
    private final HashMap<String, Long> sizes;
    private final HashMap<String, String> paths;

    private Map<String, Long> cachedTagHistogram = null;

    public static TagBaseII getBase(File mainDir) {
        try {
            mainDir = BaseChooser.choose(mainDir);
            if (mainDir == null) return null; // exit when user cancels


            return new TagBaseII(mainDir);
        } catch (IOException exc) {
            return null; // exit on error
        }
    }

    private TagBaseII(File mainDir) throws IOException {
        this.mainDir = mainDir;
        names = new ArrayList<String>();
        indexes = new HashMap<String, Integer>();
        tags = new HashMap<String, String>();
        dates = new HashMap<String, String>();
        sizes = new HashMap<String, Long>();
        paths = new HashMap<String, String>();
        totalSize = 0;
        RecordLoader loader = new MainDirRecordSaverLoader(this.mainDir);
        RecordKeeper rk = loader.load(this);
        assert rk == this;
    }

    /**
     * Accessors
     */
    public String name(int index) {
        return names.get(index);
    }

    public int index(String name) {
        return indexes.get(name);
    }

    public String tag(int index) {
        return tag(names.get(index));
    }

    public String tag(String name) {
        return tags.get(name);
    }

    public String date(int index) {
        return date(names.get(index));
    }

    public String date(String name) {
        return dates.get(name);
    }

    public Long size(int index) {
        return size(names.get(index));
    }

    public Long size(String name) {
        return sizes.get(name);
    }

    public String path(int index) {
        return path(names.get(index));
    }

    public String path(String name) {
        return paths.get(name);
    }

    @Override
    public void addRecord(Record r) {
        int idx = names.size();
        names.add(r.getName());
        indexes.put(r.getName(), idx);
        tags.put(r.getName(), r.getTags());
        dates.put(r.getName(), r.getDateAdded());
        sizes.put(r.getName(), r.getSizeBytes());
        paths.put(r.getName(), r.getPath());
        totalSize += r.getSizeBytes();
    }

    @Override
    public RecordKeeper build() {
        return this;
    }

    @Override
    public void synchronize() {
        scry();
    }

    @Override
    public void use(Record r) {
        //System.out.println("Do something with item " + idx);
        File fi = new File(mainDir, r.getPath());
        if (!fi.exists()) {
            System.err.println("Couldnt find " + fi.getName() + ", try rescanning");
            return;
        }
        try {
            Main.use(fi);
        } catch (IOException e1) {
            System.err.println("Failed to open");
        }
    }

    /**
     * Methods
     */

    private class Recordlet implements Record {
        private int idx;

        Recordlet(int i) {
            this.idx = i;
        }

        @Override
        public String getName() {
            return name(idx);
        }

        @Override
        public String getPath() {
            return path(idx);
        }

        @Override
        public String getTags() {
            return tag(idx);
        }

        @Override
        public long getSizeBytes() {
            return size(idx);
        }

        @Override
        public String getDateAdded() {
            return date(idx);
        }
    }

    @Override
    public int getCount() {
        return count();
    }

    @Override
    public long getTotalSizeBytes() {
        return totalSize;
    }

    @Override
    public Map<String, Long> getTagHistogram() {
        if(cachedTagHistogram == null) {
            cachedTagHistogram = tags.values().stream()
                    .flatMap(t -> Arrays.stream(t.split(" ")))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        }
        return cachedTagHistogram;
    }

    @Override
    public Record getRecord(int i) {
        return new Recordlet(i);
    }

    public int count() {
        return names.size();
    }

    public RecordSaver saver(){
        return new MainDirRecordSaverLoader(mainDir);
    }

    public String getTotalSize() {
        String[] ends = {"B", "KB", "MB", "GB", "TB"};
        int end = 0;
        double div = 1;
        while (totalSize / div > 2048.0 && end < ends.length - 1) {
            end++;
            div = div * 1024;
        }
        return Main.twoDecimal(totalSize / div) + " " + ends[end];
    }

    public void retag(int idx, String tag) {
        retag(names.get(idx), tag);
    }

    public void retag(String name, String tag) {
        tags.put(name, tag);
        cachedTagHistogram = null;
    }

    public void scry() {
        ArrayList<String> newPaths = new ArrayList<String>();
        Set<String> knownNames = new HashSet<String>();
        for (String nm : names) knownNames.add(nm);
        recurFileTree(newPaths, knownNames, "", mainDir);

        //removing
        if (knownNames.size() > 0) {
            String msg = knownNames.size() + " files were lost:";
            if (knownNames.size() < MAX_PROMPT_LINES) for (String lst : knownNames) msg = msg + "\n - " + lst;
            msg = msg + "\n\nRemove them?";
            if (Main.confirmPrompt(msg)) {
                indexes.clear();
                for (String lost : knownNames) {
                    tags.remove(lost);
                    dates.remove(lost);
                    totalSize = totalSize - sizes.get(lost);
                    sizes.remove(lost);
                    paths.remove(lost);
                }
                names.removeAll(knownNames);
                for (int i = 0; i < names.size(); i++) indexes.put(names.get(i), i);
            }
        }

        //adding
        if (newPaths.size() > 0) {
            String msg = newPaths.size() + " files were Found:";
            if (newPaths.size() < MAX_PROMPT_LINES) for (String fnd : newPaths) msg = msg + "\n - " + fnd;
            msg = msg + "\n\nAdd them?";
            if (Main.confirmPrompt(msg)) {
                for (String fnd : newPaths) {
                    addNew(fnd);
                }
            }
        } else if (knownNames.size() <= 0)
            Main.informPrompt("Scan completed\nNo new or missing files\nFiles which were moved have been reaquired");
    }

    private void addNew(String newPath) {
        File fi = new File(mainDir, newPath);
        addDetailed(newPath, Main.longDate(fi.lastModified()), NEW_TAG);
    }

    private void addDetailed(String newPath, String date, String tag) {
        File fi = new File(mainDir, newPath);
        if (indexes.keySet().contains(fi.getName())) {
            String msg = "Base already contains a file named: " + fi.getName() + "\nThe File:\n - "
                    + newPath + "\nIs a duplicate of\n - " + paths.get(fi.getName());
            Main.informPrompt(msg);
            return;
        }
        String nm = fi.getName();
        names.add(nm);
        indexes.put(nm, names.size() - 1);
        dates.put(nm, date);
        tags.put(nm, tag);
        totalSize += fi.length();
        sizes.put(nm, fi.length());
        paths.put(nm, newPath);
    }

    private void fullImport(File dir, String path) {
        File impf = new File(dir, IMPORT_FILENAME);
        try {
            Scanner sca = new Scanner(impf);
            while (sca.hasNextLine()) {
                String nm = sca.nextLine();
                File chk = new File(mainDir, path + nm);
                if (chk.exists()) {
                    addDetailed(path + nm, sca.nextLine(), sca.nextLine());
                }
            }
            sca.close();
            if (!impf.delete()) System.err.println("Delete import file manually: " + impf.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Main.informPrompt("Something went horribly wrong while importing");
        }
    }

    private void recurFileTree(ArrayList<String> newPaths, Set<String> knownNames, String curPath, File curDir) {
        //when this folder is from an export
        if (new File(curDir, IMPORT_FILENAME).exists()) {
            if (Main.confirmPrompt("Fully import the directory:\n - " + curDir.getAbsolutePath()))
                fullImport(curDir, curPath);
        }
        //otherwise
        else for (File fi : curDir.listFiles()) {
            if (fi.isDirectory()) {
                if (!fi.getName().startsWith(".Trash"))
                    recurFileTree(newPaths, knownNames, curPath + fi.getName() + "/", fi);
            } else {
                String name = fi.getName();
                String loc = curPath + name;
                if (!fi.getName().equals(MainDirRecordSaverLoader.LIST_NAME)) {
                    if (knownNames.contains(name)) {
                        knownNames.remove(name);
                        if (fi.length() != sizes.get(name)) {
                            totalSize = totalSize - sizes.get(name) + fi.length();
                            sizes.put(name, fi.length());
                        }
                        paths.put(name, loc);
                    } else {
                        newPaths.add(loc);
                    }
                }
            }
        }
    }

    public static <T> void sortBy(T[] vals, int[] sorts) {
        //bubble sort, cos im lazy
        for (int end = sorts.length; end > 1; end--) {
            for (int start = 1; start < end; start++) {
                if (sorts[start] < sorts[start - 1]) {
                    int ts = sorts[start];
                    T tv = vals[start];
                    sorts[start] = sorts[start - 1];
                    vals[start] = vals[start - 1];
                    sorts[start - 1] = ts;
                    vals[start - 1] = tv;
                }
            }
        }
    }
}
