package p000t040;

public class Euler015GridWalk {

	public static long[][] memo = new long[21][21];

	public static void main(String[] args) {
		memo[0][0] = 1;
		System.out.println("20 " + paths(20, 20));
	}

	//could exclude triangles butt-fuck it
	public static long paths(int wid, int hei){
		if(memo[wid][hei] == 0){
			if(wid > 0) memo[wid][hei] += paths(wid-1, hei);
			if(hei > 0) memo[wid][hei] += paths(wid, hei-1);
			System.out.println(wid + ", " + hei + " = " + memo[wid][hei]);
		}
		return memo[wid][hei];
	}

}
