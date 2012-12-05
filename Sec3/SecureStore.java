import java.io.*;
import java.util.*;

public abstract class SecureStore {
	
	public static final int DEFAULT_FIELDS = 5;
	public static final String CRYP_IDENT = "cipher";
	public static final String PLAIN_IDENT = "plain";
	public static final String DATE_IDENT = "date";
	public static final String SIZE_IDENT = "size";
	public static final String TAG_IDENT = "tag";
	
	public static final String NEW_TAG = "new";
	
	public final ArrayList<File> stores;
	public final Secureify sec;
	
	public SecureStore(ArrayList<File> stores, Secureify sec){
		this.stores = stores;
		this.sec = sec;
	}
	
	public ArrayList<File> getTmpLibs(File temp){
		ArrayList<File> ret = new ArrayList<File>(stores.size());
		for(int i=0; i<stores.size(); i++){
			ret.add(new File(temp, i + "-list.tmp"));
		}
		return ret;
	}
	public void unlock(File temp) throws IOException{
		synchronized(this){
			ArrayList<File> tmpFiles = getTmpLibs(temp);
			for(int i=0; i<stores.size(); i++){
				if(!stores.get(i).exists()) stores.get(i).mkdirs();
				else{
					File lib = new File(stores.get(i), Ssys3.LIBRARY_NAME);
					if(lib.exists()){
						File tmpLib = sec.encryptSpecialFile(lib, tmpFiles.get(i), false);
						if(tmpLib == null) throw new IOException("Decrypt library file failed");
						loadLib(tmpLib, i);
					}
				}
			}
		}
	}
	public void lock(File temp) throws IOException{
		synchronized(this){
			//init
			ArrayList<File> tmpFiles = getTmpLibs(temp);
			ArrayList<PrintWriter> pws = new ArrayList<PrintWriter>();
			for(File fi : tmpFiles) pws.add(new PrintWriter(fi));
			
			//write the headers
			for(PrintWriter pw : pws){
				pw.println(CRYP_IDENT);
				pw.println(PLAIN_IDENT);
				pw.println(DATE_IDENT);
				pw.println(SIZE_IDENT);
				pw.println(TAG_IDENT);
				pw.println();//empty line designates end of header
			}
			
			//write the content
			for(int i=0; i<size(); i++){
				PrintWriter pw = pws.get(getStoreLoc(i));
				pw.println(getCipherName(i));
				pw.println(getPlainName(i));
				pw.println(getDate(i));
				pw.println(getSize(i));
				pw.println(getTags(i));
			}
			
			//close the files
			for(PrintWriter pw : pws) pw.close();
			
			//encrypt the files
			for(int i=0; i<stores.size(); i++){
				File ret = sec.encryptSpecialFile(tmpFiles.get(i), new File(stores.get(i), Ssys3.LIBRARY_NAME), true);
				if(ret == null) throw new IOException("Failed to encrypt temp file");
			}
		}
	}
	public File getEncFile(int i){
		File ret = new File(stores.get(getStoreLoc(i)), getCipherName(i));
		if(!ret.exists() || ret.isDirectory()) return null;
		return ret;
	}
	
	public abstract int size();
	public abstract int totalLength();
	
	protected abstract String getCipherName(int i);
	protected abstract String getPlainName(int i);
	protected abstract String getDate(int i);
	protected abstract long getSize(int i);
	protected abstract String getTags(int i);
	protected abstract int getStoreLoc(int i);
	
	public abstract int importFile(File in, int store);
	public abstract File exportFile(int i, File temp);
	
	protected abstract boolean add(String cName, String pName, String date, long size, String tags, int storeLoc);
	protected abstract boolean rename(int i, String newName);
	protected abstract boolean move(int i, int newStore);
	protected abstract boolean retag(int i, String newTag);
	protected abstract boolean delete(int i);
	
	//Private utility methods
	private void loadLib(File libFile, int loc) throws IOException{
		Scanner sca = new Scanner(libFile);
		boolean header = true;
		int stage = 0;
		
		String[] fields = new String[DEFAULT_FIELDS];
		
		while(sca.hasNextLine()){
			String ln = sca.nextLine();
			if(header){
				if(ln.length() == 0){//skip line
					header = false;
				}
				else{
					//maybe when not lazy
				}
			}
			else{
				fields[stage] = ln;
				stage++;
				if(stage >= DEFAULT_FIELDS){
					add(fields[0], fields[1], fields[2], Long.parseLong(fields[3]), fields[4], loc);
					stage = 0;
				}
			}
		}
		sca.close();
	}
	
}