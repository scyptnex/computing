import java.io.*;
import java.text.*;
import java.util.*;

public class Dates {
	
	public static void main(String[] args){
		File fi = new File("out.tex");
		Date d = new Date(fi.lastModified());
		System.out.println(d);
		d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(d));
	}
	
}