import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

public class Ssys2 extends JFrame implements ActionListener, KeyListener{
	
	public static final String STORE_LOC = "store";
	public static final String TEMP_LOC = "temp";
	public static final String IN_LOC = "in";
	public static final String FILE_PASS_CHECK = "zzpcf.dat";
	
	public static final File storeFold = new File(STORE_LOC);
	public static final File tempFold = new File(TEMP_LOC);
	public static final File inFold = new File(IN_LOC);
	
	public static final String IDLE_STATUS = "idle";
	
	/*
	 * GUI
	 */
	public Container c;
	public JButton btnImport;
	public JButton btnQuit;
	public JPasswordField pswPass;
	public JTextArea txaSearch;
	public JTable tblItems;
	public JTextArea txaStats;
	public JTextArea txaInfo;
	public JLabel lblStatus;
	public JProgressBar pgbStatus;
	
	/*
	 * Atts
	 */
	public boolean locked;
	public byte[] check;
	public SecureUtils sec;
	public final SBase base;
	public final TableRowSorter<SBase> baseSorter;
	public final Set<String> importhack;
	
	public static void main(String[] args){
		System.out.println(Thread.MAX_PRIORITY + ", " + Thread.MIN_PRIORITY + ", " + Thread.NORM_PRIORITY);
		new Ssys2();
	}
	
