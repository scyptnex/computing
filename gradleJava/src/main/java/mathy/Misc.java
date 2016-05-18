package mathy;

public class Misc {
    public static long fibo(int n){
        if(n < 2) return 1;
        return fibo(n-1) + fibo(n-2);
    }
    public static void main(String[] args){
        try{
            int num = Integer.parseInt(args[0]);
            System.out.println("Fib(" + num + ") = " + fibo(num) + ".");
        } catch(Exception e){
            System.err.println("Usage: java mathy.Misc <number>");
        } 
    }
}
