import java.io.*;
import java.util.*;

public class TagBase {
	
	public static final String LIST_NAME = "zzBase.txt";
	
	public static final String NEW_TAG = "new";
	public static final String HIDDEN_TAG = "new";
	
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
		try {
			return new TagBase(list);
		} catch (IOException e) {
			return null;
		}
	}
	
	private TagBase(File baseList) throws IOException{
		mainDir = baseList.getParentFile();
		locs = new ArrayList<String>();
		names = new ArrayList<String>();
		tags = new ArrayList<String>();
		dates = new ArrayList<String>();
		sizes = new ArrayList<Long>();
		Scanner sca = new Scanner(baseList);
		while(sca.hasNextLine()){
			locs.add(sca.nextLine());
			names.add(sca.nextLine());
			tags.add(sca.nextLine());
			dates.add(sca.nextLine());
			sizes.add(Long.parseLong(sca.nextLine()));
		}
		sca.close();
	}
	
	public int getIdx(String loc){
		return locs.indexOf(loc);
	}
	
	public void rename(int idx, String newName){
		names.set(idx, newName);
	}
	public void retag(int idx, String newTag){
		tags.set(idx, newTag);
	}
	
	public String loc(int row){
		return locs.get(row);
	}
	public String name(int row){
		return names.get(row);
	}
	public String tag(int row){
		return tags.get(row);
	}
	public String date(int row){
		return dates.get(row);
	}
	public Long size(int row){
		return sizes.get(row);
	}
	
	public int count(){
		return locs.size();
	}
	
	public boolean save(){
		try{
			PrintWriter pw = new PrintWriter(new File(mainDir, LIST_NAME));
			for(int i=0; i<locs.size(); i++){
				pw.println(locs.get(i));
				pw.println(names.get(i));
				pw.println(tags.get(i));
				pw.println(dates.get(i));
				pw.println(sizes.get(i));
			}
			pw.close();
			return true;
		}
		catch(IOException e){
			return false;
		}
	}
	
	public void scry(){
		ArrayList<String> newFiles = new ArrayList<String>();
		HashSet<String> missingFiles = new HashSet<String>();
		for(String l : locs) missingFiles.add(l);
		scry(mainDir, "", newFiles, missingFiles);
		//TODO moving
		//confirm removal
		if(missingFiles.size() > 0){
			String msg = missingFiles.size() + " files were lost:";
			for(String lst : missingFiles) msg = msg + "\n - " + lst;
			msg = msg + "\n\nDelete them?";
			if(Main.confirmPrompt(msg)){
				for(String lst : missingFiles){
					int idx = locs.indexOf(lst);
					locs.remove(idx);
					names.remove(idx);
					tags.remove(idx);
					dates.remove(idx);
					sizes.remove(idx);
				}
			}
		}
		//confirm addition
		if(newFiles.size() > 0){
			String msg = newFiles.size() + " files were Found:";
			for(String fnd : newFiles) msg = msg + "\n - " + fnd;
			msg = msg + "\n\nAdd them?";
			if(Main.confirmPrompt(msg)){
				for(String fnd : newFiles){
					File fi = new File(mainDir, fnd);
					locs.add(fnd);
					names.add(fi.getName());
					tags.add(NEW_TAG);
					dates.add(Main.longDate(fi.lastModified()));
					sizes.add(fi.length());
				}
			}
		}
	}
	
	private void scry(File dir, String curPath, ArrayList<String> newFiles, HashSet<String> missingFiles){
		for(File fi : dir.listFiles()){
			if(fi.isDirectory()){
				scry(fi, curPath + dir.getName() + "/", newFiles, missingFiles);
			}
			else{
				if(!fi.getName().equals(LIST_NAME)){
					String loc = curPath + fi.getName();
					if(missingFiles.contains(loc)){
						//TODO change size and (date?)
						missingFiles.remove(loc);
					}
					else{
						newFiles.add(loc);
					}
				}
			}
		}
	}
	
}