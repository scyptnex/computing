import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.interfaces.*;
import java.security.spec.*;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.io.*;

public class SecureLayer {
	
	public static final String RSA_STANDARD = "RSA/ECB/PKCS1Padding";
	public static final String MAC_STANDARD = "HmacSHA256";
	public static final String AES_STANDARD = "AES/CBC/PKCS5Padding";
	public static final int AES_LENGTH = 128; // 192 and 256 bits may not be available
	public static final int RSA_LENGTH = 2048;
	public static final int MAC_LENGTH = 256;
	
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	//some nice randomness
	private SecureRandom rand;
	
	//rsa bits
	private RSAPublicKey yourPublic;
	private RSAPublicKey myPublic;
	private RSAPrivateKey myPrivate;
	private Cipher outCipher;
	private Cipher inCipher;
	
	//aes bits
	private byte[] rawKey;
	private byte[] initVec;
	private SecretKeySpec aesSpec;
	Cipher enc;
	Cipher dec;
	
	//all macs use hmacSHA256
	private Mac rsaMac;
	private Mac aesMac;
	private byte[] rsaMacRaw;
	private byte[] aesMacRaw;
	
	public static void main(String[] args) throws Exception{
		long tmp = System.currentTimeMillis();
		SecureLayer sl = new SecureLayer();
		sl.selfInitAES();
		sl.selfInitMac(true);
		sl.selfInitRSA();
		sl.selfInitMac(false);
		long init = System.currentTimeMillis() - tmp;
		
		tmp = System.currentTimeMillis();
		String message = "This was a triumph.  I'm making a note here: 'HUGE SUCCESS!'.  It's hard to overstate my satisfaction";
		byte[] mb = stob(message);
		System.out.println(btos(mb));
		long control = System.currentTimeMillis() - tmp;

		tmp = System.currentTimeMillis();
		byte[] eb = sl.sendAES(mb);
		byte[] db = sl.receiveAES(eb);
		System.out.println(btos(db));
		long aes = System.currentTimeMillis() - tmp;

		tmp = System.currentTimeMillis();
		byte[] pubd = sl.descMyPublic();
		sl.initRSAYou(pubd);
		byte[] rsao = sl.sendRSA(mb);
		byte[] rsai = sl.receiveRSA(rsao);
		System.out.println(btos(rsai));
		long rsa = System.currentTimeMillis() - tmp;
		
		System.out.println("Init: " + init + "\nControl: " + control + "\nAES: " + aes + "\nRSA: " + rsa);
		
	}
	
	public SecureLayer(){
		rand = new SecureRandom(new SecureRandom().generateSeed(32));
		
		yourPublic = null;
		myPublic = null;
		myPrivate = null;
		outCipher = null;
		inCipher = null;
		
		rawKey = null;
		initVec = null;
		aesSpec = null;
		enc = null;
		dec = null;
		
		rsaMac = null;
		aesMac = null;
		rsaMacRaw = null;
		aesMacRaw = null;
	}
	
	/**
	 * Auto methods
	 * These are the actual ones the user will call alot
	 * These methods string together secure and non-secure functions to perform some useful task
	 */
	
	//This method DOES CHECK THE PASSWORDED MAC
	//but, not all rsa messages are sent with a passworded mac, so use this one with caution
	public byte[] sendRSA(byte[] message) throws SecurityException{
		return getRSAEncrypted(addMAC(message, false));
	}
	
	//Some funny things:
	//the message containing the passworded RSA mac is sent with a mac
	//to decode that ONE message, the password is extracted manually and initialised in the layer, then the check for mac is performed
	public byte[] receiveRSA(byte[] message) throws SecurityException{
		byte[] dec = getRSADecrypted(message);
		byte[] macPart = new byte[MAC_LENGTH/8];
		byte[] messagePart = new byte[dec.length - macPart.length];
		//presumes the mac was naively appended to the message, THIS MAY CHANGE
		byteSplit(dec, messagePart, macPart);
		if(!checkMAC(messagePart, macPart, false)) throw new SecurityException("RSA Message does not match its MAC");
		return messagePart;
	}
	
	//this one does the adition of the mac and encrypts for send
	public byte[] sendAES(byte[] message) throws SecurityException{
		return getAESEncrypted(addMAC(message, true));
	}
	
