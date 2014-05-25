package p000t040;

public class Euler005lcm20 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int cur = 1;
		while(true){
			boolean divis = true;
			for(int i=2; i<=20; i++){
				if(cur%i != 0){
					divis = false;
					break;
				}
			}
			if(divis){
				break;
			}
			else{
				cur++;
			}
		}
		System.out.println(cur);
	}

}