	public Ssys2(){
		super("Secure System 2");
		
		base = new SBase();
		baseSorter = new TableRowSorter<SBase>(base);
		
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
		
		btnQuit = new JButton("Quit");
		btnQuit.addActionListener(this);
		btnImport = new JButton("Import");
		btnImport.addActionListener(this);
		
		lblStatus = new JLabel(IDLE_STATUS);
		pgbStatus = new JProgressBar(0, 100);
		pgbStatus.setValue(0);
		pgbStatus.setStringPainted(true);
		
		pswPass = new JPasswordField(20);
		pswPass.addKeyListener(this);
		pswPass.setBorder(BorderFactory.createTitledBorder("Password"));
		tblItems = new JTable(base);
		tblItems.setRowSorter(baseSorter);
		tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblItems.setFillsViewportHeight(true);
		tblItems.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent event) {
                        updateInfo();
                    }
                }
        );
		tblItems.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON3){
					tblItems.setRowSelectionInterval(e.getY()/tblItems.getRowHeight(), e.getY()/tblItems.getRowHeight());
				}
				if(e.getClickCount() > 1 || e.getButton() == MouseEvent.BUTTON3){
					int idx = tblItems.convertRowIndexToModel(tblItems.getSelectedRow());
					base.retrieveFile(idx, sec);
				}
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		txaStats = new JTextArea(1, 16);
		txaStats.setBorder(BorderFactory.createTitledBorder("Statistics"));
		txaStats.setEditable(false);
		txaSearch = new JTextArea();
		txaSearch.setBorder(BorderFactory.createTitledBorder("Search"));
		txaSearch.addKeyListener(this);
		txaInfo = new JTextArea();
		txaInfo.setBorder(BorderFactory.createTitledBorder("Info"));
		txaInfo.setEditable(false);
		
		JPanel pnlEast = new JPanel(new BorderLayout());
		JPanel pnlEastCenter = new JPanel(new GridLayout(3, 1));
		JPanel pnlEastNorth = new JPanel(new BorderLayout());
		JPanel pnlNorth = new JPanel(new GridBagLayout());
		
		pnlNorth.add(pswPass);
		pnlEastNorth.add(lblStatus, BorderLayout.NORTH);
		pnlEastNorth.add(pgbStatus, BorderLayout.CENTER);
		pnlEastNorth.add(btnImport, BorderLayout.SOUTH);
		pnlEastCenter.add(txaStats);
		pnlEastCenter.add(txaSearch);
		pnlEastCenter.add(txaInfo);
		pnlEast.add(btnQuit, BorderLayout.SOUTH);
		pnlEast.add(pnlEastNorth, BorderLayout.NORTH);
		pnlEast.add(pnlEastCenter, BorderLayout.CENTER);
		c.add(pnlEast, BorderLayout.EAST);
		c.add(pnlNorth, BorderLayout.NORTH);
		c.add(new JScrollPane(tblItems), BorderLayout.CENTER);
		
		importhack = new HashSet<String>();
		lock();
		checkPassword();
		
		this.setContentPane(c);
		this.addWindowListener(new WindowListener(){
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {
				quit();
			}
			public void windowClosing(WindowEvent e) {
				quit();
			}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}
		});
		this.setSize(800,600);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnQuit){
			quit();
		}
		else if(e.getSource() == btnImport){
			importFiles();
		}
	}
	
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		if(e.getSource() == pswPass){
			checkPassword();
		}
		else if(e.getSource() == txaSearch){
			String txt = txaSearch.getText();
			if(txt.equals("-export")){
				exportAll();
			}
			filterBase(txt);
			updateInfo();
		}
	}
	public void keyTyped(KeyEvent e) {}
	
	public void checkPassword(){
		if(!locked){
			return;
		}
		if(check == null){
			File pcf = getFile(FILE_PASS_CHECK);
			if(!pcf.exists()){
				String res = JOptionPane.showInputDialog("Make a new password");
				try{
					SecureUtils su = new SecureUtils(res, this);
					byte[] ver = su.getVerify();
					if(ver != null){
						File store = new File(STORE_LOC);
						if(!store.exists()) store.mkdir();
						if(!pcf.createNewFile()){
							System.err.println("Failed to make new file");
							System.exit(0);
						}
						FileOutputStream fos = new FileOutputStream(pcf);
						fos.write(ver);
						fos.close();
					}
				}
				catch(Exception e){
					e.printStackTrace();
					System.err.println("\n big fail, terminatin");
					System.exit(0);
				}
			}
			
			try{
				FileInputStream fis = new FileInputStream(pcf);
				check = new byte[(int)pcf.length()];
				int amt = fis.read(check);
				if(amt != check.length){
					System.err.println("didnt read enough");
					System.exit(0);
				}
			}
			catch(IOException e){
				System.err.println("trouble reading files");
				return;
			}
		}
		if(check != null){
			try{
				sec = new SecureUtils(new String(pswPass.getPassword()), this);
				if(sec.selfVerify(check)) unlock();
			}
			catch(Exception e){
				e.printStackTrace();
				
				System.err.println(" Failed to make the check");
				System.exit(0);
			}
		}
	}
	
	public void exportAll(){
		if(JOptionPane.showConfirmDialog(this, "Really export everything?", "Export", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) return;
		base.exportAll(sec);
	}
	
	public void lock(){
		txaStats.setText("");
		txaSearch.setText("");
		txaInfo.setText("");
		btnImport.setEnabled(false);
		tblItems.setEnabled(false);
		txaStats.setEnabled(false);
		txaSearch.setEnabled(false);
		txaInfo.setEnabled(false);
		locked = true;
	}
	
	public void unlock(){
		btnImport.setEnabled(true);
		tblItems.setEnabled(true);
		txaStats.setEnabled(true);
		txaSearch.setEnabled(true);
		txaInfo.setEnabled(true);
		pswPass.setEnabled(false);
		
		if(!tempFold.exists()) tempFold.mkdir();
		if(!inFold.exists()) inFold.mkdir();
		
		if(!base.load(sec)){
			System.err.println("loading failed");
		}
		base.fireTableDataChanged();
		updateInfo();
		
		txaSearch.grabFocus();
		locked = false;
	}
	
	public void quit(){
		System.out.println("Quitting");
		if(!locked){
			base.save(sec);
			rmrf(tempFold);
			for(String s : importhack){
				File imported = new File(inFold, s);
				if(imported.exists()) rmrf(imported);
			}
			if(inFold.list().length > 0){
				System.out.println("Unimported files remain in the in folder\n - delete at your leisure");
			}
			else{
				rmrf(inFold);
			}
		}
		System.exit(0);
	}
	
	public void updateProgress(){
		updateProgress(IDLE_STATUS, 0);
	}
	
	public void updateProgress(String desc, int percentage){
		lblStatus.setText(desc);
		pgbStatus.setValue(percentage);
	}
	
	public void updateInfo(){
		String stats =  "Total\n - Num:\t" + base.count() + "\n - Size:\t" + base.length;
		stats = stats + "\n\nSearch\n - Num:\t" + tblItems.getRowCount();
		txaStats.setText(stats);
		String info = "";
		if(tblItems.getSelectedRow() != -1){
			int idx = tblItems.convertRowIndexToModel(tblItems.getSelectedRow());
			info = base.name.get(idx) + "\n(" + idx + ")\n\n - " + base.tags.get(idx) + "\n - " + base.date.get(idx) + "\n - " + base.size.get(idx) + "kb";
			
		}
		
		txaInfo.setText(info);
	}
	
	public void importFiles(){
		Thread t = new Thread(){
			public void run(){
				for(String name : inFold.list()){
					if(importhack.contains(name)){
						System.out.println("Already imported " + name);
					}
					else{
						if(base.importFile(name, sec)){
							importhack.add(name);
						}
						else{
							System.err.println("Failed to import " + name);
						}
					}
				}
				if(base.save(sec)){
					//do nothing
				}
				else{
					System.err.println("problem saving");
				}
				base.fireTableDataChanged();
				updateInfo();
			}
		};
		t.start();
	}
	
	public void filterBase(String ft){
		
		if(ft.contains("-i")){
			RowFilter<SBase, Object> tmpFilter = null;
			try{
				tmpFilter = RowFilter.regexFilter("^new$", SBase.COL_TAGS);
			}
			catch(Exception e){
				System.err.println("Failed to filter new ones");
			}
			baseSorter.setRowFilter(tmpFilter);
			return;
		}
		else if(ft.matches("-r.* [0-9][0-9]*")){
			int num = Integer.parseInt(ft.substring(ft.lastIndexOf(" ")+1));
			Set<Integer> selects = new HashSet<Integer>();
			while(selects.size() < num){
				selects.add((int)Math.floor(Math.random()*base.count()));
			}
			System.out.println(selects.toString());
			ArrayList<RowFilter<SBase, Object>> randomFilters = new ArrayList<RowFilter<SBase, Object>>();
			for(int i : selects){
				RowFilter<SBase, Object> temp = RowFilter.regexFilter(base.name.get(i), SBase.COL_NAME);
				randomFilters.add(temp);
			}
			RowFilter<SBase, Object> exactFilter = RowFilter.orFilter(randomFilters);
			baseSorter.setRowFilter(exactFilter);
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
		ArrayList<RowFilter<SBase, Object>> termFilters = new ArrayList<RowFilter<SBase, Object>>();
		for(int i=0; i<terms.length; i++){
			if(terms[i].length() > 0){
				try{
					RowFilter<SBase, Object> tmp = RowFilter.regexFilter(".*" + terms[i] + ".*", SBase.COL_TAGS);  
					if(names) tmp = RowFilter.regexFilter(".*" + terms[i] + ".*", SBase.COL_NAME);
					termFilters.add(tmp);
				}
				catch(Exception e){
					//do nothing
					System.err.println("Term filter error for term " + terms[i]);
				}
			}
		}
		
		RowFilter<SBase, Object> badFilter = RowFilter.regexFilter(".*zz.*", SBase.COL_TAGS);
		if(!bads) badFilter = RowFilter.notFilter(badFilter);
		
		RowFilter<SBase, Object> omniFilter = badFilter;
		if(termFilters.size() != 0){
			ArrayList<RowFilter<SBase, Object>> tmpFilters = new ArrayList<RowFilter<SBase, Object>>();
			RowFilter<SBase, Object> orFilter = (exclusive ? RowFilter.andFilter(termFilters) : RowFilter.orFilter(termFilters));
			tmpFilters.add(orFilter);
			tmpFilters.add(badFilter);
			omniFilter = RowFilter.andFilter(tmpFilters);
		}
		
		baseSorter.setRowFilter(omniFilter);
	}
	
	/*
	 * Statics
	 */
	public static File getFile(String storeName){
		return new File(storeFold, storeName);
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
	
}
