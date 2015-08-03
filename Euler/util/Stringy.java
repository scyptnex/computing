package util;

import java.util.ArrayList;
import java.util.List;

public class Stringy {

    public static List<Character> chars(String s){
        return s.chars().collect(ArrayList::new, (al, i) -> al.add((char)i), (alm, alr) -> alm.addAll(alr));
    }

    public static String reverse(String s){
        StringBuffer ret = new StringBuffer();
        for(int i=s.length()-1; i>=0; i--){
            ret.append(s.charAt(i));
        }
        return ret.toString();
    }

    public static boolean isPallindrome(String s){
        for(int i=0; i<s.length()/2; i++){
            if(s.charAt(i) != s.charAt(s.length()-i-1)) return false;
        }
        return true;
    }

}
