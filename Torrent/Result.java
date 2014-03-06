import java.util.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class Result {
	
	public static final int EXPECTED_TABLE_COLS = 4;

	public final String desc;
	public final String magnet;
	public final String pageStem;
	public final int seeders;
	public final int leechers;
	
	public Result(Element trElement){
		Elements subs = trElement.getElementsByTag("td");
		if(subs.size() != EXPECTED_TABLE_COLS){
			throw new RuntimeException("Invalid number of table children");
		}
		seeders = Integer.parseInt(subs.get(2).text());
		leechers = Integer.parseInt(subs.get(3).text());
		desc = subs.get(1).text();
		
		Elements subDatas = subs.get(1).getElementsByTag("a");
		pageStem = subDatas.get(0).attr("href");
		magnet = subs.get(1).getElementsByAttributeValueContaining("href", "magnet:?xt=").get(0).attr("href");
	}
	
	public static ArrayList<Result> grabResultsFromDocument(Document rsltPage){
		ArrayList<Result> ret = new ArrayList<Result>();
		for(Element elem : rsltPage.getElementsByTag("tr")){
			if(!elem.hasClass("header")){
				ret.add(new Result(elem));
			}
		}
		return ret;
	}
	
}
