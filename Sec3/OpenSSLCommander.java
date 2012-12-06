import java.io.*;
import java.util.*;

public class OpenSSLCommander extends Secureify {
	
	private final String sslCommand;
	//private final char[] password;
	private final byte[] passwordBytes;
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
		//password = pass;
		passwordBytes = new String(pass).getBytes();
		sslCommand = openSSLCom;
		algorithm = algo;
		salting = salt;
	}

	@Override
	public boolean check(File checkFile) {
		File blob = new File("check");
		File esf = encryptSpecialFile(checkFile, blob, false);
		boolean ret = true;
		if(esf == null) ret = false;
		blob.delete();
		return ret;
	}

	@Override
	public File encryptSpecialFile(File in, File out, boolean encrypt) {
		//File out = new File(store, outName);
		ArrayList<String> command = new ArrayList<String>();
		command.add(sslCommand);
		command.add("enc");
		command.add("-" + algorithm);
		command.add("-pass");
		command.add("stdin");
		command.add("-in");
		command.add(commandLineFile(in.getAbsolutePath()));
		command.add("-out");
		command.add(commandLineFile(out.getAbsolutePath()));
		//String command = sslCommand + " enc -" + algorithm + " -pass stdin -in " + commandLineFile(in.getAbsolutePath()) + " -out " + commandLineFile(out.getAbsolutePath());
		if(!salting) command.add("-nosalt");
		if(!encrypt) command.add("-d");
		try {
			Process p = Runtime.getRuntime().exec(command.toArray(new String[0]));
			auth(p.getOutputStream());
			p.getOutputStream().close();
			boolean success = checkSuccess(p);
			return (success ? out : null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void auth(OutputStream os) throws IOException{
		//password, return, (password, return)
		os.write(passwordBytes);
		os.write("\n".getBytes());
	}
	
	private boolean checkSuccess(Process p) throws IOException, InterruptedException{
		p.waitFor();
		int ret = p.exitValue();
		if(ret == 0) return true;
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		String ln = null;
		while((ln = br.readLine()) != null) System.err.println("OSSL E: " + ln);
		return false;
	}

	@Override
	public String encryptString(String in, boolean encrypt) {
		String command = sslCommand + " enc -" + algorithm + " -pass stdin -a";
		if(!salting) command += " -nosalt";
		if(!encrypt){
			command += " -d";
			in = in.replaceAll("\\.", "=").replaceAll("-", "+").replaceAll("_", "/");
		}
		try {
			Process p = Runtime.getRuntime().exec(command);
			auth(p.getOutputStream());
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
				ret = ret.replaceAll("=", ".").replaceAll("\\+", "-").replaceAll("/", "_");
			}
			boolean success = checkSuccess(p);
			return (success ? ret : null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String commandLineFile(String abs){
		return abs;
	}
	
	public static String test(String command){
		if(!command.contains("open") || !command.contains("ssl")) return null;
		try{
			Process p = Runtime.getRuntime().exec(command + " version");
			String ret = "";
			String ers = "";
			String ln = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while((ln = br.readLine()) != null) ret = ret + ln + "\n";
			while((ln = er.readLine()) != null) ers = ers + ln + "\n";
			br.close();
			er.close();
			if(ers.length() > 0) return null;
			return ret.trim();
		}
		catch(Exception e){
			return null;
		}
		
	}
}
