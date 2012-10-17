import java.io.*;
import java.util.*;

public class All {
	
	//printing type things
	public static final String TRUE_TEST = "accept";
	public static final String FALSE_TEST = "reject";
	public static final String Q1_FORMAT = " ->";
	public static final String Q2_FORMAT = " = ";
	
	//Exceptions
	public static final String[] ERROR_MESSAGES = {"No Start Term", "Multiple Start Terms", "Grammar is not LL(1)!", "Non-LL(1) Grammars may not be parsed"};
	
	public static void main(String[] args){
		if(args.length == 2){
			runAll(args[0], args[1]);
		}
		else{
			System.out.println("Usage:");
			System.out.println("\tjava All");
			System.out.println("\tjava All someGrammar.grammar someInput.input");
		}
	}
	
	public static void runAll(String gfname, String ifname){
		printFirstFollow(gfname);
		printM(gfname);
		printTests(gfname, ifname);
	}
	
	public static void printFirstFollow(String fname){
		try{
			Grammar g = new Grammar(fname, false);
			pr1(g);
		}
		catch(IOException iexc){
			System.out.println(iexc.toString());
		}
		catch(GrammarException gexc){
			System.out.println(gexc.toString());
		}
	}
	
	public static void printM(String fname){
		try{
			for(String str : new Grammar(fname, true).getTable()) System.out.println(str);
		}
		catch(IOException iexc){
			System.out.println(iexc.toString());
		}
		catch(GrammarException gexc){
			System.out.println(gexc.toString());
		}
	}
	
	public static void printTests(String gfname, String ifname){
		try{
			for(Boolean ans : new Grammar(gfname, true).parseAll(ifname)) System.out.println((ans) ? TRUE_TEST: FALSE_TEST);
		}
		catch(IOException iexc){
			System.out.println(iexc.toString());
		}
		catch(GrammarException gexc){
			System.out.println(gexc.toString());
		}
	}
	
	private static void pr1(Grammar g){
		System.out.println("First:");
		prls(g.getFirsts());
		System.out.println("Follow:");
		prls(g.getFollows());
	}
	
	private static void prls(Map<String, Set<String>> mp){
		for(String s : mp.keySet()){
			System.out.print("  " + s + Q1_FORMAT);
			for(String fir : mp.get(s)) System.out.print(" " + fir);
			System.out.println();
		}
	}
	
}
