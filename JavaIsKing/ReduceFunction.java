public interface ReduceFunction <A, B>{
	
	public B init();
	
	public B update(A a, B b);
	
}