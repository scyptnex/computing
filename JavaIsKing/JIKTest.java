import java.util.*;

public class JIKTest {
	
	public static void main(String[] args){
		MFI tmp = new MFI();
		System.out.println(tmp.map(12345));
		
		ArrayList<Integer> nums = new ArrayList<Integer>();
		for(int i=0; i<20; i++){
			nums.add(i);
		}
		List<String> res = map(nums, new NTS());
		for(String s : res){
			System.out.println(s);
		}
		
		System.out.println(reduce(nums, new Addition()));
		System.out.println(reduce(res, new Concatenation()));
		
		
	}
	
	public static <A, B> B reduce(List<A> list, ReduceFunction <A, B> rf){
		B ret = rf.init();
		for(A elem : list){
			ret = rf.update(elem, ret);
		}
		return ret;
	}
	
	public static <A, B> List<B> map(List<A> list, MapFunction<A, B> mf){
		List<B> ret = new ArrayList<B>(list.size());
		for(A elem: list){
			ret.add(mf.map(elem));
		}
		return ret;
	}
	
	public static class Concatenation implements ReduceFunction<String, String>{
		public String init(){
			return "";
		}
		public String update(String a, String b){
			return b + a;
		}
	}
	
	public static class Addition implements ReduceFunction<Integer, Integer>{
		public Integer init() {
			return 0;
		}
		public Integer update(Integer a, Integer b) {
			return a + b;
		}
	}
	
	public static class NTS implements MapFunction<Integer, String>{
		public String map(Integer val) {
			return "[" + val.toString() + "]";
		}
	}
	
	public static class MFI implements MapFunction<Integer, String>{
		public String map(Integer val) {
			return "|" + val.toString() + "|";
		}
	}
	
}
