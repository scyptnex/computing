import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class Ssys3 {
	
	public static final File CONF_FILE = new File("ssys3.cfg");
	public static final String[] CONF_OPTS = {"secure_directories", "openssl_executable", "use_player", "player_executable"};
	public static final String[] CONF_DEFAULTS = {"store", null, "false", "vlc"};
	
	/**
	 * GUI
	 */
	private JFrame frm;
	private Container c;
	private JTable tblItems;
	private JButton btnImport;
	private JButton btnMove;
	private JButton btnDelete;
	private JPasswordField pswPass;
	private JTextArea txaStatus;
	private JTextArea txaSearch;
	private JTextArea txaInfo;
	
	Secureify sec;
	
	public static void main(String[] args){
		//Configure cfg = Configure.getConfig(CONF_FILE, CONF_OPTS, CONF_DEFAULTS);
		//if(cfg != null) new Ssys3(cfg.values[0].split(";"), cfg.values[2].equalsIgnoreCase("true"), cfg.values[1], cfg.values[3]);
		String osslLoc = null;
		String playerLoc = null;
		ArrayList<String> stores = new ArrayList<String>();
		if(CONF_FILE.exists()){
			try{
				Scanner sca = new Scanner(CONF_FILE);
				osslLoc = sca.nextLine();
				playerLoc = sca.nextLine();
				while(sca.hasNextLine()) stores.add(sca.nextLine());
				sca.close();
			}
			catch(IOException e){
				
			}
		}
		if(osslLoc == null || stores.size() == 0){
			//TODO io default methods
			osslLoc = "openssl";
			playerLoc = "vlc";
			stores.add("store");
			//TODO write initial config file
		}
		new Ssys3(stores.toArray(new String[0]), osslLoc, playerLoc);
	}
	
	public Ssys3(String[] storeDirs, String sslX, String playX){
		makeGUI();
		frm.setSize(800, 600);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
		for(int i=0; i<storeDirs.length; i++){
			File sd = new File(storeDirs[i]);
			if(!sd.exists()) sd.mkdirs();
		}
		//TODO sec = OpenSSLCommander.getCommander(null, "hi".toCharArray(), sslX);
	}
	
	public void makeGUI(){
		frm = new JFrame();
		c = frm.getContentPane();
		
		btnImport = new JButton("Import");
		btnMove = new JButton("Move");
		btnDelete = new JButton("Delete");
		
		tblItems = new JTable();
		
		pswPass = new JPasswordField();
		
		JPanel pnlTop = new JPanel(new GridLayout());
		JPanel pnlEast = new JPanel(new BorderLayout());
		JPanel pnlCenterEast = new JPanel(new GridLayout());
		
		frm.setContentPane(c);
	}
	
}
