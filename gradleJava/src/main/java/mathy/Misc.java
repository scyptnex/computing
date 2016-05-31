package mathy;

import exprs.ExprLexer;
import exprs.ExprParser;
import org.antlr.v4.runtime.*;

import java.util.Arrays;
import java.util.stream.Collectors;

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
        parse(Arrays.stream(args).map(s -> s + "\n").collect(Collectors.joining()));
    }
}
