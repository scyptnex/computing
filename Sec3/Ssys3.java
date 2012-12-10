import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

public class Ssys3 {
	
	//TODO automatic save, delete-list, ximport (lost import)
	
	public static final int KILOBYTE = 1024;
	
	public static final int TXA_WIDTH = 15;
	public static final int TXA_HEIGHT = 29;
	
	public static final char IMPORT_FLAG = 'I';
	public static final char EXPORT_FLAG = 'X';
	
	public static final String LIBRARY_NAME = "zzlib.dat";
	
	public static final File CONF_FILE = new File("ssys3.cfg");
	public static final String CONF_SSL = "Open_SSL_Command = ";
	public static final String CONF_THREAD = "Worker_Thread_Count = ";
	public static final String CONF_PRIORITY = "Prioritise_Decryption = ";
	public static final String CONF_EXPORT = "Allow_Export = ";
	public static final String CONF_CONFIRM = "Check_Imports = ";
	public static final String CONF_STORE = "Storage_Locations:";
	
	/**
	 * GUI
	 */
	private JFrame frm;
	private Container c;
	private JTable tblItems;
	private JButton btnImport;
	private JButton btnMove;
	private JButton btnDelete;
	private JButton btnAnalyse;
	private JTextArea txaStatus;
	private JTextArea txaSearch;
	
	private ArrayList<File> storeLocs;
	private final File tempLoc;
	private LinkedList<String> jobs;
	private EDT[] encryptDecryptThreads;
	
	public final Secureify sec;
	public final Storage store;
	public final TableRowSorter<Storage> tableSorter;
	
	/**
	 * Guide to adding configuration
	 *  - Add the configure variable here
	 *  - Add a config flag CONF_XXXXXX above
	 *  - Modify the config reader to read the config option into a temporary variable
	 *  - Set the configure variable from the temp
	 *  - Modify the configure writer to save the option
	 */
	public final int numThreads;
	public final boolean priorityExport;
	public final boolean allowExport;
	public final boolean checkImports;
	
	public static void main(String[] args){
		new Ssys3();
	}
	