	public byte[] receiveAES(byte[] message) throws SecurityException{
		byte[] dec = getAESDecrypted(message);
		byte[] macPart = new byte[MAC_LENGTH/8];
		byte[] messagePart = new byte[dec.length - macPart.length];
		//presumes the mac was naively appended to the message, THIS MAY CHANGE
		byteSplit(dec, messagePart, macPart);
		if(!checkMAC(messagePart, macPart, true)) throw new SecurityException("AES Message does not match its MAC");
		return messagePart;
	}
	
	public byte[] addMAC(byte[] message, boolean useAES) throws SecurityException{
		//simple method
		//was it not suggested in lectures we use MAC(AES(m + MAC(m))%p)+AES(MAC(m%q)) or something...
		return byteJoin(message, getMac(message, useAES));
	}
	
	public boolean checkMAC(byte[] message, byte[] mac, boolean useAES) throws SecurityException{
		byte[] expectedMac = getMac(message, useAES);
		for(int i=0; i<MAC_LENGTH/8; i++){
			if(expectedMac[i] != mac[i]) return false;
		}
		return true;
	}
	
	
	/**
	 * Self init methods
	 * These rely on a good and random secure random, its as random as it'll ever be after initialisation
	 */
	
	public void selfInitRSA() throws SecurityException{
		try{
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(RSA_LENGTH, rand);
			KeyPair keyPair = keyPairGenerator.genKeyPair();
			byte[] pubDesc = keyDesc((RSAPublicKey)keyPair.getPublic());
			byte[] priDesc = keyDesc((RSAPrivateKey)keyPair.getPrivate());
			initRSAMe(pubDesc, priDesc);
		}
		catch (NoSuchAlgorithmException e) {
			throw new SecurityException("RSA Init: No Such Algorithm");
		}
	}
	
	public void selfInitAES() throws SecurityException{
		try{
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(AES_LENGTH, rand);
			byte[] niv = new byte[AES_LENGTH/8];
			rand.nextBytes(niv);
			initAES(kgen.generateKey().getEncoded(), niv);
		}
		catch(NoSuchAlgorithmException exc){
			throw new SecurityException("AES Init: No Such Algorithm");
		}
	}
	
	public void selfInitMac(boolean useAES) throws SecurityException{
		byte[] nmac = new byte[MAC_LENGTH/8];
		rand.nextBytes(nmac);
		initMac(nmac, useAES);
	}
	
	
	/**
	 * Initialisers, adds to the security layer as parts become available
	 * The security layer starts non functional, each init gives it a new ability
	 */
	
	public void initRSAYou(byte[] keyDesc) throws SecurityException{
		try{
			yourPublic = descToRSAPublic(keyDesc);
			outCipher = Cipher.getInstance(RSA_STANDARD);
			outCipher.init(Cipher.ENCRYPT_MODE, yourPublic);
		}
		catch (InvalidKeyException e) {
			throw new SecurityException("RSA me Init: Invalid Key");
		}
		catch (NoSuchAlgorithmException e) {
			throw new SecurityException("RSA me Init: No Such Algorithm");
		}
		catch (NoSuchPaddingException e) {
			throw new SecurityException("RSA me Init: No Such Padding");
		}
	}
	
	public void initRSAMe(byte[] pubDesc, byte[] priDesc) throws SecurityException{
		try{
			myPublic = descToRSAPublic(pubDesc);
			myPrivate = descToRSAPrivate(priDesc);
			inCipher = Cipher.getInstance(RSA_STANDARD);
			inCipher.init(Cipher.DECRYPT_MODE, myPrivate);
		}
		catch (InvalidKeyException e) {
			throw new SecurityException("RSA me Init: Invalid Key");
		}
		catch (NoSuchAlgorithmException e) {
			throw new SecurityException("RSA me Init: No Such Algorithm");
		}
		catch (NoSuchPaddingException e) {
			throw new SecurityException("RSA me Init: No Such Padding");
		}
	}
	
