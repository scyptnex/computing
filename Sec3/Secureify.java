import java.io.*;

public abstract class Secureify {
	
	public abstract boolean check(File checkFile);
	
	public abstract File encryptSpecialFile(File in, File out, boolean encrypt);
	
	public abstract String encryptString(String in, boolean encrypt);
	
	public File encryptMainFile(File in, File store, boolean encrypt){
		if(!store.exists() || !in.exists() || !store.isDirectory() || in.isDirectory()) return null;
		String newName = encryptString(in.getName(), encrypt);
		if(newName == null) return null;
		return encryptSpecialFile(in, new File(store, newName), encrypt);
	}
	
}