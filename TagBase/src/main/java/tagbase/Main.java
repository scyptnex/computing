package tagbase;

import tagbase.files.MultiSaver;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
	
	public static final String[] endings = {"avi", "mpg", "wmv", "mkv", "flv", "mp4", "mp3"};
	
	public static TagBaseII tb;
	public static Gui gui;
	
	public static void main(String[] args){
		tb = TagBaseII.getBase(args.length < 1 || ! new File(args[0]).isDirectory() ? new File("."): new File(args[0]));
		if(tb == null) exitErr("Failed to load tagbase");
		else{
			gui = new Gui(tb, tb);
		}
	}
	
	public static void use(File fi) throws IOException{
		boolean ismovie = false;
		for(String s : endings){
			if(fi.getName().endsWith(s)) ismovie = true;
		}
		if(ismovie){
			try{
				String[] cmd = new String[]{"vlc", fi.getAbsolutePath()};
				Process p = Runtime.getRuntime().exec(cmd);
				return;
			} catch (IOException exc){
				// do nothing
			}
		}
		Desktop.getDesktop().open(fi);
	}

	public static boolean confirmPrompt(String msg){
		return JOptionPane.showConfirmDialog(gui, msg, "Confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}
	
	public static void informPrompt(String msg){
		JOptionPane.showMessageDialog(gui, msg);
	}
	
	public static void exit(){
		try {
			new MultiSaver().save(tb.mainDir, tb);
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
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
	
	public static double twoDecimal(double dbl){
		if(dbl > 100) return Math.round(dbl);
		else if(dbl > 10) return Double.valueOf(new DecimalFormat("#.#").format(dbl));
		else return Double.valueOf(new DecimalFormat("#.##").format(dbl));
	}
}
