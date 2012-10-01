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
			File fi = new File("ncd1.tar.gz");
			byte[] hsh = SecureUtils.hash(fi);
			System.out.println(SecureUtils.hexify(hsh));
			sendFile(sck, fi, hsh);
			sck.close();
		}
		else{
			Socket sck = new Socket("localhost", DEFAULT_PORT);
			byte[] hsh = recvFile(sck, new File("transfer.tar.gz"));
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
			int blockSize = (int)Math.min(len-block*BLOCK_SIZE, BLOCK_SIZE);
			writeInteger(sock.getOutputStream(), blockSize);//send the size of this block
			byte[] minorHash = hashpipe(fis, sock.getOutputStream(), blockSize);//send the contents of the block
			if(hashMatch(sock.getInputStream(), minorHash)){//read the client's minor hash, if the client's hash was correct
				writeInteger(sock.getOutputStream(), TRANS_SUCCESS);//tell the client it read everything correctly
				block++;//increment the block and keep reading
			}
			else{//if the client's hash was incorrect
				writeInteger(sock.getOutputStream(), TRANS_ERROR);//tell the client it misread that last block
				fis = resetInput(fis, in, block*BLOCK_SIZE);//reset the file reader to the start of the current block
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
					fis = resetInput(fis, in, 0);//reset the file reader to the start of the current block
				}
			}
		}
	}
	
	public static byte[] recvFile(Socket sock, File out) throws IOException, NoSuchAlgorithmException{
		FileOutputStream mainOut = new FileOutputStream(out);
		byte[] ret = null;
		boolean reading = true;
		MessageDigest major = MessageDigest.getInstance(SecureUtils.STD_HASH);
		int block = 0;
		while(reading){//while there is data to be read
			long readln = readInteger(sock.getInputStream());//read the length of the next block
			if(readln == TRANS_FINISH){//if there are no more block
				mainOut.close();
				ret = major.digest();
				sock.getOutputStream().write(ret);//send my major hash
				long finishReply = readInteger(sock.getInputStream());//read the reply
				if(finishReply == TRANS_FINISH){//if the major hash was correct
					reading = false;//complete the transaction
				}
				else{//else the major has was incorrect
					out.delete();//delete the file
					mainOut = new FileOutputStream(out);//reset the output streams
					//start again
					block = 0;
					major = MessageDigest.getInstance(SecureUtils.STD_HASH);
					ret = null;
				}
			}
			else{//else there are more blocks
				File tempOut = new File(out.getParentFile(), out.getName() + ".part" + block);
				FileOutputStream partStream = new FileOutputStream(tempOut);//spawn a temporary file
				byte[] hash = hashpipe(sock.getInputStream(), partStream, readln);//read the block to temp file
				partStream.close();
				sock.getOutputStream().write(hash);//send my minor hash
				long minorReply = readInteger(sock.getInputStream());//read the reply
				if(minorReply == TRANS_SUCCESS){//if the transfer was correct
					FileInputStream partIn = new FileInputStream(tempOut);
					hashpipe(partIn, mainOut, readln, major);//send the temp file to the main file
					block++;
				}
				else{//if the transfer was wrong
					//do nothing
				}
				tempOut.delete();//delete the temp file
			}
		}
		return ret;
	}
	
	public static FileInputStream resetInput(FileInputStream stream, File in, long toPoint) throws IOException{
		stream.close();
		FileInputStream ret = new FileInputStream(in);
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
		MessageDigest md = MessageDigest.getInstance(SecureUtils.STD_HASH);
		hashpipe(is, os, len, md);
		return md.digest();
	}
	public static void hashpipe(InputStream is, OutputStream os, long len, MessageDigest md) throws IOException{
		byte[] buf = new byte[SMALL_BUFF];
		long read = 0;
		while(read < len){
			int thisRead = is.read(buf, 0, (int)Math.min(SMALL_BUFF, len-read));
			read += thisRead;
			os.write(buf, 0, thisRead);
			md.update(buf, 0, thisRead);
		}
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