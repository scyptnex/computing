package p041t080;

public class Euler063DigitRoot {

    public static void main(String[] args){
        int pow=1;
        int lb;
        int sum = 0;
        do{
            lb = (int)Math.ceil(Math.pow(Math.pow(10, pow - 1), 1.0 / (double) pow));
            sum += 10-lb;
            pow++;
        }while (lb <= 9);
        System.out.println(sum);
    }

}
