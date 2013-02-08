public class Squirt {
	
	public static void main(String[] args){
		for(int i=0; i<10; i++){
			System.out.println(i + ": " + squirt(16, 1, i));
		}
		
	}
	
	public static double squirt(int num, double guess, int reps){
		if(reps <= 0) return guess;
		else{
			return squirt(num, 0.5*(guess + (num/guess)), reps-1);
		}
	}
	
}