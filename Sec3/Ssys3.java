import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

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
	
	public final Secureify sec;
	public final Storage store;
	public final TableRowSorter<Storage> tableSorter;
	
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
		//TODO sec = OpenSSLCommander.getCommander(null, "hi".toCharArray(), sslX);
		sec = null;
		store = new Storage();
		store.testFill();
		tableSorter = new TableRowSorter<Storage>(store);
		makeGUI();
		frm.setSize(800, 600);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
		for(int i=0; i<storeDirs.length; i++){
			File sd = new File(storeDirs[i]);
			if(!sd.exists()) sd.mkdirs();
		}
	}
	
	public void makeGUI(){
		frm = new JFrame();
		c = frm.getContentPane();
		
		btnImport = new JButton("Import");
		btnMove = new JButton("Move");
		btnDelete = new JButton("Delete");
		
		tblItems = new JTable(store);
		tblItems.setRowSorter(tableSorter);
		tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblItems.setFillsViewportHeight(true);
		
		pswPass = new JPasswordField(20);
		pswPass.setBorder(BorderFactory.createTitledBorder("Password"));
		
		txaStatus = new JTextArea();
		txaStatus.setEditable(false);
		txaStatus.setBorder(BorderFactory.createTitledBorder("Status"));
		txaSearch = new JTextArea();
		txaSearch.setBorder(BorderFactory.createTitledBorder("Search"));
		txaSearch.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent arg0) {
				filterBase(txaSearch.getText());
			}
			public void keyTyped(KeyEvent arg0) {
			}
		});
		txaInfo = new JTextArea();
		txaInfo.setEditable(false);
		txaInfo.setBorder(BorderFactory.createTitledBorder("Info"));
		
		JPanel pnlTop = new JPanel(new GridLayout(1, 3));
		JPanel pnlEast = new JPanel(new BorderLayout());
		JPanel pnlCenterEast = new JPanel(new GridLayout(3, 1));
		JScrollPane jspItems = new JScrollPane(tblItems);
		
		pnlTop.add(btnImport);
		pnlTop.add(btnMove);
		pnlTop.add(btnDelete);
		pnlCenterEast.add(txaStatus);
		pnlCenterEast.add(txaSearch);
		pnlCenterEast.add(txaInfo);
		pnlEast.add(pswPass, BorderLayout.NORTH);
		pnlEast.add(pnlCenterEast, BorderLayout.CENTER);
		c.setLayout(new BorderLayout());
		c.add(pnlTop, BorderLayout.NORTH);
		c.add(pnlEast, BorderLayout.EAST);
		c.add(jspItems, BorderLayout.CENTER);
		frm.setContentPane(c);
	}
	
	public void filterBase(String ft){
		if(ft.contains("-i")){
			RowFilter<Storage, Object> tmpFilter = null;
			try{
				tmpFilter = RowFilter.regexFilter("^new$", Storage.COL_TAGS);
			}
			catch(Exception e){
				System.err.println("Failed to filter new ones");
			}
			tableSorter.setRowFilter(tmpFilter);
			return;
		}
		
		boolean names = false;
		boolean bads = false;
		boolean exclusive = true;
		if(ft.contains("-n")){
			names = true;
			ft = ft.substring(0, ft.indexOf("-n")) + ft.substring(ft.indexOf("-n") + 2);
		}
		if(ft.contains("-z")){
			bads = true;
			ft = ft.substring(0, ft.indexOf("-z")) + ft.substring(ft.indexOf("-z") + 2);
		}
		if(ft.contains("-x")){
			exclusive = false;
			ft = ft.substring(0, ft.indexOf("-x")) + ft.substring(ft.indexOf("-x") + 2);
		}
		
		ft = ft.replaceAll("[^a-zA-Z0-9]", " ").replaceAll("  *", " ").trim().toLowerCase();
		String[] terms = ft.split(" ");
		ArrayList<RowFilter<Storage, Object>> termFilters = new ArrayList<RowFilter<Storage, Object>>();
		for(int i=0; i<terms.length; i++){
			if(terms[i].length() > 0){
				try{
					RowFilter<Storage, Object> tmp = RowFilter.regexFilter(".*" + terms[i] + ".*", Storage.COL_TAGS);  
					if(names) tmp = RowFilter.regexFilter(".*" + terms[i] + ".*", Storage.COL_NAME);
					termFilters.add(tmp);
				}
				catch(Exception e){
					//do nothing
					System.err.println("Term filter error for term " + terms[i]);
				}
			}
		}
		
		RowFilter<Storage, Object> badFilter = RowFilter.regexFilter(".*zz.*", Storage.COL_TAGS);
		if(!bads) badFilter = RowFilter.notFilter(badFilter);
		
		RowFilter<Storage, Object> omniFilter = badFilter;
		if(termFilters.size() != 0){
			ArrayList<RowFilter<Storage, Object>> tmpFilters = new ArrayList<RowFilter<Storage, Object>>();
			RowFilter<Storage, Object> orFilter = (exclusive ? RowFilter.andFilter(termFilters) : RowFilter.orFilter(termFilters));
			tmpFilters.add(orFilter);
			tmpFilters.add(badFilter);
			omniFilter = RowFilter.andFilter(tmpFilters);
		}
		
		tableSorter.setRowFilter(omniFilter);
	}
	
}
