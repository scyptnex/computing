package p001t040;

public class Euler036PalindomicBases {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int sum = 0;
		for(int i = 1; i<1000000; i++){
			String ten = i + "";
			String bin = Long.toBinaryString(i);
			if(isPal(ten) && isPal(bin)){
				System.out.println(ten + " - " + bin);
				sum += i;
			}
		}
		System.out.println(sum);
	}
	
	public static boolean isPal(String s){
		for(int i=0; i<=s.length()/2; i++){
			if(s.charAt(i) != s.charAt(s.length()-1-i)) return false;
		}
		return true;
	}

}
