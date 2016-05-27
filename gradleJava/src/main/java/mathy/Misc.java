package mathy;

import exprs.ExprLexer;
import exprs.ExprParser;
import org.antlr.v4.runtime.*;

public class Misc {

    public static long fibo(int n){
        if(n < 2) return 1;
        return fibo(n-1) + fibo(n-2);
    }

    public static void parse(String expr){
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(expr);
        Lexer lexer = new ExprLexer(antlrInputStream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokenStream);
        ExprParser.ProgContext tree = parser.prog();
        System.out.println(tree.toStringTree(parser));
    }

    public static void main(String[] args){
        try{
            int num = Integer.parseInt(args[0]);
            System.out.println("Fib(" + num + ") = " + fibo(num) + ".");
        } catch(Exception e){
            System.err.println("Usage: java mathy.Misc <number>");
        }

        parse("x = 5\n y = 3 \n");
    }
}
