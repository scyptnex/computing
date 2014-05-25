package p000t040;

public class Euler006SumSquare {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long sum = 0;
		long ssum = 0;
		for(int i=1; i<=100; i++){
			sum+=i;
			ssum+=(i*i);
		}
		System.out.println(Math.abs((sum*sum)-ssum));
	}

}
