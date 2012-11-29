import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

public class Ssys3 {
	
	public static final int TXA_WIDTH = 15;
	public static final int TXA_HEIGHT = 13;
	
	public static final String LIBRARY_NAME = "zzlib.dat";
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
	private JTextArea txaStatus;
	private JTextArea txaSearch;
	private JTextArea txaInfo;
	
	private ArrayList<File> storeLocs;
	private final File tempLoc;
	
	public final Secureify sec;
	public final Storage store;
	public final TableRowSorter<Storage> tableSorter;
	
	public static void main(String[] args){
		new Ssys3();
	}
	
	public Ssys3(){
		store = new Storage();
		tableSorter = new TableRowSorter<Storage>(store);
		tempLoc = new File("temp");
		if(!tempLoc.exists()) tempLoc.mkdirs();
		
		makeGUI();
		frm.setSize(800, 600);
		frm.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent evt) {}
			public void windowClosed(WindowEvent evt) {
				try {
					store.saveAll(storeLocs, tempLoc, sec);
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\nFailed to save properly\n\n!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.exit(1);
				}
				clean();
				System.exit(0);
			}
			public void windowClosing(WindowEvent evt) {
				windowClosed(evt);
			}
			public void windowDeactivated(WindowEvent evt) {}
			public void windowDeiconified(WindowEvent evt) {}
			public void windowIconified(WindowEvent evt) {}
			public void windowOpened(WindowEvent evt) {}
		});
		frm.setVisible(true);
		//lockGUI();
		
		//TODO load config
		storeLocs = new ArrayList<File>();
		String ossl = "openssl";
		storeLocs.add(new File("store2"));
		storeLocs.add(new File("store"));
		
		File chk = null;
		for(File fi : storeLocs){
			File lib = new File(fi, LIBRARY_NAME);
			if(lib.exists()){
				chk = lib;
				//break;
			}
		}
		//TODO if no libraries, ask for new pass
		char[] pss = "hi".toCharArray();
		sec = OpenSSLCommander.getCommander(chk, pss, ossl);
		if(sec == null){
			System.err.println("Wrong Password");
			System.exit(1);
		}
		
		//load stores
		try {
			store.loadAll(storeLocs, tempLoc, sec);
		} catch (IOException e) {
			System.err.println("Storage loading failure");
			System.exit(1);
		}
		/**int idx = 0;
		for(File str : storeLocs){
			if(!str.exists()) str.mkdirs();
			else{
				File lib = new File(str, LIBRARY_NAME);
				if(lib.exists()) loadLib(lib, store, idx);
			}
			idx++;
		}**/
	}
	
	public void secureImport(){
		JFileChooser imp = new JFileChooser();
		imp.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int ret = imp.showOpenDialog(frm);
		if(ret != JFileChooser.APPROVE_OPTION){
			return;
		}
		File fi = imp.getSelectedFile();
		System.out.println("import not done: " + fi.getAbsolutePath());
		//TODO working on importing files
	}
	
	public void secureMove(){
		System.out.println("move not done");
	}
	
	public void secureDelete(){
		System.out.println("delete not done");
	}
	
	public void loadLib(File lib, Storage store, int idx){
		//TODO load library
		store.add(lib.getName(), lib.getAbsolutePath(), "2012-11-28", (long)0, sec.encryptString(lib.getAbsolutePath(), true), idx);
	}
	
	public void makeGUI(){
		frm = new JFrame();
		c = frm.getContentPane();
		
		btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				secureImport();
			}
		});
		btnMove = new JButton("Move");
		btnMove.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				secureMove();
			}
		});
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				secureDelete();
			}
		});
		
		tblItems = new JTable(store);
		tblItems.setRowSorter(tableSorter);
		tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblItems.setFillsViewportHeight(true);
		
		txaStatus = new JTextArea(TXA_HEIGHT, TXA_WIDTH);
		txaStatus.setEditable(false);
		txaStatus.setBorder(BorderFactory.createTitledBorder("Status"));
		txaSearch = new JTextArea(4, TXA_WIDTH);
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
		txaInfo = new JTextArea(TXA_HEIGHT, TXA_WIDTH);
		txaInfo.setEditable(false);
		txaInfo.setBorder(BorderFactory.createTitledBorder("Info"));
		
		JPanel pnlTop = new JPanel(new GridLayout(1, 3));
		JPanel pnlEast = new JPanel(new BorderLayout());
		JPanel pnlCenterEast = new JPanel(new BorderLayout());
		JScrollPane jspItems = new JScrollPane(tblItems);
		
		pnlTop.add(btnImport);
		pnlTop.add(btnMove);
		pnlTop.add(btnDelete);
		pnlCenterEast.add(txaStatus, BorderLayout.NORTH);
		pnlCenterEast.add(txaSearch, BorderLayout.CENTER);
		pnlCenterEast.add(txaInfo, BorderLayout.SOUTH);
		//pnlEast.add(pswPass, BorderLayout.NORTH);
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
	
	public static boolean rmrf(File fi){
		if(fi.isDirectory()){
			boolean done = true;
			for(File subfi : fi.listFiles()){
				if(!rmrf(subfi)){
					done = false;
				}
			}
			if(!done) return false;
			return fi.delete();
		}
		else{
			boolean done = true;
			for(int i=0; i<10; i++){
				done = fi.delete();
				if(done) break;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//do nothing
				}
			}
			return done;
		}
	}
	
	public void clean(){
		rmrf(tempLoc);
	}
	
	public char[] jetPass(){
		JPasswordField psf = new JPasswordField();
		psf.grabFocus();
		int opt = JOptionPane.showConfirmDialog(frm, psf, "Password", JOptionPane.PLAIN_MESSAGE);
		if(opt == JOptionPane.OK_OPTION) return psf.getPassword();
		return null;
	}
	
	public String jetString(String msg, String ttl){
		return JOptionPane.showInputDialog(frm, msg, ttl, JOptionPane.QUESTION_MESSAGE);
	}
	
}
