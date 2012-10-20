package eu.isas.peptideshaker.gui;

import com.compomics.util.gui.filehandling.FileSelectionDialog;
import com.compomics.util.gui.waiting.waitinghandlers.WaitingDialog;
import com.compomics.util.Util;
import com.compomics.util.gui.error_handlers.HelpDialog;
import com.compomics.util.examples.BareBonesBrowserLaunch;
import com.compomics.util.experiment.MsExperiment;
import com.compomics.util.experiment.ProteomicAnalysis;
import com.compomics.util.experiment.SampleAnalysisSet;
import com.compomics.util.experiment.biology.PTM;
import com.compomics.util.experiment.biology.PTMFactory;
import com.compomics.util.experiment.biology.Sample;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.IdentificationMethod;
import com.compomics.util.experiment.identification.SearchParameters;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.io.identifications.IdentificationParametersReader;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import com.compomics.util.preferences.ModificationProfile;
import eu.isas.peptideshaker.PeptideShaker;
import eu.isas.peptideshaker.gui.preferencesdialogs.ImportSettingsDialog;
import eu.isas.peptideshaker.gui.preferencesdialogs.ProcessingPreferencesDialog;
import eu.isas.peptideshaker.gui.preferencesdialogs.SearchPreferencesDialog;
import eu.isas.peptideshaker.preferences.PTMScoringPreferences;
import eu.isas.peptideshaker.preferences.ProcessingPreferences;
import eu.isas.peptideshaker.preferences.ProjectDetails;
import com.compomics.util.protein.Header.DatabaseType;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * A dialog for selecting the files to load.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class NewDialog extends javax.swing.JDialog {

    /**
     * The compomics PTM factory.
     */
    private PTMFactory ptmFactory = PTMFactory.getInstance();
    /**
     * The sequence factory.
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance(100000);
    /**
     * The experiment conducted.
     */
    private MsExperiment experiment = null;
    /**
     * The sample analyzed.
     */
    private Sample sample;
    /**
     * The replicate number.
     */
    private int replicateNumber;
    /**
     * A reference to the main frame.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * The list of identification files.
     */
    private ArrayList<File> idFiles = new ArrayList<File>();
    /**
     * The xml modification files found.
     */
    private ArrayList<File> modificationFiles = new ArrayList<File>();
    /**
     * A file where the input will be stored.
     */
    public final static String SEARCHGUI_INPUT = "searchGUI_input.txt";
    /**
     * The list of spectrum files.
     */
    private ArrayList<File> spectrumFiles = new ArrayList<File>();
    /**
     * The peptide shaker class which will take care of the pre-processing.
     */
    private PeptideShaker peptideShaker;
    /**
     * The processing preferences.
     */
    private ProcessingPreferences processingPreferences = new ProcessingPreferences();
    /**
     * The ptm scoring preferences.
     */
    private PTMScoringPreferences ptmScoringPreferences = new PTMScoringPreferences();
    /**
     * The progress dialog.
     */
    private ProgressDialogX progressDialog;
    /**
     * The search parameters corresponding to the files selected.
     */
    private SearchParameters searchParameters = null;

    /**
     * Creates a new open dialog.
     *
     * @param peptideShaker a reference to the main frame
     * @param modal boolean indicating whether the dialog is modal
     */
    public NewDialog(PeptideShakerGUI peptideShaker, boolean modal) {
        super(peptideShaker, modal);
        this.peptideShakerGUI = peptideShaker;

        // @TODO: this does not work! have to create a new object and transfer all the values...

        // store the current settings  
//        oldSearchParameters = peptideShakerGUI.getSearchParameters();
//        oldProfileFile = peptideShakerGUI.getModificationProfileFile();
//        oldIdFilter = peptideShakerGUI.getIdFilter();

        setUpGui();
        this.setLocationRelativeTo(peptideShaker);
    }

    /**
     * Creates a new open dialog.
     *
     * @param peptideShaker a reference to the main frame
     * @param modal boolean indicating whether the dialog is modal
     * @param experiment The experiment conducted
     * @param sample The sample analyzed
     * @param replicateNumber The replicate number
     */
    public NewDialog(PeptideShakerGUI peptideShaker, boolean modal, MsExperiment experiment, Sample sample, int replicateNumber) {
        super(peptideShaker, modal);

        this.peptideShakerGUI = peptideShaker;
        this.experiment = experiment;
        this.sample = sample;
        this.replicateNumber = replicateNumber;

        // @TODO: this does not work! have to create a new object and transfer all the values...

        // store the current settings
//        oldSearchParameters = peptideShakerGUI.getSearchParameters();
//        oldProfileFile = peptideShakerGUI.getModificationProfileFile();
//        oldIdFilter = peptideShakerGUI.getIdFilter();

        setUpGui();
        this.setLocationRelativeTo(peptideShaker);
    }

    /**
     * Set up the gui.
     */
    private void setUpGui() {
        initComponents();
        idFilesTxt.setText(idFiles.size() + " file(s) selected");
        spectrumFilesTxt.setText(spectrumFiles.size() + " file(s) selected");
        fastaFileTxt.setText("");
        validateInput();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sampleDetailsPanel = new javax.swing.JPanel();
        openButton = new javax.swing.JButton();
        projectDetailsPanel = new javax.swing.JPanel();
        replicateNumberIdtxt = new javax.swing.JTextField();
        projectNameIdTxt = new javax.swing.JTextField();
        replicateLabel = new javax.swing.JLabel();
        sampleNameLabel = new javax.swing.JLabel();
        projectReferenceLabel = new javax.swing.JLabel();
        sampleNameIdtxt = new javax.swing.JTextField();
        helpLabel = new javax.swing.JLabel();
        processingParametersPanel = new javax.swing.JPanel();
        importFilterTxt = new javax.swing.JTextField();
        importFiltersLabel = new javax.swing.JLabel();
        searchParamsLabel = new javax.swing.JLabel();
        searchTxt = new javax.swing.JTextField();
        editSearchButton = new javax.swing.JButton();
        editImportFilterButton = new javax.swing.JButton();
        importFiltersLabel1 = new javax.swing.JLabel();
        preferencesTxt = new javax.swing.JTextField();
        editPreferencesButton = new javax.swing.JButton();
        inputFilesPanel = new javax.swing.JPanel();
        idFilesLabel = new javax.swing.JLabel();
        idFilesTxt = new javax.swing.JTextField();
        browseId = new javax.swing.JButton();
        clearId = new javax.swing.JButton();
        spectrumFilesLabel = new javax.swing.JLabel();
        spectrumFilesTxt = new javax.swing.JTextField();
        browseSpectra = new javax.swing.JButton();
        clearSpectra = new javax.swing.JButton();
        databaseLabel = new javax.swing.JLabel();
        fastaFileTxt = new javax.swing.JTextField();
        browseDbButton = new javax.swing.JButton();
        clearDbButton = new javax.swing.JButton();
        openDialogHelpJButton = new javax.swing.JButton();
        exampleFilesLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("PeptideShaker - New Project");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        sampleDetailsPanel.setBackground(new java.awt.Color(230, 230, 230));

        openButton.setBackground(new java.awt.Color(0, 153, 0));
        openButton.setFont(openButton.getFont().deriveFont(openButton.getFont().getStyle() | java.awt.Font.BOLD));
        openButton.setForeground(new java.awt.Color(255, 255, 255));
        openButton.setText("Create!");
        openButton.setEnabled(false);
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        projectDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Project Details"));
        projectDetailsPanel.setOpaque(false);

        replicateNumberIdtxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        replicateNumberIdtxt.setText("0");
        replicateNumberIdtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                replicateNumberIdtxtKeyReleased(evt);
            }
        });

        projectNameIdTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        projectNameIdTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                projectNameIdTxtKeyReleased(evt);
            }
        });

        replicateLabel.setForeground(new java.awt.Color(255, 0, 0));
        replicateLabel.setText("Replicate*");

        sampleNameLabel.setForeground(new java.awt.Color(255, 0, 0));
        sampleNameLabel.setText("Sample Name*");

        projectReferenceLabel.setForeground(new java.awt.Color(255, 0, 0));
        projectReferenceLabel.setText("Project Reference*");

        sampleNameIdtxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sampleNameIdtxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sampleNameIdtxtKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout projectDetailsPanelLayout = new javax.swing.GroupLayout(projectDetailsPanel);
        projectDetailsPanel.setLayout(projectDetailsPanelLayout);
        projectDetailsPanelLayout.setHorizontalGroup(
            projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(projectDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(projectReferenceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sampleNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(projectNameIdTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                    .addComponent(sampleNameIdtxt))
                .addGap(20, 20, 20)
                .addComponent(replicateLabel)
                .addGap(18, 18, 18)
                .addComponent(replicateNumberIdtxt, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        projectDetailsPanelLayout.setVerticalGroup(
            projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(projectDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(projectNameIdTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(projectReferenceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(projectDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sampleNameIdtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(replicateNumberIdtxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(replicateLabel)
                    .addComponent(sampleNameLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        helpLabel.setFont(helpLabel.getFont().deriveFont((helpLabel.getFont().getStyle() | java.awt.Font.ITALIC)));
        helpLabel.setText("Insert the required information (*) and click Create to load and view the results.");

        processingParametersPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Processing Parameters"));
        processingParametersPanel.setOpaque(false);

        importFilterTxt.setEditable(false);
        importFilterTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        importFilterTxt.setText("Default");
        importFilterTxt.setToolTipText("Minimum Peptide Length");

        importFiltersLabel.setText("Import Filters");

        searchParamsLabel.setText("Search Parameters");

        searchTxt.setEditable(false);
        searchTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchTxt.setText("Default");

        editSearchButton.setText("Edit");
        editSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSearchButtonActionPerformed(evt);
            }
        });

        editImportFilterButton.setText("Edit");
        editImportFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editImportFilterButtonActionPerformed(evt);
            }
        });

        importFiltersLabel1.setText("Preferences");

        preferencesTxt.setEditable(false);
        preferencesTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        preferencesTxt.setText("Default");
        preferencesTxt.setToolTipText("Minimum Peptide Length");

        editPreferencesButton.setText("Edit");
        editPreferencesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPreferencesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout processingParametersPanelLayout = new javax.swing.GroupLayout(processingParametersPanel);
        processingParametersPanel.setLayout(processingParametersPanelLayout);
        processingParametersPanelLayout.setHorizontalGroup(
            processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(processingParametersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(importFiltersLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                        .addComponent(searchParamsLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                    .addComponent(importFiltersLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(importFilterTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addComponent(preferencesTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
                    .addComponent(searchTxt, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editPreferencesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editSearchButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editImportFilterButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                .addContainerGap())
        );
        processingParametersPanelLayout.setVerticalGroup(
            processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(processingParametersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchParamsLabel)
                    .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editSearchButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importFiltersLabel)
                    .addComponent(importFilterTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editImportFilterButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(processingParametersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importFiltersLabel1)
                    .addComponent(preferencesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editPreferencesButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inputFilesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input Files"));
        inputFilesPanel.setOpaque(false);

        idFilesLabel.setForeground(new java.awt.Color(255, 0, 0));
        idFilesLabel.setText("Identification File(s)*");

        idFilesTxt.setEditable(false);
        idFilesTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        browseId.setText("Browse");
        browseId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseIdActionPerformed(evt);
            }
        });

        clearId.setText("Clear");
        clearId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearIdActionPerformed(evt);
            }
        });

        spectrumFilesLabel.setForeground(new java.awt.Color(255, 0, 0));
        spectrumFilesLabel.setText("Spectrum File(s)*");

        spectrumFilesTxt.setEditable(false);
        spectrumFilesTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        browseSpectra.setText("Browse");
        browseSpectra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseSpectraActionPerformed(evt);
            }
        });

        clearSpectra.setText("Clear");
        clearSpectra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearSpectraActionPerformed(evt);
            }
        });

        databaseLabel.setForeground(new java.awt.Color(255, 0, 0));
        databaseLabel.setText("Database File (FASTA)*");

        fastaFileTxt.setEditable(false);
        fastaFileTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        browseDbButton.setText("Browse");
        browseDbButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseDbButtonActionPerformed(evt);
            }
        });

        clearDbButton.setText("Clear");
        clearDbButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearDbButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inputFilesPanelLayout = new javax.swing.GroupLayout(inputFilesPanel);
        inputFilesPanel.setLayout(inputFilesPanelLayout);
        inputFilesPanelLayout.setHorizontalGroup(
            inputFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputFilesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inputFilesPanelLayout.createSequentialGroup()
                        .addComponent(idFilesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idFilesTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(browseId)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearId))
                    .addGroup(inputFilesPanelLayout.createSequentialGroup()
                        .addComponent(spectrumFilesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spectrumFilesTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(browseSpectra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearSpectra))
                    .addGroup(inputFilesPanelLayout.createSequentialGroup()
                        .addComponent(databaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fastaFileTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(browseDbButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearDbButton)))
                .addContainerGap())
        );

        inputFilesPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {browseId, clearId});

        inputFilesPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {browseSpectra, clearSpectra});

        inputFilesPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {browseDbButton, clearDbButton});

        inputFilesPanelLayout.setVerticalGroup(
            inputFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputFilesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inputFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idFilesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idFilesLabel)
                    .addComponent(clearId)
                    .addComponent(browseId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(inputFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spectrumFilesLabel)
                    .addComponent(clearSpectra)
                    .addComponent(browseSpectra)
                    .addComponent(spectrumFilesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(inputFilesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearDbButton)
                    .addComponent(browseDbButton)
                    .addComponent(fastaFileTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(databaseLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        openDialogHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help.GIF"))); // NOI18N
        openDialogHelpJButton.setToolTipText("Help");
        openDialogHelpJButton.setBorder(null);
        openDialogHelpJButton.setBorderPainted(false);
        openDialogHelpJButton.setContentAreaFilled(false);
        openDialogHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                openDialogHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                openDialogHelpJButtonMouseExited(evt);
            }
        });
        openDialogHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDialogHelpJButtonActionPerformed(evt);
            }
        });

        exampleFilesLabel.setForeground(new java.awt.Color(0, 0, 255));
        exampleFilesLabel.setText("<html><u><i>Need example files?</i></u></html>");
        exampleFilesLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exampleFilesLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exampleFilesLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exampleFilesLabelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout sampleDetailsPanelLayout = new javax.swing.GroupLayout(sampleDetailsPanel);
        sampleDetailsPanel.setLayout(sampleDetailsPanelLayout);
        sampleDetailsPanelLayout.setHorizontalGroup(
            sampleDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sampleDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sampleDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(projectDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(inputFilesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(processingParametersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(sampleDetailsPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(openDialogHelpJButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(helpLabel)
                        .addGap(18, 18, 18)
                        .addComponent(exampleFilesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(openButton, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)))
                .addContainerGap())
        );
        sampleDetailsPanelLayout.setVerticalGroup(
            sampleDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sampleDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(projectDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputFilesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(processingParametersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sampleDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(openButton)
                    .addComponent(exampleFilesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openDialogHelpJButton)
                    .addComponent(helpLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sampleDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sampleDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Tries to process the identification files, closes the dialog and then
     * opens the results in the main frame.
     *
     * @param evt
     */
    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        if (validateUserInput()) {

            this.setVisible(false);
            peptideShakerGUI.clearData(true);

            experiment = new MsExperiment(projectNameIdTxt.getText().trim());
            sample = new Sample(sampleNameIdtxt.getText().trim());
            SampleAnalysisSet analysisSet = new SampleAnalysisSet(sample, new ProteomicAnalysis(getReplicateNumber()));
            replicateNumber = getReplicateNumber();
            experiment.addAnalysisSet(sample, analysisSet);

            peptideShakerGUI.setProjectDetails(getProjectDetails());

            peptideShaker = new PeptideShaker(experiment, sample, replicateNumber);

            WaitingDialog waitingDialog = new WaitingDialog(peptideShakerGUI,
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                    true, peptideShakerGUI.getTips(), "Importing Data", true);

            int progressCounter = idFiles.size() + spectrumFiles.size();

            progressCounter++; // establishing the database connection
            progressCounter++; // the FASTA file
            progressCounter++; // the peptide to protein map
            progressCounter += 6; // computing probabilities etc
            progressCounter += 1; // resolving protein inference
            progressCounter += 4; // Correcting protein probabilities, Validating identifications at 1% FDR, Scoring PTMs in peptides, Scoring PTMs in proteins.
            progressCounter += 3; // Scoring PTMs in PSMs. Estimating PTM FLR. Resolving peptide inference issues.

            // add one more just to not start at 0%
            progressCounter++;

            waitingDialog.setMaxProgressValue(progressCounter);
            waitingDialog.increaseProgressValue(); // just to not start at 0%

            boolean needDialog = false;

            // load the identification files
            if (idFiles.size() > 0
                    || searchParameters != null
                    || searchParameters.getFastaFile() != null
                    || spectrumFiles.size() > 0) {
                needDialog = true;
                importIdentificationFiles(waitingDialog);
            }

            if (needDialog) {

                try {
                    waitingDialog.setVisible(true);
                } catch (IndexOutOfBoundsException e) {
                    // ignore
                }
                this.dispose();
            }

            if (!needDialog || !waitingDialog.isRunCanceled()) {
                peptideShakerGUI.setSearchParameters(searchParameters);
                peptideShakerGUI.setProcessingPreferences(processingPreferences);
                peptideShakerGUI.setPtmScoringPreferences(ptmScoringPreferences);
                peptideShakerGUI.updateAnnotationPreferencesFromSearchSettings();
                peptideShakerGUI.setProject(experiment, sample, replicateNumber);
                peptideShakerGUI.setMetrics(peptideShaker.getMetrics());
                peptideShakerGUI.setCache(peptideShaker.getCache());
                peptideShakerGUI.setUpInitialFilters();
                peptideShakerGUI.resetFeatureGenerator();
                peptideShakerGUI.displayResults();
                peptideShakerGUI.initiateDisplay(); // display the overview tab
                peptideShakerGUI.getProjectDetails().setReport(waitingDialog.getReport(null));
                this.dispose();
            } else if (waitingDialog.isRunCanceled()) {

                // close the database
                try {
                    ProteomicAnalysis proteomicAnalysis = experiment.getAnalysisSet(sample).getProteomicAnalysis(replicateNumber);
                    Identification identification = proteomicAnalysis.getIdentification(IdentificationMethod.MS2_IDENTIFICATION);
                    identification.close();
                } catch (SQLException e) {
                    System.out.println("Failed to close the database!");
                    e.printStackTrace();
                }
            }
        }
}//GEN-LAST:event_openButtonActionPerformed

    /**
     * Clear the database field.
     *
     * @param evt
     */
    private void clearDbButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearDbButtonActionPerformed
        fastaFileTxt.setText("");
        validateInput();
}//GEN-LAST:event_clearDbButtonActionPerformed

    /**
     * Opens a file chooser where the user can select the database FATA file to
     * use.
     *
     * @param evt
     */
    private void browseDbButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseDbButtonActionPerformed

        JFileChooser fileChooser;

        if (searchParameters != null) {
            fileChooser = new JFileChooser(searchParameters.getFastaFile()); 
        } else {
            fileChooser = new JFileChooser(peptideShakerGUI.getLastSelectedFolder()); 
        }

        fileChooser.setDialogTitle("Select FASTA File(s)");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);

        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File myFile) {
                return myFile.getName().toLowerCase().endsWith("fasta")
                        || myFile.getName().toLowerCase().endsWith("fast")
                        || myFile.getName().toLowerCase().endsWith("fas")
                        || myFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Supported formats: FASTA (.fasta)";
            }
        };

        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showDialog(this.getParent(), "Open");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fastaFile = fileChooser.getSelectedFile();
            peptideShakerGUI.setLastSelectedFolder(fastaFile.getAbsolutePath());
            fastaFileTxt.setText(fastaFile.getName());
            checkFastaFile(fastaFile);
            if (searchParameters != null) {
                searchParameters.setFastaFile(fastaFile);
            }
        }

        validateInput();
}//GEN-LAST:event_browseDbButtonActionPerformed

    /**
     * Clear the spectra selection.
     *
     * @param evt
     */
    private void clearSpectraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearSpectraActionPerformed
        spectrumFiles = new ArrayList<File>();
        spectrumFilesTxt.setText(spectrumFiles.size() + " file(s) selected");
        validateInput();
}//GEN-LAST:event_clearSpectraActionPerformed

    private void browseSpectraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseSpectraActionPerformed

        // @TODO: implement mzML
        JFileChooser fileChooser = new JFileChooser(peptideShakerGUI.getLastSelectedFolder());
        fileChooser.setDialogTitle("Select Spectrum File(s)");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);

        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File myFile) {
                return myFile.getName().toLowerCase().endsWith("mgf")
                        || myFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Supported formats: Mascot Generic Format (.mgf)";
            }
        };

        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showDialog(this.getParent(), "Add");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            for (File newFile : fileChooser.getSelectedFiles()) {
                if (newFile.isDirectory()) {
                    File[] tempFiles = newFile.listFiles();
                    for (File file : tempFiles) {
                        if (file.getName().endsWith("mgf")) {
                            spectrumFiles.add(file);
                        }
                    }
                } else {
                    spectrumFiles.add(newFile);
                }
                peptideShakerGUI.setLastSelectedFolder(newFile.getAbsolutePath());
            }
            spectrumFilesTxt.setText(spectrumFiles.size() + " file(s) selected");
        }

        validateInput();
}//GEN-LAST:event_browseSpectraActionPerformed

    private void clearIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearIdActionPerformed
        idFiles = new ArrayList<File>();
        idFilesTxt.setText(idFiles.size() + " file(s) selected");
        searchParameters = new SearchParameters();
        validateInput();
}//GEN-LAST:event_clearIdActionPerformed

    private void browseIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseIdActionPerformed

        JFileChooser fileChooser = new JFileChooser(peptideShakerGUI.getLastSelectedFolder());
        fileChooser.setDialogTitle("Select Identification File(s)");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        ArrayList<File> folders = new ArrayList<File>();

        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File myFile) {

                if (myFile.getName().equalsIgnoreCase("mods.xml")
                        || myFile.getName().equalsIgnoreCase("usermods.xml")) {
                    return false;
                }

                return myFile.getName().toLowerCase().endsWith("dat")
                        || myFile.getName().toLowerCase().endsWith("omx")
                        || myFile.getName().toLowerCase().endsWith("xml")
                        || myFile.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Supported formats: Mascot (.dat), OMSSA (.omx), X!Tandem (.xml)";
            }
        };

        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showDialog(this.getParent(), "Add");

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ArrayList<File> parameterFiles = new ArrayList<File>();
            for (File newFile : fileChooser.getSelectedFiles()) {
                if (newFile.isDirectory()) {
                    folders.add(newFile);
                    File[] tempFiles = newFile.listFiles();
                    for (File file : tempFiles) {
                        if (file.getName().toLowerCase().endsWith("dat")
                                || file.getName().toLowerCase().endsWith("omx")
                                || file.getName().toLowerCase().endsWith("xml")) {
                            if (!file.getName().equals("mods.xml")
                                    && !file.getName().equals("usermods.xml")) {
                                idFiles.add(file);
                            } else if (file.getName().endsWith("usermods.xml")) {
                                modificationFiles.add(file);
                            }
                        } else if (file.getName().toLowerCase().endsWith(".parameters")) {
                            boolean found = false;
                            for (File tempFile : parameterFiles) {
                                if (tempFile.getName().equals(file.getName())) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                parameterFiles.add(file);
                            }
                        }
                    }
                } else {
                    folders.add(newFile.getParentFile());
                    idFiles.add(newFile);
                    for (File file : newFile.getParentFile().listFiles()) {
                        if (file.getName().toLowerCase().endsWith(".parameters")) {
                            if (!parameterFiles.contains(file)) {
                                parameterFiles.add(file);
                            }
                        }
                        if (file.getName().endsWith("usermods.xml")) {
                            modificationFiles.add(file);
                        }
                    }
                }
                peptideShakerGUI.setLastSelectedFolder(newFile.getAbsolutePath());
            }

            File parameterFile = null;
            if (parameterFiles.size() == 1) {
                parameterFile = parameterFiles.get(0);
            } else if (parameterFiles.size() > 1) {
                FileSelectionDialog fileSelection = new FileSelectionDialog(peptideShakerGUI, parameterFiles, "Please select the SearchGUI parameters corresponding to the imported identifications.");
                if (!fileSelection.isCanceled()) {
                    parameterFile = fileSelection.getSelectedFile();
                }
            }
            if (parameterFile != null) {
                importSearchParameters(parameterFile);
            }
            boolean importSuccessfull = true;

            for (int i = 0; i < folders.size() && importSuccessfull; i++) {
                File folder = folders.get(i);
                File inputFile = new File(folder, SEARCHGUI_INPUT);
                if (inputFile.exists()) {
                    importSuccessfull = importMgfFiles(inputFile);
                }
            }
            idFilesTxt.setText(idFiles.size() + " file(s) selected");
        }

        validateInput();
}//GEN-LAST:event_browseIdActionPerformed

    /**
     * Open the SearchPreferences dialog.
     *
     * @param evt
     */
    private void editSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSearchButtonActionPerformed
        SearchPreferencesDialog searchPreferencesDialog = new SearchPreferencesDialog(peptideShakerGUI, true, searchParameters, peptideShakerGUI.loadPrideToPtmMap(), peptideShakerGUI.getSelectedRowHtmlTagFontColor(), peptideShakerGUI.getNotSelectedRowHtmlTagFontColor());
        if (!searchPreferencesDialog.isCanceled()) {
            try {
            searchPreferencesDialog.updatePtmToPrideMap();
            } catch (Exception e) {
                peptideShakerGUI.catchException(e);
            }
            searchParameters = searchPreferencesDialog.getSearchParameters();
            searchTxt.setText("user defined");
            validateInput();
        }
    }//GEN-LAST:event_editSearchButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void openDialogHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openDialogHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
}//GEN-LAST:event_openDialogHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void openDialogHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_openDialogHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_openDialogHelpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void openDialogHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDialogHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/OpenDialog.html"),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/help.GIF")),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                "PeptideShaker - Help");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
}//GEN-LAST:event_openDialogHelpJButtonActionPerformed

    private void editImportFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editImportFilterButtonActionPerformed
        new ImportSettingsDialog(peptideShakerGUI, this, true);
    }//GEN-LAST:event_editImportFilterButtonActionPerformed

    /**
     * Change the icon to a hand icon.
     *
     * @param evt
     */
    private void exampleFilesLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exampleFilesLabelMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_exampleFilesLabelMouseEntered

    /**
     * Change the icon to the default icon.
     *
     * @param evt
     */
    private void exampleFilesLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exampleFilesLabelMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exampleFilesLabelMouseExited

    /**
     * Open the example file web page.
     *
     * @param evt
     */
    private void exampleFilesLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exampleFilesLabelMouseClicked
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        BareBonesBrowserLaunch.openURL("http://code.google.com/p/peptide-shaker/downloads/detail?name=peptide-shaker_test_files.zip");
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exampleFilesLabelMouseClicked

    /**
     * Closes the dialog.
     *
     * @param evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        // @TODO: this does not work! have to create a new object and transfer all the values...

        // reset the preferences as this can have been changed
//        peptideShakerGUI.setSearchParameters(oldSearchParameters);
//        peptideShakerGUI.setModificationProfileFile(oldProfileFile);
//        peptideShakerGUI.setIdFilter(oldIdFilter);

        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void projectNameIdTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_projectNameIdTxtKeyReleased
        validateInput();
    }//GEN-LAST:event_projectNameIdTxtKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void sampleNameIdtxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sampleNameIdtxtKeyReleased
        validateInput();
    }//GEN-LAST:event_sampleNameIdtxtKeyReleased

    /**
     * Validate the input.
     *
     * @param evt
     */
    private void replicateNumberIdtxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_replicateNumberIdtxtKeyReleased
        validateInput();
    }//GEN-LAST:event_replicateNumberIdtxtKeyReleased

    /**
     * Open the ProcessingPreferences dialog.
     *
     * @param evt
     */
    private void editPreferencesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPreferencesButtonActionPerformed
        new ProcessingPreferencesDialog(peptideShakerGUI, true, processingPreferences, ptmScoringPreferences);
        if (processingPreferences.getProteinFDR() != 1
                || processingPreferences.getPeptideFDR() != 1
                || processingPreferences.getPsmFDR() != 1
                || ptmScoringPreferences.getFlrThreshold() != 1
                || ptmScoringPreferences.isaScoreNeutralLosses()) {
            preferencesTxt.setText("User Defined");
        } else if (!ptmScoringPreferences.aScoreCalculation()) {
            preferencesTxt.setText("Reduced PTM specificity");
        } else {
            preferencesTxt.setText("Default");
        }
    }//GEN-LAST:event_editPreferencesButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseDbButton;
    private javax.swing.JButton browseId;
    private javax.swing.JButton browseSpectra;
    private javax.swing.JButton clearDbButton;
    private javax.swing.JButton clearId;
    private javax.swing.JButton clearSpectra;
    private javax.swing.JLabel databaseLabel;
    private javax.swing.JButton editImportFilterButton;
    private javax.swing.JButton editPreferencesButton;
    private javax.swing.JButton editSearchButton;
    private javax.swing.JLabel exampleFilesLabel;
    private javax.swing.JTextField fastaFileTxt;
    private javax.swing.JLabel helpLabel;
    private javax.swing.JLabel idFilesLabel;
    private javax.swing.JTextField idFilesTxt;
    private javax.swing.JTextField importFilterTxt;
    private javax.swing.JLabel importFiltersLabel;
    private javax.swing.JLabel importFiltersLabel1;
    private javax.swing.JPanel inputFilesPanel;
    private javax.swing.JButton openButton;
    private javax.swing.JButton openDialogHelpJButton;
    private javax.swing.JTextField preferencesTxt;
    private javax.swing.JPanel processingParametersPanel;
    private javax.swing.JPanel projectDetailsPanel;
    private javax.swing.JTextField projectNameIdTxt;
    private javax.swing.JLabel projectReferenceLabel;
    private javax.swing.JLabel replicateLabel;
    private javax.swing.JTextField replicateNumberIdtxt;
    private javax.swing.JPanel sampleDetailsPanel;
    private javax.swing.JTextField sampleNameIdtxt;
    private javax.swing.JLabel sampleNameLabel;
    private javax.swing.JLabel searchParamsLabel;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JLabel spectrumFilesLabel;
    private javax.swing.JTextField spectrumFilesTxt;
    // End of variables declaration//GEN-END:variables

    /**
     * Sets the filter settings field to the given text.
     *
     * @param text
     */
    public void updateFilterSettingsField(String text) {
        importFilterTxt.setText(text);
    }

    /**
     * Sets the search params field to the given text.
     *
     * @param text
     */
    public void updateSearchParamsField(String text) {
        searchTxt.setText(text);
    }

    /**
     * Validates the input parameters.
     *
     * @return true if the input is valid, false otherwise.
     */
    private void validateInput() {

        boolean allValid = true;

        // highlight the fields that have not been filled
        if (projectNameIdTxt.getText().length() > 0) {
            projectReferenceLabel.setForeground(Color.BLACK);
            projectReferenceLabel.setToolTipText(null);
            projectNameIdTxt.setToolTipText(null);
        } else {
            projectReferenceLabel.setForeground(Color.RED);
            projectReferenceLabel.setToolTipText("Please provide a project reference.");
            projectNameIdTxt.setToolTipText("Please provide a project reference.");
            allValid = false;
        }

        if (sampleNameIdtxt.getText().length() > 0) {
            sampleNameLabel.setForeground(Color.BLACK);
            sampleNameLabel.setToolTipText(null);
            sampleNameIdtxt.setToolTipText(null);
        } else {
            sampleNameLabel.setForeground(Color.RED);
            sampleNameLabel.setToolTipText("Please provide a project sample name.");
            sampleNameIdtxt.setToolTipText("Please provide a project sample name.");
            allValid = false;
        }

        if (replicateNumberIdtxt.getText().length() > 0) {
            replicateLabel.setForeground(Color.BLACK);
            replicateLabel.setToolTipText(null);
            replicateNumberIdtxt.setToolTipText(null);
        } else {
            replicateLabel.setForeground(Color.RED);
            replicateLabel.setToolTipText("Please provide a replicate number.");
            replicateNumberIdtxt.setToolTipText("Please provide a replicate number.");
            allValid = false;
        }

        if (idFiles.size() > 0) {
            idFilesLabel.setForeground(Color.BLACK);
            idFilesLabel.setToolTipText(null);
            idFilesTxt.setToolTipText(null);
        } else {
            idFilesLabel.setForeground(Color.RED);
            idFilesLabel.setToolTipText("Please select at least one identification file.");
            idFilesTxt.setToolTipText("Please select at least one identification file.");
            allValid = false;
        }

        if (spectrumFiles.size() > 0) {
            spectrumFilesLabel.setForeground(Color.BLACK);
            idFilesLabel.setToolTipText(null);
            spectrumFilesTxt.setToolTipText(null);
        } else {
            spectrumFilesLabel.setForeground(Color.RED);
            idFilesLabel.setToolTipText("Please select at least one identification file.");
            spectrumFilesTxt.setToolTipText("Please select the spectrum file(s) for the identfication files.");
            allValid = false;
        }

        if (fastaFileTxt.getText().length() > 0) {
            databaseLabel.setForeground(Color.BLACK);
            databaseLabel.setToolTipText(null);
            fastaFileTxt.setToolTipText(null);
        } else {
            databaseLabel.setForeground(Color.RED);
            databaseLabel.setToolTipText("Please select the database file used.");
            fastaFileTxt.setToolTipText("Please select the database file used.");
            allValid = false;
        }

        // enable/disable the Create! button
        openButton.setEnabled(allValid);
    }

    /**
     * Validates the format of the replicate number and the FASTA file.
     *
     * @return true if the input is valid, false otherwise.
     */
    private boolean validateUserInput() {

        for (String forbiddenChar : Util.forbiddenCharacters) {
            if (projectNameIdTxt.getText().contains(forbiddenChar)) {
                JOptionPane.showMessageDialog(null, "The project name should not contain " + forbiddenChar + ".\n"
                        + "Forbidden character in project name",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                projectNameIdTxt.setForeground(Color.red);
                return false;
            }
        }
        for (String forbiddenChar : Util.forbiddenCharacters) {
            if (sampleNameIdtxt.getText().contains(forbiddenChar)) {
                JOptionPane.showMessageDialog(null, "The sample name should not contain " + forbiddenChar + ".\n"
                        + "Forbidden character in sample name",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                sampleNameIdtxt.setForeground(Color.red);
                return false;
            }
        }

        try {
            getReplicateNumber();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please verify the input for replicate number.\n"
                    + "Has to be a number!",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            replicateLabel.setForeground(Color.RED);
            return false;
        }

        if (searchParameters == null) {
            JOptionPane.showMessageDialog(null, "Please edit the search parameters.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (searchParameters.getFastaFile() == null) {
            JOptionPane.showMessageDialog(null, "Please verify the input for FASTA file.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Returns the replicate number.
     *
     * @return the replicate number
     */
    private int getReplicateNumber() {
        return new Integer(replicateNumberIdtxt.getText().trim());
    }

    /**
     * Imports identifications from identification files.
     *
     * @param waitingDialog a dialog to display feedback to the user
     */
    private void importIdentificationFiles(WaitingDialog waitingDialog) {

        peptideShaker.importFiles(waitingDialog, peptideShakerGUI.getIdFilter(), idFiles,
                spectrumFiles, searchParameters,
                peptideShakerGUI.getAnnotationPreferences(), peptideShakerGUI.getProjectDetails(),
                processingPreferences, ptmScoringPreferences, peptideShakerGUI.getSpectrumCountingPreferences());
    }

    /**
     * Imports the search parameters from a searchGUI file.
     *
     * @param file the selected searchGUI file
     */
    public void importSearchParameters(File file) {

        try {
            searchParameters = SearchParameters.getIdentificationParameters(file);
            PeptideShaker.loadModifications(searchParameters);
        } catch (Exception e) {
            try {
                // Old school format, overwrite old file
                Properties props = loadProperties(file);
                searchParameters = IdentificationParametersReader.getSearchParameters(props);

                String fileName = file.getName();
                if (fileName.endsWith(".properties")) {
                    String newName = fileName.substring(0, fileName.lastIndexOf(".")) + ".parameters";
                    try {
                        file.delete();
                    } catch (Exception deleteException) {
                        deleteException.printStackTrace();
                    }
                    file = new File(file.getParentFile(), newName);
                }
                SearchParameters.saveIdentificationParameters(searchParameters, file);
            } catch (Exception saveException) {
                e.printStackTrace();
                saveException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error occured while reading " + file + ". Please verify the search paramters.", "File error", JOptionPane.ERROR_MESSAGE);
            }
        }

        ModificationProfile modificationProfile = searchParameters.getModificationProfile();

        ArrayList<String> missing = new ArrayList<String>();

        for (String name : modificationProfile.getAllNotFixedModifications()) {
            if (!ptmFactory.containsPTM(name)) {
                missing.add(name);
            } else {
                PTM ptm = ptmFactory.getPTM(name);
                if (ptm.getShortName() == null) {
                    int index = name.length() - 1;
                    if (name.lastIndexOf(" ") > 0) {
                        index = name.indexOf(" ");
                    }
                    if (name.lastIndexOf("-") > 0) {
                        index = Math.min(index, name.indexOf("-"));
                    }
                    ptm.setShortName(name.substring(0, index));
                }
                if (modificationProfile.getColor(name) == null) {
                    searchParameters.getModificationProfile().setColor(name, Color.lightGray);
                }
            }
        }
        if (!missing.isEmpty()) {
            // Might happen with old parameters files or when no parameter file is found
            for (File modFile : modificationFiles) {
                try {
                    ptmFactory.importModifications(modFile, true);
                } catch (Exception e) {
                    // ignore error
                }
            }
            ArrayList<String> missing2 = new ArrayList<String>();
            for (String ptmName : missing) {
                if (!ptmFactory.containsPTM(ptmName)) {
                    missing2.add(ptmName);
                }
            }
            if (!missing2.isEmpty()) {
                if (missing2.size() == 1) {
                    JOptionPane.showMessageDialog(this, "The following modification is currently not recognized by PeptideShaker: "
                            + missing2.get(0) + ".\nPlease import it by editing the search parameters.", "Modification Not Found", JOptionPane.WARNING_MESSAGE);
                } else {
                    String output = "The following modifications are currently not recognized by PeptideShaker:\n";
                    boolean first = true;
                    for (String ptm : missing2) {
                        if (first) {
                            first = false;
                        } else {
                            output += ", ";
                        }
                        output += ptm;
                    }
                    output += ".\nPlease import it by editing the search parameters.";
                    JOptionPane.showMessageDialog(this, output, "Modification Not Found", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        File fastaFile = searchParameters.getFastaFile();
        if (fastaFile.exists()) {
            fastaFileTxt.setText(fastaFile.getName());
            checkFastaFile(fastaFile);
        } else {
            // try to find it in the same folder as the SearchGUI.properties file
            if (new File(file.getParentFile(), fastaFile.getName()).exists()) {
                fastaFile = new File(file.getParentFile(), fastaFile.getName());
                searchParameters.setFastaFile(fastaFile);
                fastaFileTxt.setText(fastaFile.getName());
                checkFastaFile(fastaFile);
            } else {
                JOptionPane.showMessageDialog(this, "FASTA file \'" + fastaFile.getName() + "\' not found.\nPlease locate it manually.", "File Not Found", JOptionPane.WARNING_MESSAGE);
            }
        }

        searchTxt.setText(file.getName().substring(0, file.getName().lastIndexOf(".")));
        importFilterTxt.setText(file.getName().substring(0, file.getName().lastIndexOf(".")));

        if (!searchParameters.getEnzyme().enzymeCleaves()) {
            // create an empty label to put the message in
            JLabel label = new JLabel();

            // html content 
            JEditorPane ep = new JEditorPane("text/html", "<html><body bgcolor=\"#" + Util.color2Hex(label.getBackground()) + "\">"
                    + "The cleavage site of the selected enzyme is not configured.<br><br>"
                    + "PeptideShaker functionalities will be limited.<br><br>"
                    + "Edit enzyme configuration in:<br>"
                    + "<i>peptideshaker_enzymes.xml</i> located in the conf folder.<br><br>"
                    + "For more information on enzymes, contact us via:<br>"
                    + "<a href=\"http://groups.google.com/group/peptide-shaker\">http://groups.google.com/group/peptide-shaker</a>."
                    + "</body></html>");

            // handle link events 
            ep.addHyperlinkListener(new HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                        BareBonesBrowserLaunch.openURL(e.getURL().toString());
                    }
                }
            });

            ep.setBorder(null);
            ep.setEditable(false);

            JOptionPane.showMessageDialog(this, ep, "Enzyme Not Configured", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method loads the necessary parameters for populating (part of) the
     * GUI from a properties file.
     *
     * @deprecated use SearchParameters instead
     * @param aFile File with the relevant properties file.
     * @return Properties with the loaded properties.
     */
    private Properties loadProperties(File aFile) {
        Properties screenProps = new Properties();
        try {
            FileInputStream fis = new FileInputStream(aFile);
            if (fis != null) {
                screenProps.load(fis);
                fis.close();
            } else {
                throw new IllegalArgumentException("Could not read the file you specified ('" + aFile.getAbsolutePath() + "').");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            JOptionPane.showMessageDialog(this, new String[]{"Unable to read file: " + aFile.getName(), ioe.getMessage()}, "Error Reading File", JOptionPane.WARNING_MESSAGE);
        }
        return screenProps;
    }

    /**
     * Imports the mgf files from a searchGUI file.
     *
     * @param searchGUIFile a searchGUI file @returns true of the mgf files were
     * imported successfully
     */
    private boolean importMgfFiles(File searchGUIFile) {

        boolean success = true;

        try {
            BufferedReader br = new BufferedReader(new FileReader(searchGUIFile));
            String line;
            ArrayList<String> names = new ArrayList<String>();
            String missing = "";
            for (File file : spectrumFiles) {
                names.add(file.getName());
            }
            while ((line = br.readLine()) != null) {
                // Skip empty lines.
                line = line.trim();
                if (line.equals("")) {
                } else {
                    try {
                        File newFile = new File(line);
                        if (!names.contains(newFile.getName())) {
                            if (newFile.exists()) {
                                names.add(newFile.getName());
                                spectrumFiles.add(newFile);
                            } else {
                                // try to find it in the same folder as the SearchGUI.properties file
                                if (new File(searchGUIFile.getParentFile(), newFile.getName()).exists()) {
                                    names.add(new File(searchGUIFile.getParentFile(), newFile.getName()).getName());
                                    spectrumFiles.add(new File(searchGUIFile.getParentFile(), newFile.getName()));
                                } else {
                                    missing += newFile.getName() + "\n";
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!missing.equals("")) {
                JOptionPane.showMessageDialog(this, "Input file(s) not found:\n" + missing
                        + "\nPlease locate them manually.", "File Not Found", JOptionPane.WARNING_MESSAGE);
                success = false;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        spectrumFilesTxt.setText(spectrumFiles.size() + " file(s) selected");

        return success;
    }

    /**
     * Creates the project details for this new project.
     *
     * @return the project details
     */
    private ProjectDetails getProjectDetails() {
        ProjectDetails projectDetails = new ProjectDetails();
        projectDetails.setCreationDate(new Date());
        projectDetails.setIdentificationFiles(idFiles);

        return projectDetails;
    }

    /**
     * Checks the FASTA file: 1) if it's a UniProt database, and 2) that it's a
     * target-decoy database. Shows warnings if one of these is false.
     *
     * @param fastaFile the fasta file to test
     */
    private void checkFastaFile(File file) {

        progressDialog = new ProgressDialogX(peptideShakerGUI,
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                true);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Checking FASTA File. Please Wait...");

        final NewDialog finalRef = this;
        final File fastaFile = file;

        new Thread(new Runnable() {

            public void run() {
                try {
                    progressDialog.setVisible(true);
                } catch (IndexOutOfBoundsException e) {
                    // ignore
                }
            }
        }, "ProgressDialog").start();

        new Thread("DisplayThread") {

            @Override
            public void run() {

                try {
                    sequenceFactory.loadFastaFile(fastaFile, progressDialog);
                    progressDialog.setRunFinished();

                    String firstAccession = sequenceFactory.getAccessions().get(0);
                    if (sequenceFactory.getHeader(firstAccession).getDatabaseType() != DatabaseType.UniProt) {
                        showDataBaseHelpDialog();
                    }

                    if (!sequenceFactory.concatenatedTargetDecoy()) {
                        JOptionPane.showMessageDialog(finalRef, "PeptideShaker validation requires the use of a taget-decoy database.\n"
                                + "Some features will be limited if using other types of databases.\n\n"
                                + "Note that using Automatic Decoy Search in Mascot is not supported.\n\n"
                                + "See the PeptideShaker home page for details.",
                                "No Decoys Found",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(finalRef, "File " + fastaFile + " was not found. Please select a different FASTA file.", "File Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    fastaFileTxt.setText("");
                    validateInput();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(finalRef, "An error occured while loading " + fastaFile + ".", "File Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    fastaFileTxt.setText("");
                    validateInput();
                } catch (InterruptedException e) {
                    JOptionPane.showMessageDialog(finalRef, "An error occured while loading " + fastaFile + ".", "File Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    fastaFileTxt.setText("");
                    validateInput();
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(finalRef, e.getLocalizedMessage() + "\n" + "Please refer to the troubleshooting section at http://peptide-shaker.googlecode.com.",
                            "File Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    fastaFileTxt.setText("");
                    validateInput();
                } catch (ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(finalRef, "Serialization issue while processing the FASTA file. Please delete the .fasta.cui file and retry.\n"
                            + "If the error occurs again please report bug at http://peptide-shaker.googlecode.com.", "File Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    fastaFileTxt.setText("");
                    validateInput();
                }
            }
        }.start();
    }

    /**
     * Show a simple dialog saying that UniProt databases is recommended and
     * display a link to the Database Help web page.
     */
    private void showDataBaseHelpDialog() {

        // create an empty label to put the message in
        JLabel label = new JLabel();

        // html content 
        JEditorPane ep = new JEditorPane("text/html", "<html><body bgcolor=\"#" + Util.color2Hex(label.getBackground()) + "\">"
                + "We strongly recommend the use of UniProt databases. Some<br>"
                + "features will be limited if using other databases.<br><br>"
                + "See <a href=\"http://code.google.com/p/searchgui/wiki/DatabaseHelp\">Database Help</a> for details."
                + "</body></html>");

        // handle link events 
        ep.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                    BareBonesBrowserLaunch.openURL(e.getURL().toString());
                }
            }
        });

        ep.setBorder(null);
        ep.setEditable(false);

        progressDialog.displayHtmlMessage(ep, "Database Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
