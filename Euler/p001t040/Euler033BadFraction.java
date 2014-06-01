package p001t040;

import util.Numeral.Fraction;

public class Euler033BadFraction {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Fraction tot = new Fraction(1, 1);
		for (int d = 11; d < 100; d++) {
			// for (int d = 98; d < 100; d++) {
			if (d % 10 == 0)
				continue;
			for (int n = 11; n < d; n++) {
				// for (int n = 49; n < 50; n++) {
				int da = d / 10;
				int db = d % 10;
				int na = n / 10;
				int nb = n % 10;
				Fraction frac = new Fraction(n, d);
				// System.out.println(frac + ", " + da + ", " + db + ", " + na +
				// ", " + nb);
				if (da == na) {
					if (frac.equals(new Fraction(nb, db))) {
						System.out.println(n + "/" + d + ": " + frac);
						tot = tot.multiply(frac);
					}
				} else if (da == nb) {
					if (frac.equals(new Fraction(na, db))) {
						System.out.println(n + "/" + d + ": " + frac);
						tot = tot.multiply(frac);
					}
				} else if (db == na) {
					if (frac.equals(new Fraction(nb, da))) {
						System.out.println(n + "/" + d + ": " + frac);
						tot = tot.multiply(frac);
					}
				} else if (db == nb) {
					if (frac.equals(new Fraction(na, da))) {
						System.out.println(n + "/" + d + ": " + frac);
						tot = tot.multiply(frac);
					}
				}
			}
		}
		System.out.println(tot);
	}

}
