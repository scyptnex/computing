import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class AutoPort {
	
	public static final int REFRESH_LOCS_COUNT = 30;
	public static final int COMPUTER_BUFFER_COUNT = 3;
	public static final int SLEEP_AMT = 1000;

	public static void main(String[] args) throws IOException{

		//retrieve the system store
		String sysStore = System.getenv("APPDATA");
		if(sysStore == null || sysStore.length() < 1)
			sysStore = System.getProperty("user.home");
		sysStore = sysStore + File.separator + ".minecraft";

		new AutoPort("luas", sysStore, "C:\\Scyptnex\\FTB\\Unleashed\\minecraft", ".");
	}

	public AutoPort(String sourceDir, String ... targetDirs) throws IOException{
		ArrayList<File> computerDirs = null;
		ArrayList<File> luaFiles = null;
		HashMap<String, Long> knownModTimes = new HashMap<String, Long>();
		int iter = 0;
		while(true){
			//refresh the list of computers every so-often
			if(iter % REFRESH_LOCS_COUNT == 0){
				computerDirs = getComputerDirs(targetDirs);
				luaFiles = getLuaFiles(sourceDir);
				System.out.println("Keeping " + computerDirs.size() + " computers, " + luaFiles.size() + " luas");
			}
			
			//check if a new version has been made
			for(File lFi : luaFiles){
				long fiModTime = lFi.lastModified();
				for(File cDir : computerDirs){
					String key = lFi.getPath() + ":" + cDir.getPath();
					File targ = new File(cDir, lFi.getPath().substring((sourceDir + File.separator).length()));
					if(!knownModTimes.containsKey(key) || knownModTimes.get(key) < fiModTime || !targ.exists()){
						knownModTimes.put(key, tryPort(lFi, targ));
					}
				}
			}
			
			try {
				Thread.sleep(SLEEP_AMT);
			} catch (InterruptedException e) {
				break;
			}
			iter++;
		}
	}
	
	/**
	 * Port one lua into one computedir as targetFile.
	 * if the file already exists, only port if its modtime is before the lua's
	 * Return the timestamp of the file in the compdir
	 */
	public static long tryPort(File lua, File targetFile) throws IOException{
		if(targetFile.exists() && targetFile.lastModified() >= lua.lastModified()) return targetFile.lastModified();
		
		//the computer's file is older than the lua (or it doesnt exist), so copy it
		if(!targetFile.getParentFile().exists()) targetFile.getParentFile().mkdirs();
		if(!targetFile.exists()) targetFile.createNewFile();
		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(lua).getChannel();
			destination = new FileOutputStream(targetFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally {
			if(source != null) {
				source.close();
			}
			if(destination != null) {
				destination.close();
			}
		}
		System.out.println("TRANSFERRED " + lua.getPath() + " TO " + targetFile.getPath());
		return targetFile.lastModified();
	}
	
	/**
	 * Retrieve the list of lua files recursively
	 */
	public static ArrayList<File> getLuaFiles(String sourceDir){
		ArrayList<File> ret = new ArrayList<File>();
		getLuaFiles(new File(sourceDir), ret);
		return ret;
	}
	private static void getLuaFiles(File searchDir, ArrayList<File> list){
		if(!searchDir.exists()) return;
		else if(searchDir.isDirectory()) for(File fi : searchDir.listFiles()) getLuaFiles(fi, list);
		else if (!searchDir.isHidden()) list.add(searchDir);
	}

	/**
	 * Retrieve (and create) the directories of all the relevant computers 
	 */
	public static ArrayList<File> getComputerDirs(String ... searchDirs){
		ArrayList<File> ret = new ArrayList<File>();
		for(String s : searchDirs){
			File svDir = new File(s + File.separator + "saves");
			if(svDir.exists() && svDir.isDirectory()) for(File world : svDir.listFiles()) if(world.isDirectory()){
				File compDir = new File(world, "computer");
				if(!compDir.exists()) continue;//avoid worlds without computercraft
				int nid = 0;
				try {
					File lid = new File(compDir, "lastid.txt");
					if(lid.exists()){
						Scanner sca = new Scanner(lid);
						nid += sca.nextInt();
						sca.close();
					}
				} catch (FileNotFoundException e) {
					nid = 0;
				}
				nid += COMPUTER_BUFFER_COUNT;
				for(int i=0; i<= nid; i++){
					File nd = new File(compDir, i + "");
					if(!nd.exists()) nd.mkdir();
					ret.add(nd);
				}
			}
		}
		return ret;
	}

}
