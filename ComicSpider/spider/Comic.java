package spider;
import java.util.ArrayList;

public abstract class Comic extends ArrayList<Page>{
	
	public Comic(){
		super();
	}
	
	public String getComicName(){
		String clName = this.getClass().getName(); 
		return clName.substring(clName.lastIndexOf(".") + 1);
	}
	
	public int getLatestPage(){
		int count = -1;
		for(Page p : this){
			count++;
			if(p.successorUrl == null){
				//this is a sanity check tbh, the whole method could be replaced by size()
				assert count == size()-1 : "The latest page was not the last page";
			}
		}
		return count;
	}
	
	public void getNextN(int n){
		int latestPage = getLatestPage();
		while(n > 0){
			
			n--;
		}
	}
	
	public abstract String getFirstPage();
	
	public abstract Page notePage(ArrayList<String> curPage, int curIndex);
	
	public abstract String noteNextPage(ArrayList<String> curPage, int curIndex);
	
}