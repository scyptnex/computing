import java.util.*;

public class SharedState {
	
	//statics
	private final ArrayList<Long> sNum;
	private final ArrayList<String> sText;
	private final ArrayList<byte[]> sData;
	
	//dynamics
	
	
	public SharedState(int mss, int mds){
		sNum = new ArrayList<Long>();
		sText = new ArrayList<String>();
		sData = new ArrayList<byte[]>();
	}
	
	public int addStaticNum(long n){
		sNum.add(n);
		return sNum.size()-1;
	}
	
	public int addStaticNum(int i){
		return addStaticNum(i);
	}
	
	public int addStaticText(String s){
		sText.add(s);
		return sNum.size()-1;
	}
	
	public int addStaticData(byte[] d){
		sData.add(d);
		return sData.size()-1;
	}
	
}