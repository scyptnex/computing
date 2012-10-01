import java.net.*;
import java.io.*;

import java.security.*;

public class GodServer {
	
	public static void main(String[] args) throws Exception{
		new GodServer();
	}
	
	public GodServer() throws Exception{
		ServerSocket ss = new ServerSocket(12345);
		GodSocket g = new GodSocket(ss.accept());
		SecureRandom sr = new SecureRandom();
		byte[] b = sr.generateSeed(8);//64 bit seed
		System.out.println("Seed is " + GodSocket.hexify(b));
		g.send(b);
		sr.setSeed(b);
		
		boolean repeating = true;
		byte[] blok = new byte[8];
		int n = 0;
		while(repeating){
			sr.nextBytes(blok);
			System.out.println(n + " - " + GodSocket.hexify(blok));
			g.send(blok);
			String rep = g.recvString();
			if(!rep.equals("ok")){
				repeating = false;
			}
			n++;
		}
		g.close();
		System.out.println("Server: goodnight");
	}
	
}
