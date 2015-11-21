import java.util.ArrayList;


public class RandomEndless {
	
	public static void main(String[] args){
		ArrayList<String> emps = new ArrayList<String>(11);
		emps.add("Empire");
		//emps.add("Sophon");
		emps.add("Hissho");
		emps.add("Amoeba");
		emps.add("Automaton");
		emps.add("Sheredyn");
		emps.add("Craver");
		emps.add("Pilgrim");
		emps.add("Sower");
		emps.add("Harmony");
		emps.add("Horatio");
		for(int i=0; i<7; i++){
			System.out.println(random(emps));
		}
	}
	
	public static <T> T random(ArrayList<T> lst){
		int c = (int)Math.floor(Math.random()*lst.size());
		T ret = lst.get(c);
		lst.remove(c);
		return ret;
	}
	
}
