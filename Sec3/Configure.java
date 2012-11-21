import java.io.*;
import java.util.*;


public class Configure {
	
	public final String[] options;
	public final String[] values;
	public final boolean isValid;
	
	public static Configure getConfig(File confi, String[] conOpts, String[] conDefaults){
		try{
			Configure c = new Configure(confi, conOpts, conDefaults);
			if(c.isValid) return c;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.err.println("Improper configuration, see " + confi.getName());
		return null;
	}
	
	private Configure(File confi, String[] conOpts, String[] conDefaults) throws IOException{
		options = conOpts;
		if(!confi.exists()) makeConf(confi, conDefaults);
		values = new String[conOpts.length];
		for(int i=0; i<conOpts.length; i++){
			values[i] = conDefaults[i];
		}
		getConf(confi);
		boolean  vald = true;
		for(int i=0; i<conOpts.length; i++) if(values[i] == null){
			System.err.println("Option " + options[i] + " not set");
			vald = false;
		}
		isValid = vald;
	}
	
	public void getConf(File confi) throws IOException{
		Scanner sca = new Scanner(confi);
		while(sca.hasNextLine()){
			String line = sca.nextLine();
			String opt = line.substring(0, line.indexOf("="));
			String val = line.substring(line.indexOf("=") + 1);
			if(val.length() > 0) for(int i=0; i<options.length; i++) if(options[i].equalsIgnoreCase(opt)){
				values[i] = val;
			}
		}
	}
	
	public void makeConf(File confi, String[] defaults) throws IOException{
		PrintWriter pw = new PrintWriter(confi);
		for(int i=0; i<options.length; i++) pw.println(options[i].toUpperCase() + "=" + (defaults[i] == null ? "" : defaults[i]));
		pw.close();
	}
	
}
