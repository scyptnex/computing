package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

public class Primer {
	
	public static void main(String[] args){
		//for testing
	}

	public static class Sieve implements Iterable<Integer>{
		private final BitSet sieve;
		public final int max_size;
		public Sieve(int size){
			max_size = size;
			sieve = new BitSet((size+2)/2);
			for (int i = 3; i*i <= size; i += 2) {
				if (sieve.get((i-3)/2))
					continue;

				// We increment by 2*i to skip even multiples of i
				for (int multiple_i = i*i; multiple_i <= size; multiple_i += 2*i)
					sieve.set((multiple_i - 3) / 2);
			}
		}
        public String toString(){
            StringBuilder sb = new StringBuilder("<2");
            for(int i=3; i<max_size; i+=2){
                if(isPrime(i)) sb.append(",").append(i);
            }
            return sb.append(">").toString();
        }
		public boolean isPrime(int i){
            if(i == 2) return true;
			if(i > max_size) throw new RuntimeException("Too large");
			return !sieve.get((i-3)/2);
		}

		@Override
		public Iterator<Integer> iterator() {
			return new Iterator<Integer>() {
				int cur = 2;
				int next = 3;
				@Override
				public boolean hasNext() {
					return next < max_size;
				}

				@Override
				public Integer next() {
					int i=next+2;
					while(i < max_size){
						if(isPrime(i)) break;
						i+=2;
					}
					int ret = cur;
					cur = next;
					next = i;
					return ret;
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException("Cannot remove a prime from the sieve");
				}
			};
		}
	}

	public static ArrayList<Long> primeSieve(int size){
		Sieve sieve = new Sieve(size);
		System.out.println("Sieve completed, writing ");
		ArrayList<Long> ret = new ArrayList<>();
        for(int i : sieve){
            ret.add((long)i);
        }
		return ret;
	}

}
