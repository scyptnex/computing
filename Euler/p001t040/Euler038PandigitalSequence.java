package p001t040;

public class Euler038PandigitalSequence {

	public static void main(String[] args) {
		long max = 0;
		for(long i=1; i<100000000; i++){
			String pans = pans(i);
			if(pans != null){
				System.out.println(i + " - " + pans);
				max = Math.max(Long.parseLong(pans), max);
			}
		}
		System.out.println(max);
	}

	public static String pans(long i){
		String cur = "";
		int n = 1;
		while(n < 10 && cur.length() < 9){
			cur = cur + "" + (n*i);
			if(cur.length() == 9){
				return (Euler032Pandigital.isPan(cur) ? cur : null);
			}
			n++;
		}
		return null;
	}
	
}
