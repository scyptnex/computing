import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import scyp.net.WUtil;

public class PirateBaySpider {

	public static final String DOMAIN = "https://thepiratebay.se";
	private String searchString = null;

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException{
		int count = 0;
		PirateBaySpider pbs = new PirateBaySpider("bioshock infinite");
		Document doc = Jsoup.connect(pbs.getSearchPage(0).toString()).get();
		Elements e = doc.getElementsByTag("tr");
		for(Element elem : e){
			if(!elem.hasClass("header")){
				System.out.println(new Result(elem).desc);
			}
		}
		System.out.println(doc.title());
		System.out.println(e.size());
		BufferedWriter htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("tst.html"), "UTF-8"));
		htmlWriter.write(doc.toString());
		htmlWriter.close();
		System.out.println(count);

	}

	public PirateBaySpider(String search){
		searchString = search;
	}

	public URL getSearchPage(int pageIndex){
		return WUtil.urlNoThrow(DOMAIN + "/search/" + WUtil.urlEncode(searchString) + "/" + pageIndex + "/7/0");
	}

}
