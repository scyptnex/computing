package spider;
public abstract class Page{
	public final String url;
	public String successorUrl;
	public final String title;
	public final String meta;
	public final String[] imgURLs;
	public final String[] imgNames;

	public Page(String ur, String nam, String met, String[] iUrl, String[] iName){
		url = ur;
		successorUrl = null;
		title = nam;
		meta = met;
		imgURLs = iUrl;
		imgNames = iName;
	}
}