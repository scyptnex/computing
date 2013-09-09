import java.io.*;
import java.util.*;

public class Compiler {
	
	public static final String DECL_ANYTHING = "#";
	public static final String DECL_COM = "#COMMENT";
	public static final String DECL_CUD = "#CUDA";
	public static final String DECL_RUN = "#RUNSDF-";
	public static final String DECL_SDF = "#SDF-";
	
	//TODO check the names of SVN actors that they exist in the cuda code
	//public static final String DEFS_CUDA_DEVICE = ".*__device__.*void.*\\(.*";
	
	public static void main(String[] args) throws IOException{
		Scanner sca = null;
		if(args.length == 1){
			File fi = new File(args[0]);
			if(!fi.exists()) usage();
			else sca = new Scanner(fi);
		}
		else sca = new Scanner(System.in);
		new Compiler(sca, System.out);
		sca.close();
	}
	
	public static void usage(){
		System.out.println("You suck");
		System.exit(1);
	}
	
	public HashMap<String, SDFNetwork> definedNetworks;
	
	public Compiler(Scanner tokenStream, OutputStream os) throws IOException{
		definedNetworks = new HashMap<String, SDFNetwork>();
		
		PrintStream ps = new PrintStream(os);
		boolean printing = false;
		while(tokenStream.hasNextLine()){
			String trueLine = tokenStream.nextLine();
			String line = trueLine.trim();
			if(line.startsWith(DECL_SDF)){
				printing = false;
				String sdfName = line.substring(DECL_SDF.length());
				definedNetworks.put(sdfName, new SDFNetwork(sdfName, tokenStream));
			}
			else if(line.startsWith(DECL_COM)){
				//commens, do nothing
			}
			else if(line.startsWith(DECL_CUD)){
				printing = true;
			}
			else{
				if(printing){
					if(line.startsWith(DECL_RUN)){
						//TODO print the runstream
					}
					else{
						ps.println(trueLine);
					}
				}
			}
		}
	}
	
	private class SDFNetActor{
		public String name;
		public ArrayList<String> inputs;
		public ArrayList<String> outputs;
		public SDFNetActor(String nam){
			this.name = nam;
		}
		public void addIn(String in) throws IOException{
			if(!in.matches("[a-zA-Z0-9_-]*\\[[0-9]*\\]")) throw new IOException("Input edge " + in + " to " + name + " invalid");
			inputs.add(in);
		}
		public void addOut(String out) throws IOException{
			if(!out.matches("[a-zA-Z0-9_-]*\\[[0-9]*\\]")) throw new IOException("Output edge " + out + " to " + name + " invalid");
			outputs.add(out);
		}
	}
	
	private class SDFNetwork{
		public final String name;
		
		public SDFNetwork(String name, Scanner tokenStream) throws IOException{
			this.name = name;
			while(tokenStream.hasNextLine()){
				String line = tokenStream.nextLine().trim();
				if(line.startsWith(DECL_ANYTHING)) throw new IOException("Incomplete SDF Network " + this.name);
			}
		}
	}
}
