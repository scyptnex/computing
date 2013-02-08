import java.awt.Desktop;
import java.io.*;
import javax.swing.*;

public class FileOpen {
	
	public static void main(String[] args) throws Exception{
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(null);
		File fi = jfc.getSelectedFile();
		Desktop.getDesktop().open(fi);
	}
	
}