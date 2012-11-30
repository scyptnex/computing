import java.util.*;
import java.io.*;
import javax.swing.table.*;

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
	
	private ArrayList<String> cypNames;
	private ArrayList<String> plainNames;
	private ArrayList<String> dates;
	private ArrayList<Long> sizes;//kilobytes
	private ArrayList<String> tags;
	private ArrayList<Integer> storeOrigs;
	
	public Storage(){
		cypNames = new ArrayList<String>();
		plainNames = new ArrayList<String>();
		dates = new ArrayList<String>();
		sizes = new ArrayList<Long>();
		tags = new ArrayList<String>();
		storeOrigs = new ArrayList<Integer>();
	}
	
	public void add(String cname, String pname, String d, Long s, String t, int o){
		cypNames.add(cname);
		plainNames.add(pname);
		dates.add(d);
		sizes.add(s);
		tags.add(t);
		storeOrigs.add(o);
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
	
	public void loadAll(ArrayList<File> stores, File tmp, Secureify sec) throws IOException{
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
	
	public void saveAll(ArrayList<File> stores, File tmp, Secureify sec) throws IOException{
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
		if(col == COL_NAME){
			//TODO rename file
			plainNames.set(row, (String)value);
		}
		else if(col == COL_TAGS){
			tags.set(row, ((String)value).toLowerCase());
		}
	}
	
	public static String curDate(){
		String mon = (Calendar.getInstance().get(Calendar.MONTH)+1) + "";
		String day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
		return Calendar.getInstance().get(Calendar.YEAR) + "-" + (mon.length() == 1 ? "0" + mon : mon) + "-" + (day.length() == 1 ? "0" + day : day);
	}
	
}