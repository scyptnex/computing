import java.io.*;


public class OpenSSLCommander extends Secureify {
	
	private final String sslCommand;
	private final char[] password;
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
		password = pass;
		passwordBytes = new String(pass).getBytes();
		sslCommand = openSSLCom;
		algorithm = algo;
		salting = salt;
	}

	@Override
	public boolean check(File checkFile) {
		return encryptString(checkFile.getName(), false) != null;
	}

	@Override
	public File encryptSpecialFile(File in, File out, boolean encrypt) {
	//	File out = new File(store, outName);
		String command = sslCommand + " enc -" + algorithm + " -a -in \"" + in.getAbsolutePath() + "\" -out \"" + out.getAbsolutePath() + "\"";
		if(!salting) command += " -nosalt";
		if(!encrypt) command += " -d";
		System.out.println(command);
		try {
			Process p = Runtime.getRuntime().exec(command);
			auth(p.getOutputStream(), encrypt);
			p.getOutputStream().close();
			boolean success = checkSuccess(p.getErrorStream(), encrypt);
			p.waitFor();
			return (success ? out : null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void auth(OutputStream os, boolean encrypt) throws IOException{
		//password, return, (password, return)
		os.write(passwordBytes);
		os.write("\n".getBytes());
		if(encrypt){
			os.write(passwordBytes);
			os.write("\n".getBytes());
		}
	}
	
	private boolean checkSuccess(InputStream err, boolean encrypt) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(err));
		//TODO output all lines
		br.readLine();
		if(encrypt) br.readLine();
		String ln = br.readLine();
		System.out.println(ln);
		return (ln == null);
	}

	@Override
	public String encryptString(String in, boolean encrypt) {
		String command = sslCommand + " enc -" + algorithm + " -a";
		if(!salting) command += " -nosalt";
		if(!encrypt){
			command += " -d";
			in = in.replaceAll("\\.", "=").replaceAll("-", "+").replaceAll("_", "/");
		}
		try {
			Process p = Runtime.getRuntime().exec(command);
			auth(p.getOutputStream(), encrypt);
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
			boolean success = checkSuccess(p.getErrorStream(), encrypt);
			p.waitFor();
			return (success ? ret : null);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
