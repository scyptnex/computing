import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.io.*;

public class Josl {
	
	public static final String ALG = "AES";
	public static final String CHAIN = "CBC";
	public static final String PADD = "PKCS5Padding";
	
	public static final int KEY_LENGTH = 256;
	public static final int IV_LENGTH = 128;
	public static final int SALT_LENGTH = 64;
	
	public static final File swapdir = new File("/home/scyptnex/Desktop");
	
	public static void main(String[] args) throws Exception{
		byte[] pwd = "hi".getBytes();
		System.out.println(SecureUtils.hexify(pwd));
		byte[] key = new byte[KEY_LENGTH/8];
		byte[] iv = new byte[IV_LENGTH/8];
		byte[] salt = new byte[SALT_LENGTH/8];
		for(int i=0; i<salt.length; i++) salt[i] = (byte)i;
		System.out.println(SecureUtils.hexify(salt));
		//passer(pwd, salt, key, iv, MessageDigest.getInstance("md5"), 0);
		passer(pwd, null, key, iv, MessageDigest.getInstance("md5"), 0);
		System.out.println(SecureUtils.hexify(key));
		System.out.println(SecureUtils.hexify(iv));
		
		SecretKeySpec aesSpec = new SecretKeySpec(key, ALG);
		AlgorithmParameterSpec ivs = new IvParameterSpec(iv);
		Cipher enc = Cipher.getInstance(ALG + "/" + CHAIN + "/" + PADD);
		enc.init(Cipher.ENCRYPT_MODE, aesSpec, ivs);
		Cipher dec = Cipher.getInstance(ALG + "/" + CHAIN + "/" + PADD);
		dec.init(Cipher.DECRYPT_MODE, aesSpec, ivs);
		
		long t = System.currentTimeMillis();
		SecureUtils.fastFileSwap(new File(swapdir, "thirdrun.tar.gz"), new File(swapdir, "j.enc"), enc);
		System.out.println("Encrypt Time(s) = " + ((System.currentTimeMillis()-t)/1000.0));
		t = System.currentTimeMillis();
		SecureUtils.fastFileSwap(new File(swapdir, "j.enc"), new File(swapdir, "j.tar.gz"), dec);
		System.out.println("Decrypt Time(s) = " + ((System.currentTimeMillis()-t)/1000.0));
	}
	
	//EVP_BytesToKey
	public static int passer(byte[] pass, byte[] salt, byte[] key, byte[] iv, MessageDigest d, int flip){
		byte[] oldBuf = null;
		int keyFill = 0, ivFill = 0;
		while(keyFill < key.length || ivFill < iv.length){
			if(oldBuf != null) d.update(oldBuf);
			d.update(pass);
			if(salt != null) d.update(salt);
			byte[] buf = d.digest();
			for(int i=0; i<flip; i++){
				buf = d.digest(buf);
			}
			int bufRead = 0;
			while(keyFill < key.length && bufRead < buf.length){
				key[keyFill] = buf[bufRead];
				keyFill++;
				bufRead++;
			}
			while(ivFill < iv.length && bufRead < buf.length){
				iv[ivFill] = buf[bufRead];
				ivFill++;
				bufRead++;
			}
			oldBuf = buf;
		}
		return key.length;
	}
	
}