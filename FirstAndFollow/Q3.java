public class Q3 {
	
	public static void main(String[] args){
		try{
			All.printTests(args[0], args[1]);
		}
		catch(Exception e){
			System.out.println("Usage: java Q1 something.grammar something.input\n\twhere something.grammar is a grammar file amd something.input is a list of possible sentences in the grammar");
		}
	}
	
}