	public Ssys3(){
		store = new Storage();
		tableSorter = new TableRowSorter<Storage>(store);
		jobs = new LinkedList<String>();
		
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
					store.saveAll(tempLoc);
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
		
		//load config
		storeLocs = new ArrayList<File>();
		String ossl = "openssl";
		int numThreadTemp = 2;
		boolean priorityDecryptTemp = true;
		boolean allowExportTemp = false;
		boolean checkImportTemp = true;
		try{
			Scanner sca = new Scanner(CONF_FILE);
			while(sca.hasNextLine()){
				String ln = sca.nextLine();
				if(ln.startsWith(CONF_SSL)){
					ossl = ln.substring(CONF_SSL.length());
				}
				else if(ln.startsWith(CONF_THREAD)){
					try{
						numThreadTemp = Integer.parseInt(ln.substring(CONF_THREAD.length()));
					}
					catch(Exception exc){
						//do Nothing
					}
				}
				else if(ln.equals(CONF_STORE)){
					while(sca.hasNextLine()) storeLocs.add(new File(sca.nextLine()));
				}
				else if(ln.startsWith(CONF_PRIORITY)){
					try{
						priorityDecryptTemp = Boolean.parseBoolean(ln.substring(CONF_PRIORITY.length()));
					}
					catch(Exception exc){
						//do Nothing
					}
				}
				else if(ln.startsWith(CONF_EXPORT)){
					try{
						allowExportTemp = Boolean.parseBoolean(ln.substring(CONF_EXPORT.length()));
					}
					catch(Exception exc){
						//do Nothing
					}
				}
				else if(ln.startsWith(CONF_CONFIRM)){
					try{
						checkImportTemp = Boolean.parseBoolean(ln.substring(CONF_CONFIRM.length()));
					}
					catch(Exception exc){
						//do Nothing
					}
				}
			}
			sca.close();
		}
		catch(IOException e){
			
		}
		String osslWorks = OpenSSLCommander.test(ossl);
		while(osslWorks == null){
			ossl = JOptionPane.showInputDialog(frm, "Please input the command used to run open ssl\n  We will run \"<command> version\" to confirm\n  Previous command: " + ossl, "Find open ssl", JOptionPane.OK_CANCEL_OPTION);
			if(ossl == null){
				System.err.println("Refused to provide openssl executable location");
				System.exit(1);
			}
			osslWorks = OpenSSLCommander.test(ossl);
			if(osslWorks == null) JOptionPane.showMessageDialog(frm, "Command " + ossl + " unsuccessful", "Unsuccessful", JOptionPane.ERROR_MESSAGE);
		}
		while(storeLocs.size() < 1){
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(jfc.showOpenDialog(frm) != JFileChooser.APPROVE_OPTION){
				System.err.println("Refused to provide an initial store folder");
				System.exit(1);
			}
			File sel = jfc.getSelectedFile();
			if(sel.isDirectory()) storeLocs.add(sel); 
		}
		numThreads = numThreadTemp;
		priorityExport = priorityDecryptTemp;
		allowExport = allowExportTemp;
		checkImports = checkImportTemp;
		
		try{
			PrintWriter pw = new PrintWriter(CONF_FILE);
			pw.println(CONF_SSL + ossl);
			pw.println(CONF_THREAD + numThreads);
			pw.println(CONF_PRIORITY + priorityExport);
			pw.println(CONF_EXPORT + allowExport);
			pw.println(CONF_STORE);
			for(File fi : storeLocs){
				pw.println(fi.getAbsolutePath());
			}
			pw.close();
		}
		catch(IOException e){
			System.err.println("Failed to save config");
		}
		
		File chk = null;
		for(File fi : storeLocs){
			File lib = new File(fi, LIBRARY_NAME);
			if(lib.exists()){
				chk = lib;
				//break;
			}
		}
		
		char[] pass = null;
		if(chk == null){
			JOptionPane.showMessageDialog(frm, "First time run\n  Create your password", "Create Password", JOptionPane.INFORMATION_MESSAGE);
			char[] p1 = askPassword();
			char[] p2 = askPassword();
			boolean same = p1.length == p2.length;
			for(int i=0; i<Math.min(p1.length, p2.length); i++){
				if(p1[i] != p2[i]) same = false;
			}
			if(same){
				JOptionPane.showMessageDialog(frm, "Password created", "Create Password", JOptionPane.INFORMATION_MESSAGE);
				pass = p1;
			}
			else{
				JOptionPane.showMessageDialog(frm, "Passwords dont match", "Create Password", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
		else{
			pass = askPassword();
		}
		sec = OpenSSLCommander.getCommander(chk, pass, ossl);
		if(sec == null){
			System.err.println("Wrong Password");
			System.exit(1);
		}
		store.useSecurity(sec);
		store.useStorage(storeLocs);
		
		tempLoc = new File("temp");
		if(!tempLoc.exists()) tempLoc.mkdirs();
		//load stores
		try {
			store.loadAll(tempLoc);
			store.fireTableDataChanged();
		} catch (IOException e) {
			System.err.println("Storage loading failure");
			System.exit(1);
		}
		
		encryptDecryptThreads = new EDT[numThreads];
		for(int i=0; i<encryptDecryptThreads.length; i++){
			encryptDecryptThreads[i] = new EDT(i);
			encryptDecryptThreads[i].start();
		}
		
		updateStatus();
		txaSearch.grabFocus();
	}
	
	public char[] askPassword(){
		final JPasswordField jpf = new JPasswordField();
		int result = JOptionPane.showConfirmDialog(frm, jpf, "Password", JOptionPane.DEFAULT_OPTION);
		char[] password = null;
		if(result == JOptionPane.OK_OPTION){
			password = jpf.getPassword();
		}
		else{
			System.err.println("No password given");
			System.exit(1);
		}
		return password;
	}
	
	public void updateStatus(){
		txaStatus.setText("");
		txaStatus.append("size\t" + store.getRowCount() + "\n");
		txaStatus.append("total\t" + store.sumLengths() + "KB\n");
		txaStatus.append("\nThreads:\n");
		for(int i=0; i<numThreads; i++){
			txaStatus.append(" " + i + "- ");
			String jb = encryptDecryptThreads[i].getCur();
			if(jb == null) txaStatus.append("idle\n");
			else{
				txaStatus.append(jobString(jb) + "\n");
			}
		}
		txaStatus.append("\nJobs:\n");
		int c = 6 + numThreads;
		int i = 0;
		for(String s : jobs){
			if(c + i < TXA_HEIGHT - 1) txaStatus.append(" - " + jobString(s) + "\n");
			else if (c + i == TXA_HEIGHT-1){
				txaStatus.append(" - [" + (jobs.size()-i) + "more ]");
			}
			i++;
		}
	}
	
	public void secureAnalysis(){
		int rw = tblItems.getSelectedRow();
		if(rw != -1){
			int idx = tblItems.convertRowIndexToModel(rw);
			String desc = store.describe(idx);
			if(JOptionPane.showConfirmDialog(frm, desc, "Details", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) return;
		}
		if(JOptionPane.showConfirmDialog(frm, store.tagDesc(), "Tags", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) return;
		if(JOptionPane.showConfirmDialog(frm, store.storeDesc(), "Storage", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) return;
	}
	
	public void secureUse(File fi){
		try {
			Desktop.getDesktop().open(fi);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File getExportTempFile(String trueName){
		return new File(tempLoc, trueName);
	}
	public void secureExport(int i){
		File expf = getExportTempFile(store.plainName(i));
		//check if its already been exported
		if(expf.exists()) secureUse(expf);
		else{//otherwise add to work queue
			File cipf = store.locate(i);
			synchronized(jobs){
				if(priorityExport) jobs.addFirst(expJob(cipf, expf));
				else jobs.addLast(expJob(cipf, expf));
			}
		}
		updateStatus();
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
			for(String j : impJobs){
				if(priorityExport) jobs.addLast(j);
				else jobs.addFirst(j);
			}
		}
		updateStatus();
	}
	
	public void secureMove(){
		int rw = tblItems.getSelectedRow();
		if(rw == -1){
			JOptionPane.showMessageDialog(frm, "No item selected", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int idx = tblItems.convertRowIndexToModel(rw);
		String[] opts = new String[storeLocs.size()];
		for(int i=0; i<opts.length; i++) opts[i] = storeLocs.get(i).getAbsolutePath();
		JComboBox cmbMove = new JComboBox(opts);
		cmbMove.setSelectedIndex(store.curStore(idx));
		if(JOptionPane.showConfirmDialog(frm, cmbMove, "Move item", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
		File newLoc = store.move(idx, cmbMove.getSelectedIndex());
		if(newLoc == null) System.err.println("move " + store.plainName(idx) + " unsuccessful");
	}
	
	public void secureDelete(){
		int rw = tblItems.getSelectedRow();
		if(rw == -1){
			JOptionPane.showMessageDialog(frm, "No item selected", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int idx = tblItems.convertRowIndexToModel(rw);
		if(JOptionPane.showConfirmDialog(frm, "Delete " + store.plainName(idx) + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
		File del = store.delete(idx);
		store.fireTableDataChanged();
		if(del != null){
			if(del.delete()){
				//successful
			}
			else{
				System.err.println("Delete " + del.getAbsolutePath() + " failed");
			}
		}
		updateStatus();
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
		btnAnalyse = new JButton("Analyse");
		btnAnalyse.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				secureAnalysis();
			}
		});
		
		tblItems = new JTable(store);
		tblItems.setRowSorter(tableSorter);
		tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblItems.setFillsViewportHeight(true);
		tblItems.getRowSorter().toggleSortOrder(Storage.COL_DATE);
		tblItems.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON3){
					tblItems.setRowSelectionInterval(e.getY()/tblItems.getRowHeight(), e.getY()/tblItems.getRowHeight());
				}
				if(e.getClickCount() > 1 || e.getButton() == MouseEvent.BUTTON3){
					int idx = tblItems.convertRowIndexToModel(tblItems.getSelectedRow());
					secureExport(idx);
				}
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		
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
				//EXPORT settings here, as in mass export (export everything)
				if(allowExport && txaSearch.getText().equalsIgnoreCase("-export")){
					//txaSearch.setText("");
					if(JOptionPane.showConfirmDialog(frm, "Do you really want to export the whole secure base?", "Confirm Export", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION){
						totalExport();
					}
				}
			}
			public void keyTyped(KeyEvent arg0) {
			}
		});
		
		JPanel pnlTop = new JPanel(new GridLayout(1, 4));
		JPanel pnlEast = new JPanel(new BorderLayout());
		JPanel pnlCenterEast = new JPanel(new BorderLayout());
		JScrollPane jspItems = new JScrollPane(tblItems);
		
		pnlTop.add(btnImport);
		pnlTop.add(btnMove);
		pnlTop.add(btnDelete);
		pnlTop.add(btnAnalyse);
		pnlCenterEast.add(txaStatus, BorderLayout.CENTER);
		pnlCenterEast.add(txaSearch, BorderLayout.NORTH);
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
	
	public void totalExport(){
		File expf = new File("export");
		if(expf.exists()) rmrf(expf);
		expf.mkdirs();
		for(int sto=0; sto<storeLocs.size(); sto++){
			try{
				String sl = storeLocs.get(sto).getAbsolutePath().replaceAll("/", "-").replaceAll("\\\\", "-");
				File estore = new File(expf, sl);
				estore.mkdir();
				File log = new File(estore, LIBRARY_NAME);
				PrintWriter pw = new PrintWriter(log);
				for(int i=0; i<store.getRowCount(); i++) if(store.curStore(i) == sto){
					File enc = store.locate(i);
					File dec = sec.prepareMainFile(enc, estore, false);
					pw.println(dec.getName());
					pw.println(store.getValueAt(i, Storage.COL_DATE));
					pw.println(store.getValueAt(i, Storage.COL_TAGS));
					synchronized(jobs){
						jobs.addLast(expJob(enc, dec));
					}
				}
				pw.close();
			}
			catch(IOException exc){
				exc.printStackTrace();
				JOptionPane.showMessageDialog(frm, "Exporting Failed");
				return;
			}
		}
		JOptionPane.showMessageDialog(frm, "Exporting to:\n   " + expf.getAbsolutePath());
	}
	
	/**public char[] jetPass(){
		JPasswordField psf = new JPasswordField();
		psf.grabFocus();
		int opt = JOptionPane.showConfirmDialog(frm, psf, "Password", JOptionPane.PLAIN_MESSAGE);
		if(opt == JOptionPane.OK_OPTION) return psf.getPassword();
		return null;
	}
	
	public String jetString(String msg, String ttl){
		return JOptionPane.showInputDialog(frm, msg, ttl, JOptionPane.QUESTION_MESSAGE);
	}**/
	
	

	
	public static String jobString(String job){
		if(job.charAt(0) == IMPORT_FLAG){
			String[] brk = job.split(",", 4);//4 parts to an import string
			File inf = new File(brk[1]);
			return "import " + inf.getName();
		}
		else{
			String[] brk = job.split(",", 3);//3 parts to export string
			File plf = new File(brk[2]);
			return "export " + plf.getName();
		}
	}
	public static String impJob(File fi, String date, String tags){
		if(date == null) date = Storage.curDate();
		if(tags == null) tags = Storage.NEW_TAG;
		return IMPORT_FLAG + "," + fi.getAbsolutePath() + "," + date + "," + tags;
	}
	public static String expJob(File cip, File plain){
		return EXPORT_FLAG + "," + cip.getAbsolutePath() + "," + plain.getAbsolutePath();
	}
	
	/**
	 * Helper thread
	 */
	
	public class EDT extends Thread{
		public final int idx;
		private boolean going = true;
		private String cur;
		public EDT(int i){
			idx = i;
		}
		public String getCur(){
			if(cur == null) return null;
			return cur;
		}
		public void weakStop(){
			going = false;
		}
		
		public boolean edtExport(File cip, File pla){
			if(!cip.exists()){
				System.err.println("export: file " + cip.getAbsolutePath() + " doesnt exist for " + pla.getName());
				pla.delete();
				return false;
			}
			File save = sec.encryptSpecialFile(cip, pla, false);
			if(save == null){
				System.err.println("export: Encryption failure");
				pla.delete();
				return false;
			}
			return true;
		}
		
		public boolean edtImport(File fi, String date, String tags){
			if(!fi.exists()){
				System.err.println("import: file " + fi.getAbsolutePath() + " doesnt exist");
				return false;
			}
			String pname = fi.getName();
			if(store.containsEntry(pname)){
				System.err.println("import: already have a file named " + pname);
				return false;
			}
			long size = fi.length()/KILOBYTE;
			File save = sec.encryptMainFile(fi, storeLocs.get(0), true);
			if(save == null){
				System.err.println("import: Encryption failure");
				return false;
			}
			if(checkImports){
				long tim = System.currentTimeMillis();
				boolean success = true;
				File checkfi = new File(idx + ".check");
				File checkOut = sec.encryptSpecialFile(save, checkfi, false);
				if(checkOut == null) success = false;
				else{
					String fiHash = sec.digest(fi);
					String outHash = sec.digest(checkOut);
					if(fiHash == null || outHash == null || fiHash.length() < 1 || !fiHash.equals(outHash)) success = false;
				}
				checkfi.delete();
				if(!success){
					save.delete();
					if(JOptionPane.showConfirmDialog(frm, "Confirming " + fi.getName() + "failed\n\n - Would you like to re-import the file?", "Import failed", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
						String j = impJob(fi, date, tags);
						synchronized(jobs){
							if(priorityExport) jobs.addLast(j);
							else jobs.addFirst(j);
						}
					}
					return false;
				}
				else{
					System.out.println("Import: checked " + fi.getName() + " in " + (System.currentTimeMillis()-tim) + "ms");
				}
				
			}
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
							store.fireTableDataChanged();
						}
						else{
							//no import
						}
					}
					else{
						String[] brk = chose.split(",", 3);//3 parts to an export string
						File pla = new File(brk[2]);
						if(edtExport(new File(brk[1]), pla)){
							//export succeeded
							//System.out.println(pla.getParentFile().getAbsolutePath() + ", " + tempLoc.getAbsolutePath() + ", " + pla.getParentFile().equals(tempLoc));
							try{
								if(pla.getCanonicalPath().equals(tempLoc.getCanonicalPath()))secureUse(pla);
							}
							catch(IOException exc){
								System.err.println("Failed to retrieve canonical path?");
							}
						}
						else{
							//export failed
						}
					}
					cur = null;
					updateStatus();
				}
				else{
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						//do nothing
					}
				}
			}
		}
	}
}
