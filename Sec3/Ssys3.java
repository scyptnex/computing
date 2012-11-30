import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

public class Ssys3 {
	
	public static final int NUM_THREADS = 1;
	
	public static final int KILOBYTE = 1024;
	
	public static final int TXA_WIDTH = 15;
	public static final int TXA_HEIGHT = 13;
	
	public static final char IMPORT_FLAG = 'I';
	public static final char EXPORT_FLAG = 'X';
	
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
	private LinkedList<String> jobs;
	private EDT[] encryptDecryptThreads;
	
	public final Secureify sec;
	public final Storage store;
	public final TableRowSorter<Storage> tableSorter;
	
	public static void main(String[] args){
		new Ssys3();
	}
	
	public Ssys3(){
		store = new Storage();
		jobs = new LinkedList<String>();
		encryptDecryptThreads = new EDT[NUM_THREADS];
		for(EDT edt : encryptDecryptThreads) edt.start();
		tableSorter = new TableRowSorter<Storage>(store);
		tempLoc = new File("temp");
		if(!tempLoc.exists()) tempLoc.mkdirs();
		
		makeGUI();
		frm.setSize(800, 600);
		frm.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent evt) {}
			public void windowClosed(WindowEvent evt) {
				try {
					System.out.println("joining EDT's");
					for(EDT edt : encryptDecryptThreads){
						edt.weakStop();
						try {
							edt.join();
							System.out.println("  - joined");
						} catch (InterruptedException e) {
							System.out.println("  - Not joined");
						}
					}
					System.out.println("saving storage");
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
	
	public String jobString(String job){
		if(job.charAt(0) == IMPORT_FLAG){
			String[] brk = job.split(",", 4);//4 parts to an import string
			File if = new File()
		}
		else{
			//TODO export
		}
	}
	
	public void updateStatus(){
		txaStatus.setText("");
		txaStatus.append("Threads:");
		for(int i=0; i<NUM_THREADS; i++){
			txaStatus.append(" " + i + "- ");
			String jb = encryptDecryptThreads[i].getCur();
			if(jb == null) txaStatus.append("idle\n");
			else{
				txaStatus.append(jobString(jb) + "\n");
			}
		}
		for(String s : jobs) txaStatus.append(jobString(s) + "\n");
	}
	
	public String impJob(File fi, String date, String tags){
		if(date == null) date = Storage.curDate();
		if(tags == null) tags = Storage.NEW_TAG;
		return IMPORT_FLAG + "," + fi.getAbsolutePath() + "," + date + "," + tags;
	}
	
	public void secureImport(){
		JFileChooser imp = new JFileChooser();
		imp.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		imp.setMultiSelectionEnabled(true);
		int ret = imp.showOpenDialog(frm);
		if(ret != JFileChooser.APPROVE_OPTION){
			return;
		}
		File[] fis = imp.getSelectedFiles();
		ArrayList<String> impJobs = new ArrayList<String>();
		boolean dirs = false;
		for(File fi : fis){
			if(fi.isDirectory()){
				dirs = true;
				File lib = new File(fi, LIBRARY_NAME);
				if(lib.exists()){
					try{
						Scanner sca = new Scanner(lib);
						while(sca.hasNextLine()){
							String nm = sca.nextLine();
							String date = sca.nextLine();
							String tags = sca.nextLine();
							File addr = new File(fi, nm);
							if(addr.exists() && !addr.isDirectory()) impJobs.add(impJob(addr, date, tags));
						}
						sca.close();
					}
					catch(IOException exc){
						//add nothing?
					}
				}
				else{
					for(File cont : fi.listFiles()) if(!cont.isDirectory()) impJobs.add(impJob(cont, null, null));
				}
			}
			else{
				impJobs.add(impJob(fi, null, null));
			}
		}
		if(impJobs.size() > 1 || dirs){//dont bother user if selected single file
			String shw = "Importing:";
			if(impJobs.size() > 30) shw = null;
			int pcount = 0;
			for(String jb : impJobs){
				String[] prts = jb.split(",", 4);//import jobs have 4 parts, import, name, date, tags
				if(shw != null) shw = shw + "\n  - " + new File(prts[1]).getName();
				if(!prts[3].equalsIgnoreCase(Storage.NEW_TAG)){
					pcount++;
					if(shw != null) shw = shw + " []";
				}
			}
			if(shw == null) shw = "importing ";
			else shw = shw + "\n";
			shw = shw + impJobs.size() + "(" + pcount + ") files";
			if(JOptionPane.showConfirmDialog(frm, shw, "Confirm Import", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
		}
		synchronized(jobs){
			for(String j : impJobs) jobs.addFirst(j);
		}
		updateStatus();
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
	
	
	/**
	 * Helper thread
	 */
	
	public class EDT extends Thread{
		private boolean going = true;
		private String cur;
		public String getCur(){
			if(cur == null) return null;
			return cur;
		}
		public void weakStop(){
			going = false;
		}
		
		public boolean edtImport(File fi, String date, String tags){
			if(!fi.exists()){
				System.err.println("import: file " + fi.getAbsolutePath() + " doesnt exist");
				return false;
			}
			long size = fi.length()/KILOBYTE;
			File save = sec.encryptMainFile(fi, storeLocs.get(0), true);
			if(save == null){
				System.err.println("import: Encryption failure");
				return false;
			}
			String pname = fi.getName();
			if(!fi.delete()){
				System.err.println("import: Couldnt delete old file - continuing");
			}
			store.add(save.getName(), pname, date, size, tags, 0);
			return true;
		}
		
		public void run(){
			going = true;
			while(going){
				String chose = null;
				synchronized(jobs){
					chose = jobs.poll();
				}
				if(chose != null){
					cur = chose;
					updateStatus();
					if(chose.charAt(0) == IMPORT_FLAG){
						String[] brk = chose.split(",", 4);//4 parts to an import string
						if(edtImport(new File(brk[1]), brk[2], brk[3])){
							//import successful
						}
						else{
							//no import
						}
					}
					else{
						//TODO export
					}
					cur = null;
					updateStatus();
				}
				else{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						//do nothing
					}
				}
			}
		}
	}
}
