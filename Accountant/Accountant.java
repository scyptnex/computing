import java.text.*;
import java.util.*;

public class Accountant {
	
	public static final String FILE_STORE = "bills.txt";
	public static final String BACKUP_STORE = "billsOld.txt";
	
	public ArrayList<String> dates;
	public ArrayList<String> purchases;
	public ArrayList<Integer> ammounts;
	public ArrayList<String> notes;
	
	public static void main(String[] args){
		new Accountant(FILE_STORE, BACKUP_STORE);
	}
	
	public Accountant(String current, String former){
		
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