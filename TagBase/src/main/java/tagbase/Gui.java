package tagbase;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Gui extends JFrame {

    public static final int COL_NAME = 0;
    public static final int COL_TAGS = 1;
    public static final int COL_DATE = 2;
    public static final int COL_SIZE = 3;

    public static final int SEARCH_HEIGHT = 4;
    public static final int SEARCH_WIDTH = 15;

    public static final double SIZE_UNIT = 1024;

    private final Container c;
    private final JTable tblItems;
    private final JTextArea txaSearch;
    private final JTextArea txaStatus;
    private final JButton btnScry;

    private final Tabulator tableModel;
    private final TableRowSorter<Tabulator> tableSorter;

    private final TagBaseII tb;

    public Gui(TagBaseII base) {
        super("Tag Base");
        tb = base;

        tableModel = new Tabulator();
        tableSorter = new TableRowSorter<Tabulator>(tableModel);

        c = this.getContentPane();
        btnScry = new JButton("Rescan");
        btnScry.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                tb.scry();
                tableModel.fireTableDataChanged();
                showInfo();
            }
        });

        tblItems = new JTable(tableModel);
        tblItems.setRowSorter(tableSorter);
        tblItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblItems.setFillsViewportHeight(true);
        tblItems.getRowSorter().toggleSortOrder(COL_DATE);
        tblItems.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    tblItems.setRowSelectionInterval(e.getY() / tblItems.getRowHeight(), e.getY() / tblItems.getRowHeight());
                }
                if (e.getClickCount() > 1 || e.getButton() == MouseEvent.BUTTON3) {
                    int idx = tblItems.convertRowIndexToModel(tblItems.getSelectedRow());
                    //System.out.println("Do something with item " + idx);
                    File fi = new File(tb.mainDir, tb.path(idx));
                    if (!fi.exists()) {
                        System.err.println("Couldnt find " + fi.getName() + ", try rescanning");
                        return;
                    }
                    try {
                        Main.use(fi);
                    } catch (IOException e1) {
                        System.err.println("Failed to open");
                    }
                }
                showInfo();
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });

        txaSearch = new JTextArea(SEARCH_HEIGHT, SEARCH_WIDTH);
        txaSearch.setBorder(BorderFactory.createTitledBorder("Search"));
        txaSearch.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent arg0) {
            }

            public void keyReleased(KeyEvent arg0) {
                filterBase(txaSearch.getText());
                if (txaSearch.getText().equalsIgnoreCase("-tags")) {
                    tb.displayTags();
                }
            }

            public void keyTyped(KeyEvent arg0) {
            }
        });
        txaStatus = new JTextArea();
        txaStatus.setEditable(false);
        txaStatus.setLineWrap(true);
        txaStatus.setBorder(BorderFactory.createTitledBorder("Status"));

        JPanel pnlEast = new JPanel(new BorderLayout());
        JScrollPane jspItems = new JScrollPane(tblItems);

        pnlEast.add(txaSearch, BorderLayout.NORTH);
        pnlEast.add(txaStatus, BorderLayout.CENTER);
        pnlEast.add(btnScry, BorderLayout.SOUTH);
        c.setLayout(new BorderLayout());
        c.add(pnlEast, BorderLayout.EAST);
        c.add(jspItems, BorderLayout.CENTER);
        this.setContentPane(c);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowListener() {
            public void windowActivated(WindowEvent evt) {
            }

            public void windowClosed(WindowEvent evt) {
                Main.exit();
            }

            public void windowClosing(WindowEvent evt) {
                windowClosed(evt);
            }

            public void windowDeactivated(WindowEvent evt) {
            }

            public void windowDeiconified(WindowEvent evt) {
            }

            public void windowIconified(WindowEvent evt) {
            }

            public void windowOpened(WindowEvent evt) {
            }
        });

        showInfo();
        this.setVisible(true);
    }

    private final class Tabulator extends AbstractTableModel {

        @Override
        public int getColumnCount() {
            return 4;
        }

        public String getColumnName(int col) {
            switch (col) {
                case COL_NAME: {
                    return "Name";
                }
                case COL_TAGS: {
                    return "Tags";
                }
                case COL_DATE: {
                    return "Date";
                }
                default: {
                    return "Size";
                }
            }
        }

        @Override
        public int getRowCount() {
            return tb.count();
        }

        @Override
        public Object getValueAt(int row, int col) {
            switch (col) {
                case COL_NAME: {
                    return tb.name(row);
                }
                case COL_TAGS: {
                    return tb.tag(row);
                }
                case COL_DATE: {
                    return tb.date(row);
                }
                default: {
                    return Main.twoDecimal(tb.size(row) / SIZE_UNIT);
                }
            }
        }

        public Class<?> getColumnClass(int c) {
            if (c == COL_SIZE) return Double.class;
            return String.class;
        }

        public boolean isCellEditable(int row, int col) {
            return (col == COL_NAME || col == COL_TAGS);
        }

        public void setValueAt(Object value, int row, int col) {
            if (col == COL_NAME) {
                tb.rename(row, (String) value);
            } else if (col == COL_TAGS) {
                tb.retag(row, ((String) value).toLowerCase());
            }
        }

    }

    public void showInfo() {
        txaStatus.setText("Count:\t" + tb.count() + "\nSize:\t" + tb.getTotalSize() + "\n");
        if (tblItems.getSelectedRow() != -1) {
            int idx = tblItems.convertRowIndexToModel(tblItems.getSelectedRow());
            txaStatus.append("\nSelected(" + idx + ")");
            txaStatus.append("\n - " + tb.name(idx));
            txaStatus.append("\n - " + tb.tag(idx));
            txaStatus.append("\n - " + tb.date(idx));
            txaStatus.append("\n - " + tb.size(idx));
            txaStatus.append("\n - " + tb.path(idx));

        }
    }

    public void filterBase(String ft) {

        boolean names = false;
        boolean bads = false;
        boolean news = false;
        boolean hiddens = false;
        boolean exclusive = true;
        int rdm = -1;
        if (ft.contains("-n")) {
            names = true;
            ft = ft.substring(0, ft.indexOf("-n")) + ft.substring(ft.indexOf("-n") + 2);
        }
        if (ft.contains("-i")) {
            news = true;
            ft = ft.substring(0, ft.indexOf("-i")) + ft.substring(ft.indexOf("-i") + 2);
        }
        if (ft.contains("-zz")) {
            bads = true;
            ft = ft.substring(0, ft.indexOf("-zz")) + ft.substring(ft.indexOf("-zz") + 3);
        }
        if (ft.contains("-zh")) {
            hiddens = true;
            ft = ft.substring(0, ft.indexOf("-zh")) + ft.substring(ft.indexOf("-zh") + 3);
        }
        if (ft.contains("-x")) {
            exclusive = false;
            ft = ft.substring(0, ft.indexOf("-x")) + ft.substring(ft.indexOf("-x") + 2);
        }
        if (ft.contains("-r")) try {
            int spbf = ft.indexOf("-r");
            int spaf = ft.indexOf(" ", spbf + 3);
            if (spaf == -1) spaf = ft.length();
            rdm = Integer.parseInt(ft.substring(spbf + 2, spaf).trim());
            ft = ft.substring(0, spbf) + ft.substring(spaf);
        } catch (Exception e) {
            ft = ft.substring(0, ft.indexOf("-r")) + ft.substring(ft.indexOf("-r") + 2);
        }

        ft = ft.replaceAll("[^a-zA-Z0-9]", " ").replaceAll("  *", " ").trim().toLowerCase();
        String[] terms = ft.split(" ");
        ArrayList<RowFilter<Tabulator, Object>> termFilters = new ArrayList<RowFilter<Tabulator, Object>>();
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].length() > 0) {
                try {
                    RowFilter<Tabulator, Object> tmp = RowFilter.regexFilter("(?i).*" + terms[i] + ".*", COL_TAGS);
                    if (names) tmp = RowFilter.regexFilter("(?i).*" + terms[i] + ".*", COL_NAME);
                    termFilters.add(tmp);
                } catch (Exception e) {
                    //do nothing
                    System.err.println("Term filter error for term " + terms[i]);
                }
            }
        }

        RowFilter<Tabulator, Object> badFilter = RowFilter.regexFilter(".*zz.*", COL_TAGS);
        if (!bads) badFilter = RowFilter.notFilter(badFilter);


        ArrayList<RowFilter<Tabulator, Object>> tmpFilters = new ArrayList<>();
        if (termFilters.size() != 0) {
            RowFilter<Tabulator, Object> orFilter = (exclusive ? RowFilter.andFilter(termFilters) : RowFilter.orFilter(termFilters));
            tmpFilters.add(orFilter);
        }
        tmpFilters.add(badFilter);
        if(news) tmpFilters.add(RowFilter.regexFilter("^new$", COL_TAGS));
        if (!hiddens) {
            RowFilter<Tabulator, Object> hiddenFilter = RowFilter.regexFilter(".*zh.*", COL_TAGS);
            tmpFilters.add(RowFilter.notFilter(hiddenFilter));
        }
        RowFilter<Tabulator, Object> omniFilter = RowFilter.andFilter(tmpFilters);

        tableSorter.setRowFilter(omniFilter);

        if(rdm != -1){
            if(tableSorter.getViewRowCount() < rdm) return;
            Set<Integer> selects = new HashSet<>();
            while(selects.size() < rdm){
                selects.add((int)Math.floor(Math.random()*tableSorter.getViewRowCount()));
            }
            System.out.println(rdm + "/" + tableSorter.getViewRowCount() + " " + selects.toString());
            selects = selects.stream().map(tableSorter::convertRowIndexToModel).collect(Collectors.toSet());
            tableSorter.setRowFilter(new IndexFilter(selects));
        }
    }

    class IndexFilter extends RowFilter<Tabulator, Integer> {

        private Set<Integer> indices;

        IndexFilter(Set<Integer> indices){
            this.indices = indices;
        }

        @Override
        public boolean include(Entry<? extends Tabulator, ? extends Integer> entry) {
            return indices.contains(entry.getIdentifier());
        }
    }

}
