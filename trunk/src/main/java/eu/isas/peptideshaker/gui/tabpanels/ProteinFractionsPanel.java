package eu.isas.peptideshaker.gui.tabpanels;

import com.compomics.util.Util;
import com.compomics.util.examples.BareBonesBrowserLaunch;
import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.gui.GuiUtilities;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import eu.isas.peptideshaker.gui.ExportGraphicsDialog;
import eu.isas.peptideshaker.gui.FractionDetailsDialog;
import eu.isas.peptideshaker.gui.HelpDialog;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import eu.isas.peptideshaker.gui.protein_sequence.ProteinSequencePanel;
import eu.isas.peptideshaker.gui.protein_sequence.ProteinSequencePanelParent;
import eu.isas.peptideshaker.gui.protein_sequence.ResidueAnnotation;
import eu.isas.peptideshaker.gui.tablemodels.ProteinTableModel;
import eu.isas.peptideshaker.myparameters.PSParameter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import no.uib.jsparklines.extra.HtmlLinksRenderer;
import no.uib.jsparklines.extra.TrueFalseIconRenderer;
import no.uib.jsparklines.data.JSparklinesDataSeries;
import no.uib.jsparklines.data.JSparklinesDataset;
import no.uib.jsparklines.extra.ChartPanelTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesBarChartTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesIntegerColorTableCellRenderer;
import no.uib.jsparklines.renderers.JSparklinesTwoValueBarChartTableCellRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

