
import java.io.*;
import java.util.*;

public class TagBaseII {
	
	public static void main(String[] args){Main.main(args);};
	
	public static final String LIST_NAME = "zzList.txt";
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
	
	public static TagBaseII getBase(File mainDir){
		//System.out.println(mainDir.getAbsolutePath());
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
			return new TagBaseII(list);
		} catch (IOException e) {
			return null;
		}
	}
	
	private TagBaseII(File list) throws IOException{
		mainDir = list.getParentFile();
		names = new ArrayList<String>();
		indexes = new HashMap<String, Integer>();
		tags = new HashMap<String, String>();
		dates = new HashMap<String, String>();
		sizes = new HashMap<String, Long>();
		paths = new HashMap<String, String>();
		totalSize = 0;
		Scanner sca = new Scanner(list);
		int count = 0;
		while(sca.hasNextLine()){
			String nm = sca.nextLine();
			names.add(nm);
			indexes.put(nm, count);
			tags.put(nm, sca.nextLine());
			dates.put(nm, sca.nextLine());
			Long size = Long.parseLong(sca.nextLine());
			totalSize += size;
			sizes.put(nm, size);
			paths.put(nm, sca.nextLine());
			count++;
		}
		sca.close();
	}
	
	/**
	 * Accessors
	 */
	public String name(int index){
		return names.get(index);
	}
	public int index(String name){
		return indexes.get(name);
	}
	public String tag(int index){
		return tag(names.get(index));
	}
	public String tag(String name){
		return tags.get(name);
	}
	public String date(int index){
		return date(names.get(index));
	}
	public String date(String name){
		return dates.get(name);
	}
	public Long size(int index){
		return size(names.get(index));
	}
	public Long size(String name){
		return sizes.get(name);
	}
	public String path(int index){
		return path(names.get(index));
	}
	public String path(String name){
		return paths.get(name);
	}
	
	/**
	 * Methods
	 */
	public int count(){
		return names.size();
	}
	public String getTotalSize(){
		String[] ends = {"B", "KB", "MB", "GB", "TB"};
		int end = 0;
		double div = 1;
		while(totalSize/div > 2048.0 && end < ends.length-1){
			end++;
			div = div*1024;
		}
		return Main.twoDecimal(totalSize/div) + " " + ends[end];
	}
	public void retag(int idx, String tag){
		retag(names.get(idx), tag);
	}
	public void retag(String name, String tag){
		tags.put(name, tag);
	}
	public void rename(int idx, String name){
		File oldName = new File(mainDir, paths.get(names.get(idx)));
		File newName = new File(oldName.getParentFile(), name);
		if(oldName.renameTo(newName)){
			String oldn = names.get(idx);
			String nm = newName.getName();
			names.set(idx, nm);
			indexes.put(nm, indexes.remove(oldn));
			tags.put(nm, tags.remove(oldn));
			sizes.put(nm, sizes.remove(oldn));
			dates.put(nm, dates.remove(oldn));
			paths.put(nm, paths.remove(oldn));
		}
		else{
			Main.informPrompt("Failed to rename " + oldName.getName() + " to " + newName.getName());
		}
	}
	public void rename(String oldName, String newName){
		rename(indexes.get(oldName), newName);
	}
	public boolean save(){
		try{
			PrintWriter pw = new PrintWriter(new File(mainDir, LIST_NAME));
			for(int i=0; i<names.size(); i++){
				String nm = names.get(i);
				pw.println(nm);
				pw.println(tags.get(nm));
				pw.println(dates.get(nm));
				pw.println(sizes.get(nm));
				pw.println(paths.get(nm));
			}
			pw.close();
			return true;
		}
		catch(IOException e){
			return false;
		}
	}
	public void scry(){
		ArrayList<String> newPaths = new ArrayList<String>();
		Set<String> knownNames = new HashSet<String>();
		for(String nm : names) knownNames.add(nm);
		recurFileTree(newPaths, knownNames, "", mainDir);
		
		//removing
		if(knownNames.size() > 0){
			String msg = knownNames.size() + " files were lost:";
			if(knownNames.size() < MAX_PROMPT_LINES) for(String lst : knownNames) msg = msg + "\n - " + lst;
			msg = msg + "\n\nRemove them?";
			if(Main.confirmPrompt(msg)){
				indexes.clear();
				for(String lost : knownNames){
					tags.remove(lost);
					dates.remove(lost);
					totalSize  = totalSize - sizes.get(lost);
					sizes.remove(lost);
					paths.remove(lost);
				}
				names.removeAll(knownNames);
				for(int i=0; i<names.size(); i++) indexes.put(names.get(i), i);
			}
		}
		
		//adding
		if(newPaths.size() > 0){
			String msg = newPaths.size() + " files were Found:";
			if(newPaths.size() < MAX_PROMPT_LINES) for(String fnd : newPaths) msg = msg + "\n - " + fnd;
			msg = msg + "\n\nAdd them?";
			if(Main.confirmPrompt(msg)){
				for(String fnd : newPaths){
					addNew(fnd);
				}
			}
		}
		else if(knownNames.size() <= 0) Main.informPrompt("Scan completed\nNo new or missing files\nFiles which were moved have been reaquired");
	}
	private void addNew(String newPath){
		File fi = new File(mainDir, newPath);
		addDetailed(newPath, Main.longDate(fi.lastModified()), NEW_TAG);
	}
	private void addDetailed(String newPath, String date, String tag){
		File fi = new File(mainDir, newPath);
		if(indexes.keySet().contains(fi.getName())){
			String msg = "Base already contains a file named: " + fi.getName() + "\nThe File:\n - "
						+ newPath + "\nIs a duplicate of\n - " + paths.get(fi.getName());
			Main.informPrompt(msg);
			return;
		}
		String nm = fi.getName();
		names.add(nm);
		indexes.put(nm, names.size()-1);
		dates.put(nm, date);
		tags.put(nm, tag);
		totalSize += fi.length();
		sizes.put(nm, fi.length());
		paths.put(nm, newPath);
	}
	private void fullImport(File dir, String path){
		File impf = new File(dir, IMPORT_FILENAME);
		try{
			Scanner sca = new Scanner (impf);
			while(sca.hasNextLine()){
				String nm = sca.nextLine();
				File chk = new File(mainDir, path + nm);
				if(chk.exists()){
					addDetailed(path + nm, sca.nextLine(), sca.nextLine());
				}
			}
			sca.close();
			if(!impf.delete()) System.err.println("Delete import file manually: " + impf.getAbsolutePath());
		}
		catch(IOException e){
			e.printStackTrace();
			Main.informPrompt("Something went horribly wrong while importing");
		}
	}
	private void recurFileTree(ArrayList<String> newPaths, Set<String> knownNames, String curPath, File curDir){
		//when this folder is from an export
		if(new File(curDir, IMPORT_FILENAME).exists()){
			if(Main.confirmPrompt("Fully import the directory:\n - " + curDir.getAbsolutePath())) fullImport(curDir, curPath);
		}
		//otherwise
		else for(File fi : curDir.listFiles()){
			if(fi.isDirectory()){
				if(!fi.getName().startsWith(".Trash")) recurFileTree(newPaths, knownNames, curPath + fi.getName() + "/", fi);
			}
			else{
				String name = fi.getName();
				String loc = curPath + name;
				if(!fi.getName().equals(LIST_NAME)){
					if(knownNames.contains(name)){
						knownNames.remove(name);
						if(fi.length() != sizes.get(name)){
							totalSize = totalSize - sizes.get(name) + fi.length();
							sizes.put(name, fi.length());
						}
						paths.put(name, loc);
					}
					else{
						newPaths.add(loc);
					}
				}
			}
		}
	}
}
