package p001t040;

public class Euler012TriFactors {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int cur = 1;
		long tri = 0;
		while(true){
			tri += cur;
			int sz =util.Numeral.divisors(tri).size();
			System.out.println(cur + ", " + tri + ", " + sz); 
			if(sz > 500){
				break;
			}
			cur++;
		}
	}

}
