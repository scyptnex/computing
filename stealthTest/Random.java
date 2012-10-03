import java.security.*;

public class Random {
	
	public static void main(String[] args) throws Exception{
		Provider[] p = Security.getProviders();
		for(Provider prov : p){
			System.out.println(prov.getName());
			for(Provider.Service ps : prov.getServices()){
				System.out.println("\t" + ps.getAlgorithm());
			}
		}
		byte[] bts = new byte[4];
		
		SecureRandom secr = SecureRandom.getInstance("SHA1PRNG");
		SecureRandom.getInstance("SHA1PRNG").nextBytes(bts);
		System.out.println(hexify(bts));
		byte[] seed = "test seed for the datas".getBytes();//SecureRandom.getInstance("NativePRNG").generateSeed(20);
		secr.setSeed(seed);
		secr.nextBytes(bts);
		System.out.println(hexify(bts));
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
	
}