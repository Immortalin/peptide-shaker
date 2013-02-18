package eu.isas.peptideshaker.utils;

import com.compomics.util.experiment.biology.Peptide;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.identification.matches.SpectrumMatch;
import com.compomics.util.experiment.massspectrometry.Precursor;
import com.compomics.util.experiment.massspectrometry.Spectrum;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import eu.isas.peptideshaker.filtering.MatchFilter;
import eu.isas.peptideshaker.filtering.PeptideFilter;
import eu.isas.peptideshaker.filtering.ProteinFilter;
import eu.isas.peptideshaker.filtering.PsmFilter;
import eu.isas.peptideshaker.gui.PeptideShakerGUI;
import eu.isas.peptideshaker.gui.tabpanels.PtmPanel;
import eu.isas.peptideshaker.myparameters.PSParameter;
import eu.isas.peptideshaker.preferences.FilterPreferences;
import java.awt.Toolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.RowFilter.ComparisonType;

/**
 * This class provides information whether a hit should be hidden or starred.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class StarHider {

    /**
     * PeptideShakerGUI instance.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * The sequence factory.
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance();
    /**
     * The progress dialog.
     */
    private ProgressDialogX progressDialog;

    /**
     * Constructor.
     *
     * @param peptideShakerGUI the peptideShakerGUI main class
     */
    public StarHider(PeptideShakerGUI peptideShakerGUI) {
        this.peptideShakerGUI = peptideShakerGUI;
    }

    /**
     * Updates the star/hide status of all identification items.
     */
    public void starHide() {

        progressDialog = new ProgressDialogX(peptideShakerGUI,
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")),
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")),
                true);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Hiding/Starring Items. Please Wait...");

        new Thread(new Runnable() {
            public void run() {
                try {
                    progressDialog.setVisible(true);
                } catch (IndexOutOfBoundsException e) {
                    // ignore
                }
            }
        }, "ProgressDialog").start();

        new Thread("Star/Hide") {
            @Override
            public void run() {

                try {
                    Identification identification = peptideShakerGUI.getIdentification();
                    progressDialog.setIndeterminate(false);
                    progressDialog.setMaxProgressValue(identification.getProteinIdentification().size());

                    PSParameter psParameter = new PSParameter();

                    identification.loadProteinMatches(null);
                    identification.loadProteinMatchParameters(psParameter, null);

                    // @TODO: implement better database batch interaction!!

                    for (String proteinKey : identification.getProteinIdentification()) {

                        if (progressDialog.isRunCanceled()) {
                            break;
                        }

                        ProteinMatch proteinMatch = identification.getProteinMatch(proteinKey);
                        boolean peptideSurvived = false;

                        identification.loadPeptideMatches(proteinMatch.getPeptideMatches(), null);
                        identification.loadPeptideMatchParameters(proteinMatch.getPeptideMatches(), psParameter, null);

                        for (String peptideKey : proteinMatch.getPeptideMatches()) {

                            if (progressDialog.isRunCanceled()) {
                                break;
                            }

                            PeptideMatch peptideMatch = identification.getPeptideMatch(peptideKey);
                            boolean psmSurvived = false;

                            identification.loadSpectrumMatchParameters(peptideMatch.getSpectrumMatches(), psParameter, null);

                            for (String spectrumKey : peptideMatch.getSpectrumMatches()) {

                                if (progressDialog.isRunCanceled()) {
                                    break;
                                }

                                psParameter = (PSParameter) identification.getSpectrumMatchParameter(spectrumKey, psParameter);

                                if (isPsmHidden(spectrumKey)) {
                                    psParameter.setHidden(true);
                                } else {
                                    psParameter.setHidden(false);
                                    psmSurvived = true;
                                }

                                psParameter.setStarred(isPsmStarred(spectrumKey));
                                identification.updateSpectrumMatchParameter(spectrumKey, psParameter);
                            }

                            psParameter = (PSParameter) identification.getPeptideMatchParameter(peptideKey, psParameter);

                            if (!psmSurvived) {
                                psParameter.setHidden(true);
                            } else if (isPeptideHidden(peptideKey)) {
                                psParameter.setHidden(true);
                            } else {
                                psParameter.setHidden(false);
                                peptideSurvived = true;
                            }

                            psParameter.setStarred(isPeptideStarred(peptideKey));

                            identification.updatePeptideMatchParameter(peptideKey, psParameter);
                        }

                        psParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, psParameter);

                        if (!peptideSurvived) {
                            psParameter.setHidden(true);
                        } else {
                            psParameter.setHidden(isProteinHidden(proteinKey));
                        }

                        psParameter.setStarred(isProteinStarred(proteinKey));

                        identification.updateProteinMatchParameter(proteinKey, psParameter);
                        progressDialog.increaseProgressValue();
                    }

                    progressDialog.setRunFinished();
                    peptideShakerGUI.updateTabbedPanes();

                } catch (Exception e) {
                    peptideShakerGUI.catchException(e);
                }
            }
        }.start();
    }

    /**
     * Stars a protein match.
     *
     * @param match the key of the match
     */
    public void starProtein(String match) {
        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getProteinMatchParameter(match, psParameter);
            boolean validated = false;

            for (ProteinFilter matchFilter : filterPreferences.getProteinStarFilters().values()) {
                if (matchFilter.getExceptions().contains(match)) {
                    matchFilter.removeException(match);
                }
                if (isValidated(match, matchFilter)) {
                    validated = true;
                }
            }

            if (!validated) {
                ProteinFilter proteinFilter;
                if (!filterPreferences.getProteinStarFilters().containsKey(MatchFilter.MANUAL_SELECTION)) {
                    proteinFilter = new ProteinFilter(MatchFilter.MANUAL_SELECTION);
                    proteinFilter.setDescription("Manual selection via the graphical interface");
                    filterPreferences.getProteinStarFilters().put(proteinFilter.getName(), proteinFilter);
                } else {
                    proteinFilter = filterPreferences.getProteinStarFilters().get(MatchFilter.MANUAL_SELECTION);
                }
                proteinFilter.addManualValidation(match);
            }

            psParameter.setStarred(true);
            identification.updateProteinMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Unstars a protein match.
     *
     * @param match the key of the match
     */
    public void unStarProtein(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getProteinMatchParameter(match, psParameter);

            for (ProteinFilter matchFilter : filterPreferences.getProteinStarFilters().values()) {
                if (matchFilter.getManualValidation().contains(match)) {
                    matchFilter.removeManualValidation(match);
                }
                if (isValidated(match, matchFilter)) {
                    matchFilter.addException(match);
                }
            }

            psParameter.setStarred(false);
            identification.updateProteinMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Hides a protein match.
     *
     * @param match the key of the match
     */
    public void hideProtein(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getProteinMatchParameter(match, psParameter);
            boolean validated = false;

            for (ProteinFilter matchFilter : filterPreferences.getProteinHideFilters().values()) {
                if (matchFilter.getExceptions().contains(match)) {
                    matchFilter.removeException(match);
                }
                if (isValidated(match, matchFilter)) {
                    validated = true;
                }
            }

            if (!validated) {
                ProteinFilter proteinFilter;
                if (!filterPreferences.getProteinHideFilters().containsKey(MatchFilter.MANUAL_SELECTION)) {
                    proteinFilter = new ProteinFilter(MatchFilter.MANUAL_SELECTION);
                    proteinFilter.setDescription("Manual selection via the graphical interface");
                    filterPreferences.getProteinHideFilters().put(proteinFilter.getName(), proteinFilter);
                } else {
                    proteinFilter = filterPreferences.getProteinHideFilters().get(MatchFilter.MANUAL_SELECTION);
                }
                proteinFilter.addManualValidation(match);
            }

            psParameter.setHidden(true);
            identification.updateProteinMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Unhides a protein match.
     *
     * @param match the key of the match
     */
    public void unHideProtein(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getProteinMatchParameter(match, psParameter);
            for (ProteinFilter matchFilter : filterPreferences.getProteinHideFilters().values()) {
                if (matchFilter.getManualValidation().contains(match)) {
                    matchFilter.removeManualValidation(match);
                }
                if (isValidated(match, matchFilter)) {
                    matchFilter.addException(match);
                }
            }

            psParameter.setHidden(true);
            identification.updateProteinMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Stars a peptide match.
     *
     * @param match the key of the match
     */
    public void starPeptide(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getPeptideMatchParameter(match, psParameter);
            boolean validated = false;

            for (PeptideFilter matchFilter : filterPreferences.getPeptideStarFilters().values()) {
                if (matchFilter.getExceptions().contains(match)) {
                    matchFilter.removeException(match);
                }
                if (isValidated(match, matchFilter)) {
                    validated = true;
                }
            }

            if (!validated) {
                PeptideFilter peptideFilter;
                if (!filterPreferences.getPeptideStarFilters().containsKey(MatchFilter.MANUAL_SELECTION)) {
                    peptideFilter = new PeptideFilter(MatchFilter.MANUAL_SELECTION, peptideShakerGUI.getFoundModifications());
                    peptideFilter.setDescription("Manual selection via the graphical interface");
                    filterPreferences.getPeptideStarFilters().put(peptideFilter.getName(), peptideFilter);
                } else {
                    peptideFilter = filterPreferences.getPeptideStarFilters().get(MatchFilter.MANUAL_SELECTION);
                }
                peptideFilter.addManualValidation(match);
            }

            psParameter.setStarred(true);
            identification.updatePeptideMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Unstars a peptide match.
     *
     * @param match the key of the match
     */
    public void unStarPeptide(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getPeptideMatchParameter(match, psParameter);

            for (PeptideFilter matchFilter : filterPreferences.getPeptideStarFilters().values()) {
                if (matchFilter.getManualValidation().contains(match)) {
                    matchFilter.removeManualValidation(match);
                }
                if (isValidated(match, matchFilter)) {
                    matchFilter.addException(match);
                }
            }

            psParameter.setStarred(false);
            identification.updatePeptideMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Hides a peptide match.
     *
     * @param match the key of the match
     */
    public void hidePeptide(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getPeptideMatchParameter(match, psParameter);
            boolean validated = false;

            for (PeptideFilter matchFilter : filterPreferences.getPeptideHideFilters().values()) {
                if (matchFilter.getExceptions().contains(match)) {
                    matchFilter.removeException(match);
                }
                if (isValidated(match, matchFilter)) {
                    validated = true;
                }
            }

            if (!validated) {
                PeptideFilter peptideFilter;
                if (!filterPreferences.getPeptideHideFilters().containsKey(MatchFilter.MANUAL_SELECTION)) {
                    peptideFilter = new PeptideFilter(MatchFilter.MANUAL_SELECTION, peptideShakerGUI.getFoundModifications());
                    peptideFilter.setDescription("Manual selection via the graphical interface");
                    filterPreferences.getPeptideHideFilters().put(peptideFilter.getName(), peptideFilter);
                } else {
                    peptideFilter = filterPreferences.getPeptideHideFilters().get(MatchFilter.MANUAL_SELECTION);
                }
                peptideFilter.addManualValidation(match);
            }

            psParameter.setHidden(true);
            identification.updatePeptideMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Unhides a peptide match.
     *
     * @param match the key of the match
     */
    public void unHidePeptide(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getPeptideMatchParameter(match, psParameter);

            for (PeptideFilter matchFilter : filterPreferences.getPeptideHideFilters().values()) {
                if (matchFilter.getManualValidation().contains(match)) {
                    matchFilter.removeManualValidation(match);
                }
                if (isValidated(match, matchFilter)) {
                    matchFilter.addException(match);
                }
            }

            psParameter.setHidden(false);
            identification.updatePeptideMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Stars a PSM match.
     *
     * @param match the key of the match
     */
    public void starPsm(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getSpectrumMatchParameter(match, psParameter);
            boolean validated = false;

            if (!validated) {
                for (PsmFilter matchFilter : filterPreferences.getPsmStarFilters().values()) {
                    if (matchFilter.getExceptions().contains(match)) {
                        matchFilter.removeException(match);
                    }
                    if (isValidated(match, matchFilter)) {
                        validated = true;
                    }
                }
                PsmFilter psmFilter;
                if (!filterPreferences.getPsmStarFilters().containsKey(MatchFilter.MANUAL_SELECTION)) {
                    psmFilter = new PsmFilter(MatchFilter.MANUAL_SELECTION, peptideShakerGUI.getMetrics().getFoundCharges(), peptideShakerGUI.getIdentification().getSpectrumFiles());
                    psmFilter.setDescription("Manual selection via the graphical interface");
                    filterPreferences.getPsmStarFilters().put(psmFilter.getName(), psmFilter);
                } else {
                    psmFilter = filterPreferences.getPsmStarFilters().get(MatchFilter.MANUAL_SELECTION);
                }
                psmFilter.addManualValidation(match);
            }

            psParameter.setStarred(true);
            identification.updateSpectrumMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Unstars a PSM match.
     *
     * @param match the key of the match
     */
    public void unStarPsm(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getSpectrumMatchParameter(match, psParameter);

            for (PsmFilter matchFilter : filterPreferences.getPsmStarFilters().values()) {
                if (matchFilter.getManualValidation().contains(match)) {
                    matchFilter.removeManualValidation(match);
                }
                if (isValidated(match, matchFilter)) {
                    matchFilter.addException(match);
                }
            }

            psParameter.setStarred(false);
            identification.updateSpectrumMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Hides a PSM match.
     *
     * @param match the key of the match
     */
    public void hidePsm(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getSpectrumMatchParameter(match, psParameter);
            boolean validated = false;

            if (!validated) {
                for (PsmFilter matchFilter : filterPreferences.getPsmHideFilters().values()) {
                    if (matchFilter.getExceptions().contains(match)) {
                        matchFilter.removeException(match);
                    }
                    if (isValidated(match, matchFilter)) {
                        validated = true;
                    }
                }
                PsmFilter psmFilter;
                if (!filterPreferences.getPsmHideFilters().containsKey(MatchFilter.MANUAL_SELECTION)) {
                    psmFilter = new PsmFilter(MatchFilter.MANUAL_SELECTION, peptideShakerGUI.getMetrics().getFoundCharges(), peptideShakerGUI.getIdentification().getSpectrumFiles());
                    psmFilter.setDescription("Manual selection via the graphical interface");
                    filterPreferences.getPsmHideFilters().put(psmFilter.getName(), psmFilter);
                } else {
                    psmFilter = filterPreferences.getPsmHideFilters().get(MatchFilter.MANUAL_SELECTION);
                }
                psmFilter.addManualValidation(match);
            }

            psParameter.setHidden(true);
            identification.updateSpectrumMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Unhides a psm match.
     *
     * @param match the key of the match
     */
    public void unHidePsm(String match) {

        try {
            Identification identification = peptideShakerGUI.getIdentification();
            FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
            PSParameter psParameter = new PSParameter();
            psParameter = (PSParameter) identification.getSpectrumMatchParameter(match, psParameter);

            for (PsmFilter matchFilter : filterPreferences.getPsmHideFilters().values()) {
                if (matchFilter.getManualValidation().contains(match)) {
                    matchFilter.removeManualValidation(match);
                }
                if (isValidated(match, matchFilter)) {
                    matchFilter.addException(match);
                }
            }

            psParameter.setHidden(false);
            identification.updateSpectrumMatchParameter(match, psParameter);
            peptideShakerGUI.setDataSaved(false);
        } catch (Exception e) {
            peptideShakerGUI.catchException(e);
        }
    }

    /**
     * Tests whether a protein match should be hidden according to the
     * implemented filters.
     *
     * @param match the key of the match
     * @return a boolean indicating whether a protein match should be hidden
     * according to the implemented filters
     */
    public boolean isProteinHidden(String match) {
        FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();
        for (ProteinFilter matchFilter : filterPreferences.getProteinHideFilters().values()) {
            if (matchFilter.isActive() && isValidated(match, matchFilter)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether a peptide match should be hidden according to the
     * implemented filters.
     *
     * @param match the key of the match
     * @return a boolean indicating whether a protein match should be hidden
     * according to the implemented filters
     */
    public boolean isPeptideHidden(String match) {
        FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();

        for (PeptideFilter matchFilter : filterPreferences.getPeptideHideFilters().values()) {
            if (matchFilter.isActive() && isValidated(match, matchFilter)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests whether a psm match should be hidden according to the implemented
     * filters.
     *
     * @param match the key of the match
     * @return a boolean indicating whether a protein match should be hidden
     * according to the implemented filters
     */
    public boolean isPsmHidden(String match) {
        FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();

        for (PsmFilter matchFilter : filterPreferences.getPsmHideFilters().values()) {
            if (matchFilter.isActive() && isValidated(match, matchFilter)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests whether a protein match should be starred according to the
     * implemented filters.
     *
     * @param match the key of the match
     * @return a boolean indicating whether a protein match should be hidden
     * according to the implemented filters
     */
    public boolean isProteinStarred(String match) {
        FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();

        for (ProteinFilter matchFilter : filterPreferences.getProteinStarFilters().values()) {
            if (matchFilter.isActive() && isValidated(match, matchFilter)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests whether a peptide match should be starred according to the
     * implemented filters.
     *
     * @param match the key of the match
     * @return a boolean indicating whether a protein match should be hidden
     * according to the implemented filters
     */
    public boolean isPeptideStarred(String match) {
        FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();

        for (PeptideFilter matchFilter : filterPreferences.getPeptideStarFilters().values()) {
            if (matchFilter.isActive() && isValidated(match, matchFilter)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests whether a PSM match should be starred according to the implemented
     * filters.
     *
     * @param match the key of the match
     * @return a boolean indicating whether a protein match should be hidden
     * according to the implemented filters
     */
    public boolean isPsmStarred(String match) {
        FilterPreferences filterPreferences = peptideShakerGUI.getFilterPreferences();

        for (PsmFilter matchFilter : filterPreferences.getPsmStarFilters().values()) {
            if (matchFilter.isActive() && isValidated(match, matchFilter)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests whether a protein match is validated by a given filter.
     *
     * @param proteinKey the key of the protein match
     * @param proteinFilter the filter
     * @return a boolean indicating whether a protein match is validated by a
     * given filter
     */
    public boolean isValidated(String proteinKey, ProteinFilter proteinFilter) {

        try {
            if (proteinFilter.getExceptions().contains(proteinKey)) {
                return false;
            }

            if (proteinFilter.getManualValidation().size() > 0) {
                if (proteinFilter.getManualValidation().contains(proteinKey)) {
                    return true;
                } else {
                    return false;
                }
            }

            if (proteinFilter.getIdentifierRegex() != null) {
                String test = "test_" + proteinKey + "_test";
                if (test.split(proteinFilter.getIdentifierRegex()).length == 1) {
                    boolean found = false;
                    for (String accession : ProteinMatch.getAccessions(proteinKey)) {
                        test = "test_" + sequenceFactory.getHeader(accession).getDescription().toLowerCase() + "_test";
                        if (test.split(proteinFilter.getIdentifierRegex().toLowerCase()).length > 1) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }
            }

            PSParameter psParameter = new PSParameter();
            Identification identification = peptideShakerGUI.getIdentification();
            psParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, psParameter);

            if (proteinFilter.getPi() != 5) {
                if (proteinFilter.getPiComparison() == ComparisonType.NOT_EQUAL
                        && psParameter.getProteinInferenceClass() == proteinFilter.getPi()) {
                    return false;
                } else if (proteinFilter.getPiComparison() == ComparisonType.EQUAL
                        && psParameter.getProteinInferenceClass() != proteinFilter.getPi()) {
                    return false;
                }
            }

            if (proteinFilter.getProteinScore() != null) {
                if (proteinFilter.getProteinScoreComparison() == ComparisonType.AFTER) {
                    if (psParameter.getProteinScore() <= proteinFilter.getProteinScore()) {
                        return false;
                    }
                } else if (proteinFilter.getProteinScoreComparison() == ComparisonType.BEFORE) {
                    if (psParameter.getProteinScore() >= proteinFilter.getProteinScore()) {
                        return false;
                    }
                } else if (proteinFilter.getProteinScoreComparison() == ComparisonType.EQUAL) {
                    if (psParameter.getProteinScore() != proteinFilter.getProteinScore()) {
                        return false;
                    }
                } else if (proteinFilter.getProteinScoreComparison() == ComparisonType.NOT_EQUAL) {
                    if (psParameter.getProteinScore() == proteinFilter.getProteinScore()) {
                        return false;
                    }
                }
            }

            if (proteinFilter.getProteinConfidence() != null) {
                if (proteinFilter.getProteinConfidenceComparison() == ComparisonType.AFTER) {
                    if (psParameter.getProteinConfidence() <= proteinFilter.getProteinConfidence()) {
                        return false;
                    }
                } else if (proteinFilter.getProteinConfidenceComparison() == ComparisonType.BEFORE) {
                    if (psParameter.getProteinConfidence() >= proteinFilter.getProteinConfidence()) {
                        return false;
                    }
                } else if (proteinFilter.getProteinConfidenceComparison() == ComparisonType.EQUAL) {
                    if (psParameter.getProteinConfidence() != proteinFilter.getProteinConfidence()) {
                        return false;
                    }
                } else if (proteinFilter.getProteinConfidenceComparison() == ComparisonType.NOT_EQUAL) {
                    if (psParameter.getProteinConfidence() == proteinFilter.getProteinConfidence()) {
                        return false;
                    }
                }
            }

            if (proteinFilter.getnPeptides() != null
                    || proteinFilter.getProteinNSpectra() != null
                    || proteinFilter.getProteinCoverage() != null
                    || proteinFilter.getSpectrumCounting() != null) {
                ProteinMatch proteinMatch = identification.getProteinMatch(proteinKey);

                if (proteinFilter.getnPeptides() != null) {
                    if (proteinFilter.getnPeptidesComparison() == ComparisonType.AFTER) {
                        if (proteinMatch.getPeptideMatches().size() <= proteinFilter.getnPeptides()) {
                            return false;
                        }
                    } else if (proteinFilter.getnPeptidesComparison() == ComparisonType.BEFORE) {
                        if (proteinMatch.getPeptideMatches().size() >= proteinFilter.getnPeptides()) {
                            return false;
                        }
                    } else if (proteinFilter.getnPeptidesComparison() == ComparisonType.EQUAL) {
                        if (proteinMatch.getPeptideMatches().size() != proteinFilter.getnPeptides()) {
                            return false;
                        }
                    } else if (proteinFilter.getnPeptidesComparison() == ComparisonType.NOT_EQUAL) {
                        if (proteinMatch.getPeptideMatches().size() == proteinFilter.getnPeptides()) {
                            return false;
                        }
                    }
                }
                IdentificationFeaturesGenerator identificationFeaturesGenerator = peptideShakerGUI.getIdentificationFeaturesGenerator();
                if (proteinFilter.getProteinNSpectra() != null) {
                    try {
                        if (proteinFilter.getnSpectraComparison() == ComparisonType.AFTER) {
                            if (identificationFeaturesGenerator.getNSpectra(proteinKey) <= proteinFilter.getProteinNSpectra()) {
                                return false;
                            }
                        } else if (proteinFilter.getnSpectraComparison() == ComparisonType.BEFORE) {
                            if (identificationFeaturesGenerator.getNSpectra(proteinKey) >= proteinFilter.getProteinNSpectra()) {
                                return false;
                            }
                        } else if (proteinFilter.getnSpectraComparison() == ComparisonType.EQUAL) {
                            if (identificationFeaturesGenerator.getNSpectra(proteinKey).intValue() != proteinFilter.getProteinNSpectra().intValue()) {
                                return false;
                            }
                        } else if (proteinFilter.getnSpectraComparison() == ComparisonType.NOT_EQUAL) {
                            if (identificationFeaturesGenerator.getNSpectra(proteinKey).intValue() == proteinFilter.getProteinNSpectra().intValue()) {
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        peptideShakerGUI.catchException(e);
                    }
                }

                if (proteinFilter.getProteinCoverage() != null) {
                    try {
                        double sequenceCoverage = 100 * identificationFeaturesGenerator.getSequenceCoverage(proteinKey);
                        if (proteinFilter.getProteinCoverageComparison() == ComparisonType.AFTER) {
                            if (sequenceCoverage <= proteinFilter.getProteinCoverage()) {
                                return false;
                            }
                        } else if (proteinFilter.getProteinCoverageComparison() == ComparisonType.BEFORE) {
                            if (sequenceCoverage >= proteinFilter.getProteinCoverage()) {
                                return false;
                            }
                        } else if (proteinFilter.getProteinCoverageComparison() == ComparisonType.EQUAL) {
                            if (sequenceCoverage != proteinFilter.getProteinCoverage()) {
                                return false;
                            }
                        } else if (proteinFilter.getProteinCoverageComparison() == ComparisonType.NOT_EQUAL) {
                            if (sequenceCoverage == proteinFilter.getProteinCoverage()) {
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        peptideShakerGUI.catchException(e);
                    }
                }

                if (proteinFilter.getSpectrumCounting() != null) {
                    try {
                        double spectrumCounting = identificationFeaturesGenerator.getSpectrumCounting(proteinKey);
                        if (proteinFilter.getSpectrumCountingComparison() == ComparisonType.AFTER) {
                            if (spectrumCounting <= proteinFilter.getSpectrumCounting()) {
                                return false;
                            }
                        } else if (proteinFilter.getSpectrumCountingComparison() == ComparisonType.BEFORE) {
                            if (spectrumCounting >= proteinFilter.getSpectrumCounting()) {
                                return false;
                            }
                        } else if (proteinFilter.getSpectrumCountingComparison() == ComparisonType.EQUAL) {
                            if (spectrumCounting != proteinFilter.getSpectrumCounting()) {
                                return false;
                            }
                        } else if (proteinFilter.getSpectrumCountingComparison() == ComparisonType.NOT_EQUAL) {
                            if (spectrumCounting == proteinFilter.getSpectrumCounting()) {
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        peptideShakerGUI.catchException(e);
                    }
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            peptideShakerGUI.catchException(e);
            return false;
        }
    }

    /**
     * Tests whether a peptide match is validated by a given filter.
     *
     * @param peptideKey the key of the peptide match
     * @param peptideFilter the filter
     * @return a boolean indicating whether a peptide match is validated by a
     * given filter
     */
    public boolean isValidated(String peptideKey, PeptideFilter peptideFilter) {

        try {
            if (peptideFilter.getExceptions().contains(peptideKey)) {
                return false;
            }

            if (peptideFilter.getManualValidation().size() > 0) {
                if (peptideFilter.getManualValidation().contains(peptideKey)) {
                    return true;
                } else {
                    return false;
                }
            }

            PSParameter psParameter = new PSParameter();
            boolean found = false;

            for (String ptm : peptideFilter.getModificationStatus()) {
                if (ptm.equals(PtmPanel.NO_MODIFICATION)) {
                    if (!Peptide.isModified(peptideKey)) {
                        found = true;
                        break;
                    }
                } else {
                    if (Peptide.isModified(peptideKey, ptm)) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                return false;
            }

            Identification identification = peptideShakerGUI.getIdentification();
            psParameter = (PSParameter) identification.getPeptideMatchParameter(peptideKey, psParameter);

            if (peptideFilter.getPi() != 5) {
                if (peptideFilter.getPiComparison() == ComparisonType.NOT_EQUAL
                        && psParameter.getProteinInferenceClass() == peptideFilter.getPi()) {
                    return false;
                } else if (peptideFilter.getPiComparison() == ComparisonType.EQUAL
                        && psParameter.getProteinInferenceClass() != peptideFilter.getPi()) {
                    return false;
                }
            }

            if (peptideFilter.getPeptideScore() != null) {
                if (peptideFilter.getPeptideScoreComparison() == ComparisonType.AFTER) {
                    if (psParameter.getPeptideScore() <= peptideFilter.getPeptideScore()) {
                        return false;
                    }
                } else if (peptideFilter.getPeptideScoreComparison() == ComparisonType.BEFORE) {
                    if (psParameter.getPeptideScore() >= peptideFilter.getPeptideScore()) {
                        return false;
                    }
                } else if (peptideFilter.getPeptideScoreComparison() == ComparisonType.EQUAL) {
                    if (psParameter.getPeptideScore() != peptideFilter.getPeptideScore()) {
                        return false;
                    }
                } else if (peptideFilter.getPeptideScoreComparison() == ComparisonType.NOT_EQUAL) {
                    if (psParameter.getPeptideScore() == peptideFilter.getPeptideScore()) {
                        return false;
                    }
                }
            }

            if (peptideFilter.getPeptideConfidence() != null) {
                if (peptideFilter.getPeptideConfidenceComparison() == ComparisonType.AFTER) {
                    if (psParameter.getPeptideConfidence() <= peptideFilter.getPeptideConfidence()) {
                        return false;
                    }
                } else if (peptideFilter.getPeptideConfidenceComparison() == ComparisonType.BEFORE) {
                    if (psParameter.getPeptideConfidence() >= peptideFilter.getPeptideConfidence()) {
                        return false;
                    }
                } else if (peptideFilter.getPeptideConfidenceComparison() == ComparisonType.EQUAL) {
                    if (psParameter.getPeptideConfidence() != peptideFilter.getPeptideConfidence()) {
                        return false;
                    }
                } else if (peptideFilter.getPeptideConfidenceComparison() == ComparisonType.NOT_EQUAL) {
                    if (psParameter.getPeptideConfidence() == peptideFilter.getPeptideConfidence()) {
                        return false;
                    }
                }
            }

            if (peptideFilter.getNSpectra() != null
                    || peptideFilter.getProtein() != null) {
                PeptideMatch peptideMatch = identification.getPeptideMatch(peptideKey);
                if (peptideFilter.getNSpectra() != null) {
                    if (peptideFilter.getnSpectraComparison() == ComparisonType.AFTER) {
                        if (peptideMatch.getSpectrumCount() <= peptideFilter.getNSpectra()) {
                            return false;
                        }
                    } else if (peptideFilter.getnSpectraComparison() == ComparisonType.BEFORE) {
                        if (peptideMatch.getSpectrumCount() >= peptideFilter.getNSpectra()) {
                            return false;
                        }
                    } else if (peptideFilter.getnSpectraComparison() == ComparisonType.EQUAL) {
                        if (peptideMatch.getSpectrumCount() != peptideFilter.getNSpectra()) {
                            return false;
                        }
                    } else if (peptideFilter.getnSpectraComparison() == ComparisonType.NOT_EQUAL) {
                        if (peptideMatch.getSpectrumCount() != peptideFilter.getNSpectra()) {
                            return false;
                        }
                    }
                }

                if (peptideFilter.getProtein() != null) {
                    found = false;
                    for (String accession : peptideMatch.getTheoreticPeptide().getParentProteins()) {
                        if (accession.split(peptideFilter.getProtein()).length > 1) {
                            found = true;
                            break;
                        }
                        if (sequenceFactory.getHeader(accession).getDescription() != null
                                && sequenceFactory.getHeader(accession).getDescription().split(peptideFilter.getProtein()).length > 1) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }
            }

            // sequence pattern
            if (peptideFilter.getSequence() != null && peptideFilter.getSequence().trim().length() > 0) {
                PeptideMatch peptideMatch = identification.getPeptideMatch(peptideKey);
                String peptideSequence = peptideMatch.getTheoreticPeptide().getSequence();
                Matcher m;
                if (peptideFilter.getSequencePattern() != null) {
                    m = peptideFilter.getSequencePattern().matcher(peptideSequence);
                } else {
                    Pattern p = Pattern.compile("(.*?)" + peptideFilter.getSequence() + "(.*?)");
                    m = p.matcher(peptideSequence);
                }
                if (!m.matches()) {
                    return false;
                }
            }

            // protein pattern
            if (peptideFilter.getProtein() != null && peptideFilter.getProtein().trim().length() > 0) {
                PeptideMatch peptideMatch = identification.getPeptideMatch(peptideKey);
                String accessions = "";
                for (String accession : peptideMatch.getTheoreticPeptide().getParentProteins()) {
                    accessions += accession + " ";
                }
                Matcher m;
                if (peptideFilter.getProteinPattern() != null) {
                    m = peptideFilter.getProteinPattern().matcher(accessions);
                } else {
                    Pattern p = Pattern.compile("(.*?)" + peptideFilter.getProtein() + "(.*?)");
                    m = p.matcher(accessions);
                }
                if (!m.matches()) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            peptideShakerGUI.catchException(e);
            return false;
        }
    }

    /**
     * Tests whether a spectrum match is validated by a given filter.
     *
     * @param spectrumKey the key of the spectrum match
     * @param psmFilter the filter
     * @return a boolean indicating whether a spectrum match is validated by a
     * given filter
     */
    public boolean isValidated(String spectrumKey, PsmFilter psmFilter) {

        try {
            if (psmFilter.getExceptions().contains(spectrumKey)) {
                return false;
            }
            if (psmFilter.getManualValidation().size() > 0) {
                if (psmFilter.getManualValidation().contains(spectrumKey)) {
                    return true;
                } else {
                    return false;
                }
            }

            Identification identification = peptideShakerGUI.getIdentification();
            PSParameter psParameter = new PSParameter();

            if (psmFilter.getPsmScore() != null
                    || psmFilter.getPsmConfidence() != null) {
                psParameter = (PSParameter) identification.getSpectrumMatchParameter(spectrumKey, psParameter);

                if (psmFilter.getPsmScore() != null) {
                    if (psmFilter.getPsmScoreComparison() == ComparisonType.AFTER) {
                        if (psParameter.getPsmScore() <= psmFilter.getPsmScore()) {
                            return false;
                        }
                    } else if (psmFilter.getPsmScoreComparison() == ComparisonType.BEFORE) {
                        if (psParameter.getPsmScore() >= psmFilter.getPsmScore()) {
                            return false;
                        }
                    } else if (psmFilter.getPsmScoreComparison() == ComparisonType.EQUAL) {
                        if (psParameter.getPsmScore() != psmFilter.getPsmScore()) {
                            return false;
                        }
                    } else if (psmFilter.getPsmScoreComparison() == ComparisonType.NOT_EQUAL) {
                        if (psParameter.getPsmScore() == psmFilter.getPsmScore()) {
                            return false;
                        }
                    }
                }

                if (psmFilter.getPsmConfidence() != null) {
                    if (psmFilter.getPsmConfidenceComparison() == ComparisonType.AFTER) {
                        if (psParameter.getPsmConfidence() <= psmFilter.getPsmConfidence()) {
                            return false;
                        }
                    } else if (psmFilter.getPsmConfidenceComparison() == ComparisonType.BEFORE) {
                        if (psParameter.getPsmConfidence() >= psmFilter.getPsmConfidence()) {
                            return false;
                        }
                    } else if (psmFilter.getPsmConfidenceComparison() == ComparisonType.EQUAL) {
                        if (psParameter.getPsmConfidence() != psmFilter.getPsmConfidence()) {
                            return false;
                        }
                    } else if (psmFilter.getPsmConfidenceComparison() == ComparisonType.NOT_EQUAL) {
                        if (psParameter.getPsmConfidence() == psmFilter.getPsmConfidence()) {
                            return false;
                        }
                    }
                }
            }

            if (psmFilter.getPrecursorMz() != null
                    || psmFilter.getPrecursorRT() != null
                    || psmFilter.getPrecursorMzError() != null) {
                Precursor precursor = peptideShakerGUI.getPrecursor(spectrumKey);
                if (psmFilter.getPrecursorMz() != null) {
                    if (psmFilter.getPrecursorMzComparison() == ComparisonType.AFTER) {
                        if (precursor.getMz() <= psmFilter.getPrecursorMz()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorMzComparison() == ComparisonType.BEFORE) {
                        if (precursor.getMz() >= psmFilter.getPrecursorMz()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorMzComparison() == ComparisonType.EQUAL) {
                        if (precursor.getMz() != psmFilter.getPrecursorMz()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorMzComparison() == ComparisonType.NOT_EQUAL) {
                        if (precursor.getMz() == psmFilter.getPrecursorMz()) {
                            return false;
                        }
                    }
                }

                if (psmFilter.getPrecursorRT() != null) {
                    if (psmFilter.getPrecursorRTComparison() == ComparisonType.AFTER) {
                        if (precursor.getRt() <= psmFilter.getPrecursorRT()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorRTComparison() == ComparisonType.BEFORE) {
                        if (precursor.getRt() >= psmFilter.getPrecursorRT()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorRTComparison() == ComparisonType.EQUAL) {
                        if (precursor.getRt() != psmFilter.getPrecursorRT()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorRTComparison() == ComparisonType.NOT_EQUAL) {
                        if (precursor.getRt() == psmFilter.getPrecursorRT()) {
                            return false;
                        }
                    }
                }

                if (psmFilter.getPrecursorMzError() != null) {
                    SpectrumMatch spectrumMatch = identification.getSpectrumMatch(spectrumKey);
                    double error = Math.abs(spectrumMatch.getBestAssumption().getDeltaMass(precursor.getMz(), peptideShakerGUI.getSearchParameters().isPrecursorAccuracyTypePpm()));
                    if (psmFilter.getPrecursorMzErrorComparison() == ComparisonType.AFTER) {
                        if (error <= psmFilter.getPrecursorMzError()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorMzErrorComparison() == ComparisonType.BEFORE) {
                        if (error >= psmFilter.getPrecursorMzError()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorMzErrorComparison() == ComparisonType.EQUAL) {
                        if (error != psmFilter.getPrecursorMzError()) {
                            return false;
                        }
                    } else if (psmFilter.getPrecursorMzErrorComparison() == ComparisonType.NOT_EQUAL) {
                        if (error == psmFilter.getPrecursorMzError()) {
                            return false;
                        }
                    }
                }
            }
            if (psmFilter.getCharges().size() != peptideShakerGUI.getMetrics().getFoundCharges().size()) {
                SpectrumMatch spectrumMatch = identification.getSpectrumMatch(spectrumKey);
                int charge = spectrumMatch.getBestAssumption().getIdentificationCharge().value;
                if (!psmFilter.getCharges().contains(charge)) {
                    return false;
                }
            }

            if (!psmFilter.getFileNames().contains(Spectrum.getSpectrumFile(spectrumKey))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            peptideShakerGUI.catchException(e);
            return false;
        }
    }
}
