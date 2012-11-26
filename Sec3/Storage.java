import java.util.*;
import javax.swing.table.*;

public class Storage extends AbstractTableModel{
	
	public static final int COL_NAME = 0;
	public static final int COL_TAGS = 1;
	public static final int COL_DATE = 2;
	public static final int COL_SIZE = 3;
	
	private ArrayList<String> cypNames;
	private ArrayList<String> plainNames;
	private ArrayList<String> dates;
	private ArrayList<Long> sizes;//kilobytes
	private ArrayList<String> tags;
	
	public Storage(){
		cypNames = new ArrayList<String>();
		plainNames = new ArrayList<String>();
		dates = new ArrayList<String>();
		sizes = new ArrayList<Long>();
		tags = new ArrayList<String>();
	}
	
	public void testFill(){
		cypNames.add("sd9csbdc7");
		cypNames.add("sd9dfk");
		plainNames.add("aa");
		plainNames.add("bbbb");
		dates.add("2010-11-07");
		dates.add("2008-03-21");
		sizes.add((long)324875);
		sizes.add((long)934785);
		tags.add("a b c");
		tags.add("b a d");
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
			plainNames.set(row, ((String)value).toLowerCase());
		}
		else if(col == COL_TAGS){
			tags.set(row, ((String)value).toLowerCase());
		}
	}
	
}