import java.io.*;
import java.net.*;

public class SelfSocket {
	
	public static void main(String[] args) throws Exception{
		new SelfSocket();
	}
	
	public SelfSocket() throws IOException{
		Socket sck = new Socket();
		
		sck.bind(null);
		System.out.println(sck.getLocalSocketAddress());
		Socket sck2 = new Socket();
		sck2.connect(sck.getLocalSocketAddress());
		System.out.println(sck.isConnected());
		InputStream is = sck.getInputStream();
		OutputStream os = sck.getOutputStream();
		byte[] dta = "Hello".getBytes();
		byte[] rd = new byte[dta.length];
	}
	
}