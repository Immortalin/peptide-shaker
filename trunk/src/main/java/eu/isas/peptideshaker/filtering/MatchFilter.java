package eu.isas.peptideshaker.filtering;

import com.compomics.util.experiment.ShotgunProtocol;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.preferences.IdentificationParameters;
import eu.isas.peptideshaker.utils.IdentificationFeaturesGenerator;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

/**
 * Abstract representing a filter.
 *
 * @author Marc Vaudel
 */
public abstract class MatchFilter implements Serializable {

    /**
     * Serial number for serialization compatibility.
     */
    static final long serialVersionUID = 7413446840381260115L;
    /**
     * Name of the filter.
     */
    protected String name;
    /**
     * Description of the filter.
     */
    protected String description = "";
    /**
     * boolean indicating whether the filter is active.
     */
    private boolean active = true;
    /**
     * The key of the manually validated matches.
     */
    private ArrayList<String> manualValidation = new ArrayList<String>();
    /**
     * The exceptions to the rule.
     */
    private ArrayList<String> exceptions = new ArrayList<String>();

    /**
     * Name of the manual selection filter.
     */
    public static final String MANUAL_SELECTION = "manual selection";

    /**
     * Enum for the type of possible filter.
     */
    public enum FilterType {

        /**
         * Protein filter.
         */
        PROTEIN,
        /**
         * Peptide filter.
         */
        PEPTIDE,
        /**
         * PSM filter.
         */
        PSM,
        /**
         * Peptide Assumption filter.
         */
        ASSUMPTION
    }
    /**
     * The type of filter.
     */
    protected FilterType filterType;

    /**
     * Return the name of the filter.
     *
     * @return the name of the filter
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the filter.
     *
     * @param newName the name to be given to the filter
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Return the type of the filter.
     *
     * @return the type of the filter
     */
    public FilterType getType() {
        return filterType;
    }

    /**
     * Returns the description of the filter.
     *
     * @return the description of the filter
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the filter.
     *
     * @param description the description of the filter
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Indicates whether the filter is active.
     *
     * @return a boolean indicating whether the filter is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the filter is active.
     *
     * @param active a boolean indicating whether the filter is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the exceptions to the rule.
     *
     * @return the exceptions to the rule
     */
    public ArrayList<String> getExceptions() {
        return exceptions;
    }

    /**
     * Returns the manually validated items.
     *
     * @return the manually validated items
     */
    public ArrayList<String> getManualValidation() {
        return manualValidation;
    }

    /**
     * Adds a manually validated Match.
     *
     * @param matchKey the key of the match to add
     */
    public void addManualValidation(String matchKey) {
        manualValidation.add(matchKey);
    }

    /**
     * Sets the list of manually validated keys.
     *
     * @param manualValidation list of manually validated keys
     */
    public void setManualValidation(ArrayList<String> manualValidation) {
        this.manualValidation = manualValidation;
    }

    /**
     * Adds an exception.
     *
     * @param matchKey the key of the exception to add
     */
    public void addException(String matchKey) {
        exceptions.add(matchKey);
    }

    /**
     * Sets the excepted matches.
     *
     * @param exceptions the excepted matches
     */
    public void setExceptions(ArrayList<String> exceptions) {
        this.exceptions = exceptions;
    }

    /**
     * Removes a manually validated Match.
     *
     * @param matchKey the key of the match to remove
     */
    public void removeManualValidation(String matchKey) {
        manualValidation.remove(matchKey);
    }

    /**
     * Removes an exception.
     *
     * @param matchKey the key of the exception to remove
     */
    public void removeException(String matchKey) {
        exceptions.remove(matchKey);
    }

    /**
     * Tests whether a match is validated by this filter.
     *
     * @param matchKey the key of the match
     * @param identification the identification where to get the information
     * from
     * @param identificationFeaturesGenerator the identification features
     * generator providing identification features
     * @param shotgunProtocol information on the protocol
     * @param identificationParameters the identification parameters
     *
     * @return a boolean indicating whether a match is validated by a given
     * filter
     *
     * @throws IOException thrown if an IOException occurs
     * @throws SQLException thrown if an SQLException occurs
     * @throws InterruptedException thrown if an InterruptedException occurs
     * @throws ClassNotFoundException thrown if a ClassNotFoundException occurs
     * @throws MzMLUnmarshallerException thrown if an MzMLUnmarshallerException
     * occurs
     */
    public abstract boolean isValidated(String matchKey, Identification identification, IdentificationFeaturesGenerator identificationFeaturesGenerator,
            ShotgunProtocol shotgunProtocol, IdentificationParameters identificationParameters) throws IOException, InterruptedException, ClassNotFoundException, SQLException, MzMLUnmarshallerException;
}
