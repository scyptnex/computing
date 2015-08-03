package util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Numeral {
	public static void main(String[] args) {
		System.out.println(gcd(18, 30));
	}

	public static int digitsInNumber(long num){
		int cur = 0;
		while(num > 0){
			num /= 10;
			cur++;
		}
		return cur;
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
	
	public static class Fraction implements Comparable<Fraction>{
		public final BigInteger numerator;
		public final BigInteger denominator;
        public Fraction(long n){
            this(BigInteger.valueOf(n), BigInteger.ONE);
        }
		public Fraction(long n, long d){
            this(BigInteger.valueOf(n), BigInteger.valueOf(d));
		}
        public Fraction(BigInteger n, BigInteger d){
            if(d.equals(BigInteger.ZERO)) throw new RuntimeException("Divide by zero");
            boolean neg = false;
            if(d.compareTo(BigInteger.ZERO) < 0){
                n = n.negate();
                d = d.negate();
            }
            if(n.compareTo(BigInteger.ZERO) < 0){
                neg = true;
                n = n.negate();
            }
            BigInteger gcd = n.gcd(d);
            numerator = n.divide(gcd);
            if(neg) n.negate();
            denominator = d.divide(gcd);
        }
		public String toString(){
			return numerator.toString() + (denominator.equals(BigInteger.ONE) ? "" : "/" + denominator.toString());
		}
		public boolean equals(Object obj){
			if(obj instanceof Fraction){
				Fraction other = (Fraction) obj;
				return other.numerator.equals(this.numerator) && other.denominator.equals(this.denominator);
			}
			return false;
		}
		
		public Fraction multiply(Fraction other){
			return new Fraction(this.numerator.multiply(other.numerator), this.denominator.multiply(other.denominator));
		}

		public Fraction add(Fraction other){
            return new Fraction(this.numerator.multiply(other.denominator).add(other.numerator.multiply(this.denominator)), this.denominator.multiply(other.denominator));
        }

		public Fraction subtract(Fraction other){
            return new Fraction(this.numerator.multiply(other.denominator).subtract(other.numerator.multiply(this.denominator)), this.denominator.multiply(other.denominator));
        }

		@Override
		public int compareTo(Fraction o) {
			return this.numerator.multiply(o.denominator).compareTo(o.numerator.multiply(this.denominator));
		}
	}

}