/**
 * Displays information about which fractions the peptides and proteins were
 * detected in.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class ProteinFractionsPanel extends javax.swing.JPanel implements ProteinSequencePanelParent {

    /**
     * A reference to the main PeptideShakerGUI.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * The list of protein keys.
     */
    private ArrayList<String> proteinKeys;
    /**
     * A list of the peptides in the peptide table.
     */
    private ArrayList<String> peptideKeys = new ArrayList<String>();
    /**
     * The protein table column header tooltips.
     */
    private ArrayList<String> proteinTableToolTips;
    /**
     * The peptide table column header tooltips.
     */
    private ArrayList<String> peptideTableToolTips;
    /**
     * The coverage table column header tooltips.
     */
    private ArrayList<String> coverageTableToolTips;
    /**
     * The progress dialog.
     */
    private ProgressDialogX progressDialog;
    /**
     * The sequence factory.
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance();
    /**
     * True if the fraction order has been okey'ed by the user.
     */
    private boolean fractionOrderSet = false;
    /**
     * The default line width for the line plots.
     */
    private final int LINE_WIDTH = 4;

    /**
     * Indexes for the three main data tables.
     */
    private enum TableIndex {

        PROTEIN_TABLE
    };

    /**
     * Creates a new ProteinFractionsPanel.
     *
     * @param peptideShakerGUI
     */
    public ProteinFractionsPanel(PeptideShakerGUI peptideShakerGUI) {
        initComponents();
        this.peptideShakerGUI = peptideShakerGUI;
        setupGui();
        setTableProperties();
        formComponentResized(null);
    }

    /**
     * Set up the GUI.
     */
    private void setupGui() {

        // make the tabs in the tabbed pane go from right to left
        peptidesTabbedPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // set main table properties
        proteinTable.getTableHeader().setReorderingAllowed(false);

        proteinTable.setAutoCreateRowSorter(true);

        // make sure that the scroll panes are see-through
        proteinTableScrollPane.getViewport().setOpaque(false);
        coverageTableScrollPane.getViewport().setOpaque(false);

        // set up the table header tooltips
        setUpTableHeaderToolTips();

        // correct the color for the upper right corner
        JPanel proteinCorner = new JPanel();
        proteinCorner.setBackground(proteinTable.getTableHeader().getBackground());
        proteinTableScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, proteinCorner);
    }

    /**
     * Set up the table header tooltips.
     */
    private void setUpTableHeaderToolTips() {
        proteinTableToolTips = new ArrayList<String>();
        proteinTableToolTips.add(null);
        proteinTableToolTips.add("Protein Accession Number");
        proteinTableToolTips.add("Protein Description");

        ArrayList<String> fileNames = new ArrayList<String>();

        for (String filePath : peptideShakerGUI.getSearchParameters().getSpectrumFiles()) {
            String fileName = Util.getFileName(filePath);
            fileNames.add(fileName);
        }

        for (int i = 0; i < fileNames.size(); i++) {
            proteinTableToolTips.add(fileNames.get(i));
        }

        proteinTableToolTips.add("Protein Molecular Weight (kDa)");
        proteinTableToolTips.add("Protein Confidence");
        proteinTableToolTips.add("Validated");

        peptideTableToolTips = new ArrayList<String>();
        peptideTableToolTips.add(null);
        peptideTableToolTips.add("Peptide Sequence");

        for (int i = 0; i < fileNames.size(); i++) {
            peptideTableToolTips.add(fileNames.get(i));
        }

        peptideTableToolTips.add("Peptide Confidence");
        peptideTableToolTips.add("Validated");

        coverageTableToolTips = new ArrayList<String>();
        coverageTableToolTips.add(null);
        coverageTableToolTips.add("Fraction");
        coverageTableToolTips.add("Sequence Coverage");
    }

    /**
     * Set up the properties of the tables.
     */
    private void setTableProperties() {
        setProteinTableProperties();
        setCoverageTableProperties();
    }

    /**
     * Set up the properties of the protein table.
     */
    private void setProteinTableProperties() {

        // the index column
        proteinTable.getColumn(" ").setMaxWidth(50);
        proteinTable.getColumn(" ").setMinWidth(50);

        try {
            proteinTable.getColumn("Confidence").setMaxWidth(90);
            proteinTable.getColumn("Confidence").setMinWidth(90);
        } catch (IllegalArgumentException w) {
            proteinTable.getColumn("Score").setMaxWidth(90);
            proteinTable.getColumn("Score").setMinWidth(90);
        }

        // the validated column
        proteinTable.getColumn("").setMaxWidth(30);
        proteinTable.getColumn("").setMinWidth(30);

        // the selected columns
        proteinTable.getColumn("  ").setMaxWidth(30);
        proteinTable.getColumn("  ").setMinWidth(30);

        // the protein inference column
        proteinTable.getColumn("PI").setMaxWidth(37);
        proteinTable.getColumn("PI").setMinWidth(37);

        // set up the protein inference color map
        HashMap<Integer, Color> proteinInferenceColorMap = new HashMap<Integer, Color>();
        proteinInferenceColorMap.put(PSParameter.NOT_GROUP, peptideShakerGUI.getSparklineColor()); // NOT_GROUP
        proteinInferenceColorMap.put(PSParameter.ISOFORMS, Color.YELLOW); // ISOFORMS
        proteinInferenceColorMap.put(PSParameter.ISOFORMS_UNRELATED, Color.ORANGE); // ISOFORMS_UNRELATED
        proteinInferenceColorMap.put(PSParameter.UNRELATED, Color.RED); // UNRELATED

        // set up the protein inference tooltip map
        HashMap<Integer, String> proteinInferenceTooltipMap = new HashMap<Integer, String>();
        proteinInferenceTooltipMap.put(PSParameter.NOT_GROUP, "Single Protein");
        proteinInferenceTooltipMap.put(PSParameter.ISOFORMS, "Isoforms");
        proteinInferenceTooltipMap.put(PSParameter.ISOFORMS_UNRELATED, "Unrelated Isoforms");
        proteinInferenceTooltipMap.put(PSParameter.UNRELATED, "Unrelated Proteins");

        proteinTable.getColumn("Accession").setCellRenderer(new HtmlLinksRenderer(peptideShakerGUI.getSelectedRowHtmlTagFontColor(), peptideShakerGUI.getNotSelectedRowHtmlTagFontColor()));
        proteinTable.getColumn("PI").setCellRenderer(new JSparklinesIntegerColorTableCellRenderer(peptideShakerGUI.getSparklineColor(), proteinInferenceColorMap, proteinInferenceTooltipMap));
        proteinTable.getColumn("#Peptides").setCellRenderer(new JSparklinesTwoValueBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0,
                peptideShakerGUI.getSparklineColor(), peptideShakerGUI.getSparklineColorNonValidated(), false));
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Peptides").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth(), new DecimalFormat("0"));
        proteinTable.getColumn("#Spectra").setCellRenderer(new JSparklinesTwoValueBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0,
                peptideShakerGUI.getSparklineColor(), peptideShakerGUI.getSparklineColorNonValidated(), false));
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Spectra").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth(), new DecimalFormat("0"));
        proteinTable.getColumn("MS2 Quant.").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 10.0, peptideShakerGUI.getSparklineColor()));
        ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MS2 Quant.").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth());
        proteinTable.getColumn("MW").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 10.0, peptideShakerGUI.getSparklineColor()));
        ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MW").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth());

        try {
            proteinTable.getColumn("Confidence").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, peptideShakerGUI.getSparklineColor()));
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Confidence").getCellRenderer()).showNumberAndChart(
                    true, peptideShakerGUI.getLabelWidth() - 20, peptideShakerGUI.getScoreAndConfidenceDecimalFormat());
        } catch (IllegalArgumentException e) {
            proteinTable.getColumn("Score").setCellRenderer(new JSparklinesBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0, peptideShakerGUI.getSparklineColor()));
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Score").getCellRenderer()).showNumberAndChart(
                    true, peptideShakerGUI.getLabelWidth() - 20, peptideShakerGUI.getScoreAndConfidenceDecimalFormat());
        }

        proteinTable.getColumn("Coverage").setCellRenderer(new JSparklinesTwoValueBarChartTableCellRenderer(PlotOrientation.HORIZONTAL, 100.0,
                peptideShakerGUI.getSparklineColor(), peptideShakerGUI.getUtilitiesUserPreferences().getSparklineColorNotFound(), true));
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("Coverage").getCellRenderer()).showNumberAndChart(true, peptideShakerGUI.getLabelWidth(), new DecimalFormat("0.00"));
        proteinTable.getColumn("").setCellRenderer(new TrueFalseIconRenderer(
                new ImageIcon(this.getClass().getResource("/icons/accept.png")),
                new ImageIcon(this.getClass().getResource("/icons/Error_3.png")),
                "Validated", "Not Validated"));
        proteinTable.getColumn("  ").setCellRenderer(new TrueFalseIconRenderer(
                new ImageIcon(this.getClass().getResource("/icons/star_yellow.png")),
                new ImageIcon(this.getClass().getResource("/icons/star_grey.png")),
                new ImageIcon(this.getClass().getResource("/icons/star_grey.png")),
                "Starred", null, null));


        // set the preferred size of the accession column
        Integer width = peptideShakerGUI.getPreferredAccessionColumnWidth(proteinTable, proteinTable.getColumn("Accession").getModelIndex(), 6);

        if (width != null) {
            proteinTable.getColumn("Accession").setMinWidth(width);
            proteinTable.getColumn("Accession").setMaxWidth(width);
        } else {
            proteinTable.getColumn("Accession").setMinWidth(15);
            proteinTable.getColumn("Accession").setMaxWidth(Integer.MAX_VALUE);
        }
    }

    /**
     * Set up the properties of the coverage table.
     */
    private void setCoverageTableProperties() {

        // the index column
        coverageTable.getColumn(" ").setMaxWidth(50);
        coverageTable.getColumn(" ").setMinWidth(50);

        coverageTable.getColumn("Coverage").setCellRenderer(new ChartPanelTableCellRenderer());
    }

    /**
     * Display the results.
     */
    public void displayResults() {

        if (!fractionOrderSet) {
            fractionOrderSet = true;
            new FractionDetailsDialog(peptideShakerGUI, true);
        }

        progressDialog = new ProgressDialogX(peptideShakerGUI,
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                true);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Loading Fractions. Please Wait...");
        progressDialog.setUnstoppable(true);

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    progressDialog.setVisible(true);
                } catch (IndexOutOfBoundsException e) {
                    // ignore
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                proteinKeys = peptideShakerGUI.getIdentificationFeaturesGenerator().getProcessedProteinKeys(progressDialog);

                // update the table model
                if (proteinTable.getModel() instanceof ProteinTableModel) {
                    ((ProteinTableModel) proteinTable.getModel()).updateDataModel(peptideShakerGUI);
                } else {
                    ProteinTableModel proteinTableModel = new ProteinTableModel(peptideShakerGUI);
                    proteinTable.setModel(proteinTableModel);
                }

                DefaultTableModel dm = (DefaultTableModel) proteinTable.getModel();
                dm.fireTableStructureChanged();

                setProteinTableProperties();
                showSparkLines(peptideShakerGUI.showSparklines());

                updateSelection();
                proteinTable.requestFocus();

                ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MW").getCellRenderer()).setMaxValue(peptideShakerGUI.getMetrics().getMaxMW());
                setUpTableHeaderToolTips();

                peptideShakerGUI.setUpdated(PeptideShakerGUI.PROTEIN_FRACTIONS_TAB_INDEX, true);

                // update the border titles
                ((TitledBorder) proteinPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Proteins ("
                        + peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedProteins() + "/" + proteinTable.getRowCount() + ")"
                        + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
                proteinPanel.repaint();

                // enable the contextual export options
                exportProteinsJButton.setEnabled(true);
                exportPeptidesJButton.setEnabled(true);

                progressDialog.setRunFinished();
            }
        });
    }

    /**
     * Update the peptide counts plot.
     */
    private void updatePlots() {

        try {

            // @TODO: this method should be split into smaller methods...

            ArrayList<String> fileNames = new ArrayList<String>();

            for (String filePath : peptideShakerGUI.getSearchParameters().getSpectrumFiles()) {
                String fileName = Util.getFileName(filePath);
                fileNames.add(fileName);
            }

            PSParameter psParameter = new PSParameter();
            DefaultCategoryDataset peptidePlotDataset = new DefaultCategoryDataset();
            DefaultCategoryDataset spectrumPlotDataset = new DefaultCategoryDataset();
            DefaultCategoryDataset intensityPlotDataset = new DefaultCategoryDataset();

            int[] selectedRows = proteinTable.getSelectedRows();

            // disable the coverage tab if more than one protein is selected
            peptidesTabbedPane.setEnabledAt(1, selectedRows.length == 1);

            if (selectedRows.length > 1) {
                peptidesTabbedPane.setSelectedIndex(4);
            }

            for (int row = 0; row < selectedRows.length; row++) {

                int currentRow = selectedRows[row];

                int proteinIndex = proteinTable.convertRowIndexToModel(currentRow);
                String proteinKey = proteinKeys.get(proteinIndex);
                ProteinMatch proteinMatch = peptideShakerGUI.getIdentification().getProteinMatch(proteinKey);
                peptideKeys = peptideShakerGUI.getIdentificationFeaturesGenerator().getSortedPeptideKeys(proteinKey);

                String currentProteinSequence = "";

                try {
                    currentProteinSequence = sequenceFactory.getProtein(proteinMatch.getMainMatch()).getSequence();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int[][] coverage = new int[fileNames.size()][currentProteinSequence.length() + 1];

                // get the chart data
                for (int i = 0; i < fileNames.size(); i++) {

                    String fraction = fileNames.get(i);
                    int validatedPeptideCounter = 0;
                    int notValidatedPeptideCounter = 0;

                    for (int j = 0; j < peptideKeys.size(); j++) {

                        String peptideKey = peptideKeys.get(j);

                        try {
                            psParameter = (PSParameter) peptideShakerGUI.getIdentification().getPeptideMatchParameter(peptideKey, psParameter);

                            if (psParameter.getFractions() != null && psParameter.getFractions().contains(fraction)) {
                                if (psParameter.isValidated()) {
                                    validatedPeptideCounter++;

                                    String peptideSequence = Peptide.getSequence(peptideKey);
                                    String tempSequence = currentProteinSequence;

                                    boolean includePeptide = false;

                                    if (coverageShowAllPeptidesJRadioButtonMenuItem.isSelected()) {
                                        includePeptide = true;
                                    } else if (coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItem.isSelected()) {
                                        if (peptideSequence.endsWith("R") || peptideSequence.endsWith("K")) { // @TODO: this test should be made more generic!!!
                                            includePeptide = true;
                                        }
                                    } else if (coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItem.isSelected()) {
                                        if (!peptideSequence.endsWith("R") && !peptideSequence.endsWith("K")) { // @TODO: this test should be made more generic!!!
                                            includePeptide = true;
                                        }
                                    }

                                    if (includePeptide && selectedRows.length == 1) {

                                        while (tempSequence.lastIndexOf(peptideSequence) >= 0) {
                                            int peptideTempStart = tempSequence.lastIndexOf(peptideSequence) + 1;
                                            int peptideTempEnd = peptideTempStart + peptideSequence.length();
                                            for (int k = peptideTempStart; k < peptideTempEnd; k++) {
                                                coverage[i][k]++;
                                            }
                                            tempSequence = currentProteinSequence.substring(0, peptideTempStart);
                                        }
                                    }
                                } else {
                                    notValidatedPeptideCounter++;
                                }
                            }
                        } catch (Exception e) {
                            peptideShakerGUI.catchException(e);
                        }
                    }
                }

                psParameter = new PSParameter();
                psParameter = (PSParameter) peptideShakerGUI.getIdentification().getProteinMatchParameter(proteinKey, psParameter);

                for (int i = 0; i < fileNames.size(); i++) {
                    String fraction = fileNames.get(i);
                    psParameter = (PSParameter) peptideShakerGUI.getIdentification().getProteinMatchParameter(proteinKey, psParameter);

                    if (selectedRows.length == 1) {
                        peptidePlotDataset.addValue(psParameter.getFractionValidatedPeptides(fraction), "Validated Peptides", "" + (i + 1));
                    } else {
                        peptidePlotDataset.addValue(psParameter.getFractionValidatedPeptides(fraction), proteinMatch.getMainMatch(), "" + (i + 1));
                    }
                }


                // update the coverage table
                if (selectedRows.length == 1) {

                    DefaultTableModel coverageTableModel = (DefaultTableModel) coverageTable.getModel();
                    coverageTableModel.getDataVector().removeAllElements();

                    for (int i = 0; i < fileNames.size(); i++) {

                        // create the coverage plot
                        ArrayList<JSparklinesDataSeries> sparkLineDataSeriesCoverage = new ArrayList<JSparklinesDataSeries>();

                        for (int j = 0; j < currentProteinSequence.length(); j++) {

                            boolean covered = coverage[i][j] > 0;

                            int sequenceCounter = 1;

                            if (covered) {
                                while (j + 1 < coverage[0].length && coverage[i][j + 1] > 0) {
                                    sequenceCounter++;
                                    j++;
                                }
                            } else {
                                while (j + 1 < coverage[0].length && coverage[i][j + 1] == 0) {
                                    sequenceCounter++;
                                    j++;
                                }
                            }


                            ArrayList<Double> data = new ArrayList<Double>();
                            data.add(new Double(sequenceCounter));

                            JSparklinesDataSeries sparklineDataseries;

                            if (covered) {
                                sparklineDataseries = new JSparklinesDataSeries(data, peptideShakerGUI.getSparklineColor(), null);
                            } else {
                                sparklineDataseries = new JSparklinesDataSeries(data, new Color(0, 0, 0, 0), null);
                            }


                            sparkLineDataSeriesCoverage.add(sparklineDataseries);
                        }

                        ChartPanel coverageChart = new ProteinSequencePanel(Color.WHITE).getSequencePlot(this, new JSparklinesDataset(sparkLineDataSeriesCoverage),
                                new HashMap<Integer, ArrayList<ResidueAnnotation>>(), true, true);

                        ((DefaultTableModel) coverageTable.getModel()).addRow(new Object[]{(i + 1), fileNames.get(i), coverageChart});
                    }
                }

                // get the psms per fraction
                for (int i = 0; i < fileNames.size(); i++) {
                    String fraction = fileNames.get(i);
                    psParameter = (PSParameter) peptideShakerGUI.getIdentification().getProteinMatchParameter(proteinKey, psParameter);

                    if (selectedRows.length == 1) {
                        spectrumPlotDataset.addValue(psParameter.getFractionValidatedSpectra(fraction), "Validated Spectra", "" + (i + 1));
                        intensityPlotDataset.addValue(psParameter.getPrecursorIntensityAveragePerFraction(fraction), "Average Intensity", "" + (i + 1));
                    } else {
                        spectrumPlotDataset.addValue(psParameter.getFractionValidatedSpectra(fraction), proteinMatch.getMainMatch(), "" + (i + 1));
                        intensityPlotDataset.addValue(psParameter.getPrecursorIntensityAveragePerFraction(fraction), proteinMatch.getMainMatch(), "" + (i + 1));
                    }
                }
            }


            // molecular mass plot
            DefaultBoxAndWhiskerCategoryDataset mwPlotDataset = new DefaultBoxAndWhiskerCategoryDataset();

            HashMap<String, Double> molecularWeights = peptideShakerGUI.getSearchParameters().getFractionMolecularWeights();
            ArrayList<String> spectrumFiles = peptideShakerGUI.getSearchParameters().getSpectrumFiles();

            for (int i = 0; i < spectrumFiles.size(); i++) {

//                Double mw = null;
//
//                if (molecularWeights != null) {
//                    mw = molecularWeights.get(spectrumFiles.get(i));
//                }

                //mwPlotDataset.addValue(mw, "Expected MW", "" + (i + 1));

                try {
                    if (peptideShakerGUI.getMetrics().getObservedFractionalMassesAll().containsKey(Util.getFileName(spectrumFiles.get(i)))) {
                        mwPlotDataset.add(peptideShakerGUI.getMetrics().getObservedFractionalMassesAll().get(Util.getFileName(spectrumFiles.get(i))), "Observed MW", "" + (i + 1));
                    } else {
                        mwPlotDataset.add(new ArrayList<Double>(), "Observed MW", "" + (i + 1));
                    }
                } catch (ClassCastException e) {
                    // do nothing, no data to show
                }
            }

            // create the peptide chart
            JFreeChart chart = ChartFactory.createBarChart(null, "Fraction", "#Peptides", peptidePlotDataset, PlotOrientation.VERTICAL, false, true, true);
            ChartPanel chartPanel = new ChartPanel(chart);

            AbstractCategoryItemRenderer renderer;

            // set up the renderer
            if (selectedRows.length == 1) {
                renderer = new BarRenderer();
                ((BarRenderer) renderer).setShadowVisible(false);
                renderer.setSeriesPaint(0, peptideShakerGUI.getSparklineColor());
            } else {
                renderer = new LineAndShapeRenderer(true, false);
                for (int i = 0; i < selectedRows.length; i++) {
                    ((LineAndShapeRenderer) renderer).setSeriesStroke(i, new BasicStroke(LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                }
            }

            renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
            chart.getCategoryPlot().setRenderer(renderer);

            // set background color
            chart.getPlot().setBackgroundPaint(Color.WHITE);
            chart.setBackgroundPaint(Color.WHITE);
            chartPanel.setBackground(Color.WHITE);

            // hide the outline
            chart.getPlot().setOutlineVisible(false);

            // clear the peptide plot
            peptidePlotPanel.removeAll();

            // add the new plot
            peptidePlotPanel.add(chartPanel);


            // create the spectrum chart
            chart = ChartFactory.createBarChart(null, "Fraction", "#Spectra", spectrumPlotDataset, PlotOrientation.VERTICAL, false, true, true);
            chartPanel = new ChartPanel(chart);

            // set up the renderer
            if (selectedRows.length == 1) {
                renderer = new BarRenderer();
                ((BarRenderer) renderer).setShadowVisible(false);
                renderer.setSeriesPaint(0, peptideShakerGUI.getSparklineColor());
            } else {
                renderer = new LineAndShapeRenderer(true, false);
                for (int i = 0; i < selectedRows.length; i++) {
                    ((LineAndShapeRenderer) renderer).setSeriesStroke(i, new BasicStroke(LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                }
            }

            renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
            chart.getCategoryPlot().setRenderer(renderer);

            // set background color
            chart.getPlot().setBackgroundPaint(Color.WHITE);
            chart.setBackgroundPaint(Color.WHITE);
            chartPanel.setBackground(Color.WHITE);

            // hide the outline
            chart.getPlot().setOutlineVisible(false);

            // clear the peptide plot
            spectraPlotPanel.removeAll();

            // add the new plot
            spectraPlotPanel.add(chartPanel);


            // create the intensity chart
            chart = ChartFactory.createBarChart(null, "Fraction", "Average Intensity", intensityPlotDataset, PlotOrientation.VERTICAL, false, true, true);
            chartPanel = new ChartPanel(chart);

            // set up the renderer
            if (selectedRows.length == 1) {
                renderer = new BarRenderer();
                ((BarRenderer) renderer).setShadowVisible(false);
                renderer.setSeriesPaint(0, peptideShakerGUI.getSparklineColor());
            } else {
                renderer = new LineAndShapeRenderer(true, false);
                for (int i = 0; i < selectedRows.length; i++) {
                    ((LineAndShapeRenderer) renderer).setSeriesStroke(i, new BasicStroke(LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                }
            }

            renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
            chart.getCategoryPlot().setRenderer(renderer);

            // set background color
            chart.getPlot().setBackgroundPaint(Color.WHITE);
            chart.setBackgroundPaint(Color.WHITE);
            chartPanel.setBackground(Color.WHITE);

            // hide the outline
            chart.getPlot().setOutlineVisible(false);

            // clear the peptide plot
            intensityPlotPanel.removeAll();

            // add the new plot
            intensityPlotPanel.add(chartPanel);


            // create the mw chart
            chart = ChartFactory.createBoxAndWhiskerChart(null, "Fraction", "Expected MW", mwPlotDataset, false);
            chartPanel = new ChartPanel(chart);

            // set up the renderer
            BoxAndWhiskerRenderer boxPlotRenderer = new BoxAndWhiskerRenderer();
            boxPlotRenderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
            boxPlotRenderer.setSeriesPaint(0, peptideShakerGUI.getSparklineColor());
            boxPlotRenderer.setSeriesPaint(1, Color.RED);
            chart.getCategoryPlot().setRenderer(boxPlotRenderer);

            // set background color
            chart.getPlot().setBackgroundPaint(Color.WHITE);
            chart.setBackgroundPaint(Color.WHITE);
            chartPanel.setBackground(Color.WHITE);

            // hide the outline
            chart.getPlot().setOutlineVisible(false);

            // clear the peptide plot
            mwPlotPanel.removeAll();

            // add the new plot
            mwPlotPanel.add(chartPanel);

        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }

        peptidePanel.revalidate();
        peptidePanel.repaint();
    }

    /**
     * Update the selected protein and peptide.
     */
    public void updateSelection() {

        int proteinRow = 0;
        String proteinKey = peptideShakerGUI.getSelectedProteinKey();
        String peptideKey = peptideShakerGUI.getSelectedPeptideKey();
        String psmKey = peptideShakerGUI.getSelectedPsmKey();

        if (proteinKey.equals(PeptideShakerGUI.NO_SELECTION)
                && peptideKey.equals(PeptideShakerGUI.NO_SELECTION)
                && !psmKey.equals(PeptideShakerGUI.NO_SELECTION)) {
            if (peptideShakerGUI.getIdentification().matchExists(psmKey)) {
                try {
                    SpectrumMatch spectrumMatch = peptideShakerGUI.getIdentification().getSpectrumMatch(psmKey);
                    peptideKey = spectrumMatch.getBestAssumption().getPeptide().getKey();
                } catch (Exception e) {
                    peptideShakerGUI.catchException(e);
                    return;
                }
            } else {
                peptideShakerGUI.resetSelectedItems();
            }
        }

        if (proteinKey.equals(PeptideShakerGUI.NO_SELECTION)
                && !peptideKey.equals(PeptideShakerGUI.NO_SELECTION)) {
            for (String possibleKey : peptideShakerGUI.getIdentification().getProteinIdentification()) {
                try {
                    ProteinMatch proteinMatch = peptideShakerGUI.getIdentification().getProteinMatch(possibleKey);
                    if (proteinMatch.getPeptideMatches().contains(peptideKey)) {
                        proteinKey = possibleKey;
                        peptideShakerGUI.setSelectedItems(proteinKey, peptideKey, psmKey);
                        break;
                    }
                } catch (Exception e) {
                    peptideShakerGUI.catchException(e);
                    return;
                }
            }
        }

        if (!proteinKey.equals(PeptideShakerGUI.NO_SELECTION)) {
            proteinRow = getProteinRow(proteinKey);
        }

        if (proteinKeys.isEmpty()) {
            // For the silly people like me who happen to hide all proteins
            clearData();
            return;
        }

        if (proteinRow == -1) {
            peptideShakerGUI.resetSelectedItems();
        } else if (proteinTable.getSelectedRow() != proteinRow) {
            proteinTable.setRowSelectionInterval(proteinRow, proteinRow);
            proteinTable.scrollRectToVisible(proteinTable.getCellRect(proteinRow, 0, false));
            proteinTableKeyReleased(null);
        }
    }

    /**
     * Returns the row of a desired protein.
     *
     * @param proteinKey the key of the protein
     * @return the row of the desired protein
     */
    private int getProteinRow(String proteinKey) {
        int modelIndex = proteinKeys.indexOf(proteinKey);
        if (modelIndex >= 0) {
            return proteinTable.convertRowIndexToView(modelIndex);
        } else {
            return -1;
        }
    }

    /**
     * Clear all the data.
     */
    public void clearData() {

        proteinKeys.clear();

        ProteinTableModel proteinTableModel = (ProteinTableModel) proteinTable.getModel();
        proteinTableModel.reset();

        proteinTableModel.fireTableDataChanged();

        ((TitledBorder) proteinPanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Proteins" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
        proteinPanel.repaint();
        ((TitledBorder) peptidePanel.getBorder()).setTitle(PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING + "Peptides" + PeptideShakerGUI.TITLED_BORDER_HORIZONTAL_PADDING);
        peptidePanel.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sequenceCoverageJPopupMenu = new javax.swing.JPopupMenu();
        coverageShowAllPeptidesJRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        coveragePeptideTypesButtonGroup = new javax.swing.ButtonGroup();
        disclamierPanel = new javax.swing.JPanel();
        disclaimerLabel = new javax.swing.JLabel();
        proteinPeptideSplitPane = new javax.swing.JSplitPane();
        peptidesLayeredPane = new javax.swing.JLayeredPane();
        peptidePanel = new javax.swing.JPanel();
        peptidesTabbedPane = new javax.swing.JTabbedPane();
        mwPanel = new javax.swing.JPanel();
        mwPlotPanel = new javax.swing.JPanel();
        coverageTablePanel = new javax.swing.JPanel();
        coverageTableScrollPane = new javax.swing.JScrollPane();
        coverageTable =         new JTable() {

            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {

                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        String tip = (String) coverageTableToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        intensityPlotOuterPanel = new javax.swing.JPanel();
        intensityPlotPanel = new javax.swing.JPanel();
        spectraPlotOuterPanel = new javax.swing.JPanel();
        spectraPlotPanel = new javax.swing.JPanel();
        peptidePlotOuterPanel = new javax.swing.JPanel();
        peptidePlotPanel = new javax.swing.JPanel();
        peptidesHelpJButton = new javax.swing.JButton();
        exportPeptidesJButton = new javax.swing.JButton();
        sequenceCoverageOptionsJButton = new javax.swing.JButton();
        contextMenuPeptidesBackgroundPanel = new javax.swing.JPanel();
        proteinsLayeredPane = new javax.swing.JLayeredPane();
        proteinPanel = new javax.swing.JPanel();
        proteinTableScrollPane = new javax.swing.JScrollPane();
        proteinTable = new JTable() {
            protected JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    public String getToolTipText(MouseEvent e) {
                        java.awt.Point p = e.getPoint();
                        int index = columnModel.getColumnIndexAtX(p.x);
                        int realIndex = columnModel.getColumn(index).getModelIndex();
                        String tip = (String) proteinTableToolTips.get(realIndex);
                        return tip;
                    }
                };
            }
        };
        proteinsHelpJButton = new javax.swing.JButton();
        exportProteinsJButton = new javax.swing.JButton();
        contextMenuProteinsBackgroundPanel = new javax.swing.JPanel();

        coveragePeptideTypesButtonGroup.add(coverageShowAllPeptidesJRadioButtonMenuItem);
        coverageShowAllPeptidesJRadioButtonMenuItem.setSelected(true);
        coverageShowAllPeptidesJRadioButtonMenuItem.setText("All Peptides");
        coverageShowAllPeptidesJRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverageShowAllPeptidesJRadioButtonMenuItemActionPerformed(evt);
            }
        });
        sequenceCoverageJPopupMenu.add(coverageShowAllPeptidesJRadioButtonMenuItem);

        coveragePeptideTypesButtonGroup.add(coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItem);
        coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItem.setText("Enzymatic Peptides");
        coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItemActionPerformed(evt);
            }
        });
        sequenceCoverageJPopupMenu.add(coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItem);

        coveragePeptideTypesButtonGroup.add(coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItem);
        coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItem.setText("Truncated Peptides");
        coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItemActionPerformed(evt);
            }
        });
        sequenceCoverageJPopupMenu.add(coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItem);

        setBackground(new java.awt.Color(255, 255, 255));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        disclamierPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Disclaimer"));
        disclamierPanel.setOpaque(false);

        disclaimerLabel.setText("<html>\nThe values above are based on estimations of the confidence of a peptide/protein if found in a fraction alone in the context of the whole<br>\nanalysis. <i>These are <u><b>not</b></u> equal to the confidence in the peptide/protein identifications when processing the fractions independently!</i><br><br>\nIndependant fractions (like different donors, measurements) or replicates <u><b>should be processed separately</b></u>.<br>\nTo ensure comparable fractions, verify that the PSM PEP against score plots are similar for all the fractions in the <i>Validation</i> tab.\n</html>");

        javax.swing.GroupLayout disclamierPanelLayout = new javax.swing.GroupLayout(disclamierPanel);
        disclamierPanel.setLayout(disclamierPanelLayout);
        disclamierPanelLayout.setHorizontalGroup(
            disclamierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(disclamierPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(disclaimerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(266, Short.MAX_VALUE))
        );
        disclamierPanelLayout.setVerticalGroup(
            disclamierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(disclamierPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(disclaimerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        proteinPeptideSplitPane.setBorder(null);
        proteinPeptideSplitPane.setDividerLocation(200);
        proteinPeptideSplitPane.setDividerSize(0);
        proteinPeptideSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        proteinPeptideSplitPane.setResizeWeight(0.5);
        proteinPeptideSplitPane.setOpaque(false);

        peptidePanel.setBackground(new java.awt.Color(255, 255, 255));
        peptidePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Peptides"));

        peptidesTabbedPane.setBackground(new java.awt.Color(255, 255, 255));
        peptidesTabbedPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        mwPanel.setBackground(new java.awt.Color(255, 255, 255));

        mwPlotPanel.setOpaque(false);
        mwPlotPanel.setLayout(new javax.swing.BoxLayout(mwPlotPanel, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout mwPanelLayout = new javax.swing.GroupLayout(mwPanel);
        mwPanel.setLayout(mwPanelLayout);
        mwPanelLayout.setHorizontalGroup(
            mwPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mwPlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
        );
        mwPanelLayout.setVerticalGroup(
            mwPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mwPanelLayout.createSequentialGroup()
                .addComponent(mwPlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addContainerGap())
        );

        peptidesTabbedPane.addTab("Molecular Weight", mwPanel);

        coverageTablePanel.setBackground(new java.awt.Color(255, 255, 255));

        coverageTableScrollPane.setOpaque(false);

        coverageTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " ", "Fraction", "Coverage"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        coverageTable.setOpaque(false);
        coverageTableScrollPane.setViewportView(coverageTable);

        javax.swing.GroupLayout coverageTablePanelLayout = new javax.swing.GroupLayout(coverageTablePanel);
        coverageTablePanel.setLayout(coverageTablePanelLayout);
        coverageTablePanelLayout.setHorizontalGroup(
            coverageTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
            .addGroup(coverageTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(coverageTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE))
        );
        coverageTablePanelLayout.setVerticalGroup(
            coverageTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 203, Short.MAX_VALUE)
            .addGroup(coverageTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, coverageTablePanelLayout.createSequentialGroup()
                    .addComponent(coverageTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        peptidesTabbedPane.addTab("Coverage", coverageTablePanel);

        intensityPlotOuterPanel.setBackground(new java.awt.Color(255, 255, 255));

        intensityPlotPanel.setOpaque(false);
        intensityPlotPanel.setLayout(new javax.swing.BoxLayout(intensityPlotPanel, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout intensityPlotOuterPanelLayout = new javax.swing.GroupLayout(intensityPlotOuterPanel);
        intensityPlotOuterPanel.setLayout(intensityPlotOuterPanelLayout);
        intensityPlotOuterPanelLayout.setHorizontalGroup(
            intensityPlotOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(intensityPlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
        );
        intensityPlotOuterPanelLayout.setVerticalGroup(
            intensityPlotOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intensityPlotOuterPanelLayout.createSequentialGroup()
                .addComponent(intensityPlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addContainerGap())
        );

        peptidesTabbedPane.addTab("Intensity Plot", intensityPlotOuterPanel);

        spectraPlotOuterPanel.setBackground(new java.awt.Color(255, 255, 255));

        spectraPlotPanel.setOpaque(false);
        spectraPlotPanel.setLayout(new javax.swing.BoxLayout(spectraPlotPanel, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout spectraPlotOuterPanelLayout = new javax.swing.GroupLayout(spectraPlotOuterPanel);
        spectraPlotOuterPanel.setLayout(spectraPlotOuterPanelLayout);
        spectraPlotOuterPanelLayout.setHorizontalGroup(
            spectraPlotOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spectraPlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
        );
        spectraPlotOuterPanelLayout.setVerticalGroup(
            spectraPlotOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spectraPlotOuterPanelLayout.createSequentialGroup()
                .addComponent(spectraPlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addContainerGap())
        );

        peptidesTabbedPane.addTab("Spectrum Plot", spectraPlotOuterPanel);

        peptidePlotOuterPanel.setBackground(new java.awt.Color(255, 255, 255));

        peptidePlotPanel.setOpaque(false);
        peptidePlotPanel.setLayout(new javax.swing.BoxLayout(peptidePlotPanel, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout peptidePlotOuterPanelLayout = new javax.swing.GroupLayout(peptidePlotOuterPanel);
        peptidePlotOuterPanel.setLayout(peptidePlotOuterPanelLayout);
        peptidePlotOuterPanelLayout.setHorizontalGroup(
            peptidePlotOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(peptidePlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
        );
        peptidePlotOuterPanelLayout.setVerticalGroup(
            peptidePlotOuterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peptidePlotOuterPanelLayout.createSequentialGroup()
                .addComponent(peptidePlotPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addContainerGap())
        );

        peptidesTabbedPane.addTab("Peptide Plot", peptidePlotOuterPanel);

        peptidesTabbedPane.setSelectedIndex(4);

        javax.swing.GroupLayout peptidePanelLayout = new javax.swing.GroupLayout(peptidePanel);
        peptidePanel.setLayout(peptidePanelLayout);
        peptidePanelLayout.setHorizontalGroup(
            peptidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, peptidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(peptidesTabbedPane)
                .addContainerGap())
        );
        peptidePanelLayout.setVerticalGroup(
            peptidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(peptidePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(peptidesTabbedPane)
                .addContainerGap())
        );

        peptidePanel.setBounds(0, 0, 940, 280);
        peptidesLayeredPane.add(peptidePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        peptidesHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame_grey.png"))); // NOI18N
        peptidesHelpJButton.setToolTipText("Help");
        peptidesHelpJButton.setBorder(null);
        peptidesHelpJButton.setBorderPainted(false);
        peptidesHelpJButton.setContentAreaFilled(false);
        peptidesHelpJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame.png"))); // NOI18N
        peptidesHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                peptidesHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                peptidesHelpJButtonMouseExited(evt);
            }
        });
        peptidesHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                peptidesHelpJButtonActionPerformed(evt);
            }
        });
        peptidesHelpJButton.setBounds(930, 0, 10, 19);
        peptidesLayeredPane.add(peptidesHelpJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        exportPeptidesJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPeptidesJButton.setToolTipText("Export");
        exportPeptidesJButton.setBorder(null);
        exportPeptidesJButton.setBorderPainted(false);
        exportPeptidesJButton.setContentAreaFilled(false);
        exportPeptidesJButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportPeptidesJButton.setEnabled(false);
        exportPeptidesJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame.png"))); // NOI18N
        exportPeptidesJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exportPeptidesJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exportPeptidesJButtonMouseExited(evt);
            }
        });
        exportPeptidesJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPeptidesJButtonActionPerformed(evt);
            }
        });
        exportPeptidesJButton.setBounds(920, 0, 10, 19);
        peptidesLayeredPane.add(exportPeptidesJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        sequenceCoverageOptionsJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/contextual_menu_gray.png"))); // NOI18N
        sequenceCoverageOptionsJButton.setToolTipText("Coverage Options");
        sequenceCoverageOptionsJButton.setBorder(null);
        sequenceCoverageOptionsJButton.setBorderPainted(false);
        sequenceCoverageOptionsJButton.setContentAreaFilled(false);
        sequenceCoverageOptionsJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/contextual_menu_black.png"))); // NOI18N
        sequenceCoverageOptionsJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sequenceCoverageOptionsJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sequenceCoverageOptionsJButtonMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sequenceCoverageOptionsJButtonMouseReleased(evt);
            }
        });
        sequenceCoverageOptionsJButton.setBounds(895, 5, 10, 19);
        peptidesLayeredPane.add(sequenceCoverageOptionsJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        contextMenuPeptidesBackgroundPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contextMenuPeptidesBackgroundPanelLayout = new javax.swing.GroupLayout(contextMenuPeptidesBackgroundPanel);
        contextMenuPeptidesBackgroundPanel.setLayout(contextMenuPeptidesBackgroundPanelLayout);
        contextMenuPeptidesBackgroundPanelLayout.setHorizontalGroup(
            contextMenuPeptidesBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        contextMenuPeptidesBackgroundPanelLayout.setVerticalGroup(
            contextMenuPeptidesBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        contextMenuPeptidesBackgroundPanel.setBounds(890, 0, 50, 19);
        peptidesLayeredPane.add(contextMenuPeptidesBackgroundPanel, javax.swing.JLayeredPane.POPUP_LAYER);

        proteinPeptideSplitPane.setRightComponent(peptidesLayeredPane);

        proteinPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Proteins"));
        proteinPanel.setOpaque(false);

        proteinTableScrollPane.setOpaque(false);

        proteinTable.setModel(new eu.isas.peptideshaker.gui.tablemodels.ProteinTableModel());
        proteinTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        proteinTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                proteinTableMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                proteinTableMouseReleased(evt);
            }
        });
        proteinTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                proteinTableMouseMoved(evt);
            }
        });
        proteinTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                proteinTableKeyReleased(evt);
            }
        });
        proteinTableScrollPane.setViewportView(proteinTable);

        javax.swing.GroupLayout proteinPanelLayout = new javax.swing.GroupLayout(proteinPanel);
        proteinPanel.setLayout(proteinPanelLayout);
        proteinPanelLayout.setHorizontalGroup(
            proteinPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proteinPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(proteinTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 908, Short.MAX_VALUE)
                .addContainerGap())
        );
        proteinPanelLayout.setVerticalGroup(
            proteinPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proteinPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(proteinTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addContainerGap())
        );

        proteinPanel.setBounds(0, 0, 940, 200);
        proteinsLayeredPane.add(proteinPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        proteinsHelpJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame_grey.png"))); // NOI18N
        proteinsHelpJButton.setToolTipText("Help");
        proteinsHelpJButton.setBorder(null);
        proteinsHelpJButton.setBorderPainted(false);
        proteinsHelpJButton.setContentAreaFilled(false);
        proteinsHelpJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/help_no_frame.png"))); // NOI18N
        proteinsHelpJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                proteinsHelpJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                proteinsHelpJButtonMouseExited(evt);
            }
        });
        proteinsHelpJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proteinsHelpJButtonActionPerformed(evt);
            }
        });
        proteinsHelpJButton.setBounds(930, 0, 10, 19);
        proteinsLayeredPane.add(proteinsHelpJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        exportProteinsJButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportProteinsJButton.setToolTipText("Copy to File");
        exportProteinsJButton.setBorder(null);
        exportProteinsJButton.setBorderPainted(false);
        exportProteinsJButton.setContentAreaFilled(false);
        exportProteinsJButton.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame_grey.png"))); // NOI18N
        exportProteinsJButton.setEnabled(false);
        exportProteinsJButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/export_no_frame.png"))); // NOI18N
        exportProteinsJButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exportProteinsJButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exportProteinsJButtonMouseExited(evt);
            }
        });
        exportProteinsJButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportProteinsJButtonActionPerformed(evt);
            }
        });
        exportProteinsJButton.setBounds(920, 0, 10, 19);
        proteinsLayeredPane.add(exportProteinsJButton, javax.swing.JLayeredPane.POPUP_LAYER);

        contextMenuProteinsBackgroundPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout contextMenuProteinsBackgroundPanelLayout = new javax.swing.GroupLayout(contextMenuProteinsBackgroundPanel);
        contextMenuProteinsBackgroundPanel.setLayout(contextMenuProteinsBackgroundPanelLayout);
        contextMenuProteinsBackgroundPanelLayout.setHorizontalGroup(
            contextMenuProteinsBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        contextMenuProteinsBackgroundPanelLayout.setVerticalGroup(
            contextMenuProteinsBackgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        contextMenuProteinsBackgroundPanel.setBounds(910, 0, 40, 19);
        proteinsLayeredPane.add(contextMenuProteinsBackgroundPanel, javax.swing.JLayeredPane.POPUP_LAYER);

        proteinPeptideSplitPane.setLeftComponent(proteinsLayeredPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(disclamierPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(proteinPeptideSplitPane))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(proteinPeptideSplitPane, javax.swing.GroupLayout.PREFERRED_SIZE, 481, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addComponent(disclamierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Update the selection.
     *
     * @param evt
     */
    private void proteinTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinTableMouseReleased

        int row = proteinTable.getSelectedRow();
        int proteinIndex = proteinTable.convertRowIndexToModel(row);
        int column = proteinTable.getSelectedColumn();

        if (proteinIndex != -1) {
            // remember the selection
            newItemSelection();
            updatePlots();
        }

        if (evt != null && evt.getButton() == MouseEvent.BUTTON1 && proteinIndex != -1) {

            // open protein link in web browser
            if (column == proteinTable.getColumn("Accession").getModelIndex()
                    && ((String) proteinTable.getValueAt(row, column)).lastIndexOf("<html>") != -1) {

                String link = (String) proteinTable.getValueAt(row, column);
                link = link.substring(link.indexOf("\"") + 1);
                link = link.substring(0, link.indexOf("\""));

                this.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                BareBonesBrowserLaunch.openURL(link);
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }
    }//GEN-LAST:event_proteinTableMouseReleased

    /**
     * Update the selection.
     *
     * @param evt
     */
    private void proteinTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_proteinTableKeyReleased

        int row = proteinTable.getSelectedRow();
        int proteinIndex = proteinTable.convertRowIndexToModel(row);

        if (proteinIndex != -1) {
            // remember the selection
            newItemSelection();
            updatePlots();
        }
    }//GEN-LAST:event_proteinTableKeyReleased

    /**
     * Changes the cursor back to the default cursor.
     *
     * @param evt
     */
    private void proteinTableMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinTableMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_proteinTableMouseExited

    /**
     * Changes the cursor into a hand cursor if the table cell contains an html
     * link.
     *
     * @param evt
     */
    private void proteinTableMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinTableMouseMoved
        int row = proteinTable.rowAtPoint(evt.getPoint());
        int column = proteinTable.columnAtPoint(evt.getPoint());

        proteinTable.setToolTipText(null);

        if (row != -1 && column != -1 && column == proteinTable.getColumn("Accession").getModelIndex() && proteinTable.getValueAt(row, column) != null) {

            String tempValue = (String) proteinTable.getValueAt(row, column);

            if (tempValue.lastIndexOf("<html>") != -1) {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            } else {
                this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        } else if (column == proteinTable.getColumn("Description").getModelIndex() && proteinTable.getValueAt(row, column) != null) {
            if (GuiUtilities.getPreferredWidthOfCell(proteinTable, row, column) > proteinTable.getColumn("Description").getWidth()) {
                proteinTable.setToolTipText("" + proteinTable.getValueAt(row, column));
            }
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        } else {
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_proteinTableMouseMoved

    /**
     * Update the layered panes.
     *
     * @param evt
     */
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        proteinPeptideSplitPane.setDividerLocation(proteinPeptideSplitPane.getHeight() / 100 * 30);

        // resize the layered panels
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                // move the icons
                proteinsLayeredPane.getComponent(0).setBounds(
                        proteinsLayeredPane.getWidth() - proteinsLayeredPane.getComponent(0).getWidth() - 10,
                        -3,
                        proteinsLayeredPane.getComponent(0).getWidth(),
                        proteinsLayeredPane.getComponent(0).getHeight());

                proteinsLayeredPane.getComponent(1).setBounds(
                        proteinsLayeredPane.getWidth() - proteinsLayeredPane.getComponent(1).getWidth() - 20,
                        -3,
                        proteinsLayeredPane.getComponent(1).getWidth(),
                        proteinsLayeredPane.getComponent(1).getHeight());

                proteinsLayeredPane.getComponent(2).setBounds(
                        proteinsLayeredPane.getWidth() - proteinsLayeredPane.getComponent(2).getWidth() - 5,
                        -3,
                        proteinsLayeredPane.getComponent(2).getWidth(),
                        proteinsLayeredPane.getComponent(2).getHeight());

                // resize the plot area
                proteinsLayeredPane.getComponent(3).setBounds(0, 0, proteinsLayeredPane.getWidth(), proteinsLayeredPane.getHeight());
                proteinsLayeredPane.revalidate();
                proteinsLayeredPane.repaint();

                // move the icons
                peptidesLayeredPane.getComponent(0).setBounds(
                        peptidesLayeredPane.getWidth() - peptidesLayeredPane.getComponent(0).getWidth() - 10,
                        -3,
                        peptidesLayeredPane.getComponent(0).getWidth(),
                        peptidesLayeredPane.getComponent(0).getHeight());

                peptidesLayeredPane.getComponent(1).setBounds(
                        peptidesLayeredPane.getWidth() - peptidesLayeredPane.getComponent(1).getWidth() - 20,
                        -3,
                        peptidesLayeredPane.getComponent(1).getWidth(),
                        peptidesLayeredPane.getComponent(1).getHeight());

                peptidesLayeredPane.getComponent(2).setBounds(
                        peptidesLayeredPane.getWidth() - peptidesLayeredPane.getComponent(2).getWidth() - 34,
                        0,
                        peptidesLayeredPane.getComponent(2).getWidth(),
                        peptidesLayeredPane.getComponent(2).getHeight());

                peptidesLayeredPane.getComponent(3).setBounds(
                        peptidesLayeredPane.getWidth() - peptidesLayeredPane.getComponent(3).getWidth() - 5,
                        -3,
                        peptidesLayeredPane.getComponent(3).getWidth(),
                        peptidesLayeredPane.getComponent(3).getHeight());

                // resize the plot area
                peptidesLayeredPane.getComponent(4).setBounds(0, 0, peptidesLayeredPane.getWidth(), peptidesLayeredPane.getHeight());
                peptidesLayeredPane.revalidate();
                peptidesLayeredPane.repaint();
            }
        });
    }//GEN-LAST:event_formComponentResized

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void proteinsHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinsHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_proteinsHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void proteinsHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_proteinsHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_proteinsHelpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void proteinsHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proteinsHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/FractionsTab.html"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_proteinsHelpJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void exportProteinsJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportProteinsJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_exportProteinsJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void exportProteinsJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportProteinsJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exportProteinsJButtonMouseExited

    /**
     * Export the table contents.
     *
     * @param evt
     */
    private void exportProteinsJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportProteinsJButtonActionPerformed
        copyTableContentToClipboardOrFile(TableIndex.PROTEIN_TABLE);
    }//GEN-LAST:event_exportProteinsJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void peptidesHelpJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptidesHelpJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_peptidesHelpJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void peptidesHelpJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peptidesHelpJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_peptidesHelpJButtonMouseExited

    /**
     * Open the help dialog.
     *
     * @param evt
     */
    private void peptidesHelpJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_peptidesHelpJButtonActionPerformed
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        new HelpDialog(peptideShakerGUI, getClass().getResource("/helpFiles/FractionsTab.html"), "#Peptides");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_peptidesHelpJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void exportPeptidesJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPeptidesJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_exportPeptidesJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void exportPeptidesJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exportPeptidesJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_exportPeptidesJButtonMouseExited

    /**
     * Export the table or plot.
     *
     * @param evt
     */
    private void exportPeptidesJButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPeptidesJButtonActionPerformed
        new ExportGraphicsDialog(peptideShakerGUI, true, (ChartPanel) peptidePlotPanel.getComponent(0));
    }//GEN-LAST:event_exportPeptidesJButtonActionPerformed

    /**
     * Change the cursor to a hand cursor.
     *
     * @param evt
     */
    private void sequenceCoverageOptionsJButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sequenceCoverageOptionsJButtonMouseEntered
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    }//GEN-LAST:event_sequenceCoverageOptionsJButtonMouseEntered

    /**
     * Change the cursor back to the default cursor.
     *
     * @param evt
     */
    private void sequenceCoverageOptionsJButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sequenceCoverageOptionsJButtonMouseExited
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_sequenceCoverageOptionsJButtonMouseExited

    /**
     * Show the sequence coverage options.
     *
     * @param evt
     */
    private void sequenceCoverageOptionsJButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sequenceCoverageOptionsJButtonMouseReleased
        sequenceCoverageJPopupMenu.show(sequenceCoverageOptionsJButton, evt.getX(), evt.getY());
    }//GEN-LAST:event_sequenceCoverageOptionsJButtonMouseReleased

    /**
     * Update the sequence coverage maps.
     *
     * @param evt
     */
    private void coverageShowAllPeptidesJRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coverageShowAllPeptidesJRadioButtonMenuItemActionPerformed
        if (proteinTable.getSelectedRow() != -1) {
            try {
                updatePlots();
            } catch (Exception e) {
                peptideShakerGUI.catchException(e);
            }
        }
    }//GEN-LAST:event_coverageShowAllPeptidesJRadioButtonMenuItemActionPerformed

    /**
     * @see #coverageShowAllPeptidesJRadioButtonMenuItem
     */
    private void coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItemActionPerformed
        coverageShowAllPeptidesJRadioButtonMenuItemActionPerformed(null);
    }//GEN-LAST:event_coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItemActionPerformed

    /**
     * @see #coverageShowAllPeptidesJRadioButtonMenuItem
     */
    private void coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItemActionPerformed
        coverageShowAllPeptidesJRadioButtonMenuItemActionPerformed(null);
    }//GEN-LAST:event_coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItemActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel contextMenuPeptidesBackgroundPanel;
    private javax.swing.JPanel contextMenuProteinsBackgroundPanel;
    private javax.swing.ButtonGroup coveragePeptideTypesButtonGroup;
    private javax.swing.JRadioButtonMenuItem coverageShowAllPeptidesJRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem coverageShowEnzymaticPeptidesOnlyJRadioButtonMenuItem;
    private javax.swing.JRadioButtonMenuItem coverageShowTruncatedPeptidesOnlyJRadioButtonMenuItem;
    private javax.swing.JTable coverageTable;
    private javax.swing.JPanel coverageTablePanel;
    private javax.swing.JScrollPane coverageTableScrollPane;
    private javax.swing.JLabel disclaimerLabel;
    private javax.swing.JPanel disclamierPanel;
    private javax.swing.JButton exportPeptidesJButton;
    private javax.swing.JButton exportProteinsJButton;
    private javax.swing.JPanel intensityPlotOuterPanel;
    private javax.swing.JPanel intensityPlotPanel;
    private javax.swing.JPanel mwPanel;
    private javax.swing.JPanel mwPlotPanel;
    private javax.swing.JPanel peptidePanel;
    private javax.swing.JPanel peptidePlotOuterPanel;
    private javax.swing.JPanel peptidePlotPanel;
    private javax.swing.JButton peptidesHelpJButton;
    private javax.swing.JLayeredPane peptidesLayeredPane;
    private javax.swing.JTabbedPane peptidesTabbedPane;
    private javax.swing.JPanel proteinPanel;
    private javax.swing.JSplitPane proteinPeptideSplitPane;
    private javax.swing.JTable proteinTable;
    private javax.swing.JScrollPane proteinTableScrollPane;
    private javax.swing.JButton proteinsHelpJButton;
    private javax.swing.JLayeredPane proteinsLayeredPane;
    private javax.swing.JPopupMenu sequenceCoverageJPopupMenu;
    private javax.swing.JButton sequenceCoverageOptionsJButton;
    private javax.swing.JPanel spectraPlotOuterPanel;
    private javax.swing.JPanel spectraPlotPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Displays or hide sparklines in the tables.
     *
     * @param showSparkLines boolean indicating whether sparklines shall be
     * displayed or hidden
     */
    public void showSparkLines(boolean showSparkLines) {

        updateProteinTableSparklines();

        try {
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Confidence").getCellRenderer()).showNumbers(!showSparkLines);
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MW").getCellRenderer()).showNumbers(!showSparkLines);
        } catch (NullPointerException e) {
            // ignore
        }

        proteinTable.revalidate();
        proteinTable.repaint();
    }

    /**
     * Provides to the PeptideShakerGUI instance the currently selected protein,
     * peptide and psm.
     */
    public void newItemSelection() {

        String proteinKey = PeptideShakerGUI.NO_SELECTION;
        String peptideKey = PeptideShakerGUI.NO_SELECTION;
        String psmKey = PeptideShakerGUI.NO_SELECTION;

        if (proteinTable.getSelectedRow() != -1) {
            proteinKey = proteinKeys.get(proteinTable.convertRowIndexToModel(proteinTable.getSelectedRow()));
        }

        peptideShakerGUI.setSelectedItems(proteinKey, peptideKey, psmKey);
    }

    /**
     * Returns a list of keys of the displayed proteins.
     *
     * @return a list of keys of the displayed proteins
     */
    public ArrayList<String> getDisplayedProteins() {
        return proteinKeys;
    }

    /**
     * Returns a list of keys of the displayed peptides.
     *
     * @return a list of keys of the displayed peptides
     */
    public ArrayList<String> getDisplayedPeptides() {
        return peptideKeys;
    }

    /**
     * Export the table contents to the clipboard.
     *
     * @param index
     */
    private void copyTableContentToClipboardOrFile(TableIndex index) {

        final TableIndex tableIndex = index;

        // get the file to send the output to
        final File selectedFile = peptideShakerGUI.getUserSelectedFile(".txt", "Tab separated text file (.txt)", "Export...", false);

        if (selectedFile != null) {

            progressDialog = new ProgressDialogX(peptideShakerGUI,
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                    true);
            progressDialog.setIndeterminate(true);

            new Thread(new Runnable() {

                public void run() {
                    try {
                        progressDialog.setVisible(true);
                    } catch (IndexOutOfBoundsException e) {
                        // ignore error
                    }
                    progressDialog.setTitle("Exporting to File. Please Wait...");
                }
            }, "ProgressDialog").start();

            new Thread("ExportThread") {

                @Override
                public void run() {

                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
                        Util.tableToFile(proteinTable, "\t", progressDialog, true, writer);
                        writer.close();

                        boolean processCancelled = progressDialog.isRunCanceled();
                        progressDialog.setRunFinished();

                        if (!processCancelled) {
                            JOptionPane.showMessageDialog(peptideShakerGUI, "Data copied to file:\n" + selectedFile.getAbsolutePath(), "Data Exported.", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IOException e) {
                        progressDialog.setRunFinished();
                        JOptionPane.showMessageDialog(null, "An error occured when exporting the table content.", "Export Failed", JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @Override
    public void annotationClicked(ArrayList<ResidueAnnotation> allAnnotation, ChartMouseEvent cme) {
        // do nothing, not supported
    }

    /**
     * Returns the list of peptide keys.
     *
     * @return the list of peptide keys.
     */
    public ArrayList<String> getPeptideKeys() {
        return peptideKeys;
    }

    /**
     * Updates the protein table sparklines.
     */
    private void updateProteinTableSparklines() {

        ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MS2 Quant.").getCellRenderer()).showNumbers(!peptideShakerGUI.showSparklines());
        ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("MW").getCellRenderer()).showNumbers(!peptideShakerGUI.showSparklines());
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("Coverage").getCellRenderer()).showNumbers(!peptideShakerGUI.showSparklines());
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Peptides").getCellRenderer()).showNumbers(!peptideShakerGUI.showSparklines());
        ((JSparklinesTwoValueBarChartTableCellRenderer) proteinTable.getColumn("#Spectra").getCellRenderer()).showNumbers(!peptideShakerGUI.showSparklines());

        try {
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Confidence").getCellRenderer()).showNumbers(!peptideShakerGUI.showSparklines());
        } catch (IllegalArgumentException e) {
            ((JSparklinesBarChartTableCellRenderer) proteinTable.getColumn("Score").getCellRenderer()).showNumbers(!peptideShakerGUI.showSparklines());
        }
    }
}
