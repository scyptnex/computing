package p000t040;

import java.util.ArrayList;
import java.util.Collections;

public class Euler024LexoPerm {

	public static void main(String[] args) {
		ArrayList<String> perms = new ArrayList<>();
		permute("", perms, "0123456789");
		System.out.println(perms.size());
		Collections.sort(perms);
		System.out.println(perms.get(999999));
	}

	public static void permute(String cur, ArrayList<String> perms, String rest) {
		if(rest.length() == 0){
			perms.add(cur);
		}
		else{
			for(int i=0; i<rest.length(); i++){
				permute(cur + rest.charAt(i), perms, rest.substring(0, i) + rest.substring(i+1));
			}
		}
	}

}
