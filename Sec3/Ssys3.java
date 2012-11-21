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
		sec = OpenSSLCommander.getCommander(null, "hi".toCharArray(), sslX);
		String enc = sec.encryptString("test", true);
		System.out.println(enc);
		String dec = sec.encryptString(enc, false);
		System.out.println(dec);
	}
	
}
