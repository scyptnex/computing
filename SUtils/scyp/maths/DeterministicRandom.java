import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.math.*;


public class DeterministicRandom implements Iterator<Integer>{
	
	public final int range;
	public final int start;
	private final BigInteger m;
	private final BigInteger a;
	private BigInteger cur;
	
	public static void main(String[] args){
		for(int i=3; i<30; i++){
			System.out.println(i + "\t" + getAllPrimitiveRoots(bi(i)));
			//System.out.println(i + "\t" + getEqualOrLargerPrime(i));
		}
		int chk = 17;
		ArrayList<BigInteger> prs = getAllPrimitiveRoots(bi(chk));
		System.out.println(prs);
		generate(chk, 5);
		new DeterministicRandom(17);
	}
	
	public static void generate(int n, int p){
		for(int i=0; i<n*2; i++){
			BigInteger modpow = bi(p).modPow(new BigInteger((i) + ""), bi(n));
			System.out.print(modpow.toString() + " ");
		}
		System.out.println();
	}

	public DeterministicRandom(int range, int start, int protoSeed){
		this.range = range;
		this.start = start;
		this.m = bi(getEqualOrLargerPrime(range+1));//+1 because the cycle excludes 0
		this.a = getLargestPrimitiveRoot(m);
		this.cur = BigInteger.ZERO;
		while(this.next() != start);
		System.out.println(m + " ^ " + a + " - " + cur);
	}
	
	public DeterministicRandom(int range, int start){
		this(range, start, range);
	}
	
	public DeterministicRandom(int range){
		this(range, 0);
	}
	
	public static BigInteger getLargestPrimitiveRoot(BigInteger n){
		//TODO more efficient
		ArrayList<BigInteger> chk = getAllPrimitiveRoots(n);
		if(chk.size() > 0) return chk.get(chk.size()-1);
		return null;
	}
	
	//wacky maths time:
	//If a number a coprime to n has as many unique values a^k modulo n for
	//all k below n as there are coprimes of n (those unique values turn
	//out to be the coprimes themselves) then a is a primitive root of n
	public static ArrayList<BigInteger> getAllPrimitiveRoots(BigInteger n){
		ArrayList<BigInteger> cps = getCoprimes(n);
		ArrayList<BigInteger> primRoots = new ArrayList<BigInteger>();
		BigInteger modul = new BigInteger(n + "");
		for(BigInteger i : cps){
			Set<BigInteger> modres = new HashSet<BigInteger>();
			for(int pow=0; pow < cps.size(); pow++){
				BigInteger res = i.modPow(new BigInteger(pow + ""), modul);
				modres.add(res);
			}
			if(modres.size() == cps.size()) primRoots.add(i);
		}
		return primRoots;
	}
	
	// Coprime means gcd of a and b is 1
	public static ArrayList<BigInteger> getCoprimes(BigInteger n){
		ArrayList<BigInteger> coprimes = new ArrayList<BigInteger>();
		for(BigInteger i = BigInteger.ZERO; i.compareTo(n) < 0; i = i.add(BigInteger.ONE)){
			if(n.gcd(i).equals(BigInteger.ONE)) coprimes.add(i);
		}
		return coprimes;
	}
	
	public static int getEqualOrLargerPrime(int val){
		//TODO more efficient
		while(true){
			int sta = 2;
			boolean isPrime = true;
			while(sta*sta < val){
				if(val%sta == 0){
					isPrime = false;
					break;
				}
				sta++;
			}
			if(isPrime) return val;
			val++;
		}
	}
	
	public static BigInteger bi(int i){
		return new BigInteger(i + "");
	}
	
	public static int si(BigInteger i){
		return Integer.parseInt(i.toString());
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Integer next() {
		BigInteger ret = bi(range);
		while(ret.compareTo(bi(range)) >= 0){
			
		}
		return si(ret);
	}

	@Override
	public void remove() {
		//do nothing
	}

	
	
}
