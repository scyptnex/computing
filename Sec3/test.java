import java.io.*;
import javax.swing.*;
public class test {
	
	public static void main(String[] args) throws Exception{
		File tst = new File(".");
		JOptionPane.showMessageDialog(null, tst.getAbsolutePath());
		String cmd = "openssl enc -aes-256-cbc -nosalt -in plain -out cipher -pass stdin";
		String[] cmds = cmd.split(" ");
		Process p = Runtime.getRuntime().exec(cmds);
		OutputStream in = p.getOutputStream();
		in.write("a\n".getBytes());
		in.close();
		p.waitFor();
	}
}
