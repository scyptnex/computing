package p041t080;

public class Euler043SubPan {
	
	public static long sum = 0;
	
	public static void main(String[] args) {
//		for(int f8 = 17; f8<999; f8+=17){
//			String cur = f8 + "";
//			while(cur.length() < 3) cur = "0" + cur;
//			for(char d7='0'; d7<='9'; d7++) if(mods(13, d7, cur.charAt(0), cur.charAt(1)) && !cur.contains(d7 + "")){
//				cur = d7 + cur;
//				for(char d6='0'; d6<='9'; d6++) if(mods(11, d6, cur.charAt(0), cur.charAt(1)) && !cur.contains(d6 + "")){
//					cur = d6 + cur;
//					for(char d5='0'; d5<='9'; d5++) if(mods(7, d5, cur.charAt(0), cur.charAt(1)) && !cur.contains(d5 + "")){
//						cur = d5 + cur;
//						for(char d4='0'; d4<='9'; d4++) if(mods(5, d4, cur.charAt(0), cur.charAt(1)) && !cur.contains(d4 + "")){
//							cur = d4 + cur;
//							for(char d3='0'; d3<='9'; d3++) if(mods(3, d3, cur.charAt(0), cur.charAt(1)) && !cur.contains(d3 + "")){
//								cur = d3 + cur;
//								for(char d2='0'; d2<='9'; d2++) if(mods(2, d2, cur.charAt(0), cur.charAt(1)) && !cur.contains(d2 + "")){
//									cur = d2 + cur;
//									for(char d1='1'; d1<='9'; d1++) if(!cur.contains(d1 + "")){
//										System.out.println(d1 + cur);
//									}
//									cur = cur.substring(1);
//								}
//								cur = cur.substring(1);
//							}
//							cur = cur.substring(1);
//						}
//						cur = cur.substring(1);
//					}
//					cur = cur.substring(1);
//				}
//				cur = cur.substring(1);
//			}
//		}
		
		int[] dvz = {17, 13, 11, 7, 5, 3, 2};
		boolean[] taken = {false, false, false, false, false, false, false, false, false, false};
		choose(9, dvz, new int[10], taken);
		System.out.println(sum);
	}
	
	public static void choose(int idx, int[] dvz, int[] cur, boolean[] taken){
		if(idx < 0){
			long val = 0;
			for(int i=0; i<cur.length; i++) val = 10*val + cur[i];
			System.out.println(val);
			sum += val;
			return;
		}
		for(int i=0; i<=9; i++) if(!taken[i]){
			taken[i] = true;
			cur[idx] = i;
			if(idx>7 || idx == 0 || (cur[idx]*100 + cur[idx+1]*10 + cur[idx+2]) % dvz[7-idx] == 0){
				choose(idx-1, dvz, cur, taken);
			}
			taken[i] = false;
		}
	}
	
	public static boolean mods(int mod, char a, char b, char c){
		int amt = (a-'0')*100 + (b-'0')*20 + (c-'0');
		return amt % mod == 0;
	}

}
