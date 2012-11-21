import java.io.*;

public abstract class Secureify {
	
	public abstract boolean check(File checkFile);
	
	public abstract File encryptFile(File in, boolean encrypt);
	
	public abstract String encryptString(String in, boolean encrypt);
	
}