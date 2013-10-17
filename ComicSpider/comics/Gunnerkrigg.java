package comics;

import java.util.ArrayList;

import spider.*;

public class Gunnerkrigg extends Comic{
	
	public static void main(String[] args){
		System.out.println(new Gunnerkrigg().getComicName());
	}
	
	@Override
	public String getFirstPage() {
		return "http://www.google.com";
	}

	@Override
	public Page notePage(ArrayList<String> curPage, int curIndex) {
		return null;
	}

	@Override
	public String noteNextPage(ArrayList<String> curPage, int curIndex) {
		return null;
	}

}
