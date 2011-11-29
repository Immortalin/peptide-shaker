package eu.isas.peptideshaker.gui;

import eu.isas.peptideshaker.filtering.MatchFilter;
import eu.isas.peptideshaker.filtering.MatchFilter.FilterType;
import eu.isas.peptideshaker.filtering.PeptideFilter;
import eu.isas.peptideshaker.filtering.ProteinFilter;
import eu.isas.peptideshaker.filtering.PsmFilter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Displays the filters used for star/hide items
 *
 * @author vaudel
 */
public class FiltersDialog extends javax.swing.JDialog {

    /**
     * The main gui
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * The protein star filters
     */
    private HashMap<String, ProteinFilter> proteinStarFilters = new HashMap<String, ProteinFilter>();
    /**
     * The protein hide filters
     */
    private HashMap<String, ProteinFilter> proteinHideFilters = new HashMap<String, ProteinFilter>();
    /**
     * The peptide star filters
     */
    private HashMap<String, PeptideFilter> peptideStarFilters = new HashMap<String, PeptideFilter>();
    /**
     * The peptide hide filters
     */
    private HashMap<String, PeptideFilter> peptideHideFilters = new HashMap<String, PeptideFilter>();
    /**
     * The psm star filters
     */
    private HashMap<String, PsmFilter> psmStarFilters = new HashMap<String, PsmFilter>();
    /**
     * The psm hide filters
     */
    private HashMap<String, PsmFilter> psmHideFilters = new HashMap<String, PsmFilter>();

    /** Creates new form FiltersDialog */
    public FiltersDialog(PeptideShakerGUI peptideShakerGUI) {
        super(peptideShakerGUI, true);

        initComponents();

        this.peptideShakerGUI = peptideShakerGUI;
        proteinStarFilters.putAll(peptideShakerGUI.getFilterPreferences().getProteinStarFilters());
        proteinHideFilters.putAll(peptideShakerGUI.getFilterPreferences().getProteinHideFilters());
        peptideStarFilters.putAll(peptideShakerGUI.getFilterPreferences().getPeptideStarFilters());
        peptideHideFilters.putAll(peptideShakerGUI.getFilterPreferences().getPeptideHideFilters());
        psmStarFilters.putAll(peptideShakerGUI.getFilterPreferences().getPsmStarFilters());
        psmHideFilters.putAll(peptideShakerGUI.getFilterPreferences().getPsmHideFilters());
        fillTables();
    }

