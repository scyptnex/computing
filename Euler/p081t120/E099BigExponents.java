package p081t120;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import util.Collect;
import util.Read;

public class E099BigExponents {
	
	public static void main(String[] args) throws IOException{
		List<Double> lns = Read.streamLines(E099BigExponents.class)
			.map(l -> l.split(","))
			.map(sa -> new int[]{Integer.parseInt(sa[0]), Integer.parseInt(sa[1])})
			.map(ia -> Math.log(ia[0])*(double)ia[1])
			.collect(Collectors.toList());
		int max = 0;
		for(int i=1; i<lns.size(); i++){
			if(lns.get(i).compareTo(lns.get(max)) > 0) max = i;
		}
		System.out.println(max+1);
	}
	
}
