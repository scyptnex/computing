import java.io.*;
import java.util.*;

public class Grammar {
	
	public static final String EPSILON = "epsilon";
	public static final String END_SYM = "$";
	public static final int REQUIRED_ZERO_ROUNDS = 2;
	
	public final String startNonTerm;
	//public final String[] nonTerms;
	private Map<String, ArrayList<ArrayList<String>>> productions;
	private Map<String, ArrayList<Integer>> inputMap;
	private Map<String, Set<String>> firstSets;
	private Map<String, Set<String>> followSets;
	private Map<String, Integer> m;
	private Map<Integer, ArrayList<String>> productionRows;
	private boolean wsd;//this is a flag used to determine if grammar parsing will be whitespace dependant or character dependant (i.e. if "ab ba" is equivalent to "a b b a")
	
	//to the reader of this code, the 'table' flag simply allows computation of first+follow sets for non LL(1) grammars (which would normally throw an error)
	public Grammar(String fname, boolean table) throws IOException, GrammarException{
		FileReader fr = new FileReader(fname);
		BufferedReader br = new BufferedReader(fr);
		
		productions = new HashMap<String, ArrayList<ArrayList<String>>>();
		inputMap = new HashMap<String, ArrayList<Integer>>();
		productionRows = new HashMap<Integer, ArrayList<String>>();
		
		String line;
		int c = 0;
		while((line = br.readLine()) != null){
			Scanner lScanner = new Scanner(line);
			String nt = lScanner.next();
			String sepChar = lScanner.next();
			ArrayList<String> tmp = new ArrayList<String>();
			while(lScanner.hasNext()){
				tmp.add(lScanner.next());
			}
			if(!productions.containsKey(nt)){
				productions.put(nt, new ArrayList<ArrayList<String>>());
				inputMap.put(nt, new ArrayList<Integer>());
			}
			productions.get(nt).add(tmp);
			inputMap.get(nt).add(c);
			productionRows.put(c, tmp);
			c++;
		}
		
		br.close();
		fr.close();
		
		String sterm = null;
		//int i = 0;
		//nonTerms = new String[productions.keySet().size()];
		for(String s : productions.keySet()){
			boolean appears = false;
			for(String s2 : productions.keySet()){
				if(!s.equals(s2) && appearsInProduction(s2, s)){
					appears = true;
					break;
				}
			}
			if(!appears){
				if(sterm == null){
					sterm = s;
				}
				else{
					throw new GrammarException(GrammarException.MULTI_START);
				}
			}
			//nonTerms[i] = s;
			//i++;
		}
		if(sterm == null) throw new GrammarException(GrammarException.NO_START);
		startNonTerm = sterm;
		
		calcFirsts();
		calcFollows();
		try{
			calcM();
		}
		catch(GrammarException exc){
			if(table) throw exc;
			else m = null;
		}
	}
	
	public Map<String, Set<String>> getFirsts(){
		return firstSets;
	}
	
	public Map<String, Set<String>> getFollows(){
		return followSets;
	}
	
	public ArrayList<String> getTable() throws GrammarException{
		if(m == null) throw new GrammarException(GrammarException.PARSE_NOT_LL1);
		ArrayList<String> ret = new ArrayList<String>();
		for(String str : m.keySet()){
			ret.add(str + All.Q2_FORMAT + m.get(str));
		}
		return ret;
	}
	
	public ArrayList<Boolean> parseAll(String fname) throws IOException, GrammarException{
		ArrayList<Boolean> ret = new ArrayList<Boolean>();
		FileReader fr = new FileReader(fname);
		BufferedReader br = new BufferedReader(fr);
		
		String line = "";
		while((line = br.readLine()) != null){
			Scanner lineScan = new Scanner(line);
			ArrayList<String> sen = new ArrayList<String>();
			while(lineScan.hasNext()){
				String wrd = lineScan.next();
				if(wsd) sen.add(wrd);
				else for(char c : wrd.toCharArray()) sen.add(c + "");
			}
			sen.add(END_SYM);
			ret.add(parse(sen));
			//System.out.println(line + "\n\t" + ret.get(ret.size()-1));
		}
		
		br.close();
		fr.close();
		
		return ret;
	}
	
	public boolean parse(ArrayList<String> input) throws GrammarException{
		if(m == null) throw new GrammarException(GrammarException.PARSE_NOT_LL1);
		Stack<String> s = new Stack<String>();
		s.push(END_SYM);
		s.push(startNonTerm);
		int inRec = 0;
		while(!s.isEmpty()){
			if(isNonTerminal(s.peek())){
				if(m.containsKey(getR(s.peek(), input.get(inRec)))){
					String nt = s.pop();
					ArrayList<String> prod = productionRows.get(m.get(getR(nt, input.get(inRec))));
					pushSequence(s, prod);
				}
				else{
					//System.out.println("no corresponding rule");
					return false;
				}
			}
			else{
				if(s.peek().equals(input.get(inRec))){
					if(!s.peek().equals(END_SYM)){
						inRec++;
					}
					s.pop();
				}
				else if(s.peek().equals(EPSILON)){
					s.pop();
				}
				else{
					//System.out.println("unexpected symbol " + s.peek() + ", " + input.get(inRec));
					return false;
				}
			}
			//System.out.println(s.size());
		}
		if(!input.get(inRec).equals(END_SYM)){
			//System.out.println("input incorrectly ended");
			return false;
		}
		return true;
	}
	
	private static void pushSequence(Stack<String> sta, ArrayList<String> seq){
		for(int i=seq.size(); i>0; i--){
			sta.push(seq.get(i - 1));
		}
	}
	
