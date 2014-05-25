package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Read {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	public static ArrayList<String> fRead(String nm){
		File fi = new File(nm);
		try {
			ArrayList<String> ret = new ArrayList<String>();
			Scanner sca = new Scanner(fi);
			while(sca.hasNextLine()){
				ret.add(sca.nextLine());
			}
			sca.close();
			return ret;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Long> listOfNumbers(String lon){
		ArrayList<Long> ret = new ArrayList<Long>();
		lon = lon.trim();
		Scanner sca = new Scanner(lon);
		while(sca.hasNextLong()){
			ret.add(sca.nextLong());
		}
		return ret;
	}

}
