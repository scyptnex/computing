package p000t040;

public class Euler028SpiralSum {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long sum = 1;
		long last = 1;
		for(int i=1; i<=500; i++){
			long stride = i*2;
			sum += last*4+stride*10;
			last += stride*4;
		}
		System.out.println(sum);
	}

}
