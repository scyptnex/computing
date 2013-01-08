import java.io.*;
import java.util.*;
import javax.swing.*;

public class Main {
	
	public static void main(String[] args){
		TagBase tb = TagBase.getBase(new File("."));
		if(tb == null) exitErr("Failed to load tagbase");
		else{
			tb.scry();
			exit();
		}
	}
	
	public static boolean confirmPrompt(String msg){
		return JOptionPane.showConfirmDialog(null, msg, "Confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
	}
	
	public static void informPrompt(String msg){
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public static void exit(){
		System.exit(0);
	}
	
	public static void exitErr(String errMsg){
		System.err.println(errMsg);
		System.exit(1);
	}
}
