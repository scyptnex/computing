package p001t040;

public class Euler014Collatz {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int lng = 1;
		int lrg = 1;
		for(int i=2; i<1000000; i++){
			if(i%10000 == 0) System.out.println(i);
			int len = naive(i);
			if(len > lng){
				lng = len;
				lrg = i;
			}
		}
		System.out.println(lrg);
	}
	
	public static int naive(long i){
		int len = 1;
		while(i != 1){
			if(i%2 == 0){
				i = i/2;
			}
			else{
				i = (i*3)+1;
			}
			len++;
		}
		return len;
	}

}
