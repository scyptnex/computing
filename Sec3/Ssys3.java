import java.util.*;
import java.io.*;

public class Ssys3 {
	
	public static final File CONF_FILE = new File("ssys3.cfg");
	public static final String[] CONF_OPTS = {"secure_directories", "openssl_executable", "use_player", "player_executable"};
	public static final String[] CONF_DEFAULTS = {"store", null, "false", "vlc"};
	
	Secureify sec;
	
	public static void main(String[] args){
		Configure cfg = Configure.getConfig(CONF_FILE, CONF_OPTS, CONF_DEFAULTS);
		if(cfg != null) new Ssys3(cfg.values[0].split(";"), cfg.values[2].equalsIgnoreCase("true"), cfg.values[1], cfg.values[3]);
	}
	
	public Ssys3(String[] storeDirs, boolean usePlayer, String sslX, String playX){
		for(int i=0; i<storeDirs.length; i++){
			File sd = new File(storeDirs[i]);
			if(!sd.exists()) sd.mkdirs();
		}
		sec = OpenSSLCommander.getCommander(null, "hi".toCharArray(), sslX);
		/**for(int i=0; i<1; i++){
			String wrd = i + "";
			String enc = sec.encryptString(wrd, true);
			String dec = sec.encryptString(enc, false);
			System.out.println(wrd + " -> " + enc + " -> " + dec);
		}**/
		File encry = sec.encryptFile(new File("test.txt"), new File("."), true);
		System.out.println(encry.getAbsolutePath());
		File decry = sec.encryptFile(encry, new File(storeDirs[0]), false);
	}
	
}
