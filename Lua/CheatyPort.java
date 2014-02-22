import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Scanner;

public class CheatyPort {

	public static final File MC_LOC = new File("C:\\Scyptnex\\FTB\\Unleashed\\minecraft");//new File(new File(getSysStore()), ".minecraft");
	public static final String LUA_EXT = ".lua";

	public static void main(String[] args) throws IOException{
		ArrayList<File> luas = new ArrayList<File>();
		findAllLuas(new File("."), luas);
		System.out.println("Found " + luas.size() + " luas");
		copyToComputers(MC_LOC, luas);
	}

	public static void copyToComputers(File mc, ArrayList<File> luas){
		File svdir = new File(mc, "saves");
		for(File world : svdir.listFiles()) if(world.isDirectory()){
			File compDir = new File(world, "computer");
			if(!compDir.exists()) continue;
			//read the next id
			File lid = new File(compDir, "lastid.txt");
			int nid = 0;
			if(lid.exists()){
				try {
					Scanner sca = new Scanner(lid);
					nid = sca.nextInt() + 1;
					sca.close();
				} catch (FileNotFoundException e) {
				}
				
			}
			for(int i=0; i<= nid; i++){
				File nd = new File(compDir, i + "");
				if(!nd.exists()) nd.mkdir();
			}
			for(File cmp : compDir.listFiles()) if(cmp.isDirectory()){
				System.out.println(" - " + cmp.getAbsolutePath());
				copyOver(cmp, luas);
			}
		}
	}

	public static void copyOver(File comp, ArrayList<File> luas){
		for(File fi : comp.listFiles()) rmrf(fi);
		for(File lua : luas){
			File to = new File(comp, lua.getName());
			try{
				copyFile(lua, to);
			}
			catch(IOException exc){
				System.err.println("Copying " + lua.getAbsolutePath() + " failed");
			}
		}
	}

	public static void findAllLuas(File dir, ArrayList<File> luas){
		for(File fi : dir.listFiles()){
			if(fi.isDirectory()) findAllLuas(fi, luas);
			else if(fi.getName().endsWith(LUA_EXT)) luas.add(fi);
		}
	}

	public static String getSysStore(){
		String tmp = System.getenv("APPDATA");
		if(tmp == null || tmp.length() < 1)
			tmp = System.getProperty("user.home");
		return tmp;
	}
	
	public static void rmrf(File fi){
		if(fi.isDirectory()){
			for(File fl : fi.listFiles()) rmrf(fl);
		}
		fi.delete();
	}

	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if(!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
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
	}

}
