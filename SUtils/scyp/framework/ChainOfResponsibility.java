package scyp.framework;

import java.util.HashSet;

public abstract class ChainOfResponsibility<I,O>{
	
	private ChainOfResponsibility<I,O> next = this;
	private HashSet<ChainOfResponsibility<I,O>> knownLinks = new HashSet<ChainOfResponsibility<I,O>>();
	
	public ChainOfResponsibility(){
		knownLinks.add(this);
	}
	
	public final void addLink(ChainOfResponsibility<I, O> lnk){
		if(lnk.next != lnk) throw new RuntimeException("Only singleton links can be added");
		if(knownLinks.contains(lnk)) throw new RuntimeException("Attempt to add duplicate link");
		lnk.next = this.next;
		this.next = lnk;
	}
	
	public final O handle(I i){
		return handle(i, this);
	}
	
	private O handle(I i, ChainOfResponsibility<I, O> start){
		O ret = null;
		try{
			ret = handleInternal(i);
		} catch(Exception exc){
			ret = null;
		}
		if(ret != null) return ret;
		else if(this.next == start) return null;
		else return handle(i, start);
	}
	
	protected abstract O handleInternal(I i) throws Exception;

}
