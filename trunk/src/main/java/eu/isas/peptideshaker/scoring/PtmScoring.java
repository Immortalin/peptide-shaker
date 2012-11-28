package eu.isas.peptideshaker.scoring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * This class contains score about the PTM localization scoring.
 *
 * @author Marc Vaudel
 */
public class PtmScoring implements Serializable {

    /**
     * Serial version UID for post-serialization compatibility.
     */
    static final long serialVersionUID = -3357368272501542941L;
    /**
     * Index indicating that the modification was not found.
     */
    public static final int NOT_FOUND = -1;
    /**
     * Index for a random location choice.
     */
    public static final int RANDOM = 0;
    /**
     * Index for a doubtful assignment.
     */
    public static final int DOUBTFUL = 1;
    /**
     * Index for a confident assignment.
     */
    public static final int CONFIDENT = 2;
    /**
     * Index for a very confident assignment.
     */
    public static final int VERY_CONFIDENT = 3;
    /**
     * The delta scores indexed by the modification location possibility.
     */
    private HashMap<String, Double> deltaScores = new HashMap<String, Double>();
    /**
     * The A scores indexed by the modification location possibility.
     */
    private HashMap<String, Double> aScores = new HashMap<String, Double>();
    /**
     * The name of the modification of interest.
     */
    private String ptmName;
    /**
     * The separator used to separate locations in the modification location
     * key.
     */
    public static final String separator = "|";
    /**
     * The retained PTM site assignment.
     */
    private ArrayList<Integer> ptmLocation = new ArrayList<Integer>();
    /**
     * For a peptide, other locations where this modification was found.
     */
    private ArrayList<Integer> secondaryLocations = new ArrayList<Integer>();
    /**
     * The confidence of the ptm site assignment
     */
    private int siteConfidence = NOT_FOUND;
    /**
     * Boolean indicating whether a conflict was found during PTM site
     * inference.
     */
    private boolean conflict = false;

    /**
     * Constructor.
     *
     * @param ptmName the name of the PTM of interest.
     */
    public PtmScoring(String ptmName) {
        this.ptmName = ptmName;
    }

    /**
     * Returns the name of the inspected protein.
     *
     * @return the name of the inspected protein
     */
    public String getName() {
        return ptmName;
    }

    /**
     * Adds a delta score for the given locations combination.
     *
     * @param locations the combination of locations
     * @param deltaScore The corresponding delta score
     */
    public void addDeltaScore(ArrayList<Integer> locations, double deltaScore) {
        String locationsKey = getKey(locations);
        addDeltaScore(locationsKey, deltaScore);
    }

    /**
     * Adds an A-score for the given locations combination.
     *
     * @param locations the combination of locations
     * @param aScore The corresponding A-score
     */
    public void addAScore(ArrayList<Integer> locations, double aScore) {
        String locationsKey = getKey(locations);
        addAScore(locationsKey, aScore);
    }

    /**
     * Adds a delta score for the given locations combination given as a key.
     *
     * @param locationsKey the combination of locations
     * @param deltaScore The corresponding delta score
     */
    public void addDeltaScore(String locationsKey, double deltaScore) {
        if (!deltaScores.containsKey(locationsKey)
                || deltaScores.get(locationsKey) < deltaScore) {
            deltaScores.put(locationsKey, deltaScore);
        }
    }

    /**
     * Adds an A score for the given locations combination given as a key.
     *
     * @param locationsKey the combination of locations
     * @param aScore The corresponding A-score
     */
    public void addAScore(String locationsKey, double aScore) {
        if (!aScores.containsKey(locationsKey)
                || aScores.get(locationsKey) > aScore) {
            aScores.put(locationsKey, aScore);
        }
    }

    /**
     * Returns the implemented locations possibilities for delta scores.
     *
     * @return the implemented locations possibilities for delta scores
     */
    public ArrayList<String> getDeltaScorelocations() {
        return new ArrayList<String>(deltaScores.keySet());
    }

    /**
     * Returns the best scoring modification profile based on the delta score.
     *
     * @return the best scoring modification profile based on the delta score
     */
    public String getBestDeltaScoreLocations() {
        String bestKey = null;
        for (String key : deltaScores.keySet()) {
            if (bestKey == null || deltaScores.get(bestKey) < deltaScores.get(key)) {
                bestKey = key;
            }
        }
        return bestKey;
    }

    /**
     * Returns the best scoring modification profile based on the a-score score.
     *
     * @return the best scoring modification profile based on the a-score score
     */
    public String getBestAScoreLocations() {
        String bestKey = null;
        for (String key : aScores.keySet()) {
            if (bestKey == null || aScores.get(bestKey) < aScores.get(key)) {
                bestKey = key;
            }
        }
        return bestKey;
    }

    /**
     * Returns the implemented locations for the A-score.
     *
     * @return the implemented locations for the A-score
     */
    public ArrayList<String> getAScoreLocations() {
        return new ArrayList<String>(aScores.keySet());
    }

    /**
     * Returns the delta score for the specified locations possibility given as
     * a key.
     *
     * @param locationsKey the locations possibility given as a key
     * @return the delta score
     */
    public Double getDeltaScore(String locationsKey) {
        return deltaScores.get(locationsKey);
    }

