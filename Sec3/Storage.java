import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.table.*;
import javax.swing.*;

public class Storage extends AbstractTableModel{
	
	public static final int DEFAULT_FIELDS = 5;
	public static final String CRYP_IDENT = "cipher";
	public static final String PLAIN_IDENT = "plain";
	public static final String DATE_IDENT = "date";
	public static final String SIZE_IDENT = "size";
	public static final String TAG_IDENT = "tag";
	
	public static final String NEW_TAG = "new";
	
	public static final int COL_NAME = 0;
	public static final int COL_TAGS = 1;
	public static final int COL_DATE = 2;
	public static final int COL_SIZE = 3;
	
	private JFrame frm;
	private Secureify sec;
	private ArrayList<File> stores;
	private ArrayList<String> cypNames;
	private ArrayList<String> plainNames;
	private ArrayList<String> dates;
	private ArrayList<Long> sizes;//kilobytes
	private ArrayList<String> tags;
	private ArrayList<Integer> storeOrigs;
	
	private long totSize;
	
	public long sumLengths(){
		return totSize;
	}
	
	public Storage(){
		sec = null;
		stores = null;
		cypNames = new ArrayList<String>();
		plainNames = new ArrayList<String>();
		dates = new ArrayList<String>();
		sizes = new ArrayList<Long>();
		tags = new ArrayList<String>();
		storeOrigs = new ArrayList<Integer>();
	}
	
	public void useSecurity(Secureify s){
		sec = s;
	}
	
	public void useStorage(ArrayList<File> s){
		stores = s;
	}
	
	public void useFrame(JFrame fram){
		frm = fram;
	}
	
	public String describe(int i){
		synchronized(this){
			String ret = "Name:   " + plainNames.get(i) + "\n\n";
			ret += "Added:   " + dates.get(i) + "\n";
			ret += "Size:   " + sizes.get(i) + "KB\n";
			ret += "Tagged:   " + tags.get(i) + "\n\n";
			ret += "Cipher:   " + cypNames.get(i) + "\n";
			ret += "Store:   " + stores.get(storeOrigs.get(i)).getAbsolutePath() + " (" + storeOrigs.get(i) + ")";
			return ret;
		}
	}
	
	public String tagDesc(){
		synchronized(this){
			HashMap<String, Integer> counter = new HashMap<String, Integer>();
			int tot = 0;
			for(int i=0; i<getRowCount(); i++){
				String[] tgs = tags.get(i).split(" ");
				for(String t : tgs){
					if(!counter.containsKey(t)) counter.put(t, 0);
					counter.put(t, counter.get(t)+1);
					tot++;
				}
			}
			int ntc = ("" + tot).length();
			ArrayList<String> lst = new ArrayList<String>();
			for(String tag : counter.keySet()){
				String add = "" + counter.get(tag);
				while(add.length() < ntc) add = "0" + add;
				add = add + "  -  " + tag;
				lst.add(add);
			}
			Collections.sort(lst);
			String ret = "";
			for(String s : lst){
				ret = "\n   " + s.replaceFirst("0*", "") + ret;
			}
			return "Total " + tot + "\nDistinct " + counter.size() + ret;
		}
		
	}
	
	public String storeDesc(){
		synchronized(this){
			int[] scounts = new int[stores.size()];
			for(int so : storeOrigs){
				scounts[so] = scounts[so]+1;
			}
			String ret = "";
			for(int i=0; i<stores.size(); i++){
				if(i!=0) ret += "\n";
				ret+= stores.get(i).getAbsolutePath() + "  -  " + scounts[i];
			}
			return ret;
		}
	}
	
	private void unsyncAdd(String cname, String pname, String d, long sizeKB, String t, int o){
		cypNames.add(cname);
		plainNames.add(pname);
		dates.add(d);
		sizes.add(sizeKB);
		tags.add(t);
		storeOrigs.add(o);
		totSize += sizeKB;
	}
	
	public void add(String cname, String pname, String d, long sizeKB, String t, int o){
		if(containsEntry(pname)){
			JOptionPane.showMessageDialog(frm, "Refusing to add duplicate entry " + pname, "Duplicate", JOptionPane.ERROR_MESSAGE);
			return;
		}
		synchronized(this){
			unsyncAdd(cname, pname, d, sizeKB, t, o);
		}
	}
	
	public boolean containsEntry(String plain){
		return plainNames.contains(plain);
	}
	
	public File move(int idx, int newLoc){
		synchronized(this){
			File old = locate(idx);
			if(old == null) return null;
			File nw = new File(stores.get(newLoc), old.getName());
			boolean succeed = old.renameTo(nw);
			if(succeed){
				storeOrigs.set(idx, newLoc);
				return nw;
			}
			else return null;
		}
	}
	
	private File unsyncDelete(int i){
		File ret = locate(i);
		cypNames.remove(i);
		plainNames.remove(i);
		dates.remove(i);
		totSize -= sizes.get(i);
		sizes.remove(i);
		tags.remove(i);
		storeOrigs.remove(i);
		return ret;
	}
	
	public File delete(int i){
		File ret = null;
		synchronized(this){
			ret = unsyncDelete(i);
		}
		return ret;
	}
	
	public String plainName(int i){
		return plainNames.get(i);
	}
	
	public int curStore(int i){
		return storeOrigs.get(i);
	}
	