	public void initAES(byte[] aesDesc, byte[] ivDesc) throws SecurityException{
		try{
			rawKey = byteClone(aesDesc);
			
			initVec = byteClone(ivDesc);
			AlgorithmParameterSpec ivs = new IvParameterSpec(initVec);
			
			aesSpec = new SecretKeySpec(rawKey, "AES");

			enc = Cipher.getInstance(AES_STANDARD);
			dec = Cipher.getInstance(AES_STANDARD);
			
			enc.init(Cipher.ENCRYPT_MODE, aesSpec, ivs);
			dec.init(Cipher.DECRYPT_MODE, aesSpec, ivs);
		}
		catch(NoSuchPaddingException exc){
			throw new SecurityException("AES Init: No Such Padding");
		}
		catch(NoSuchAlgorithmException exc){
			throw new SecurityException("AES Init: No Such Algorithm");
		}
		catch(InvalidAlgorithmParameterException exc){
			throw new SecurityException("AES Init: Invalid Algorithm Parameter");
		}
		catch(InvalidKeyException exc){
			throw new SecurityException("AES Init: Invalid Key");
		}
	}
	
	public void initMac(byte[] macDesc, boolean useAES) throws SecurityException{
		try{
			Mac hmac = Mac.getInstance(MAC_STANDARD);
			SecretKeySpec secret = new SecretKeySpec(macDesc, MAC_STANDARD);
			hmac.init(secret);
			if(useAES){
				if(aesMac != null) System.err.println("Warning: overwriting AES MAC");
				aesMac = hmac;
				aesMacRaw = byteClone(macDesc);
			}
			else{
				if(rsaMac != null) System.err.println("Warning: overwriting RSA MAC");
				rsaMac = hmac;
				rsaMacRaw = byteClone(macDesc);
			}
		}
		catch(Exception e){
			throw new SecurityException("Initializing the mac failed");
		}
	}
	
	
	/**
	 * Getters
	 * These perform an actual security function
	 * if the function hasnt been initialised, a SecurityException will be thrown
	 */
	
	public byte[] getRSAEncrypted(byte[] message) throws SecurityException{
		if(outCipher == null) throw new SecurityException("Unable to encrypt before Receiver RSA key has been initialized");
		try {
			return outCipher.doFinal(message);
		}
		catch (BadPaddingException e) {
			throw new SecurityException("RSA Encrypt: Bad Padding");
		}
		catch (IllegalBlockSizeException e) {
			throw new SecurityException("RSA Encrypt: Illegal Block Size");
		}
	}
	
	public byte[] getRSADecrypted(byte[] message) throws SecurityException{
		if(inCipher == null) {
			throw new SecurityException("Unable to decrypt before Sender RSA key has been initialized");
		}
		try {
			return inCipher.doFinal(message);
		}
		catch (BadPaddingException e) {
			throw new SecurityException("RSA Decrypt: Bad Padding");
		}
		catch (IllegalBlockSizeException e) {
			throw new SecurityException("RSA Decrypt: Illegal Block Size");
		}
	}
	
	//Note, this one does NOT automatically append the secure digest
	public byte[] getAESEncrypted(byte[] message) throws SecurityException{
		if(enc == null) throw new SecurityException("Unable to encrypt before AES key has been initialized");
		try{
			return enc.doFinal(message);
		}
		catch(BadPaddingException exc){
			throw new SecurityException("AES Encrypt: Bad Padding");
		}
		catch(IllegalBlockSizeException exc){
			throw new SecurityException("AES Encrypt: Illegal Block Size");
		}
	}
	
	//Note, this one does NOT check the message contents against its digest
	public byte[] getAESDecrypted(byte[] message) throws SecurityException{
		if(dec == null) {
			System.out.println("Not inited");
			throw new SecurityException("Unable to decrypt before AES key has been initialized");
		}
		try{
			System.out.println("decrypt");
			return dec.doFinal(message);
		}
		catch(BadPaddingException exc){
			System.out.println("Padding");
			throw new SecurityException("AES Decrypt: Bad Padding");
		}
		catch(IllegalBlockSizeException exc){
			System.out.println("Block size");
			throw new SecurityException("AES Decrypt: Illegal Block Size");
		}
	}
	
	public byte[] getMac(byte[] message, boolean useAES) throws SecurityException{
		Mac thisMac = useAES ? aesMac : rsaMac;
		if(thisMac == null) throw new SecurityException("Unable to generate MAC before the MAC password has been initialized");
		return thisMac.doFinal(message);
	}
	
	
	/**
	 * DESCRIBERS
	 * Slight variation on the getters, these give information about my known key state
	 */
	
