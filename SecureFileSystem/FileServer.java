import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileServer {

	public static final int DEFAULT_PORT = 23456;
	
	public static final long TRANS_FINISH = 0;
	public static final long TRANS_SUCCESS = -99;
	public static final long TRANS_ERROR = -1;
	public static final long TRANS_RESTART = -2;
	public static final int SMALL_BUFF = 1024*8;
	public static final int BLOCK_SIZE = 1024*SMALL_BUFF;

	public static void main(String[] args) throws Exception{
		if(args.length == 0){
			ServerSocket ss = new ServerSocket(DEFAULT_PORT);
			Socket sck = ss.accept();
			File fi = new File("sample.txt");
			byte[] hsh = SecureUtils.hash(fi);
			System.out.println(SecureUtils.hexify(hsh));
			sendFile(sck, fi, hsh);
			sck.close();
		}
		else{
			Socket sck = new Socket("localhost", DEFAULT_PORT);
			byte[] hsh = recvFile(sck, new File("transfer.out"));
			System.out.println(SecureUtils.hexify(hsh));
			sck.close();
		}
	}
	
	//TODO confirming final failed hash
	
	public static void sendFile(Socket sock, File in, byte[] hash) throws IOException, NoSuchAlgorithmException{
		long len = in.length();
		int block = 0;
		FileInputStream fis = new FileInputStream(in);
		while(block*BLOCK_SIZE < len){//while the client is waiting for the file
			MessageDigest minor = MessageDigest.getInstance(SecureUtils.STD_HASH);
			int blockSize = (int)Math.min(len-block*BLOCK_SIZE, BLOCK_SIZE);
			writeInteger(sock.getOutputStream(), blockSize);//send the size of this block
			byte[] minorHash = hashpipe(fis, sock.getOutputStream(), blockSize);//send the contents of the block
			if(hashMatch(sock.getInputStream(), minorHash)){//read the client's minor hash, if the client's hash was correct
				writeInteger(sock.getOutputStream(), TRANS_SUCCESS);//tell the client it read everything correctly
				block++;//increment the block and keep reading
			}
			else{//if the client's hash was incorrect
				writeInteger(sock.getOutputStream(), TRANS_ERROR);//tell the client it misread that last block
				fis = resetInput(fis, block*BLOCK_SIZE);//reset the file reader to the start of the current block
			}
			if(block*BLOCK_SIZE >= len){//if this is the last block
				writeInteger(sock.getOutputStream(), TRANS_FINISH);//send the end signal
				if(hashMatch(sock.getInputStream(), hash)){//read the client's major hash, if the hash is correct
					fis.close();//complete the transaction
					writeInteger(sock.getOutputStream(), TRANS_FINISH);
				}
				else{//if the hash is incorrect
					//ive no idea how we could have gotten to here
					writeInteger(sock.getOutputStream(), TRANS_RESTART);//send the restart signal
					block = 0;//set the block to be 0
					fis = resetInput(fis, 0);//reset the file reader to the start of the current block
				}
			}
		}
	}
	
	public static byte[] recvFile(Socket sock, File out) throws IOException, NoSuchAlgorithmException{
		File tempOut = new File(out.getParentFile(), out.getName() + ".part");
		FileOutputStream mainOut = new FileOutputStream(out);
		byte[] ret = null;
		boolean reading = true;
		while(reading){//while there is data to be read
			long readln = readInteger(sock.getInputStream());//read the length of the next block
			if(readln == TRANS_FINISH){//if there are no more block
				mainOut.close();
				sock.getOutputStream().write(major.digest());//send my major hash
				long finishReply = readInteger(sock.getInputStream());//read the reply
				if(finishReply == TRANS_FINISH){//if the major hash was correct
					reading = false;//complete the transaction
				}
				else{//else the major has was incorrect
					tempOut.delete();
					out.delete();//delete both files
					mainOut = new FileOutputStream(out);//reset the output streams
					//start again
				}
			}
			else{//else there are more blocks
				FileOutputStream partStream = new FileOutputStream(tempOut);
				hashpipe(sock.getInputStream(), partStream, readln);//read the block to temp file
				//send my minor hash
				//read the reply
				if(true){//if the transfer was correct
					//send the temp file to the main file
					//increment the block
				}
				else{//if the transfer was wrong
					//delete the temp file
				}
			}
		}
		return ret;
	}
	
	public static FileInputStream resetInput(FileInputStream stream, long toPoint) throws IOException{
		stream.close();
		FileInputStream ret = new FileInputStream(stream.getFD());
		ret.skip(toPoint);
		return ret;
	}
	
	public static boolean hashMatch(InputStream is, byte[] hash) throws IOException{
		boolean correct = true;
		for(int i=0; i<hash.length; i++){
			if(hash[i] != (byte)is.read()) correct = false;
		}
		return correct;
	}
	
	public static byte[] hashpipe(InputStream is, OutputStream os, long len) throws IOException, NoSuchAlgorithmException{
		byte[] buf = new byte[SMALL_BUFF];
		long read = 0;
		MessageDigest md = MessageDigest.getInstance(SecureUtils.STD_HASH);
		while(read < len){
			int thisRead = is.read(buf, 0, (int)Math.min(SMALL_BUFF, len-read));
			read += thisRead;
			os.write(buf, 0, thisRead);
			md.update(buf, 0, thisRead);
		}
		return md.digest();
	}

	//wonder if throwing error on end of stream fucks over other implementations? :P
	public static long readInteger(InputStream is) throws IOException{
		byte[] isr = new byte[8];
		for(int b=0; b<isr.length; b++){
			int r = is.read();
			if(r == -1) throw new IOException("readInt failed at end of stream");
			isr[b] = (byte)r;
		}
		ByteBuffer bb = ByteBuffer.wrap(isr);
		return bb.getLong();
	}

	public static void writeInteger(OutputStream os, long i) throws IOException{
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(i);
		os.write(bb.array());
	}

}