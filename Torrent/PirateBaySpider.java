import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

import scyp.net.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class PirateBaySpider {

	private String searchString = null;

	public static void main(String[] args) throws IOException{
		int count = 0;
		PirateBaySpider pbs = new PirateBaySpider("black sails s01e04");
		Document doc = Jsoup.connect(pbs.getSearchPage(0).toString()).get();
		Elements e = doc.getElementsByTag("tr");
		for(Element elem : e){
			Elements tdatas = elem.children();
			System.out.println(tdatas.get(1).text());
		}
		System.out.println(doc.title());
		System.out.println(e.size());
		BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("tst.html"), "UTF-8"));
		htmlWriter.write(doc.toString());
		htmlWriter.close();
		//System.out.println(pbs.getSearchPage(1));
		//for(String s : Unstream.toLineIterable(WUtil.streamURL(pbs.getSearchPage(0), true))){
		//	System.out.println(s);
		//}
		//		InputStream is = WUtil.streamURL(pbs.getSearchPage(0), true);
		//		Scanner sca = new Scanner(is);
		//		Pattern trStart = Pattern.compile("<tr");
		//		Pattern trEnd = Pattern.compile("</tr>");
		//		sca.useDelimiter(trStart);
		//		while(sca.hasNext()){
		//			sca.next();
		//			sca.useDelimiter(trEnd);
		//			if(!sca.hasNext()){
		//				System.out.println("noui");
		//				sca.useDelimiter("");
		//				while(sca.hasNext()) System.out.print(sca.next());
		//				break;
		//			}
		//			String mid = sca.next();
		//			System.out.println(count);
		//			sca.useDelimiter(trStart);
		//			count++;
		//		}
		//		sca.close();
		//Unstream.toFile(is, new File("test.html"));
		//System.out.println(pbs.getSearchPage(1));
		//for(String s : Unstream.toLineIterable(WUtil.streamURL(pbs.getSearchPage(0), true))){
		//	System.out.println(s);
		//	count++;
		//}
		System.out.println(count);

	}

	public PirateBaySpider(String search){
		searchString = search;
	}

	public URL getSearchPage(int pageIndex){
		return WUtil.urlNoThrow("https://thepiratebay.se/search/" + WUtil.urlEncode(searchString) + "/" + pageIndex + "/7/0");
	}

}
