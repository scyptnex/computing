package p041t080;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Euler042TriWord {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		long max = 0;
		long cur = 1;
		HashSet<Long> tris = new HashSet<Long>();
		while (max < 1000000000L) {
			max = max + cur;
			tris.add(max);
			cur++;
		}

		File fi = new File("42.in");
		Scanner sca = new Scanner(fi);
		sca.useDelimiter(",");
		ArrayList<String> nms = new ArrayList<String>();
		while (sca.hasNext()) {
			nms.add(sca.next().replaceAll("\"", "").toUpperCase());
		}
		sca.close();
		
		int count = 0;
		for(String s : nms){
			long score = 0;
			for(char c: s.toCharArray()){
				score += (c-'A' + 1);
			}
			if(tris.contains(score)){
				count++;
			} else if (score > max){
				throw new RuntimeException("wtf?");
			}
		}
		System.out.println(count);
	}

}
