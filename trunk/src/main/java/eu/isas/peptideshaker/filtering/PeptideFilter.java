package eu.isas.peptideshaker.filtering;

import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.RowFilter.ComparisonType;

/**
 * Peptide Filter.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class PeptideFilter extends MatchFilter {

    /**
     * Serial number for serialization compatibility.
     */
    static final long serialVersionUID = 959658989341486818L;
    /**
     * A protein regex.
     */
    private String protein = null;
    /**
     * Sequence regex.
     */
    private String sequence = null;
    /**
     * The compiled protein pattern.
     */
    private Pattern proteinPattern = null;
    /**
     * The compiled peptide sequence pattern.
     */
    private Pattern sequencePattern = null;
    /**
     * Number of spectra limit.
     */
    private Integer nSpectra = null;
    /**
     * The type of comparison to be used for the number of spectra.
     */
    private ComparisonType nSpectraComparison = ComparisonType.EQUAL;
    /**
     * Score limit.
     */
    private Double peptideScore = null;
    /**
     * The type of comparison to be used for the peptide score.
     */
    private ComparisonType peptideScoreComparison = ComparisonType.EQUAL;
    /**
     * Confidence limit.
     */
    private Double peptideConfidence = null;
    /**
     * The type of comparison to be used for the peptide confidence.
     */
    private ComparisonType peptideConfidenceComparison = ComparisonType.EQUAL;
    /**
     * The current protein inference filter selection.
     */
    private int pi = 5;
    /**
     * The type of comparison to be used for the PI.
     */
    private ComparisonType piComparison = ComparisonType.EQUAL;
    /**
     * The list of modifications allowed for the peptide.
     */
    private ArrayList<String> modificationStatus;

    /**
     * Constructor.
     *
     * @param name the name of the filter
     * @param allModifications list of all modifications found in peptides
     */
    public PeptideFilter(String name, ArrayList<String> allModifications) {
        this.name = name;
        this.modificationStatus = allModifications;
        this.filterType = FilterType.PEPTIDE;
    }

    /**
     * Returns the threshold for the peptide confidence.
     *
     * @return the threshold for the peptide confidence
     */
    public Double getPeptideConfidence() {
        return peptideConfidence;
    }

    /**
     * Sets the threshold for the peptide confidence.
     *
     * @param peptideConfidence the threshold for the peptide confidence
     */
    public void setPeptideConfidence(Double peptideConfidence) {
        this.peptideConfidence = peptideConfidence;
    }

    /**
     * Returns the threshold for the number of spectra.
     *
     * @return the threshold for the number of spectra
     */
    public Integer getNSpectra() {
        return nSpectra;
    }

    /**
     * Sets the threshold for the number of spectra.
     *
     * @param nSpectra the threshold for the number of spectra
     */
    public void setNSpectra(Integer nSpectra) {
        this.nSpectra = nSpectra;
    }

    /**
     * Returns the threshold for the peptide score
     *
     * @return the threshold for the peptide score
     */
    public Double getPeptideScore() {
        return peptideScore;
    }

    /**
     * Sets the threshold for the peptide score.
     *
     * @param peptideScore the threshold for the peptide score
     */
    public void setPeptideScore(Double peptideScore) {
        this.peptideScore = peptideScore;
    }

    /**
     * Returns the comparison type used for the number of spectra.
     *
     * @return the comparison type used for the number of spectra
     */
    public ComparisonType getnSpectraComparison() {
        return nSpectraComparison;
    }

    /**
     * Sets the comparison type used for the number of spectra.
     *
     * @param nSpectraComparison the comparison type used for the number of
     * spectra
     */
    public void setnSpectraComparison(ComparisonType nSpectraComparison) {
        this.nSpectraComparison = nSpectraComparison;
    }

    /**
     * Returns the protein inference desired.
     *
     * @return the protein inference desired
     */
    public int getPi() {
        return pi;
    }

    /**
     * Sets the protein inference desired.
     *
     * @param pi the protein inference desired
     */
    public void setPi(int pi) {
        this.pi = pi;
    }

    /**
     * Returns the comparison type used for the confidence.
     *
     * @return the comparison type used for the confidence
     */
    public ComparisonType getPeptideConfidenceComparison() {
        return peptideConfidenceComparison;
    }

    /**
     * Sets the comparison type used for the confidence.
     *
     * @param peptideConfidenceComparison the comparison type used for the
     * confidence
     */
    public void setPeptideConfidenceComparison(ComparisonType peptideConfidenceComparison) {
        this.peptideConfidenceComparison = peptideConfidenceComparison;
    }

    /**
     * Returns the comparison type used for the peptide score.
     *
     * @return the comparison type used for the peptide score
     */
    public ComparisonType getPeptideScoreComparison() {
        return peptideScoreComparison;
    }

    /**
     * Sets the comparison type used for the peptide score.
     *
     * @param peptideScoreComparison the comparison type used for the peptide
     * score
     */
    public void setPeptideScoreComparison(ComparisonType peptideScoreComparison) {
        this.peptideScoreComparison = peptideScoreComparison;
    }

    /**
     * Returns the modifications to retain.
     *
     * @return the modifications to retain
     */
    public ArrayList<String> getModificationStatus() {
        return modificationStatus;
    }

    /**
     * Sets the modifications to retain.
     *
     * @param modificationStatus the modifications to retain
     */
    public void setModificationStatus(ArrayList<String> modificationStatus) {
        this.modificationStatus = modificationStatus;
    }

    /**
     * Returns a regular exception to be searched in protein which contain the
     * peptide sequence.
     *
     * @return a regular exception to be searched in protein which contain the
     * peptide sequence
     */
    public String getProtein() {
        return protein;
    }

    /**
     * Sets a regular exception to be searched in protein which contain the
     * peptide sequence.
     *
     * @param protein a regular exception to be searched in protein which
     * contain the peptide sequence
     */
    public void setProtein(String protein) {
        this.protein = protein;
        this.proteinPattern = Pattern.compile("(.*?)" + protein + "(.*?)");
    }

    /**
     * Returns a regex to be found in the sequence.
     *
     * @return a regex to be found in the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets a regex to be found in the sequence.
     *
     * @param sequence a regex to be found in the sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
        this.sequencePattern = Pattern.compile("(.*?)" + sequence + "(.*?)");
    }

    /**
     * Returns the comparison type to use for the PI.
     *
     * @return the comparison type to use for the PI
     */
    public ComparisonType getPiComparison() {
        return piComparison;
    }

    /**
     * Sets the comparison type to use for the PI.
     *
     * @param piComparison the comparison type to use for the PI
     */
    public void setPiComparison(ComparisonType piComparison) {
        this.piComparison = piComparison;
    }

    /**
     * Returns the compiled protein pattern. Null if no pattern is set.
     * 
     * @return the compiled protein pattern
     */
    public Pattern getProteinPattern() {
        if (protein != null) {
            if (proteinPattern != null) {
                return proteinPattern;
            }
        }
        return null;
    }

    /**
     * Returns the compiled peptide sequence pattern. Null if no pattern is set.
     * 
     * @return the compiled peptide sequence pattern
     */
    public Pattern getSequencePattern() {
        if (sequence != null) {
            if (sequencePattern != null) {
                return sequencePattern;
            }
        }
        return null;
    }
}
