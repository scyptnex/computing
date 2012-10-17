public class Q1 {
	
	public static void main(String[] args){
		try{
			All.printFirstFollow(args[0]);
		}
		catch(Exception e){
			System.out.println("Usage: java Q1 something.grammar\n\twhere something.grammar is a grammar file");
		}
	}
}
