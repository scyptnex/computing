import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.interfaces.*;
import java.security.spec.*;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.*;

public class SecureUtils {
	
	public static final String STD_HASH = "SHA-256";
	public static final String STD_AES = "AES/CBC/PKCS5Padding";
	public static final int LEN_SHA = 256;
	public static final int LEN_AES = 128; // 192 and 256 bits may not be available
	
	public static final int FILE_BUFFERING_SIZE = 1024;
	
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	/*
	 * AUTHENTICATION IDENTITY
	 */
	
	public static class SecurePass{
		
		private byte[] pass;
		private byte[] key;
		private byte[] iv;
		public final Cipher enc;
		public final Cipher dec;
		
		private SecurePass(String pwd) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
			//bytes
			pass = hash(stob(pwd));
			key= new byte[LEN_AES/8];
			iv = new byte[LEN_AES/8];
			System.arraycopy(pass, 0, key, 0, LEN_AES/8);
			System.arraycopy(pass, LEN_AES/8, iv, 0, LEN_AES/8);
			
			//cipher streams
			SecretKeySpec aesSpec = new SecretKeySpec(key, "AES");
			enc = Cipher.getInstance(STD_AES);
			dec = Cipher.getInstance(STD_AES);
			AlgorithmParameterSpec ivs = new IvParameterSpec(iv);
			enc.init(Cipher.ENCRYPT_MODE, aesSpec, ivs);
			dec.init(Cipher.DECRYPT_MODE, aesSpec, ivs);
		}
		
		public void fileDecrypt(File start, File end) throws IOException{
			fileSwap(start, end, dec, new SecureProgressMonitor("Decrypting " + start.getName(), start.length()));
		}
		
		public void fileEncrypt(File start, File end) throws IOException{
			fileSwap(start, end, enc, new SecureProgressMonitor("Encrypting " + start.getName(), start.length()));
		}
		
		public byte[] encipherBytes(byte[] b){
			try{
				return cipherBytes(enc, b);
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		public byte[] decipherBytes(byte[] b){
			try{
				return cipherBytes(dec, b);
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static SecurePass getPass(String pwd){
		try{
			return new SecurePass(pwd);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * STATIC CYPHERING
	 */
	
	public static void fastFileSwap(File in, File out, Cipher cip) throws IOException{
		InputStream is = new FileInputStream(in);
		OutputStream os = new FileOutputStream(out);
		byte[] buffer = new byte[FILE_BUFFERING_SIZE];
		int rd = 0;
		int tot = 0;
		long start = System.currentTimeMillis();
		while((rd = is.read(buffer)) >= 0){
			tot+=rd;
			os.write(cip.update(buffer, 0, rd));
		}
		try {
			os.write(cip.doFinal());
		} catch (IllegalBlockSizeException e) {
			throw new IOException("Cipher final illegal block size");
		} catch (BadPaddingException e) {
			throw new IOException("Cipher final bad padding");
		}
		is.close();
		os.close();
	}
	
	public static void fileSwap(File in, File out, Cipher cip, SecureProgressMonitor spm) throws IOException{
		//String msg = "Decrypting " + out.getName();
		//if(enc) msg = "Encrypting " + in.getName();
		InputStream is = new FileInputStream(in);
		OutputStream os = new FileOutputStream(out);
		byte[] buffer = new byte[FILE_BUFFERING_SIZE];
		int rd = 0;
		int tot = 0;
		long start = System.currentTimeMillis();
		while((rd = is.read(buffer)) >= 0){
			tot+=rd;
			if(System.currentTimeMillis()-start > 500){
				spm.update(tot);
				System.out.println(spm);
				start = System.currentTimeMillis();
			}
			os.write(cip.update(buffer, 0, rd));
		}
		try {
			os.write(cip.doFinal());
		} catch (IllegalBlockSizeException e) {
			throw new IOException("Cipher final illegal block size");
		} catch (BadPaddingException e) {
			throw new IOException("Cipher final bad padding");
		}
		is.close();
		os.close();
	}
	
	public static byte[] cipherBytes(Cipher cip, byte[] data) throws IllegalBlockSizeException, BadPaddingException{
		return cip.doFinal(data);
	}
	
	/*
	 * Progress Monitoring
	 */
	
	public static class SecureProgressMonitor{
		public final String name;
		public final long max;
		private long cur;
		
		public SecureProgressMonitor(String nm, long mx){
			max = mx;
			name = nm;
		}
		
		public synchronized void update(long amt){
			cur = Math.min(Math.max(cur, amt), max);
		}
		
		public synchronized void finish(){
			cur = max;
		}
		
		public synchronized double progress(){
			return ((double)cur / (double)max);
		}
		
		public String toString(){
			return name + ": " + (int)Math.floor(progress()*100) + "%";
		}
	}
	
	/*
	 * STATIC UTILITY
	 */
	
	public static byte[] hash(File fi){
		try{
			MessageDigest d = MessageDigest.getInstance(STD_HASH);
			FileInputStream fis = new FileInputStream(fi);
			byte[] dta = new byte[FILE_BUFFERING_SIZE];
			int read = 0;
			while((read = fis.read(dta)) > 0){
				d.update(dta, 0, read);
			}
			fis.close();
			return d.digest();
		}
		catch(Exception exc){
			exc.printStackTrace();
			return null;
		}
	}
	
	public static byte[] hash(byte[] data){
		try{
			MessageDigest d = MessageDigest.getInstance(STD_HASH);
			return d.digest(data);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String hexify(byte[] b){
		StringBuffer ret = new StringBuffer();
		for(int i=0; i<b.length; i++){
			String lng = Long.toString((b[i] < 0 ? 256 + b[i] : b[i]), 16);
			if(lng.length() != 2) lng = "0" + lng;
			ret.append(lng);
		}
		return ret.toString();
	}
	
	public static byte[] dehexify(String s){
		byte[] ret = new byte[s.length()/2];
		for(int i=0; i<ret.length; i++){
			long amt = Long.parseLong(s.substring(i*2, (i+1)*2), 16);
			ret[i] = (byte)amt;
		}
		return ret;
	}
	
	public static byte[] stob(String s){
		return s.getBytes(UTF8_CHARSET);
	}
	
	public static String btos(byte[] b){
		return new String(b, UTF8_CHARSET);
	}
	
}
