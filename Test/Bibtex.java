import java.util.*;

/*

Samples

this is
some
	text here
		aselll
	back
		more
first line
		double indent
wheresit go
		another double
	with traling single
done

 */

public class Bibtex {
	
	int indent;
	String content;
	ArrayList<Bibtex> children;
	
	public static void main(String[] args){
		Bibtex b = new Bibtex(0);
		Scanner sca = new Scanner(System.in);
		while(sca.hasNextLine()){
			b.update(sca.nextLine());
		}
		b.finish();
	}
	
	public Bibtex(int ind){
		indent = ind;
		content = null;
		children = new ArrayList<Bibtex>();
	}
	
	public Bibtex(int ind, String line){
		indent = ind;
		content = line;
		children = null;
	}
	
	public void finish(){
		if(content != null){
			tab(indent);
			System.out.println("\\item " + content);
		}
		else{
			tab(indent);
			System.out.println("\\begin{itemize}");
			for(Bibtex bt : children){
				bt.finish();
			}
			tab(indent);
			System.out.println("\\end{itemize}");
		}
	}
	
	public void update(String line, int ind){
		if(ind == indent){
			children.add(new Bibtex(indent + 1, line));
		}
		else{//ind > indent
			if(children.size() == 0 || children.get(children.size() - 1).content != null){
				children.add(new Bibtex(indent + 1));
			}
			children.get(children.size() - 1).update(line, ind);
		}
	}
	
	public void update(String line){
		int count = 0;
		while(line.startsWith("\t")){
			line = line.substring(1);
			count++;
		}
		update(line, count);
	}
	
	public static void tab(int amt){
		for(int i=0; i<amt; i++){
			System.out.print("\t");
		}
	}
}
