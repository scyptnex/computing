package p000t040;

public class Euler002EvenFib {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int b=1;
		int a=1;
		int sum = 0;
		while(b < 4000000){
			if(b%2 == 0) sum+=b;
			int temp = b;
			b = a + b;
			a = temp;
		}
		System.out.println(sum);
	}

}
