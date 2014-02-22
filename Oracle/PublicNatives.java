import java.util.*;
import java.io.*;

public class PublicNatives {

	HashMap<String, Meth> methods;

	public static void main(String[] args) throws Exception{
		new PublicNatives();
	}

	public PublicNatives() throws Exception{
		methods = new HashMap<String, Meth>();
		Scanner sca;
		sca = new Scanner(new File("MethodModifier.facts"));
		while(sca.hasNextLine()){
			String line = sca.nextLine();
			String mod = line.substring(0, line.indexOf('\t'));
			String meth = line.substring(line.indexOf('\t')+1);
			if(!methods.containsKey(meth)){
				new Meth(meth);
			}
			if(mod.equals("native")){
				methods.get(meth).isNative = true;
			}
			if(mod.equals("public")){
				methods.get(meth).isPublic = true;
			}
		}
		sca.close();
		sca = new Scanner(new File("VirtualMethodInvocation.facts"));
		while(sca.hasNextLine()){
			String[] line = sca.nextLine().split("\t");
			if(!methods.containsKey(line[1]) || !methods.containsKey(line[2])){
				throw new RuntimeException("unknown methods " + line[1] + " and " + line[2]);
			}
			
		}
		sca.close();
		System.out.println(methods.size());
	}

	public class Meth{
		final String name;
		boolean isNative;
		boolean isPublic;
		Set<String> calls;
		Set<String> calledBy;
		public Meth(String m){
			name = m;
			isNative = false;
			isPublic = false;
			calls = new HashSet<String>();
			calledBy = new HashSet<String>();
			methods.put(name, this);
		}
	}

}
