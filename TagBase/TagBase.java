import java.io.*;
import java.util.*;

public class TagBase {
	
	public static final String LIST_NAME = "zzBase.txt";
	
	public final File mainDir;
	private final ArrayList<String> locs;
	private final ArrayList<String> names;
	private final ArrayList<String> tags;
	private final ArrayList<String> dates;
	private final ArrayList<Long> sizes;//in bytes
	
	public static TagBase getBase(File mainDir){
		File list = new File(mainDir, LIST_NAME);
		if(!list.exists()){
			if(Main.confirmPrompt("Directory " + mainDir.getAbsolutePath() + " is not a TagBase directory.\n\nMake it one?")){
				try {
					list.createNewFile();
				} catch (IOException e) {
					return null;
				}
			}
			else{
				return null;
			}
		}
		return new TagBase(list);
	}
	
	private TagBase(File baseList){
		mainDir = baseList.getParentFile();
		locs = new ArrayList<String>();
		names = new ArrayList<String>();
		tags = new ArrayList<String>();
		dates = new ArrayList<String>();
		sizes = new ArrayList<Long>();
	}
	
	public void scry(){
		ArrayList<String> newFiles = new ArrayList<String>();
		HashSet<String> missingFiles = new HashSet<String>();
		for(String l : locs) missingFiles.add(l);
		scry(mainDir, "", newFiles, missingFiles);
		System.out.println("Found " + newFiles.size() + " files");
		System.out.println("Lost " + missingFiles.size() + " files");
		//confirm removal
		//confirm addition
	}
	
	private void scry(File dir, String curPath, ArrayList<String> newFiles, HashSet<String> missingFiles){
		for(File fi : dir.listFiles()){
			if(fi.isDirectory()){
				scry(fi, curPath + dir.getName() + "/", newFiles, missingFiles);
			}
			else{
				String loc = curPath + fi.getName();
				if(missingFiles.contains(loc)){
					missingFiles.remove(loc);
				}
				else{
					newFiles.add(loc);
				}
			}
		}
	}
	
}