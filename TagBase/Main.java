import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

public class Main {
	
	public static TagBase tb;
	
	public static void main(String[] args){
		tb = TagBase.getBase(args.length < 1 || ! new File(args[0]).isDirectory() ? new File("."): new File(args[0]));
		if(tb == null) exitErr("Failed to load tagbase");
		else{
			Gui gui = new Gui(tb);
		}
	}
	
	public static boolean confirmPrompt(String msg){
		return JOptionPane.showConfirmDialog(null, msg, "Confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}
	
	public static void informPrompt(String msg){
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public static void exit(){
		tb.save();
		System.exit(0);
	}
	
	public static void exitErr(String errMsg){
		System.err.println(errMsg);
		System.exit(1);
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
