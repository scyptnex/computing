package p001t040;

public class Euler009PythagTriplet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int a=1; a<1000; a++){
			for(int b=a; b<1000-a; b++){
				int c = 1000 - b - a;
				if(a*a + b*b == c*c){
					System.out.println(a + ", " + b + ", " + c);
					System.out.println(a*b*c);
				}
			}
		}
	}

}
