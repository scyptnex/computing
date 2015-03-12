package tin;

import tin.spree.*;

public class Main {
	
	public static void main(String[] args){
		Object a = "hi";
		Object x = System.in;
		Blob bbb = new Blob();
		
		Swapper swa = new Swapper(bbb);
		Swapper.set(a, new Blob());
		Swapper.set(x, bbb);
		
		Object tst1 = swa.get();
		
		System.out.println(tst1);
	}
	
}
