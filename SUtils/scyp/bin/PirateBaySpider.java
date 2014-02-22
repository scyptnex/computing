package scyp.bin;

import java.io.*;
import java.net.URL;

import scyp.net.WGet;
import scyp.net.WGet.WPage;
import scyp.net.WUtil;

public class PirateBaySpider {

	private String searchString = null;

	public static void main(String[] args) throws IOException{
		PirateBaySpider pbs = new PirateBaySpider("black sails s01e04");
		System.out.println(pbs.getSearchPage(1));
		WPage pbPage = WGet.livePage(pbs.getSearchPage(0));
		System.out.println(pbPage.isSecure() + ", " + pbPage.isSecure);
		for(String s : pbPage){
			System.out.println(s);
		}
		
	}

	public PirateBaySpider(String search){
		searchString = search;
	}

	public URL getSearchPage(int pageIndex){
		return WUtil.urlNoThrow("https://thepiratebay.se/search/" + WUtil.urlEncode(searchString) + "/" + pageIndex + "/7/0");
	}

}
