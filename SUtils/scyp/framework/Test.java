package scyp.framework;


public class Test {
	
	public static void main(String[] args){
		String s = "{[][{{}[]}{}]}";
		
	}
	
	public static class INP{
		public String s;
		public int cur;
	}
	
	public static class Curly extends ChainOfResponsibility<INP, Boolean>{
		@Override
		protected Boolean handleInternal(INP i) throws Exception {
			if(i.cur >= i.s.length()) return true;
			else if(i.s.charAt(i.cur) == '{'){
				i.cur++;
				Boolean hand = this.handle(i);
				if(hand == null || hand == false) return false;
				return i.cur < i.s.length() && i.s.charAt(i.cur) == '}';
			}
			else return null;
		}
	}
	
}
