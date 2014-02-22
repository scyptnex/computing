package scyp.net;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

public abstract class WGet<T> implements Iterator<T>, Iterable<T>{

	public static int BUFFER_SIZE = 4096;
	
	public final URL target;
	public final ArrayList<String> content;
	public final boolean isSecure;
	protected final URLConnection connection;

	public static void main(String[] args) throws IOException{
		URL url = new URL("http://parahumans.files.wordpress.com/2011/06/cityscape2.jpg");
		toFile(new java.io.File("test.jpg"), url);
		
		System.out.println(toList(new URL("http://rp-www.cs.usyd.edu.au/~nhol8058/")));
	}
	
	public static ArrayList<String> toList(URL target) throws IOException{
		WPage page = new WPage(target);
		ArrayList<String> ret = new ArrayList<String>();
		for(String s : page){
			ret.add(s);
		}
		return ret;
	}
	
	public static void toFile(java.io.File fi, URL target) throws IOException{
		WFile olFile = liveFile(target);
		FileOutputStream fos = new FileOutputStream(fi);
		while (olFile.hasNext()){
			fos.write(olFile.buffer, 0, olFile.readAmmount);//unsafe internal use only
			olFile.internalBuffer();
		}
		fos.close();
	}

	public static WPage livePage(URL targetPage) throws IOException{
		return new WPage(targetPage);
	}

	public static WFile liveFile(URL targetFile) throws IOException{
		return new WFile(targetFile, BUFFER_SIZE);
	}
	
	

	protected WGet(URL target) throws IOException{
		this.target = target;
		isSecure = this.target.toString().startsWith("https");
		content = new ArrayList<String>();
		connection = target.openConnection();
	}

	public static class WPage extends WGet<String>{

		private final BufferedReader in;
		private String lineBuffer;

		protected WPage(URL targetPage) throws IOException {
			super(targetPage);
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			lineBuffer ="";
			bufferLine();
		}

		private void bufferLine(){
			if(hasNext()){
				try{
					lineBuffer = in.readLine();
					if(lineBuffer == null) close();
				}
				catch(IOException exc){
					exc.printStackTrace();
					lineBuffer = null;
				}
			}
		}
		
		public void close() throws IOException{
			in.close();
		}

		@Override
		public String next() {
			if(!hasNext()) return null;
			String ret = lineBuffer;
			bufferLine();
			return ret;
		}

		@Override
		public boolean hasNext() {
			return lineBuffer != null;
		}

	}

	public static class WFile extends WGet<byte[]>{
		
		private final InputStream in;
		private final byte[] buffer;
		private int readAmmount;

		protected WFile(URL targetFile, int bufferSize) throws IOException {
			super(targetFile);
			in = connection.getInputStream();
			buffer = new byte[bufferSize];
			readAmmount = 1;
			internalBuffer();
		}
		
		public int getBufferSize(){
			return buffer.length;
		}
		
		private void internalBuffer(){
			if(!hasNext()) return;
			try{
				readAmmount = in.read(buffer);
				if(!hasNext()) close();
			}
			catch(IOException xc){
				xc.printStackTrace();
				readAmmount = -1;
			}
		}

		@Override
		public byte[] next() {
			if(!hasNext()) return null;
			byte[] ret = new byte[readAmmount];
			System.arraycopy(buffer, 0, ret, 0, readAmmount);
			internalBuffer();
			return ret;
		}

		@Override
		public boolean hasNext() {
			return readAmmount > 0;
		}

		@Override
		public void close() throws IOException {
			in.close();
		}
	}
	
	public abstract void close() throws IOException;

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Cannot remove from a url connection");
	}
	
	public boolean isSecure(){
		return connection instanceof HttpsURLConnection;
	}

}
