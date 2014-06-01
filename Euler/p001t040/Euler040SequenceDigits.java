package p001t040;

public class Euler040SequenceDigits {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Euler040SequenceDigits dgs = new Euler040SequenceDigits();
		long mult = 1;
		for(int i=1; i<=1000000; i*=10){
			mult *=dgs.get(i);
		}
		System.out.println(mult);
	}
	
	int cur = 1;
	String tmp = "1";
	int total = 0;
	
	public int get(int deci){
		while(total + tmp.length() <= deci){
			total += tmp.length();
			tmp = cur + "";
			cur++;
		}
		return tmp.charAt(deci - total) - '0';
	}

}
