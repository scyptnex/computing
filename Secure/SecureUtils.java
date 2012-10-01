import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.interfaces.*;
import java.security.spec.*;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.SwingWorker;

import java.awt.Toolkit;
import java.io.*;
import java.util.*;

public class SecureUtils {
	
	public static final String STD_HASH = "SHA-256";
	public static final String STD_AES = "AES/CBC/PKCS5Padding";
	public static final int LEN_SHA = 256;
	public static final int LEN_AES = 128; // 192 and 256 bits may not be available
	
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	public static final String VERIFY = "The Quick Brown Fox jumps over the Lazy Dog.";
	
	public final Ssys2 owner;
	
	private byte[] key;
	private byte[] iv;
	private byte[] buffer = new byte[2056];
	public Cipher enc;
	public Cipher dec;
	
	public static void main(String[] args) throws Exception{
		Ssys2.main(args);
		/**SecureUtils secu = new SecureUtils("roflol", null);
		
		File plain = new File("zomgfile");
		File ci = new File("zomgciph");
		File mess = new File("zomgdec");
		
		InputStream is = new FileInputStream(plain);
		byte[] buffer = new byte[5];
		int read = 0;
		while((read = is.read(buffer)) >= 0){
			System.out.write(buffer, 0, read);
		}
		is.close();
		System.out.println("\n------------------------\n");
		
		secu.secureSwap(ci, plain, true);
		
		is = new FileInputStream(ci);
		buffer = new byte[5];
		read = 0;
		while((read = is.read(buffer)) >= 0){
			System.out.write(buffer, 0, read);
		}
		is.close();
		System.out.println("\n------------------------\n");
		
		secu.secureSwap(ci, mess, false);
		
		is = new FileInputStream(mess);
		buffer = new byte[5];
		read = 0;
		while((read = is.read(buffer)) >= 0){
			System.out.write(buffer, 0, read);
		}
		is.close();
		System.out.println("\n------------------------\n");
		
		ci.delete();
		mess.delete();**/
	}
	
	public SecureUtils(String pass, Ssys2 own) throws Exception{
		owner = own;
		
		byte[] passbytes = hash(stob(pass));
		key= new byte[LEN_AES/8];
		iv = new byte[LEN_AES/8];
		
		System.arraycopy(passbytes, 0, key, 0, LEN_AES/8);
		System.arraycopy(passbytes, LEN_AES/8, iv, 0, LEN_AES/8);
		
		resetCiphers();
	}
	
	public void unblockSecureSwap(File store, File temp, boolean encrypt, SBase callback){
		final File sto = store;
		final File tmp = temp;
		final boolean enc = encrypt;
		final SBase cb = callback;
		Thread t = new Thread(){
			public void run(){
				if(blockSecureSwap(sto, tmp, enc)){
					cb.finishRetrieve(tmp);
				}
				else{
					System.err.println("Failed to unblocking retrieve " + tmp.getName());
				}
			}
		};
		t.start();
	}
	
	public boolean blockSecureSwap(File store, File temp, boolean encrypt){
		
		File read = (encrypt ? temp: store);
		File write = (encrypt ? store: temp);
		
		System.out.println(Thread.currentThread().getPriority());
		
		if(!read.exists()) return false;
		
		try{
			InputStream is = new FileInputStream(read);
			OutputStream os = new FileOutputStream(write);
			if(encrypt){
				os = new CipherOutputStream(os, enc);
			}
			else{
				is = new CipherInputStream(is, dec);
			}
			
			
			int amtRead = 0;
			long totalRead = 0;
			int percentage = 0;
			String desc = (encrypt? "E" : "D") + ": " + temp.getName();

			if(owner != null) owner.updateProgress(desc, percentage);
			
			while ((amtRead = is.read(buffer)) >= 0) {
				os.write(buffer, 0, amtRead);
				totalRead += amtRead;
				if(totalRead > read.length()*percentage/100){
					percentage++;
					if(owner != null) owner.updateProgress(desc, percentage);
				}
			}
			if(owner != null) owner.updateProgress();
			
			is.close();
			os.close();
			return true;
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void resetCiphers() throws Exception{
		SecretKeySpec aesSpec = new SecretKeySpec(key, "AES");
		enc = Cipher.getInstance(STD_AES);
		dec = Cipher.getInstance(STD_AES);
		AlgorithmParameterSpec ivs = new IvParameterSpec(iv);
		enc.init(Cipher.ENCRYPT_MODE, aesSpec, ivs);
		dec.init(Cipher.DECRYPT_MODE, aesSpec, ivs);
	}
	
	public boolean selfVerify(byte[] chk){
		byte[] ver = getVerify();
		return(byteSame(ver, chk));
	}
	
	public byte[] getVerify(){
		try {
			return enc.doFinal(stob(VERIFY));
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Primitives
	 */
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
	
	public static byte[] byteClone(byte[] a){
		byte[] ret = new byte[a.length];
		for(int i=0; i<a.length; i++) ret[i] = a[i];
		return ret;
	}
	
	public static boolean byteSame(byte[] a, byte[] b){
		if(a.length != b.length) return false;
		for(int i=0; i<a.length; i++){
			if(a[i] != b[i]) return false;
		}
		return true;
	}
	
}