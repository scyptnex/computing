import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class DirectConnections {

	public static void main(String[] args) {
		Scanner sca = new Scanner(System.in);
		int ntc = sca.nextInt();
		for(int coun=0; coun<ntc; coun++){
			ArrayList<City> pq = new ArrayList<City>();
			int numCities = sca.nextInt();
			for (int i = 0; i < numCities; i++) {
				City c = new City();
				c.loc = sca.nextInt();
				pq.add(c);
			}
			for (int i = 0; i < numCities; i++) {
				pq.get(i).pop = sca.nextInt();
			}
			// sort cities decreasing by population
			Collections.sort(pq, new Comparator<City>() {
				@Override
				public int compare(City arg0, City arg1) {
					return arg1.pop - arg0.pop;
				}
			});
			BigInteger cables = BigInteger.ZERO;
			for(int a=0; a< pq.size()-1; a++){
				for(int b=a+1; b<pq.size(); b++){
					long apop = pq.get(a).pop;
					cables = cables.add(BigInteger.valueOf(apop*Math.abs((long)pq.get(a).loc - (long)pq.get(b).loc)));
				}
			}
			System.out.println(cables.mod(BigInteger.valueOf(1000000007)));
		}
		sca.close();
	}

	public static class City {
		int pop;
		int loc;
		
		@Override
		public String toString() {
			return "{" + loc + ":" + pop + "}";
		}
	}

}
