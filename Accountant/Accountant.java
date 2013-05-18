import java.text.*;
import java.util.*;
import java.io.*;

public class Accountant {
	
	public static final long MILLIS_PER_DAY = 1000*60*60*24;
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
		File cur = new File(current);
		File form = new File(former);
		dates = new ArrayList<String>();
		purchases = new ArrayList<String>();
		ammounts = new ArrayList<Integer>();
		notes = new ArrayList<String>();
		if(!readIn(cur)){
			if(!readIn(form)){
				System.err.println("OH SHIT!");
				System.exit(0);
			}
		}
		boolean going = true;
		Scanner scin = new Scanner(System.in);
		while(going){
			System.out.println();
			System.out.println("=====================================");
			System.out.println(dates.size() + " records; (v, a, l (number), s, q)");
			String command = scin.nextLine();
			if(command.length() == 0 || command.startsWith("q")) going = false;//quit
			else if(command.startsWith("l")){//list
				int lst = -1;
				if(command.length() > 2){
					try{
						lst = Integer.parseInt(command.substring(2));
					}
					catch(Exception exc){
						lst = -1;
					}
				}
				list(lst);
			}
			else if(command.startsWith("s")){//sort
				System.out.println("Sorting by date");
				sort();
			}
			else if(command.startsWith("v")){//eValuate
				evaluate();
			}
			else{//add
				getRecord();
			}
		}
		save(cur, form);
	}
	
	private long time(){
		return time(dateify(""));
	}
	private long time(String date){
		int yr = Integer.parseInt(date.substring(0, date.indexOf("-")));
		int mo = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
		int da = Integer.parseInt(date.substring(date.lastIndexOf("-")+1));
		Calendar c = Calendar.getInstance();
		c.set(yr, mo, da);
		return c.getTimeInMillis();
	}
	
	private void evaluate(){
		long curmil = time();
		long totDays = 0;
		int weekTotal = 0;
		double total = 0;
		int idx = 0;
		for(String d : dates){
			long dmil = time(d);
			long daysSince = (curmil-dmil)/MILLIS_PER_DAY;
			totDays = Math.max(totDays, daysSince);
			total += ammounts.get(idx);
			if(daysSince < 7) weekTotal += ammounts.get(idx);
			idx++;
		}
		System.out.println("Week spending:\t" + weekTotal);
		System.out.println("Long average:\t" + (total/totDays)*7.0);
	}
	
	private void list(int amt){
		System.out.println("Listing " + (amt == -1 ? "all" : amt+"") + " items");
		int start = (amt == -1 ? 0 : dates.size()-amt);
		if(start < 0) start = 0;
		for(int i=start; i<dates.size(); i++){
			System.out.println(dates.get(i) + "\t$" + ammounts.get(i) + "\t" + purchases.get(i) + " - " + notes.get(i));
		}
	}
	
	//bubble sort COS IM LAZY
	private void sort(){
		for(int end = dates.size(); end > 1; end--){
			for(int start = 0; start < end-1; start++){
				if(dates.get(start).compareTo(dates.get(start+1)) > 0){//sorts dates in ascending chronology
					String tdate = dates.get(start);
					String tpur = purchases.get(start);
					Integer tamt = ammounts.get(start);
					String tnote = notes.get(start);
					dates.set(start, dates.get(start+1));
					purchases.set(start, purchases.get(start+1));
					ammounts.set(start, ammounts.get(start+1));
					notes.set(start, notes.get(start+1));
					dates.set(start+1, tdate);
					purchases.set(start+1, tpur);
					ammounts.set(start+1, tamt);
					notes.set(start+1, tnote);
				}
			}
		}
	}
	
	public void save(File current, File former){
		try{
			if(!former.delete() || !current.renameTo(former)) throw new IOException("Rename File Failure");
			PrintWriter pw = new PrintWriter(current);
			for(int i=0; i<dates.size(); i++){
				pw.println(dates.get(i));
				pw.println(purchases.get(i));
				pw.println(ammounts.get(i));
				pw.println(notes.get(i));
			}
			pw.close();
		}
		catch(IOException e){
			System.err.println(e.toString());
		}
	}
	
	public void addRecord(String date, String name, int amt, String note){
		dates.add(date);
		purchases.add(name);
		ammounts.add(Math.abs(amt));
		notes.add(note);
	}
	
	public String dateify(String potential){
		String[] nums = potential.split(" ");
		int[] vals = new int[3];
		vals[0] = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		vals[1] = Calendar.getInstance().get(Calendar.MONTH) + 1;
		vals[2] = Calendar.getInstance().get(Calendar.YEAR);
		try{
			for(int i=0; i<vals.length; i++){
				if(nums.length > i && potential.length() > 0) vals[i] = Integer.parseInt(nums[i]);
			}
		}
		catch(Exception exc){
			return null;
		}
		String ret = "";
		//year
		if(vals[2] < 1000) ret = ret + "2";
		if(vals[2] < 100) ret = ret + "0";
		if(vals[2] < 10) ret = ret + "1";
		ret = ret + vals[2] + "-";
		//month
		if(vals[1] < 10) ret = ret + "0";
		ret = ret + vals[1] + "-";
		//day
		if(vals[0] < 10) ret = ret + "0";
		ret = ret + vals[0];
		return ret;
	}
	
	public void getRecord(){
		try{
			Scanner scin = new Scanner(System.in);
			System.out.println("When was the purchase([dd [mm [yy]]])");
			String dIn = dateify(scin.nextLine());
			if(dIn == null) return;
			System.out.println("What was the purchase?");
			String pIn = scin.nextLine();
			System.out.println("How much (dollars)?");
			int aIn = Integer.parseInt(scin.nextLine());
			System.out.println("Any Notes?");
			String nIn = scin.nextLine();
			addRecord(dIn, pIn, aIn, nIn);
		}
		catch(Exception exc){
			//do nothing
		}
	}
	
	private boolean readIn(File fi){
		try{
			Scanner sca = new Scanner(fi);
			while(sca.hasNextLine()){
				dates.add(sca.nextLine());
				purchases.add(sca.nextLine());
				ammounts.add(Integer.parseInt(sca.nextLine()));
				notes.add(sca.nextLine());
			}
			sca.close();
			return true;
		}
		catch(IOException exc){
			System.err.println("Error reading in " + fi.getAbsolutePath());
			return false;
		}
	}
	
}