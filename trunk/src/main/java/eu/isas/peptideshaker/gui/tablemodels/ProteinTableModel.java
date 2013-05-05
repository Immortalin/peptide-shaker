package eu.isas.peptideshaker.gui.tablemodels;

import com.compomics.util.experiment.annotation.gene.GeneFactory;
import com.compomics.util.experiment.biology.Protein;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.gui.tablemodels.SelfUpdatingTableModel;
import com.compomics.util.gui.waiting.WaitingHandler;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import eu.isas.peptideshaker.myparameters.PSParameter;
import eu.isas.peptideshaker.preferences.DisplayPreferences;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.Collections;
import no.uib.jsparklines.data.XYDataPoint;

/**
 * Model for the protein table.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class ProteinTableModel extends SelfUpdatingTableModel {

    /**
     * The sequence factory.
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance();
    /**
     * The gene factory. 
     */
    private GeneFactory geneFactory = GeneFactory.getInstance();
    /**
     * The main GUI class.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * The identification of this project.
     */
    private Identification identification;
    /**
     * A list of ordered protein keys.
     */
    private ArrayList<String> proteinKeys = null;
    /**
     * Indicates whether data in DB shall be used.
     */
    private boolean useDB = false;

    /**
     * Constructor which sets a new table.
     *
     * @param peptideShakerGUI instance of the main GUI class
     */
    public ProteinTableModel(PeptideShakerGUI peptideShakerGUI) {
        setUpTableModel(peptideShakerGUI);
    }

    /**
     * Update the data in the table model without having to reset the whole
     * table model. This keeps the sorting order of the table.
     *
     * @param peptideShakerGUI
     */
    public void updateDataModel(PeptideShakerGUI peptideShakerGUI) {
        setUpTableModel(peptideShakerGUI);
    }

    /**
     * Set up the table model.
     *
     * @param peptideShakerGUI
     */
    private void setUpTableModel(PeptideShakerGUI peptideShakerGUI) {
        this.peptideShakerGUI = peptideShakerGUI;
        identification = peptideShakerGUI.getIdentification();
        if (identification != null) {
            try {
                if (peptideShakerGUI.getDisplayPreferences().showValidatedProteinsOnly()) {
                    proteinKeys = peptideShakerGUI.getIdentificationFeaturesGenerator().getValidatedProteins(); // show validated proteins only
                } else {
                    proteinKeys = peptideShakerGUI.getIdentificationFeaturesGenerator().getProcessedProteinKeys(null, peptideShakerGUI.getFilterPreferences()); // show all proteins
                }
            } catch (Exception e) {
                peptideShakerGUI.catchException(e);
            }
        }
    }

    /**
     * Reset the protein keys.
     */
    public void reset() {
        proteinKeys = null;
    }

    /**
     * Constructor which sets a new empty table.
     *
     */
    public ProteinTableModel() {
    }

    @Override
    public int getRowCount() {
        if (proteinKeys != null) {
            return proteinKeys.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount() {
        return 13;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return " ";
            case 1:
                return "  ";
            case 2:
                return "PI";
            case 3:
                return "Accession";
            case 4:
                return "Description";
            case 5:
                return "CR";
            case 6:
                return "Coverage";
            case 7:
                return "#Peptides";
            case 8:
                return "#Spectra";
            case 9:
                return "MS2 Quant.";
            case 10:
                return "MW";
            case 11:
                if (peptideShakerGUI != null && peptideShakerGUI.getDisplayPreferences().showScores()) {
                    return "Score";
                } else {
                    return "Confidence";
                }
            case 12:
                return "";
            default:
                return "";
        }
    }

    @Override
    public Object getValueAt(int row, int column) {

        try {
            switch (column) {
                case 0:
                    return row + 1;
                case 1:
                    String proteinKey = proteinKeys.get(row);
                    PSParameter pSParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, new PSParameter(), useDB);
                    if (!useDB && pSParameter == null) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    return pSParameter.isStarred();
                case 2:
                    proteinKey = proteinKeys.get(row);
                    pSParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, new PSParameter(), useDB);
                    if (!useDB && pSParameter == null) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    return pSParameter.getProteinInferenceClass();
                case 3:
                    proteinKey = proteinKeys.get(row);
                    ProteinMatch proteinMatch = identification.getProteinMatch(proteinKey, useDB);
                    if (!useDB && proteinMatch == null) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    return peptideShakerGUI.getDisplayFeaturesGenerator().addDatabaseLink(proteinMatch.getMainMatch());
                case 4:
                    proteinKey = proteinKeys.get(row);
                    proteinMatch = identification.getProteinMatch(proteinKey, useDB);
                    if (!useDB && proteinMatch == null) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    String description = "";
                    try {
                        description = sequenceFactory.getHeader(proteinMatch.getMainMatch()).getDescription();
                    } catch (Exception e) {
                        peptideShakerGUI.catchException(e);
                    }
                    return description;
                case 5:
                    proteinKey = proteinKeys.get(row);
                    proteinMatch = identification.getProteinMatch(proteinKey, useDB);
                    if (!useDB && proteinMatch == null) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    String geneName = sequenceFactory.getHeader(proteinMatch.getMainMatch()).getGeneName();
                    return geneFactory.getChromosomeFromGeneName(geneName);
                case 6:
                    proteinKey = proteinKeys.get(row);
                    proteinMatch = identification.getProteinMatch(proteinKey, useDB);
                    if (!useDB && (!peptideShakerGUI.getIdentificationFeaturesGenerator().sequenceCoverageInCache(proteinKey)
                            || !peptideShakerGUI.getIdentificationFeaturesGenerator().observableCoverageInCache(proteinKey))
                            && (proteinMatch == null || !identification.proteinDetailsInCache(proteinKey))) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    double sequenceCoverage;
                    try {
                        sequenceCoverage = 100 * peptideShakerGUI.getIdentificationFeaturesGenerator().getSequenceCoverage(proteinKey);
                    } catch (Exception e) {
                        peptideShakerGUI.catchException(e);
                        return Double.NaN;
                    }
                    double possibleCoverage = 100;
                    try {
                        possibleCoverage = 100 * peptideShakerGUI.getIdentificationFeaturesGenerator().getObservableCoverage(proteinKey);
                    } catch (Exception e) {
                        peptideShakerGUI.catchException(e);
                    }
                    return new XYDataPoint(sequenceCoverage, possibleCoverage - sequenceCoverage, true);
                case 7:
                    proteinKey = proteinKeys.get(row);
                    proteinMatch = identification.getProteinMatch(proteinKey, useDB);
                    if (!useDB && (proteinMatch == null
                            || !peptideShakerGUI.getIdentificationFeaturesGenerator().nValidatedPeptidesInCache(proteinKey)
                            && !identification.proteinDetailsInCache(proteinKey))) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    int nValidatedPeptides = peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinKey);
                    return new XYDataPoint(nValidatedPeptides, proteinMatch.getPeptideCount() - nValidatedPeptides, false);
                case 8:
                    proteinKey = proteinKeys.get(row);
                    proteinMatch = identification.getProteinMatch(proteinKey, useDB);
                    if (!useDB
                            && (!peptideShakerGUI.getIdentificationFeaturesGenerator().nValidatedSpectraInCache(proteinKey)
                            || !peptideShakerGUI.getIdentificationFeaturesGenerator().nSpectraInCache(proteinKey))
                            && (proteinMatch == null || !identification.proteinDetailsInCache(proteinKey))) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    int nValidatedSpectra = peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey);
                    int nSpectra = peptideShakerGUI.getIdentificationFeaturesGenerator().getNSpectra(proteinKey);
                    return new XYDataPoint(nValidatedSpectra, nSpectra - nValidatedSpectra, false);
                case 9:
                    proteinKey = proteinKeys.get(row);
                    proteinMatch = identification.getProteinMatch(proteinKey, useDB);
                    if (!useDB && !peptideShakerGUI.getIdentificationFeaturesGenerator().spectrumCountingInCache(proteinKey)
                            && (proteinMatch == null || !identification.proteinDetailsInCache(proteinKey))) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    return peptideShakerGUI.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey);
                case 10:
                    proteinKey = proteinKeys.get(row);
                    proteinMatch = identification.getProteinMatch(proteinKey, useDB);
                    if (!useDB && proteinMatch == null) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    String mainMatch = proteinMatch.getMainMatch();
                    Protein currentProtein = sequenceFactory.getProtein(mainMatch);
                    if (currentProtein != null) {
                        return sequenceFactory.computeMolecularWeight(mainMatch);
                    } else {
                        return null;
                    }
                case 11:
                    proteinKey = proteinKeys.get(row);
                    pSParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, new PSParameter(), useDB);
                    if (!useDB && pSParameter == null) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    if (pSParameter != null) {
                        if (peptideShakerGUI.getDisplayPreferences().showScores()) {
                            return pSParameter.getProteinScore();
                        } else {
                            return pSParameter.getProteinConfidence();
                        }
                    } else {
                        return null;
                    }
                case 12:
                    proteinKey = proteinKeys.get(row);
                    pSParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, new PSParameter(), useDB);
                    if (!useDB && pSParameter == null) {
                        dataMissingAtRow(row);
                        return DisplayPreferences.LOADING_MESSAGE;
                    }
                    if (pSParameter != null) {
                        return pSParameter.isValidated();
                    } else {
                        return null;
                    }
                default:
                    return "";
            }
        } catch (SQLNonTransientConnectionException e) {
            // this one can be ignored i think?
            return null;
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
            return null;
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        for (int i = 0; i < getRowCount(); i++) {
            if (getValueAt(i, columnIndex) != null) {
                return getValueAt(i, columnIndex).getClass();
            }
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Sets whether or not data shall be looked for in the database. If false
     * only the cache will be used.
     *
     * @param useDB
     */
    public void useDB(boolean useDB) {
        this.useDB = useDB;
    }

    /**
     * Indicates whether the table model uses the database.
     *
     * @return true if the table model uses the database
     */
    public boolean isDB() {
        return useDB;
    }

    @Override
    protected void catchException(Exception e) {
        useDB = true;
        peptideShakerGUI.catchException(e);
    }

    @Override
    protected int loadDataForRows(ArrayList<Integer> rows, boolean interrupted) {
        ArrayList<String> tempKeys = new ArrayList<String>();
        for (int i : rows) {
            String proteinKey = proteinKeys.get(i);
            tempKeys.add(proteinKey);
        }
        try {
            loadProteins(tempKeys);

            for (int i : rows) {
                String proteinKey = proteinKeys.get(i);
                peptideShakerGUI.getIdentificationFeaturesGenerator().getSequenceCoverage(proteinKey);
                if (interrupted) {
                    loadProteins(tempKeys);
                    return i;
                }
                peptideShakerGUI.getIdentificationFeaturesGenerator().getObservableCoverage(proteinKey);
                if (interrupted) {
                    loadProteins(tempKeys);
                    return i;
                }
                peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinKey);
                if (interrupted) {
                    loadProteins(tempKeys);
                    return i;
                }
                peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey);
                if (interrupted) {
                    loadProteins(tempKeys);
                    return i;
                }
                peptideShakerGUI.getIdentificationFeaturesGenerator().getNSpectra(proteinKey);
                if (interrupted) {
                    loadProteins(tempKeys);
                    return i;
                }
                peptideShakerGUI.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey);
                if (interrupted) {
                    loadProteins(tempKeys);
                    return i;
                }
            }
        } catch (Exception e) {
            if (!peptideShakerGUI.isClosing()) { // ignore errors related to accesing the database when closing the tool
                catchException(e);
            }
            return rows.get(0);
        }
        return rows.get(rows.size() - 1);
    }

    /**
     * Loads the protein matches and matches parameters in cache.
     *
     * @param keys the keys of the matches to load
     * @throws SQLException
     * @throws IOException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void loadProteins(ArrayList<String> keys) throws SQLException, IOException, IOException, ClassNotFoundException, InterruptedException {
        identification.loadProteinMatches(keys, null);
        identification.loadProteinMatchParameters(keys, new PSParameter(), null);
    }

    @Override
    protected void loadDataForColumn(int column, WaitingHandler waitingHandler) {
        try {
            ArrayList<String> reversedList = new ArrayList(proteinKeys);
            Collections.reverse(reversedList);
            if (column == 1
                    || column == 2
                    || column == 11
                    || column == 12) {
                identification.loadProteinMatchParameters(reversedList, new PSParameter(), null);
            } else if (column == 3
                    || column == 4
                    || column == 5
                    || column == 6
                    || column == 7
                    || column == 8
                    || column == 9
                    || column == 10) {
                identification.loadProteinMatches(reversedList, null);
            }
            for (String proteinKey : reversedList) {
                if (column == 6) {
                    peptideShakerGUI.getIdentificationFeaturesGenerator().getSequenceCoverage(proteinKey);
                    peptideShakerGUI.getIdentificationFeaturesGenerator().getObservableCoverage(proteinKey);
                } else if (column == 7) {
                    peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedPeptides(proteinKey);
                } else if (column == 8) {
                    peptideShakerGUI.getIdentificationFeaturesGenerator().getNValidatedSpectra(proteinKey);
                    peptideShakerGUI.getIdentificationFeaturesGenerator().getNSpectra(proteinKey);
                } else if (column == 9) {
                    peptideShakerGUI.getIdentificationFeaturesGenerator().getSpectrumCounting(proteinKey);
                }
                if (waitingHandler != null) {
                    waitingHandler.increaseSecondaryProgressValue();
                    if (waitingHandler.isRunCanceled()) {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            if (!peptideShakerGUI.isClosing()) { // ignore errors related to accesing the database when closing the tool
                catchException(e);
            }
        }
    }
}
