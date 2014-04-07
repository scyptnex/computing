import java.io.*;
import java.util.*;

public class ThaumGraph {
	
	public static void main(String[] args) throws Exception{
		new ThaumGraph(new File("aspects")).dump();
	}
	
	public final HashMap<String, Aspect> aspects;
	
	public ThaumGraph(File aspectFile) throws IOException{
		this.aspects = new HashMap<String, Aspect>();
		int read = -1;
		int iter = 0;
		while(read != 0){
			read = 0;
			Scanner sca = new Scanner(aspectFile);
			while(sca.hasNextLine()){
				String[] asp = sca.nextLine().split(" ");
				if(aspects.containsKey(asp[0]) || asp[0].equals("tempus")){
					continue;
				}
				Aspect add = null;
				try{
					if(asp.length == 1){
						add = new Aspect(asp[0], iter);
					} else if(asp[1].equals(asp[2])){
						add = new Aspect(asp[0], iter, asp[1]);
					} else{
						add = new Aspect(asp[0], iter, asp[1], asp[2]);
					}
				}
				catch(Exception exc){
					add = null;
				}
				if(add != null){
					aspects.put(asp[0], add);
					read++;
				}
			}
			sca.close();
			iter++;
		}
	}
	
	public Set<String> getAllAspects(){
		return aspects.keySet();
	}
	
	public ArrayList<String> getNeighbours(String aspect){
		Aspect asp = aspects.get(aspect);
		ArrayList<String> ret = new ArrayList<String>();
		for(Aspect f : asp.from) ret.add(f.name);
		for(Aspect t : asp.to) ret.add(t.name);
		return ret;
	}
	
	public void dump(){
		for(String s : aspects.keySet()){
			System.out.println(s);
			for(Aspect f : aspects.get(s).from){
				System.out.println("\t<- " + f.name);
			}
			for(Aspect t : aspects.get(s).to){
				System.out.println("\t-> " + t.name);
			}
		}
	}
	
	public class Aspect{
		public final ArrayList<Aspect> from;
		public final ArrayList<Aspect> to;
		public final String name;
		public final int tier;
		
		public Aspect(String name, int tier, String...from) throws Exception{
			this.name = name;
			this.tier = tier;
			this.from = new ArrayList<Aspect>();
			this.to = new ArrayList<Aspect>();
			for(String s : from){
				Aspect other = aspects.get(s);
				this.from.add(other);
				other.to.add(this);
			}
		}
	}
	
}