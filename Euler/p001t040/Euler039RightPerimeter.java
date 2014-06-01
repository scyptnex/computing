package p001t040;

public class Euler039RightPerimeter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int max = 0;
		int maxP = -1;
		for(int i=2; i<=1000; i++){
			int count = 0;
			for(int c=1; c<i; c++){
				for(int b=1; b<=c; b++){
					for(int a=1; a<=b; a++){
						if(a + b + c == i && a*a + b*b == c*c){
							System.out.print("{" + a + "," + b + "," + c + "} ");
							count++;
						}
					}
				}
			}
			System.out.println(" - " + count + " . " + i);
			if(count > max){
				max = count;
				maxP = i;
			}
		}
		System.out.println(maxP);
	}

}
