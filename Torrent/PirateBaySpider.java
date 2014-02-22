import java.io.*;
import java.net.URL;

import scyp.net.*;

public class PirateBaySpider {

	private String searchString = null;

	public static void main(String[] args) throws IOException{
		PirateBaySpider pbs = new PirateBaySpider("black sails s01e04");
		System.out.println(pbs.getSearchPage(1));
		for(String s : Unstream.toLineIterable(WUtil.streamURL(pbs.getSearchPage(0), true))){
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
