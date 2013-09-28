public class Logistic {
	
	public static void logistic(double[] gens, double r, double init){
		gens[0] = init;
		for(int i=1; i<gens.length; i++){
			gens[i] = r*(gens[i-1])*(1-gens[i-1]);
		}
	}
	
}