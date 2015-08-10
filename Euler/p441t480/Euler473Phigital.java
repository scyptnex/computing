package p441t480;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Euler473Phigital {

public static final int bitrange = 100;
	
	public static BigInteger[][] pascal = new BigInteger[bitrange][bitrange];// k, n
	public static Phixp[] expos = new Phixp[bitrange];

	public static void main(String[] args) {
		pascal[0][0] = BigInteger.ONE;
		expos[0] = Phixp.fromExponent(0);
		for (int k = 1; k < pascal.length; k++) {
			for (int n = 0; n <= k; n++) {
				if (n == 0 || n == k)
					pascal[k][n] = BigInteger.ONE;
				else {
					pascal[k][n] = pascal[k - 1][n].add(pascal[k - 1][n - 1]);
				}
			}
			expos[k] = Phixp.fromExponent(k);
			System.out.println(expos[k] + ", " + expos[k].estimate());
		}
		// System.out.println(new Phrac(new Phixp(BigInteger.valueOf(4),
		// BigInteger.valueOf(6)), new Phixp(BigInteger.valueOf(3),
		// BigInteger.valueOf(3))).solu());
		// System.out.println(Phrac.palind(0));
		// System.out.println(Phrac.palind(1).solu());
		// System.out.println(Phrac.palind(2).solu());
		// System.out.println(Phrac.palind(3));
		// System.exit(0);
		// System.out.println(Phrac.palind(2).add(Phrac.palind(5)).solu());
		// System.out.println(Phrac.palind(3).add(Phrac.palind(0)));
		/*
		 * Stage 1
		 * Collect all the 'base' palindromes
		 * Palindromes whose integral value is not the sum of two previously known palindromes
		 */
		int cur = 1;
		BigInteger max = BigInteger.TEN.pow(10);
		ArrayList<Phrac> available = new ArrayList<Phrac>();
		available.add(null);//'zero' is always unaavailable because of the non-consecutive rule
		//HashSet<Integer> taken = new HashSet<Integer>();
		//HashMap<Integer, Phrac> open = new HashMap<Integer, Phrac>();
		ArrayList<BigInteger> intys = new ArrayList<BigInteger>();
		ArrayList<ArrayList<Integer>> lineages = new ArrayList<ArrayList<Integer>>();
		while(true){
			boolean compl = false;
			Phrac pal = Phrac.palind(cur);
			if(pal.estimate().compareTo(max) > 0){
				break;
			}
			if(pal.solu() == null){
				//first try permutations of all the subparts
				ArrayList<Integer> lowers = permuteLowers(pal, available, 0);
				//then act on it
				if(lowers != null){
					available.add(null);
					for(int i : lowers){
						pal = pal.add(available.get(i));
						available.set(i, null);
					}
					lowers.add(cur);
					lineages.add(lowers);
					intys.add(pal.solu());
					compl = true;
				}
				else{
					available.add(pal);
				}
				
			} else {
				available.add(null);
				intys.add(pal.solu());
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(cur);
				lineages.add(temp);
				compl = true;
			}
			if(compl){
				int newIdx = intys.size()-1;
				for(int i=0; i<newIdx; i++){
					ArrayList<Integer> testLineage = new ArrayList<Integer>();
					testLineage.addAll(lineages.get(newIdx));
					testLineage.addAll(lineages.get(i));
					Collections.sort(testLineage);
					boolean isValid = true;
					for(int l=1; l<testLineage.size(); l++){
						if(testLineage.get(l) == testLineage.get(l-1)+1){
							isValid = false;
							break;
						} else if(testLineage.get(l) == testLineage.get(l-1)){
							isValid = false;
							break;
						}
					}
					if(isValid){
						intys.add(intys.get(newIdx).add(intys.get(i)));
						lineages.add(testLineage);
					}
				}
			}
			cur++;
		}
		System.out.println(intys);
		BigInteger tot = BigInteger.ONE;
		for(int i =0; i<intys.size(); i++){
			tot = tot.add(intys.get(i));
			System.out.println(intys.get(i) + " - " + lineages.get(i));
		}
		System.out.println(tot);
			
		/*
		 * Stage 2, add all the integrals
		 */
	}
	
	public static ArrayList<Integer> permuteLowers(Phrac greatest, ArrayList<Phrac> lowers, int choice){
		if(greatest.solu() != null){
			return new ArrayList<Integer>();
		}
		else if(choice >= lowers.size()){
			return null;
		} else{
			ArrayList<Integer> without = permuteLowers(greatest, lowers, choice+1);
			if(without != null || lowers.get(choice) == null){//when we must not include this lower, better hope a solution was found without it
				return without;
			}
			ArrayList<Integer> with = permuteLowers(greatest.add(lowers.get(choice)), lowers, choice+2);//no doubles
			if(with != null){
				with.add(choice);
			}
			return with;
		}
	}
	
	public static ArrayList<Integer> permuteLowers(Phrac greatest, ArrayList<Integer> intys, ArrayList<Phrac> phracs, int idx){
		if(greatest.solu() != null){
			System.out.print(greatest.solu() + " - ");
			return new ArrayList<Integer>();
		}
		else if(idx >= intys.size()){
			return null;
		}
		ArrayList<Integer> without = permuteLowers(greatest, intys, phracs, idx+1);
		if(without != null){
			return without;
		}
		ArrayList<Integer> with = permuteLowers(greatest.add(phracs.get(idx)), intys, phracs, idx+1);
		if(with != null){
			System.out.print(intys.get(idx) + " ");
			with.add(intys.get(idx));
		}
		return with;
	}

	public static class Phrac {
		public final Phixp numer;
		public final Phixp denom;

		public Phrac(Phixp n, Phixp d) {
			this.numer = n;
			this.denom = d;
		}
		
		public static Phrac palind(int n){
			Phixp num = Phixp.fromExponent(1+2*n).add(BigInteger.valueOf(2).pow(1+2*n), BigInteger.ZERO);
			Phixp den = Phixp.fromExponent(1+n).mult(BigInteger.valueOf(2).pow(n));
			return new Phrac(num, den);
		}
		
		public Phrac add(Phrac a){
			return new Phrac(this.numer.mult(a.denom).add(a.numer.mult(this.denom)), this.denom.mult(a.denom));
		}
		
		public BigInteger estimate(){
			return numer.estimate().divide(denom.estimate());
		}

		public BigInteger solu() {
			if (numer.con.mod(denom.con).equals(BigInteger.ZERO)) {
				if (numer.coe.mod(denom.coe).equals(BigInteger.ZERO)) {
					BigInteger ret = numer.con.divide(denom.con);
					if(ret.equals(numer.coe.divide(denom.coe))){
						return ret;
					} else{
						return null;
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		}
		
		public String toString(){
			return numer + " / " + denom;
		}
	}

	public static final BigInteger FIVE = BigInteger.valueOf(5);

	public static class Phixp {
		public final BigInteger con;
		public final BigInteger coe;

		public Phixp(BigInteger con, BigInteger coe) {
			this.con = con;
			this.coe = coe;
		}

		public static Phixp fromExponent(int exp) {
			BigInteger con = BigInteger.ONE;
			BigInteger coe = BigInteger.ZERO;
			if (exp != 0) {
				con = BigInteger.ZERO;
				coe = BigInteger.ZERO;
				for (int x = 0; x <= exp; x++) {
					if (x % 2 == 0) {// evenly powered
						con = con.add(FIVE.pow(x / 2).multiply(pascal[exp][x]));
					} else {
						coe = coe.add(FIVE.pow(x / 2).multiply(pascal[exp][x]));
					}
				}
			}
			return new Phixp(con, coe);
		}

		public Phixp add(BigInteger cons, BigInteger coef) {
			return new Phixp(this.con.add(cons), this.coe.add(coef));
		}

		public Phixp add(Phixp other) {
			return add(other.con, other.coe);
		}

		public Phixp mult(BigInteger m) {
			return new Phixp(this.con.multiply(m), this.coe.multiply(m));
		}

		public Phixp mult(Phixp other) {
			return new Phixp(this.con.multiply(other.con).add(
					FIVE.multiply(this.coe).multiply(other.coe)), this.con
					.multiply(other.coe).add(this.coe.multiply(other.con)));
		}
		
		public BigInteger estimate(){
			return con.add(coe.multiply(BigInteger.valueOf(223)).divide(BigInteger.valueOf(100)));
		}

		public String toString() {
			return "(" + con + " + " + coe + "v5)";
		}
	}

}
