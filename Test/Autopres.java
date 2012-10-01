import java.util.*;

public class Autopres {
	
	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		boolean printing = true;
		Bibtex bt = null;
		while(in.hasNextLine()){
			String line = in.nextLine();
			if(printing){
				System.out.println(line);
				if(line.contains("\\begin{column}")){
					bt = new Bibtex(0);
					printing = false;
				}
			}
			else{
				if(line.contains("\\end{column}")){
					bt.finish();
					System.out.println(line);
					printing = true;
				}
				else{
					bt.update(line);
				}
			}
		}
	}
	
}