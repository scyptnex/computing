package scyp.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class WUtil {
	
	/**
	 * Non-throwing version of URL constructor
	 * @param wellFormedURL the string to construct the url from
	 * @return a URL from the string, or null if an error occurred
	 */
	public static URL urlNoThrow(String wellFormedURL){
		try{
			return new URL(wellFormedURL);
		}
		catch(Exception exc){
			return null;
		}
	}
	
	/**
	 * Opens a connection to the URL resource, returning the InputStream to read the source
	 * @param url A url for the resource
	 * @param secure true if we expect the resource to be secure
	 * @return the input stream connecting to the resource
	 * @throws IOException when security was expected and not found, or the URL failes somehow
	 */
	public static InputStream streamURL(URL url, boolean secure) throws IOException{
		URLConnection con = url.openConnection();
		InputStream ret = con.getInputStream();
		if(secure && !(con instanceof HttpsURLConnection)){
			ret.close();
			throw new IOException("Expected secure connection but the connection was not secure: " + url.toString());
		}
		return ret;
	}
	
	/**
	 * Alias: urlEncode(unencoded, "UTF-8")
	 */
	public static String urlEncode(String unencoded){
		return urlEncode(unencoded, "UTF-8");
	}
	
	/**
	 * Encodes the string as a URL by the standard.
	 * Also changes " " encode from "+" to "%20"
	 * @param unencoded the unencoded string
	 * @param standard the standard to encode to, "UTF-8" and "ISO-8859-1" for example
	 * @return the string encoded as a URL
	 */
	public static String urlEncode(String unencoded, String standard){
		try {
			return URLEncoder.encode(unencoded, standard).replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

}