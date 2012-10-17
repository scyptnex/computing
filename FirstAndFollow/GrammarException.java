public class GrammarException extends Exception {
	
	public static final int NO_START = 0;
	public static final int MULTI_START = 1;
	public static final int NOT_LL1 = 2;
	public static final int PARSE_NOT_LL1 = 3;
	
	int message;
	
	public GrammarException(int type){
		message = type;
	}
	
	public String toString(){
		return All.ERROR_MESSAGES[NOT_LL1];
	}
}
