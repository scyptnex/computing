import java.io.*;
import java.util.*;

public abstract class SecureStore {
	
	public final ArrayList<File> stores;
	public final Secureify sec;
	
	public SecureStore(ArrayList<File> stores, Secureify sec){
		this.stores = stores;
		this.sec = sec;
	}
	
	public abstract boolean unlock(File temp);
	public abstract boolean lock(File temp);
	
	public abstract File getEncFile(int i);
	public abstract String getPlainName(int i);
	public abstract String getDate(int i);
	public abstract long getSize(int i);
	public abstract String getTags(int i);
	
	public abstract int importFile(File in, int store);
	public abstract File exportFile(int i, File temp);
	
	public abstract boolean rename(int i, String newName);
	public abstract boolean move(int i, int newStore);
	public abstract boolean retag(int i, String newTag);
	public abstract boolean delete(int i);
	
}