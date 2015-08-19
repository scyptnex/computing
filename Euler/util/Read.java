package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class Read {

	public static void main(String[] args){
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
        return listOfNumbers(lon, null);
    }

	public static ArrayList<Long> listOfNumbers(String lon, String delim){
		ArrayList<Long> ret = new ArrayList<Long>();
		lon = lon.trim();
		Scanner sca = new Scanner(lon);
		if(delim != null) sca.useDelimiter(delim);
		while(sca.hasNextLong()){
			ret.add(sca.nextLong());
		}
		return ret;
	}

	public static Stream<String> streamLines(Class<?> cla) throws IOException{
		int num = -1;
		if(cla.getSimpleName().startsWith("Euler") || cla.getSimpleName().startsWith("E")){
			num = Integer.parseInt(cla.getSimpleName().replaceAll("[^0-9]", ""));
		}
		if(num == -1) throw new IOException("Unrecognised class-name format");
		String optA = num + ".in";
		String optB = "E" + (num < 100 ? "0" : "") + (num < 10 ? "0" : "") + num + ".txt";
		for(File f : Arrays.asList(new File(optA), new File(optB), new File("Euler/" + optA), new File("Euler/" + optB))) if(f.exists() && !f.isDirectory()){
			return streamLines(f.getAbsolutePath());
		}
		throw new IOException("Could not find a suitable file for " + cla.getSimpleName());
	}

	public static Stream<String> streamLines(String fName) throws IOException {
		return Files.lines(new File(fName).toPath());
	}

}
