import java.io.*;


public class OpenSSLCommander extends Secureify {
	
	private final String sslCommand;
	private final char[] password;
	private final boolean salting;
	private final String algorithm;
	
	public static OpenSSLCommander getCommander(File checkFile, char[] pass, String openSSLCom){
		return getCommander(checkFile, pass, openSSLCom, false, "aes-256-cbc");
	}
	
	public static OpenSSLCommander getCommander(File checkFile, char[] pass, String openSSLCom, boolean salting, String algorithm){
		OpenSSLCommander sec = new OpenSSLCommander(pass, openSSLCom, salting, algorithm);
		if(checkFile == null || sec.check(checkFile)) return sec;
		return null;
	}
	
	private OpenSSLCommander(char[] pass, String openSSLCom, boolean salt, String algo){
		password = pass;
		sslCommand = openSSLCom;
		algorithm = algo;
		salting = salt;
	}

	@Override
	public boolean check(File checkFile) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File encryptFile(File in, boolean encrypt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encryptString(String in, boolean encrypt) {
		String command = sslCommand + " enc -" + algorithm + " -k " + new String(password) + " -a";
		if(!salting) command += " -nosalt";
		if(!encrypt){
			command += " -d";
			in = in.replaceAll("\\.", "=").replaceAll("-", "+");
			in.replaceAll("-", "+");
			in.replaceAll("-", "+");
		}
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.getOutputStream().write(in.getBytes());
			if(!encrypt) p.getOutputStream().write("\n".getBytes());
			p.getOutputStream().close();
			byte[] buff = new byte[2048];
			int rd = 0;
			while(true){
				int read = p.getInputStream().read(buff, rd, buff.length-rd);
				if(read <= 0) break;
				rd += read;
			}
			String ret = new String(buff, 0, rd-(encrypt ? 1 : 0));
			if(encrypt){
				
			}
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
