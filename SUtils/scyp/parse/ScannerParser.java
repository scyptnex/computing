package scyp.parse;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScannerParser {
	
	public static void main(String[] args) throws IOException{
		int count = 0;
		Scanner sca = new Scanner(new File("tpb.html"));
		while(runPast("<tr>", sca)){
			if(runPast("</tr>", sca)){
				count++;
			}
		}
		sca.close();
		System.out.println(count);
	}
	
	/**
	 * Reads from the current scanner until "pattern" HAS ALREADY BEEN MATCHED
	 * @param regexPattern a (java) regular expression for the pattern we want
	 * @param scan the scanner to exhaust
	 * @return true if we matched the pattern EVEN IF ITS AT THE END OF THE INPUT
	 */
	public static boolean runPast(String regexPattern, Scanner scan){
		Pattern dlim = scan.delimiter();//save the old delimiter
		Pattern lookout = Pattern.compile(regexPattern);
		scan.useDelimiter("");
		String curMatch = "";
		while(scan.hasNext()){
			Matcher mat = lookout.matcher(curMatch);
			if(mat.matches()){
				break;
			}
			
			if(mat.hitEnd()){
				curMatch = curMatch + scan.next();
			}
			else{
				curMatch = scan.next();
			}
		}
		scan.useDelimiter(dlim);
		return lookout.matcher(curMatch).matches();
	}
	
}
