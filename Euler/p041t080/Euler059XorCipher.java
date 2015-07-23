package p041t080;

import util.Read;

import java.util.ArrayList;

public class Euler059XorCipher {

    public static void main(String[] args){
        ArrayList<Byte> chrs = new ArrayList<>();
        for(String s : Read.fRead("/home/nic/computing/Euler/59.in")){
            for(Long l : Read.listOfNumbers(s.replaceAll("[^0-9]", " "))){
                chrs.add((byte)(long)l);
            }
        }
        int engLetters = getEngLetters(chrs);
        for(byte a='a'; a<= 'z'; a++){
            for(byte b='a'; b<= 'z'; b++){
                for(byte c='a'; c<= 'z'; c++){
                    ArrayList<Byte> key = new ArrayList<Byte>();
                    key.add(a);
                    key.add(b);
                    key.add(c);
                    ArrayList<Byte> dec = xor(chrs, key);
                    int curLetters = getEngLetters(dec);
                    if(curLetters > engLetters){
                        System.out.print(key + " ");
                        System.out.print(engLetters + " ");
                        long sum = 0;
                        for(byte db : dec){
                            System.out.print((char)db);
                            sum += db;
                        }
                        System.out.println();
                        System.out.println("  " + sum);
                        engLetters = curLetters;
                    }
                }
            }
        }
    }

    public static ArrayList<Byte> xor(ArrayList<Byte> m, ArrayList<Byte> k){
        ArrayList<Byte> c = new ArrayList<>(m.size());
        int ki = 0;
        for(byte b : m){
            c.add((byte)(b ^ (byte)(k.get(ki))));
            ki = (ki+1)%k.size();
        }
        return c;
    }

    //simple heuristic for checking if the message is english text :)
    public static int getEngLetters(Iterable<Byte> txt){
        int count = 0;
        for(byte b : txt){
            if((b <= 'z' && b >= 'a') || (b <= 'Z' && b >= 'A') || (b <= '9' && b >= '0') || b == ' ') count++;
        }
        return count;
    }

}
