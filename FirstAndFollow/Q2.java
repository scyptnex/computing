public class Q2 {
	
	public static void main(String[] args){
		try{
			All.printM(args[0]);
		}
		catch(Exception e){
			System.out.println("Usage: java Q2 something.grammar\n\twhere something.grammar is a grammar file");
		}
	}
}
