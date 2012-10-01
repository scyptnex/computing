import java.io.*;

public class Test {
	
	public static void main(String[] args){
		//System.out.println(testPass("zomg", "Hello Cheese!"));
		
		//System.out.println(testFileSwap("rofl", new File("/home/scyptnex/Desktop/ncd1.tar.gz")));
		
		System.out.println(testHashSum(new File("/home/scyptnex/Desktop/ncd1.tar.gz")));
	}
	
	public static boolean testHashSum(File fi){
		byte[] hs = SecureUtils.hash(fi);
		if(hs == null){
			return false;
		}
		else{
			System.out.println(fi.getName() + ": " + SecureUtils.hexify(hs));
			return true;
		}
	}
	
	public static boolean testFileSwap(String pass, File fi){
		File enc = new File("enc.suit");
		File dec = new File("dec.suit");
		SecureUtils.SecurePass sp = SecureUtils.getPass(pass);
		long t = System.currentTimeMillis();
		try{
			sp.fileEncrypt(fi, enc);
			System.out.println("encryption takes " + (System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			sp.fileDecrypt(enc, dec);
			System.out.println("decryption takes " + (System.currentTimeMillis() - t));
			return true;
		}
		catch(IOException e){
			return false;
		}
	}
	
	public static boolean testPass(String pass, String phrase){
		SecureUtils.SecurePass sp = SecureUtils.getPass(pass);
		byte[] b = SecureUtils.stob(phrase);
		System.out.println("str: " + SecureUtils.hexify(b) + ".");
		byte[] enc = sp.encipherBytes(b);
		System.out.println("enc: " + SecureUtils.hexify(enc) + ".");
		byte[] dec = sp.decipherBytes(enc);
		System.out.println("dec: " + SecureUtils.hexify(dec) + ".");
		String wd = SecureUtils.btos(dec);
		System.out.println("phr: " + wd);
		return phrase.equals(wd);
	}
	
}