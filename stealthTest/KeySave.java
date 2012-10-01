
import javax.security.*;
import javax.security.auth.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class KeySave {

	public static void main(String[] args) throws Exception{
		PBEKeySpec pbeKeySpec;
		PBEParameterSpec pbeParamSpec;
		SecretKeyFactory keyFac;

		// Salt
		byte[] salt = {
				(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
				(byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
		};

		// Iteration count
		int count = 20;

		// Create PBE parameter set
		pbeParamSpec = new PBEParameterSpec(salt, count);

		// Prompt user for encryption password.
		// Collect user password as char array (using the
		// "readPasswd" method from above), and convert
		// it into a SecretKey object, using a PBE key
		// factory.
		System.out.print("Enter encryption password:  ");
		System.out.flush();
		pbeKeySpec = new PBEKeySpec("rofl".toCharArray());
		keyFac = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede");
		SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

		// Create PBE Cipher
		Cipher pbeCipher = Cipher.getInstance("PBEWithSHA1AndDESede");

		// Initialize PBE Cipher with key and parameters
		pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

		// Our cleartext
		byte[] cleartext = "This is another example".getBytes();
		
		bout(cleartext);

		// Encrypt the cleartext
		byte[] ciphertext = pbeCipher.doFinal(cleartext);
		bout(ciphertext);
		
		Cipher pbed = Cipher.getInstance("PBEWithSHA1AndDESede");
		pbed.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
		byte[] decrypted = pbed.doFinal(ciphertext);
		bout(decrypted);

	}
	
	public static void bout(byte[] b){
		System.out.print("|m| = " + b.length);
		for(int i=0; i<b.length; i++){
			if(i%8 == 0) System.out.println();
			int v = (b[i] < 0 ? 256+b[i] : b[i]);
			System.out.print(Long.toHexString(v) + "\t");
		}
		System.out.println();
	}

}