	public void loadLib(File libFile, int loc) throws IOException{
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
	
	public File locate(int i){
		File ret = new File(stores.get(storeOrigs.get(i)), cypNames.get(i));
		if(!ret.exists()) return null;
		if(ret.isDirectory()) return null;
		return ret;
	}
	
	public void loadAll(File tmp) throws IOException{
		ArrayList<File> tmpFiles = getTmpFiles(stores, tmp);
		for(int i=0; i<stores.size(); i++){
			if(!stores.get(i).exists()) stores.get(i).mkdirs();
			else{
				File lib = new File(stores.get(i), Ssys3.LIBRARY_NAME);
				if(lib.exists()){
					sec.encryptSpecialFile(lib, tmpFiles.get(i), false);
					loadLib(tmpFiles.get(i), i);
				}
			}
		}
	}
	
	public void saveAll(File tmp) throws IOException{
		synchronized(this){
			//init
			ArrayList<File> tmpFiles = getTmpFiles(stores, tmp);
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
			for(int i=0; i<cypNames.size(); i++){
				PrintWriter pw = pws.get(storeOrigs.get(i));
				pw.println(cypNames.get(i));
				pw.println(plainNames.get(i));
				pw.println(dates.get(i));
				pw.println(sizes.get(i));
				pw.println(tags.get(i));
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
	
	public boolean stockTake(int sto){
		boolean madeChange = false;
		synchronized(this){
			ArrayList<Integer> losts = new ArrayList<Integer>();
			Set<String> unknowns = new HashSet<String>();
			for(String fn : stores.get(sto).list()) unknowns.add(fn);
			for(int i=0; i<cypNames.size(); i++) if(storeOrigs.get(i) == sto){
				if(unknowns.contains(cypNames.get(i))) unknowns.remove(cypNames.get(i));
				else losts.add(i);
			}
			if(losts.size() > 0){
				String removal = stores.get(sto).getAbsolutePath() + " has lost track of " + losts.size() + " files.\nRemove the following:";
				for(int i : losts) removal = removal + "\n - " + plainNames.get(i);
				if(JOptionPane.showConfirmDialog(frm, removal, "Remove lost files", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					for(int x=losts.size()-1; x>= 0; x--) unsyncDelete(losts.get(x));
					madeChange = true;
				}
			}
			unknowns.remove(Ssys3.LIBRARY_NAME);
			for(String fn : unknowns){
				String decr = sec.encryptString(fn, false);
				int res = JOptionPane.NO_OPTION;
				if(decr != null) res = JOptionPane.showConfirmDialog(frm, "Add the file " + decr + "?\n\n - Cipher: " + fn, "Found file", JOptionPane.YES_NO_OPTION);
				if(res == JOptionPane.YES_OPTION){
					File orig = new File(stores.get(sto), fn);
					unsyncAdd(fn, decr, longDate(orig.lastModified()), orig.length()/Ssys3.KILOBYTE, NEW_TAG, sto);
				}
			}
		}
		return madeChange;
	}
	
	public static ArrayList<File> getTmpFiles(ArrayList<File> stores, File tmp){
		ArrayList<File> ret = new ArrayList<File>(stores.size());
		for(int i=0; i<stores.size(); i++){
			ret.add(new File(tmp, i + "-list.tmp"));
		}
		return ret;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return plainNames.size();
	}
	
	public String getColumnName(int col) {
		switch(col){
		case COL_NAME :{
			return "Name";
		}
		case COL_TAGS :{
			return "Tags";
		}
		case COL_DATE :{
			return "Date";
		}
		default :{
			return "Size";
		}
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch(col){
		case COL_NAME :{
			return plainNames.get(row);
		}
		case COL_TAGS :{
			return tags.get(row);
		}
		case COL_DATE :{
			return dates.get(row);
		}
		default :{
			return sizes.get(row);
		}
		}
	}
	
	public Class<?> getColumnClass(int c) {
		if(c == COL_SIZE) return Long.class;
		return String.class;
	}

	public boolean isCellEditable(int row, int col) {
		return (col == COL_NAME || col == COL_TAGS);
	}
	
	public void setValueAt(Object value, int row, int col) {
		synchronized(this){
			if(col == COL_NAME){
				String newPlain = (String) value;
				if(containsEntry(newPlain)){
					JOptionPane.showMessageDialog(frm, "Refusing to rename duplicate " + newPlain, "Duplicate", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String newCipher = sec.encryptString(newPlain, true);
				File oldf = locate(row);
				if(oldf == null){
					JOptionPane.showMessageDialog(frm, "Failed to locate " + plainNames.get(row), "Duplicate", JOptionPane.ERROR_MESSAGE);
					return;
				}
				File newf = new File(stores.get(storeOrigs.get(row)), newCipher);
				if(oldf.renameTo(newf)){
					plainNames.set(row, newPlain);
					cypNames.set(row, newCipher);
				}
				else{
					JOptionPane.showMessageDialog(frm, "Name change failed", "Duplicate", JOptionPane.ERROR_MESSAGE);
				}
			}
			else if(col == COL_TAGS){
				tags.set(row, ((String)value).toLowerCase());
			}
		}
	}
	
	public static String dateString(Date d){
		return new SimpleDateFormat("yyyy-MM-dd").format(d);
	}
	
	public static String longDate(long tstamp){
		return dateString(new Date(tstamp));
	}
	
	public static String curDate(){
		return dateString(new Date());
	}
	
}