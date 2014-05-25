package p000t040;

import java.io.File;
import java.math.BigInteger;
import java.util.*;

public class Euler022NameScore {
	
	public static void main(String[] args) throws Exception{
		File fi = new File("22.in");
		Scanner sca = new Scanner(fi);
		sca.useDelimiter(",");
		ArrayList<String> nms = new ArrayList<String>();
		while(sca.hasNext()){
			nms.add(sca.next().replaceAll("\"", ""));
		}
		sca.close();
		Collections.sort(nms);
		BigInteger sum = BigInteger.ZERO;
		int i = 1;
		for(String str : nms){
			int score = 0;
			for(char c : str.toCharArray()){
				score += (c - 'A' + 1);
			}
			sum = sum.add(new BigInteger((i*score) + ""));
			i++;
		}
		System.out.println(sum);
	}
	
}