    /**
     * Returns the A-score for the specified locations possibility given as a
     * key.
     *
     * @param locationsKey the locations possibility given as a key
     * @return the A-score
     */
    public Double getAScore(String locationsKey) {
        return aScores.get(locationsKey);
    }

    /**
     * Adds all scorings from another score.
     *
     * @param anotherScore another score
     */
    public void addAll(PtmScoring anotherScore) {
        for (String positions : anotherScore.getDeltaScorelocations()) {
            addDeltaScore(positions, anotherScore.getDeltaScore(positions));
        }
        for (String positions : anotherScore.getAScoreLocations()) {
            addAScore(positions, anotherScore.getAScore(positions));
        }
        siteConfidence = Math.max(siteConfidence, anotherScore.getPtmSiteConfidence());
        if (anotherScore.getPtmSiteConfidence() < CONFIDENT) {
            for (int newLocation : anotherScore.getPtmLocation()) {
                if (!secondaryLocations.contains(newLocation) && !ptmLocation.contains(newLocation)) {
                    secondaryLocations.add(newLocation);
                }
            }
        } else {
            for (Integer newLocation : anotherScore.getPtmLocation()) {
                if (!ptmLocation.contains(newLocation)) {
                    ptmLocation.add(newLocation);
                }
                if (secondaryLocations.contains(newLocation)) {
                    secondaryLocations.remove(newLocation);
                }
            }
        }
    }

    /**
     * Returns the modification locations from a key.
     *
     * @param locationsKey the modification locations key
     * @return the modification locations as an ArrayList containing all
     * possible locations
     */
    public static ArrayList<Integer> getLocations(String locationsKey) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (locationsKey.length() > 1) {
            String tempKey = locationsKey.substring(0, locationsKey.lastIndexOf(separator));
            while (tempKey.length() >= 1) {
                int index = tempKey.lastIndexOf(separator);
                result.add(new Integer(tempKey.substring(index + 1)));
                if (index > -1) {
                    tempKey = tempKey.substring(0, index);
                } else {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Returns the key corresponding to modification possible locations.
     *
     * @param locations the possible modification locations in the sequence
     * @return the corresponding key
     */
    public static String getKey(ArrayList<Integer> locations) {
        Collections.sort(locations);
        String result = "";
        for (int loc : locations) {
            result += loc + separator;
        }
        return result;
    }

    /**
     * Sets the PTM site assignment results.
     *
     * @param location the location of the PTM
     * @param ptmSiteConfidence the location confidence as indexed by the static
     * fields
     */
    public void setPtmSite(String location, int ptmSiteConfidence) {
        this.siteConfidence = ptmSiteConfidence;
        this.ptmLocation = getLocations(location);
    }

    /**
     * Returns the PTM locations.
     *
     * @return the PTM location
     */
    public ArrayList<Integer> getPtmLocation() {
        return ptmLocation;
    }

    /**
     * Returns the secondary PTM locations.
     *
     * @return the PTM location
     */
    public ArrayList<Integer> getSecondaryPtmLocations() {
        return secondaryLocations;
    }

    /**
     * Adds a ptm location
     *
     * @param newLocation the new location
     */
    public void addPtmLocation(int newLocation) {
        if (!ptmLocation.contains(newLocation)) {
            ptmLocation.add(newLocation);
        }
    }

    /**
     * Adds a ptm secondary location
     *
     * @param newLocation the ptm secondary location
     */
    public void addPtmSecondaryLocation(int newLocation) {
        if (!secondaryLocations.contains(newLocation)) {
            secondaryLocations.add(newLocation);
        }
    }

    /**
     * Removes a ptm location
     *
     * @param location the location to remove
     */
    public void removePtmLocation(Integer location) {
        ptmLocation.remove(location);
    }

    /**
     * Removes a secondary location
     *
     * @param location the location to remove
     */
    public void removePtmSecondaryLocation(Integer location) {
        secondaryLocations.remove(location);
    }

    /**
     * Returns the PTM location confidence as indexed by the satic fields.
     *
     * @return the PTM location confidence
     */
    public int getPtmSiteConfidence() {
        return siteConfidence;
    }

    /**
     * Indicates whether the site inference is conflicting.
     *
     * @return a boolean indicating whether the site inference was conflicting
     */
    public boolean isConflict() {
        return conflict;
    }

    /**
     * Sets whether the site inference is conflicting.
     *
     * @param conflict a boolean indicating whether the site inference was
     * conflicting
     */
    public void setConflict(boolean conflict) {
        this.conflict = conflict;
    }

    /**
     * Convenience method returning all confidence levels as string.
     *
     * @return an array with all confidence levels as string
     */
    public static String[] getPossibleConfidenceLevels() {
        String[] result = new String[5];
        result[0] = "Not Found";
        result[1] = "Random";
        result[2] = "Doubtful";
        result[3] = "Confident";
        result[4] = "Very Confident";
        return result;
    }

    /**
     * Convenience method returning the given confidence level as a string.
     *
     * @param index the confidence level
     * @return the corresponding string
     */
    public static String getConfidenceLevel(int index) {
        switch (index) {
            case -1:
                return "Not Found";
            case 0:
                return "Random";
            case 1:
                return "Doubtful";
            case 2:
                return "Confident";
            case 3:
                return "Very Confident";
            default:
                return "";
        }
    }
}
