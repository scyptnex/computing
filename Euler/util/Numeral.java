package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Numeral {
	public static void main(String[] args) {
		System.out.println(gcd(18, 30));
	}
	
	public static Set<Long> divisors(long l){
		Set<Long> ret = new HashSet<Long>();
		for(long i=1; i*i<=l; i++){
			if(l%i==0){
				ret.add(i);
				ret.add(l/i);
			}
		}
		return ret;
	}
	
	public static long gcd(long a, long b){
		if(b == 0) return a;
		return gcd(b, a%b);
	}
	
	public static class Fraction{
		public final long numerator;
		public final long denominator;
		public Fraction(long n, long d){
			if(d < 0) throw new RuntimeException("Divide by zero");
			boolean neg = false;
			if(d < 0){
				n *= -1;
				d *= -1;
			}
			if(n < 0){
				neg = true;
				n *= -1;
			}
			long gcd = gcd(n, d);
			numerator = (n/gcd)*(neg ? -1 : 1);
			denominator = d/gcd;
		}
		public String toString(){
			return numerator + (denominator == 1 ? "" : "/" + denominator);
		}
		public boolean equals(Object obj){
			if(obj instanceof Fraction){
				Fraction other = (Fraction) obj;
				return other.numerator == this.numerator && other.denominator == this.denominator;
			}
			return false;
		}
		
		public Fraction multiply(Fraction other){
			return new Fraction(this.numerator*other.numerator, this.denominator*other.denominator);
		}
	}

}
