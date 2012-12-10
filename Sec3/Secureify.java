import java.io.*;

public abstract class Secureify {
	
	public abstract boolean check(File checkFile);
	
	public abstract File encryptSpecialFile(File in, File out, boolean encrypt);
	
	public abstract String encryptString(String in, boolean encrypt);
	
	public abstract String digest(File in);
	
	public File prepareMainFile(File in, File store, boolean encrypt){
		if(!store.exists() || !in.exists() || !store.isDirectory() || in.isDirectory()) return null;
		String newName = encryptString(in.getName(), encrypt);
		if(newName == null) return null;
		return new File(store, newName);
	}
	
	public File encryptMainFile(File in, File store, boolean encrypt){
		File out = prepareMainFile(in, store, encrypt);
		if(out == null) return null;
		return encryptSpecialFile(in, out, encrypt);
	}
	
}