    private void fillTables() {
        for (MatchFilter matchFilter : proteinStarFilters.values()) {
            ((DefaultTableModel) starredProteinsTable.getModel()).addRow(new Object[]{
                        starredProteinsTable.getRowCount() + 1,
                        matchFilter.isActive(),
                        matchFilter.getName(),
                        matchFilter.getDescription()
                    });
        }
        for (MatchFilter matchFilter : peptideStarFilters.values()) {
            ((DefaultTableModel) starredPeptidesTable.getModel()).addRow(new Object[]{
                        starredPeptidesTable.getRowCount() + 1,
                        matchFilter.isActive(),
                        matchFilter.getName(),
                        matchFilter.getDescription()
                    });
        }
        for (MatchFilter matchFilter : psmStarFilters.values()) {
            ((DefaultTableModel) starredPsmTable.getModel()).addRow(new Object[]{
                        starredPsmTable.getRowCount() + 1,
                        matchFilter.isActive(),
                        matchFilter.getName(),
                        matchFilter.getDescription()
                    });
        }
        for (MatchFilter matchFilter : proteinHideFilters.values()) {
            ((DefaultTableModel) hiddenProteinsTable.getModel()).addRow(new Object[]{
                        hiddenProteinsTable.getRowCount() + 1,
                        matchFilter.isActive(),
                        matchFilter.getName(),
                        matchFilter.getDescription()
                    });
        }
        for (MatchFilter matchFilter : peptideHideFilters.values()) {
            ((DefaultTableModel) hiddenPeptidesTable.getModel()).addRow(new Object[]{
                        hiddenPeptidesTable.getRowCount() + 1,
                        matchFilter.isActive(),
                        matchFilter.getName(),
                        matchFilter.getDescription()
                    });
        }
        for (MatchFilter matchFilter : psmHideFilters.values()) {
            ((DefaultTableModel) hiddenPsmTable.getModel()).addRow(new Object[]{
                        hiddenPsmTable.getRowCount() + 1,
                        matchFilter.isActive(),
                        matchFilter.getName(),
                        matchFilter.getDescription()
                    });
        }
        setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        starredProteinsTable = new javax.swing.JTable();
        addStarredProtein = new javax.swing.JButton();
        editStarredProtein = new javax.swing.JButton();
        clearStarredProtein = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hiddenProteinsTable = new javax.swing.JTable();
        addHiddenProtein = new javax.swing.JButton();
        editHiddenProtein = new javax.swing.JButton();
        clearHiddenProtein = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        starredPeptidesTable = new javax.swing.JTable();
        addStarredPeptides = new javax.swing.JButton();
        editStarredPeptides = new javax.swing.JButton();
        clearStarredPeptides = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        hiddenPeptidesTable = new javax.swing.JTable();
        addHiddenPeptides = new javax.swing.JButton();
        editHiddenPeptides = new javax.swing.JButton();
        clearHiddenPeptides = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        starredPsmTable = new javax.swing.JTable();
        addStarredPsm = new javax.swing.JButton();
        editStarredPsm = new javax.swing.JButton();
        clearStarredPsm = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        hiddenPsmTable = new javax.swing.JTable();
        addHiddenPsm = new javax.swing.JButton();
        editHiddenPsm = new javax.swing.JButton();
        clearHiddenPsm = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Starred Proteins"));
        jPanel5.setPreferredSize(new java.awt.Dimension(613, 195));

        starredProteinsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "Name", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        starredProteinsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                starredProteinsTableMouseReleased(evt);
            }
        });
        starredProteinsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                starredProteinsTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(starredProteinsTable);

        addStarredProtein.setText("Add");

        editStarredProtein.setText("Edit");

        clearStarredProtein.setText("Clear");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editStarredProtein, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(addStarredProtein, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(clearStarredProtein, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(addStarredProtein)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editStarredProtein)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearStarredProtein)))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Hidden Proteins"));
        jPanel6.setPreferredSize(new java.awt.Dimension(613, 195));

        hiddenProteinsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "Name", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        hiddenProteinsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hiddenProteinsTableMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(hiddenProteinsTable);

        addHiddenProtein.setText("Add");

        editHiddenProtein.setText("Edit");

        clearHiddenProtein.setText("Clear");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editHiddenProtein, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(addHiddenProtein, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(clearHiddenProtein, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(addHiddenProtein)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editHiddenProtein)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearHiddenProtein)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Proteins", jPanel2);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Starred Peptides"));
        jPanel7.setPreferredSize(new java.awt.Dimension(613, 195));

        starredPeptidesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "Name", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        starredPeptidesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                starredPeptidesTableMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(starredPeptidesTable);

        addStarredPeptides.setText("Add");

        editStarredPeptides.setText("Edit");

        clearStarredPeptides.setText("Clear");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editStarredPeptides, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(addStarredPeptides, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(clearStarredPeptides, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(addStarredPeptides)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editStarredPeptides)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearStarredPeptides)))
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Hidden Peptides"));

        hiddenPeptidesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "Name", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        hiddenPeptidesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hiddenPeptidesTableMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(hiddenPeptidesTable);

        addHiddenPeptides.setText("Add");

        editHiddenPeptides.setText("Edit");

        clearHiddenPeptides.setText("Clear");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editHiddenPeptides, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(addHiddenPeptides, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(clearHiddenPeptides, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(addHiddenPeptides)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editHiddenPeptides)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearHiddenPeptides)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Peptides", jPanel3);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Starred PSMs"));
        jPanel9.setPreferredSize(new java.awt.Dimension(613, 195));

        starredPsmTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "Name", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        starredPsmTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                starredPsmTableMouseReleased(evt);
            }
        });
        jScrollPane5.setViewportView(starredPsmTable);

        addStarredPsm.setText("Add");
        addStarredPsm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStarredPsmActionPerformed(evt);
            }
        });

        editStarredPsm.setText("Edit");

        clearStarredPsm.setText("Clear");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editStarredPsm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(addStarredPsm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(clearStarredPsm, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(addStarredPsm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editStarredPsm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearStarredPsm)))
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Hidden PSMs"));

        hiddenPsmTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "Name", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        hiddenPsmTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hiddenPsmTableMouseReleased(evt);
            }
        });
        jScrollPane6.setViewportView(hiddenPsmTable);

        addHiddenPsm.setText("Add");

        editHiddenPsm.setText("Edit");

        clearHiddenPsm.setText("Clear");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGap(0, 601, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editHiddenPsm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(addHiddenPsm, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(clearHiddenPsm, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGap(0, 168, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(addHiddenPsm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editHiddenPsm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearHiddenPsm)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("PSMs", jPanel4);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("OK");
        okButton.setPreferredSize(new java.awt.Dimension(65, 23));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(492, 492, 492)
                .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        peptideShakerGUI.getFilterPreferences().setProteinStarFilters(proteinStarFilters);
        peptideShakerGUI.getFilterPreferences().setProteinHideFilters(proteinHideFilters);
        peptideShakerGUI.getFilterPreferences().setPeptideStarFilters(peptideStarFilters);
        peptideShakerGUI.getFilterPreferences().setPeptideHideFilters(peptideHideFilters);
        peptideShakerGUI.getFilterPreferences().setPsmStarFilters(psmStarFilters);
        peptideShakerGUI.getFilterPreferences().setPsmHideFilters(psmHideFilters);
        setVisible(false);
        dispose();
        peptideShakerGUI.setUpdateNeeded(PeptideShakerGUI.OVER_VIEW_TAB_INDEX);
        peptideShakerGUI.setUpdateNeeded(PeptideShakerGUI.MODIFICATIONS_TAB_INDEX);
        peptideShakerGUI.setUpdateNeeded(PeptideShakerGUI.STRUCTURES_TAB_INDEX);
    }//GEN-LAST:event_okButtonActionPerformed

    private void starredProteinsTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_starredProteinsTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_starredProteinsTableKeyReleased

    private void starredProteinsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_starredProteinsTableMouseReleased
        int column = starredProteinsTable.getSelectedColumn();
        int row = starredProteinsTable.getSelectedRow();
        if (evt.getButton() == MouseEvent.BUTTON1) {
            MatchFilter matchFilter;
            String key = (String) starredProteinsTable.getValueAt(row, 2);
            matchFilter = proteinStarFilters.get(key);
            if (evt.getClickCount() == 1) {
                if (column == 1) {
                    matchFilter.setActive(!matchFilter.isActive());
                }
            } else if (evt.getClickCount() == 2) {
                if (column != 2) {
                    //@TODO edit matchFilter
                }
            }
        }
    }//GEN-LAST:event_starredProteinsTableMouseReleased

    private void starredPeptidesTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_starredPeptidesTableMouseReleased
        int column = starredPeptidesTable.getSelectedColumn();
        int row = starredPeptidesTable.getSelectedRow();
        if (evt.getButton() == MouseEvent.BUTTON1) {
            MatchFilter matchFilter;
            String key = (String) starredPeptidesTable.getValueAt(row, 2);
            matchFilter = peptideStarFilters.get(key);
            if (evt.getClickCount() == 1) {
                if (column == 1) {
                    matchFilter.setActive(!matchFilter.isActive());
                }
            } else if (evt.getClickCount() == 2) {
                if (column != 2) {
                    //@TODO edit matchFilter
                }
            }
        }
    }//GEN-LAST:event_starredPeptidesTableMouseReleased

    private void starredPsmTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_starredPsmTableMouseReleased
        int column = starredPsmTable.getSelectedColumn();
        int row = starredPsmTable.getSelectedRow();
        if (evt.getButton() == MouseEvent.BUTTON1) {
            MatchFilter matchFilter;
            String key = (String) starredPsmTable.getValueAt(row, 2);
            matchFilter = psmStarFilters.get(key);
            if (evt.getClickCount() == 1) {
                if (column == 1) {
                    matchFilter.setActive(!matchFilter.isActive());
                }
            } else if (evt.getClickCount() == 2) {
                if (column != 2) {
                    //@TODO edit matchFilter
                }
            }
        }
    }//GEN-LAST:event_starredPsmTableMouseReleased

    private void hiddenProteinsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hiddenProteinsTableMouseReleased
        int column = hiddenProteinsTable.getSelectedColumn();
        int row = hiddenProteinsTable.getSelectedRow();
        if (evt.getButton() == MouseEvent.BUTTON1) {
            MatchFilter matchFilter;
            String key = (String) hiddenProteinsTable.getValueAt(row, 2);
            matchFilter = proteinHideFilters.get(key);
            if (evt.getClickCount() == 1) {
                if (column == 1) {
                    matchFilter.setActive(!matchFilter.isActive());
                }
            } else if (evt.getClickCount() == 2) {
                if (column != 2) {
                    //@TODO edit matchFilter
                }
            }
        }
    }//GEN-LAST:event_hiddenProteinsTableMouseReleased

    private void hiddenPeptidesTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hiddenPeptidesTableMouseReleased
        int column = hiddenPeptidesTable.getSelectedColumn();
        int row = hiddenPeptidesTable.getSelectedRow();
        if (evt.getButton() == MouseEvent.BUTTON1) {
            MatchFilter matchFilter;
            String key = (String) hiddenPeptidesTable.getValueAt(row, 2);
            matchFilter = peptideHideFilters.get(key);
            if (evt.getClickCount() == 1) {
                if (column == 1) {
                    matchFilter.setActive(!matchFilter.isActive());
                }
            } else if (evt.getClickCount() == 2) {
                if (column != 2) {
                    //@TODO edit matchFilter
                }
            }
        }
    }//GEN-LAST:event_hiddenPeptidesTableMouseReleased

    private void hiddenPsmTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hiddenPsmTableMouseReleased
        int column = hiddenPsmTable.getSelectedColumn();
        int row = hiddenPsmTable.getSelectedRow();
        if (evt.getButton() == MouseEvent.BUTTON1) {
            MatchFilter matchFilter;
            String key = (String) hiddenPsmTable.getValueAt(row, 2);
            matchFilter = psmHideFilters.get(key);
            if (evt.getClickCount() == 1) {
                if (column == 1) {
                    matchFilter.setActive(!matchFilter.isActive());
                }
            } else if (evt.getClickCount() == 2) {
                if (column != 2) {
                    //@TODO edit matchFilter
                }
            }
        }
    }//GEN-LAST:event_hiddenPsmTableMouseReleased

    private void addStarredPsmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStarredPsmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addStarredPsmActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addHiddenPeptides;
    private javax.swing.JButton addHiddenProtein;
    private javax.swing.JButton addHiddenPsm;
    private javax.swing.JButton addStarredPeptides;
    private javax.swing.JButton addStarredProtein;
    private javax.swing.JButton addStarredPsm;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clearHiddenPeptides;
    private javax.swing.JButton clearHiddenProtein;
    private javax.swing.JButton clearHiddenPsm;
    private javax.swing.JButton clearStarredPeptides;
    private javax.swing.JButton clearStarredProtein;
    private javax.swing.JButton clearStarredPsm;
    private javax.swing.JButton editHiddenPeptides;
    private javax.swing.JButton editHiddenProtein;
    private javax.swing.JButton editHiddenPsm;
    private javax.swing.JButton editStarredPeptides;
    private javax.swing.JButton editStarredProtein;
    private javax.swing.JButton editStarredPsm;
    private javax.swing.JTable hiddenPeptidesTable;
    private javax.swing.JTable hiddenProteinsTable;
    private javax.swing.JTable hiddenPsmTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JTable starredPeptidesTable;
    private javax.swing.JTable starredProteinsTable;
    private javax.swing.JTable starredPsmTable;
    // End of variables declaration//GEN-END:variables
}
