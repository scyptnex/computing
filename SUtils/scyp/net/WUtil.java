package scyp.net;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class WUtil {
	
	public static void main(String[] args) throws MalformedURLException{
		String a ="a b c =/+?";
		System.out.println(a);
		System.out.println(urlEncode(a));
		URL url = new URL("http://www.test.com/is this ok/");
		System.out.println(url.toString());
	}
	
	public static URL urlNoThrow(String wellFormedURL){
		try{
			return new URL(wellFormedURL);
		}
		catch(Exception exc){
			return null;
		}
	}
	
	public static String urlEncode(String unencoded){
		//ISO-8859-1
		//UTF-8
		try {
			return URLEncoder.encode(unencoded, "UTF-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

}