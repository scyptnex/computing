import java.io.*;
import java.net.*;

import java.security.*;

public class GodClient {
	
	public static void main(String[] args) throws Exception{
		new GodClient();
	}
	
	public GodClient() throws Exception{
		Socket sock = new Socket("localhost", 12345);
		GodSocket g = new GodSocket(sock);
		byte[] b = g.recv();
		System.out.println("Got seed " + GodSocket.hexify(b));
		SecureRandom sr = new SecureRandom(b);
		
		boolean repeating = true;
		byte[] blok = new byte[8];
		int n = 0;
		while(repeating){
			sr.nextBytes(blok);
			byte[] comp = g.recv();
			for(int i=0; i<blok.length; i++){
				if(blok[i] != comp[i]){
					repeating = false;
					break;
				}
			}
			if(!repeating){
				System.out.println(n + " BROKE");
				System.out.println("Expecting: " + GodSocket.hexify(blok));
				System.out.println("Got:       " + GodSocket.hexify(comp));
				g.sendString("bad");
			}
			else{
				System.out.println(n + " ok");
				g.sendString("ok");
				n++;
			}
		}
		
		
		g.close();
		System.out.println("Client: see you");
	}
	
}
