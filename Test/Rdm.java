
public class Rdm {

	public static void main(String[] args){
		final int num = 621;
		int start = (int)Math.floor(Math.random()*num);
		for(int i=0; i<5; i++)
		System.out.println((start +i) % num);
	}
	
}
