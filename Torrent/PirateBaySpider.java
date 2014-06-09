import java.io.*;
import java.net.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;

import scyp.net.WUtil;

public class PirateBaySpider {

	public static final String DOMAIN = "https://thepiratebay.se";
	private String searchString = null;

	public static void main(String[] args) throws IOException{
		String str = "";
		for(String s : args){
			str = str + " " + s;
		}
		str = str.trim();
		if(str.length() < 0) str = "continuum s01e04";
		PirateBaySpider pbs = new PirateBaySpider(str);
		Document doc = pbs.getSearchPage(0);
		ArrayList<Result> rslts = Result.grabResultsFromDocument(doc);
		for(Result r : rslts){
			System.out.println(String.format("%6d - %s", r.seeders, r.desc));
			System.out.println("        " + r.magnet);
		}
		System.out.println(doc.title());
		System.out.println(rslts.size());
	}

	public PirateBaySpider(String search){
		searchString = search;
	}

	public Document getSearchPage(int pageIndex) throws IOException{
		URL pageUrl = WUtil.urlNoThrow(DOMAIN + "/search/" + WUtil.urlEncode(searchString) + "/" + pageIndex + "/7/0");
		return Jsoup.connect(pageUrl.toString()).get();
	}
	
	public static void dumpPage(File output, Document page){
		try {
			BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
			htmlWriter.write(page.toString());
			htmlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
