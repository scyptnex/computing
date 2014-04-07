import java.io.*;
import java.util.*;

public class ThaumPath {
	
	public final int maxLength = 8;
	public final ThaumGraph graph;
	public final ArrayList<String> allAspects;
	public final ArrayList<HashMap<String, ArrayList<TPath>>> allPaths;
	
	public static void main(String[] args) throws IOException{
		System.out.println("Calculating");
		ThaumGraph tg = new ThaumGraph(new File("aspects"));
		ThaumPath tp = new ThaumPath(tg);
		System.out.println("Ready");
		Scanner sca = new Scanner(System.in);

		while(sca.hasNextLine()){
			String[] ln = sca.nextLine().split(" ");
			try{
				tp.doAPath(ln[0], ln[1], Integer.parseInt(ln[2]));
			}
			catch(Exception exc){
				System.out.println(tp.allAspects);
			}
			System.out.println();
		}
	}
	
	public void doAPath(String from, String to, int length){
		System.out.println("===============================================");
		System.out.println(from + " -> " + graph.getNeighbours(from));
		System.out.println(to + " -> " + graph.getNeighbours(to));
		System.out.println("--------------------------");
		for(ArrayList<String> path : allPathsFrom(from, to, length)){
			System.out.println(path);
		}
		System.out.println("--------------------------");
	}
	
	public ThaumPath(ThaumGraph gra){
		this.graph = gra;
		this.allAspects = new ArrayList<String>(graph.getAllAspects());
		Collections.sort(this.allAspects);
		this.allPaths = new ArrayList<HashMap<String, ArrayList<TPath>>>();
		
		for(int i=0; i<= maxLength; i++){
			allPaths.add(new HashMap<String, ArrayList<TPath>>());
		}
		
		for(String s : allAspects){
			new TPath(s, null);
		}
		
		for(int i=1; i<maxLength; i++){
			HashMap<String, ArrayList<TPath>> prior = allPaths.get(i);
			for(String pName : prior.keySet()){
				for(TPath pat : prior.get(pName)){
					for(String neigh : this.graph.getNeighbours(pat.addition)){
						new TPath(neigh, pat);
					}
				}
			}
		}
	}
	
	public ArrayList<ArrayList<String>> allPathsFrom(String from, String to, int length){
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		ArrayList<TPath> paths = allPaths.get(length).get(pathName(from, to));
		if(paths == null) return ret;
		for(TPath pat : paths){
			ArrayList<String> pta = new ArrayList<String>();
			pat.get(pta);
			ret.add(pta);
		}
		return ret;
	}
	
	public void dump(){
		int count = 0;
		for(HashMap<String, ArrayList<TPath>> length : allPaths){
			System.out.println((count++) + " --------------------------------------");
			for(String s : length.keySet()){
				System.out.println("\t(" + length.get(s).size() + ")" + s);
			}
		}
	}
	
	public void addPath(TPath path){
		HashMap<String, ArrayList<TPath>> pSet = allPaths.get(path.length);
		if(!pSet.containsKey(path.name)){
			pSet.put(path.name, new ArrayList<TPath>());
		}
		pSet.get(path.name).add(path);
	}
	
	public static String pathName(String from, String to){
		return from + "->" + to;
	}
	
	public class TPath{
		public final int length;
		public final String name;
		public final String addition;
		public final String target;
		public final TPath following;
		public TPath(String addit, TPath after){
			addition = addit;
			length = after == null ? 1 : after.length+1;
			target = after == null ? addit : after.target;
			following = after;
			name = pathName(addition, target);
			addPath(this);
		}
		public void get(ArrayList<String> into){
			into.add(addition);
			if(following != null) following.get(into);
		}
	}
	
}
