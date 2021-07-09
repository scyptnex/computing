package tagbase.gui;

import tagbase.files.PriorityLoader;
import tagbase.files.RecordLoader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class BaseChooser {

    private static final String TITLE = "TagBase - Chooser";
    private static final Object[] OPTIONS = {"Cancel", "Choose elsewhere", "Create here"};
    private static final String MESSAGE_LOC = "\"%s\" is not a TagBase directory.\n\nMake it one?";

    private RecordLoader loader;

    public BaseChooser(RecordLoader loader){
        this.loader = loader;
    }

    public File choose() throws IOException {
        return choose(".");
    }

    public File choose(String s) throws IOException {
        return choose(new File(s));
    }

    public File choose(File fi) throws IOException {
        while (!checkDirIsBase(fi)) {
            if (!fi.isDirectory()) fi = fi.getParentFile();
            fi = fi.getCanonicalFile();
            int n = JOptionPane.showConfirmDialog(null, String.format(MESSAGE_LOC, fi.getAbsolutePath()), TITLE, JOptionPane.YES_NO_CANCEL_OPTION);
            if (n == JOptionPane.CANCEL_OPTION) {
                fi = null;
                break;
            } else if (n == JOptionPane.YES_OPTION) {
                break;
            }
            JFileChooser jfc = new JFileChooser(fi);
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int code = jfc.showOpenDialog(null);
            if(code == JFileChooser.APPROVE_OPTION){
                fi = jfc.getSelectedFile();
            }
            jfc.setVisible(false);
        }
        return fi;
    }

    private boolean checkDirIsBase(File fi) {
        return fi.isDirectory() && loader.isTagBaseDir(fi);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new BaseChooser(new PriorityLoader()).choose());
    }

}
