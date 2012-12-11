import java.io.*;
import java.util.*;

import javax.swing.event.TableModelListener;
import javax.swing.table.*;

public class SBase extends AbstractTableModel{

	public static final long KILOBYTE = 1024;
	public static final long MEGABYTE = KILOBYTE*KILOBYTE;
	public static final long GIGABYTE = KILOBYTE*MEGABYTE;
	
	public static final int COL_NAME = 0;
	public static final int COL_TAGS = 1;
	public static final int COL_DATE = 2;
	public static final int COL_SIZE = 3;

	public static final String[] STORE_NAMES ={"zzAlpha.dat", "zzBeta.dat", "zzGamma.dat"};
	
	public static final String DEFAULT_TAG = "new";
	public static final String SECURE_EXTENSION = ".scr";
	public static final String DELETE_ADDITION = "[zz, deleted]";

	public ArrayList<String> name;
	public ArrayList<String> tags;
	public ArrayList<String> date;
	public ArrayList<Long> size;
	
	public long length;
	
	public static void main(String[] args){
		Ssys2.main(args);
	}

	public SBase(){
		name = new ArrayList<String>();
		tags = new ArrayList<String>();
		date = new ArrayList<String>();
		size = new ArrayList<Long>();
		
		length = 0;
	}
	
	public boolean load(SecureUtils sec){
		
		System.out.println("--loading--");
		
		File stfi = getCur(true);
		if(stfi == null) return false;//first time load
		File tmfi = new File(Ssys2.tempFold, stfi.getName());
		if(!sec.blockSecureSwap(stfi, tmfi, false)) return false;
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(tmfi));
			name = (ArrayList<String>)ois.readObject();
			tags = (ArrayList<String>)ois.readObject();
			date = (ArrayList<String>)ois.readObject();
			size = (ArrayList<Long>)ois.readObject();
			ois.close();
			
			for(Long l : size){
				length += l;
			}
			
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean save(SecureUtils sec){
		
		System.out.println("--saving--");
		
		File stfi = getCur(false);
		File tmfi = new File(Ssys2.tempFold, stfi.getName());
		
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tmfi));
			oos.writeObject(name);
			oos.writeObject(tags);
			oos.writeObject(date);
			oos.writeObject(size);
			oos.close();
			
			return sec.blockSecureSwap(stfi, tmfi, true);
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void exportAll(SecureUtils sec){
		File topFold = new File("export");
		Ssys2.rmrf(topFold);
		int cur = 0;
		File curFold = null;
		PrintWriter curLib = null;
		long maxLength = 20*GIGABYTE;
		long curLength = 0;
		try{
			for(int i=0; i<name.size(); i++){
				//make a home
				if(curFold == null){
					curFold = new File(topFold, "exp-" + cur);
					if(!curFold.exists()) curFold.mkdirs();
					curLib = new PrintWriter(new File(curFold, "zzlib.dat"));
					curLength = 0;
					cur++;
				}
				File enc = Ssys2.getFile(i + SECURE_EXTENSION);
				if(enc.exists()){
					File dec = new File(curFold, name.get(i));
					if(sec.blockSecureSwap(enc, dec, false)){
						curLib.println(dec.getName());//name
						curLib.println(date.get(i));//date
						curLib.println(tags.get(i));//tags
						curLength += dec.length();
					}
					else{
						System.err.println("Total export " + name.get(i) + " failed");
						dec.delete();
					}
				}
				if(curLength >= maxLength){
					curLib.close();
					curLib = null;
					curFold = null;
				}
			}
			if(curLib != null) curLib.close();
		}
		catch(IOException exc){
			exc.printStackTrace();
		}
	}
	
	public void finishRetrieve(File fi){
		System.out.println("Now we do something with " + fi);
		String oname = Ssys2.tempFold.getName() + "\\" + fi.getName();
		try {
			String comm = "cmd /c \"start " + oname + "\"";
			Process p = Runtime.getRuntime().exec(comm);
		} catch (IOException e) {
			System.err.println("Couldnt use");
		}
	}
	
	public void retrieveFile(int idx, SecureUtils sec){
		if(idx >= count()) return;
		File out = new File(Ssys2.tempFold, name.get(idx));
		if(out.exists()){
			finishRetrieve(out);
			return;
		}
		
		File st = Ssys2.getFile(idx + SECURE_EXTENSION);
		if(!st.exists()){
			tags.set(idx, tags.get(idx) + " " + DELETE_ADDITION);
			return;
		}
		
		sec.unblockSecureSwap(st, out, false, this);
	}
	
	public boolean importFile(String inloc, SecureUtils sec){
		File fi = new File(Ssys2.inFold, inloc);
		if(!fi.exists() || fi.isDirectory()) return false;
		String storeLoc = name.size() + SECURE_EXTENSION;
		File crypt = Ssys2.getFile(storeLoc);
		if(!sec.blockSecureSwap(crypt, fi, true)) return false;
		name.add(inloc.toLowerCase().replaceAll(" ", "."));
		tags.add(DEFAULT_TAG.toLowerCase());
		String mon = (Calendar.getInstance().get(Calendar.MONTH)+1) + "";
		String day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
		date.add(Calendar.getInstance().get(Calendar.YEAR) + "-" + (mon.length() == 1 ? "0" + mon : mon) + "-" + (day.length() == 1 ? "0" + day : day));
		size.add((fi.length() + 1023) / 1024);
		length += size.get(size.size() - 1);
		return true;
	}
	
	public int count(){
		return name.size();
	}

	/*
	 * Table model
	 */
	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return name.size();
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

	public Object getValueAt(int row, int col) {
		switch(col){
		case COL_NAME :{
			return name.get(row);
		}
		case COL_TAGS :{
			return tags.get(row);
		}
		case COL_DATE :{
			return date.get(row);
		}
		default :{
			return size.get(row);
		}
		}
	}
	
	public Class getColumnClass(int c) {
		if(c == COL_SIZE) return Long.class;
		return String.class;
	}

	public boolean isCellEditable(int row, int col) {
		return (col == COL_NAME || col == COL_TAGS);
	}
	
	public void setValueAt(Object value, int row, int col) {
		if(col == COL_NAME){
			name.set(row, ((String)value).toLowerCase());
		}
		else if(col == COL_TAGS){
			tags.set(row, ((String)value).toLowerCase());
		}
	}
	
	/*
	 * statics
	 */
	public static File getCur(boolean newest){
		/**File go = Ssys2.getFile(STORE_NAMES[0]);
		if(go.exists()) return go;
		return newest ? null : go;**/
		File[] fis = new File[STORE_NAMES.length];
		for(int i=0; i<STORE_NAMES.length; i++){
			fis[i] = Ssys2.getFile(STORE_NAMES[i]);
		}

		if(newest){
			int nwst = -1;
			for(int i=0; i<fis.length; i++){
				if(fis[i].exists() && (nwst == -1 || fis[i].lastModified() > fis[nwst].lastModified())){
					nwst = i;
				}
			}
			if(nwst == -1) return null;
			return fis[nwst];
		}

		//else oldest
		int oldst = -1;
		for(int i=0; i<fis.length; i++){
			if(!fis[i].exists()) return fis[i];
			else{
				if(oldst == -1 || fis[i].lastModified() < fis[oldst].lastModified()){
					oldst = i;
				}
			}
		}
		return fis[oldst];
	}
}
