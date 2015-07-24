package p041t080;

import util.Collect;

import java.util.ArrayList;
import java.util.HashMap;

public class Euler062CubicPerm {

    public static void main(String[] args){
        long i = 0;
        int chars = -1;
        HashMap<String, ArrayList<Long>> hm = new HashMap<>();
        while(i++ >= 0){
            String cubeHash = cubeHash(i);
            if(cubeHash.length() > chars){
                hm = new HashMap<>();
                chars = cubeHash.length();
            }
            if(!hm.containsKey(cubeHash)){
                hm.put(cubeHash, new ArrayList<Long>());
            }
            hm.get(cubeHash).add(i);
            if(hm.get(cubeHash).size() >= 5){
                long sml = hm.get(cubeHash).get(0);
                System.out.println(sml*sml*sml);
                break;
            }
        }
    }

    public static String cubeHash(long i){
        return new String(Collect.toArrayC(Collect.sort(Collect.fromArray((i*i*i + "").toCharArray()))));
    }

}
