import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.interfaces.*;
import java.security.spec.*;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.net.*;
import java.io.*;

public class GodSocket {
	
	public static final String RSA_STANDARD = "RSA/ECB/PKCS1Padding";
	public static final String MAC_STANDARD = "HmacSHA256";
	public static final String AES_STANDARD = "AES/CBC/PKCS5Padding";
	public static final int AES_LENGTH = 128; // 192 and 256 bits may not be available
	public static final int RSA_LENGTH = 2048;
	public static final int MAC_LENGTH = 256;
	
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	
	public final Socket sock;
	private final PrintWriter pw;
	private final BufferedReader br;
	
	public GodSocket(Socket so) throws IOException{
		sock = so;
		pw = new PrintWriter(sock.getOutputStream());
		br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}
	
	public void close() throws IOException{
		pw.close();
		br.close();
		sock.close();
	}
	
	//receiving from the socket
	public byte[] recv(){
		try{
			String ln = br.readLine();
			if(ln == null || ln.length() < 1 || ln.length()%2 == 1) return null;
			return dehexify(ln);
		}
		catch(IOException e){
			e.printStackTrace();
			System.err.println("This error was caught voluntarily, please disregard it");
			return null;
		}
	}
	public String recvString(){
		return btos(recv());
	}
	
	//sending to the socket
	public void send(byte[] b){
		String hx = hexify(b);
		pw.println(hx);
		pw.flush();
	}
	public void sendString(String str){
		send(stob(str));
	}
	
	public static String hexify(byte[] b){
		if(b.length > 2000) System.err.println("Sending size " + b.length + " is doomed to fail");
		StringBuffer ret = new StringBuffer();//because many small additions are required
		for(int i=0; i<b.length; i++){
			int v = (b[i] < 0 ? 256+b[i] : b[i]);
			String tmp = Long.toHexString(v);
			ret.append(tmp.length() > 1 ? tmp: "0" + tmp);
		}
		return ret.toString();
	}
	
	public static byte[] dehexify(String m){
		byte[] ret = new byte[m.length()/2];
		for(int i=0; i<ret.length; i++){
			ret[i] = (byte)Long.parseLong(m.substring(i*2, i*2+2), 16);
		}
		return ret;
	}
	
	public static byte[] stob(String s){
		return s.getBytes(UTF8_CHARSET);
	}
	
	public static String btos(byte[] b){
		return new String(b, UTF8_CHARSET);
	}
	
	public static byte[] byteClone(byte[] a){
		byte[] ret = new byte[a.length];
		for(int i=0; i<a.length; i++) ret[i] = a[i];
		return ret;
	}
}