	public byte[] descIV() throws SecurityException{
		if(initVec == null) throw new SecurityException("cannot describe IV before it is initialised");
		return byteClone(initVec);
	}
	
	public byte[] descMAC(boolean useAES) throws SecurityException{
		byte[] typ = useAES ? aesMacRaw : rsaMacRaw;
		if(typ == null) throw new SecurityException("cannot describe MAC before it is initialised");
		return byteClone(typ);
	}
	
	public byte[] descAES() throws SecurityException{
		if(rawKey == null) throw new SecurityException("cannot describe AES Key before it is initialised");
		return byteClone(rawKey);
	}
	
	public byte[] descMyPublic() throws SecurityException{
		if(myPublic == null) throw new SecurityException("cannot describe My RSA Public before it is initialised");
		return keyDesc(myPublic);
	}
	
	public byte[] descMyPrivate() throws SecurityException{
		if(myPrivate == null) throw new SecurityException("cannot describe My RSA Private before it is initialised");
		return keyDesc(myPrivate);
	}
	
	public byte[] descYourPublic() throws SecurityException{
		if(yourPublic == null) throw new SecurityException("cannot describe Your RSA Private before it is initialised");
		return keyDesc(yourPublic);
	}
	
	/**
	 * Security exception
	 * literally just so i have an exception of my own to catch
	 * Raised with warnings when something bad happens, like verification failure, decryption failure, mac failure etc
	 */
	public static class SecurityException extends IOException{
		public SecurityException(String message){
			super(message);
		}
	}
	
	/**
	 * Static methods
	 */
	
	public static byte[] byteJoin(byte[] a, byte[] b){
		byte[] ret = new byte[a.length + b.length];
		for(int i=0; i<a.length; i++) ret[i] = a[i];
		for(int i=0; i<b.length; i++) ret[i+a.length] = b[i];
		return ret;
	}
	
	public static void byteSplit(byte[] in, byte[] a, byte[] b){
		for(int i=0; i<in.length; i++){
			if(i < a.length){
				a[i] = in[i];
			}
			else if(i < a.length + b.length){
				b[i-a.length] = in[i];
			}
			else{
				break;
			}
		}
	}
	
	public static RSAPrivateKey descToRSAPrivate(byte[] keyDesc) throws SecurityException{
		try{
			KeyFactory fac = KeyFactory.getInstance("RSA");
			String keyMessage = btos(keyDesc);//modulus,space,exponent
			String[] parts = keyMessage.split(" ");
			RSAPrivateKeySpec npk = new RSAPrivateKeySpec(new BigInteger(parts[0]), new BigInteger(parts[1]));
			return (RSAPrivateKey)fac.generatePrivate(npk);
		}
		catch(NoSuchAlgorithmException exc){
			throw new SecurityException("RSA Private Desc: No Such Algorithm");
		}
		catch (InvalidKeySpecException e) {
			throw new SecurityException("RSA Private Desc: Invalid Key Spec");
		}
	} 
	
	public static RSAPublicKey descToRSAPublic(byte[] keyDesc) throws SecurityException{
		try{
			KeyFactory fac = KeyFactory.getInstance("RSA");
			String keyMessage = btos(keyDesc);//modulus,space,exponent
			String[] parts = keyMessage.split(" ");
			RSAPublicKeySpec npk = new RSAPublicKeySpec(new BigInteger(parts[0]), new BigInteger(parts[1]));
			return (RSAPublicKey)fac.generatePublic(npk);
		}
		catch(NoSuchAlgorithmException exc){
			throw new SecurityException("RSA Public Desc: No Such Algorithm");
		}
		catch (InvalidKeySpecException e) {
			throw new SecurityException("RSA Public Desc: Invalid Key Spec");
		}
	}
	
	public static byte[] keyDesc(RSAPublicKey pub){
		BigInteger ex = pub.getPublicExponent();
		BigInteger mo = pub.getModulus();
		String tmp = mo.toString() + " " + ex.toString();
		return stob(tmp);
	}
	
	public static byte[] keyDesc(RSAPrivateKey pub){
		BigInteger ex = pub.getPrivateExponent();
		BigInteger mo = pub.getModulus();
		String tmp = mo.toString() + " " + ex.toString();
		return stob(tmp);
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
	
}