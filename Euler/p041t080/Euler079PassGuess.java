package p041t080;

import util.Stringy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Euler079PassGuess {

    public static void main(String[] args) throws IOException {
        Path p = new File("Euler/79.in").toPath();
        try(Stream<String> s = Files.lines(p)){
            List<String> fil = s.collect(Collectors.toList());
            int idfs = 7;
            while(true){
                System.out.println(idfs);
                String pass = cpass(fil, idfs, "");
                if(pass != null){
                    System.out.println(pass);
                    break;
                }
                idfs++;
            }
        }
    }

    private static String cpass(List<String> knowns, int maxLength, String curPass) {
        if(curPass.length() > maxLength) return null;
        Set<Character> firsts = knowns.stream().collect(HashSet::new, (hs, s) -> {if(s.length() > 0) hs.add(s.charAt(0));}, HashSet::addAll);
        if(firsts.size() == 0){
            return curPass;
        }
        for(Character f : firsts){
            String np = curPass + f;
            String p = cpass(knowns.parallelStream().map(s -> s.startsWith(f + "") ? s.substring(1) : s).collect(Collectors.toList()), maxLength, np);
            if(p != null){
                System.out.println(knowns);
                return p;
            }
        }
        return null;
    }

}