	/**
	 * Very simply:
	 * loop over each non-terminal, adding known productions as well as known subproductions's productions
	 * Eg:
	 * S := AAAA
	 * A := a
	 * A := epsilon
	 * A := B
	 * B := b
	 * B := epsilon
	 * B := A
	 * only add A's firsts to S, eventually B's will be added to A, thereafter S can add them (indirectly)
	 */
	private void calcFirsts(){
		firstSets = new HashMap<String, Set<String>>();
		for(String s : productions.keySet()){
			firstSets.put(s, new HashSet<String>());
		}
		int zeroRounds = 0;
		while(zeroRounds < REQUIRED_ZERO_ROUNDS){
			int addedThisRound = 0;
			for(String curNonTerm : productions.keySet()){
				for(ArrayList<String> rule : productions.get(curNonTerm)){
					Set<String> ruleFirsts = streamToSet(rule, 0, true, firstSets);
					int sz = firstSets.get(curNonTerm).size();
					firstSets.get(curNonTerm).addAll(ruleFirsts);
					addedThisRound += (firstSets.get(curNonTerm).size() - sz);
				}
			}
			if(addedThisRound == 0){
				zeroRounds++;
			}
			else{
				zeroRounds = 0;
			}
		}
	}
	
	private void calcFollows(){
		followSets = new HashMap<String, Set<String>>();
		for(String s : productions.keySet()){
			followSets.put(s, new HashSet<String>());
		}
		followSets.get(startNonTerm).add(END_SYM);
		int zeroRounds = 0;
		while(zeroRounds < REQUIRED_ZERO_ROUNDS){
			int addedThisRound = 0;
			for(String curNonTerm : productions.keySet()){
				for(ArrayList<String> rule : productions.get(curNonTerm)){
					for(int cur = 0; cur < rule.size(); cur++) if(isNonTerminal(rule.get(cur))){
						Set<String> afterFirsts = streamToSet(rule, cur+1, true, firstSets);
						addedThisRound += collectNotEpsilon(followSets.get(rule.get(cur)), afterFirsts);
						if(afterFirsts.contains(EPSILON)){
							addedThisRound += collectNotEpsilon(followSets.get(rule.get(cur)), followSets.get(curNonTerm));
						}
					}
				}
			}
			if(addedThisRound == 0){
				zeroRounds++;
			}
			else{
				zeroRounds = 0;
			}
		}
	}
	
	private void calcM() throws GrammarException{
		m = new HashMap<String, Integer>();
		wsd = false;
		for(String nonTerm: productions.keySet()){
			for(int c=0; c<productions.get(nonTerm).size(); c++){
				ArrayList<String> rule = productions.get(nonTerm).get(c);
				Integer ruleRow = inputMap.get(nonTerm).get(c);
				Set<String> productionFirsts = streamToSet(rule, 0, true, firstSets);
				for(String term : productionFirsts) if(!term.equals(EPSILON)) setM(nonTerm, term, ruleRow);
				if(productionFirsts.contains(EPSILON)){
					for(String term : followSets.get(nonTerm)) setM(nonTerm, term, ruleRow);
				}
			}
		}
	}
	
	private void setM(String nonTerm, String term, Integer gRow) throws GrammarException{
		String key = getR(nonTerm, term);
		if(m.containsKey(key)){
			throw new GrammarException(GrammarException.NOT_LL1);
		}
		else{
			m.put(key, gRow);
		}
		if((term.length() > 1)&&(!term.equals(END_SYM))&&(!term.equals(EPSILON))) wsd = true;
	}
	
	public static int collectEpsilon(Set<String> into){
		int c = into.size();
		into.add(EPSILON);
		return into.size() - c;
	}
	
	public static int collectNotEpsilon(Set<String> into, Set<String> from){
		int c = into.size();
		for(String o : from){
			//if the term is not epsilon or already in
			if(!o.equals(EPSILON)) into.add(o);
		}
		return into.size() - c;
	}
	
	public Set<String> streamToSet(ArrayList<String> stream, int streamStart, boolean collectEpsilon, Map<String, Set<String>> pool){
		Set<String> ret = new HashSet<String>();
		for(int i=streamStart; i<stream.size(); i++){
			Set<String> sfs = new HashSet<String>();
			if(isNonTerminal(stream.get(i))) sfs = pool.get(stream.get(i));
			else sfs.add(stream.get(i));
			collectNotEpsilon(ret, sfs);
			if(!sfs.contains(EPSILON)){
				return ret;
			}
		}
		if(collectEpsilon) collectEpsilon(ret);
		return ret;
	}
	
	public static String getR(String nonTerminal, String terminal){
		return "R[" + nonTerminal + ", " + terminal + "]";
	}
	
	public boolean isNonTerminal(String s){
		return productions.containsKey(s);
	}
	
	public boolean appearsInProduction(String term, String word){
		if(!isNonTerminal(term)) return false;
		for(ArrayList<String> rule : productions.get(term)){
			for(String s : rule){
				if(s.equals(word)) return true;
			}
		}
		return false;
	}
	
	public String fullPrint(){
		String ret = startNonTerm + ", " + wsd;
		for(String s : productions.keySet()){
			ret = ret + "\n" + s + " ::=";
			for(ArrayList<String> rule : productions.get(s)){
				for(String str : rule){
					ret = ret + " " + str;
				}
				ret = ret + "  | ";
			}
			ret = ret + "\n\tFirst  -> ";
			for(String sym : firstSets.get(s)) ret = ret + " " + sym;
			ret = ret + "\n\tFollow -> ";
			for(String sym : followSets.get(s)) ret = ret + " " + sym;
		}
		if(m != null) for(String r : m.keySet()) ret += "\n" + r + " = " + m.get(r) + "(" + productionRows.get(m.get(r)) + ")";
		return ret;
	}
}