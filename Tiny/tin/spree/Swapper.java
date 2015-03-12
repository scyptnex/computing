package tin.spree;

public class Swapper {
	
	private Blob x;
	
	public Swapper(Blob b){
		x = b;
	}
	
	public Object get(){
		return x.o;
	}
	
	public static void set(Object obj, Blob b){
		b.o = obj;
	}
	
}
