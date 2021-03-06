package eu.isas.peptideshaker.preferences;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * In this class are stored the user preferences for PeptideShaker.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class UserPreferences implements Serializable {

    /**
     * The serial number for serialization compatibility.
     */
    static final long serialVersionUID = 3298905131097982664L;
    /**
     * The width to use for the annotated peaks.
     * @deprecated use the utilities version instead
     */
    private Float spectrumAnnotatedPeakWidth = 1.0f;
    /**
     * The width to use for the background peaks.
     * @deprecated use the utilities version instead
     */
    private Float spectrumBackgroundPeakWidth = 1.0f;
    /**
     * The color to use for the annotated peaks.
     * @deprecated use the utilities version instead
     */
    private Color spectrumAnnotatedPeakColor = Color.RED;
    /**
     * The color to use for the background peaks.
     * @deprecated use the utilities version instead
     */
    private Color spectrumBackgroundPeakColor = new Color(100, 100, 100, 50);
    /**
     * The color used for the sparkline bar chart plots.
     * @deprecated use the utilities version instead
     */
    private Color sparklineColor = new Color(110, 196, 97);
    /**
     * The color used for the non-validated sparkline bar chart plots.
     * @deprecated use the utilities version instead
     */
    private Color sparklineColorNonValidated = new Color(208, 19, 19);
    /**
     * The color used for the not found sparkline bar chart plots.
     * @deprecated use the utilities version instead
     */
    private Color sparklineColorNotFound = new Color(222, 222, 222);
    /**
     * The color used for the possible values sparkline bar chart plots.
     * @deprecated use the utilities version instead
     */
    private Color sparklineColorPossible = new Color(100, 150, 255);
    /**
     * The color of the selected peptide.
     * @deprecated use the utilities version instead
     */
    private Color peptideSelected = new Color(0, 0, 255);
    /**
     * The recent projects.
     */
    private ArrayList<String> recentProjects = new ArrayList<String>();
    /**
     * Show/hide sliders.
     */
    private boolean showSliders = false;
    /**
     * The memory to use by PeptideShaker.
     * @deprecated use the utilities version instead
     */
    private int memoryPreference = 4 * 1024;
    /**
     * The user preferred delta score threshold.
     * @deprecated use PTM scoring preferences instead
     */
    private Double deltaScoreThreshold = 50.0;
    /**
     * The user preferred A-score threshold.
     * @deprecated use PTM scoring preferences instead
     */
    private Double aScoreThreshold = 50.0;
    /**
     * The path to the SearchGUI installation (if any). Makes it possible to
     * start SearchGUI directly from PeptideShaker. Set to null if no path is
     * provided.
     * @deprecated use the utilities version instead
     */
    private String searchGuiPath = null;

    /**
     * Constructor.
     */
    public UserPreferences() {
    }

    /**
     * Getter for the sparkline color.
     *
     * @deprecated use the utilities version instead
     * @return the sparkline color
     */
    public Color getSparklineColor() {
        return sparklineColor;
    }

    /**
     * Setter for the sparkline color.
     *
     * @deprecated use the utilities version instead
     * @param sparklineColor the sparkline color
     */
    public void setSparklineColor(Color sparklineColor) {
        this.sparklineColor = sparklineColor;
    }

    /**
     * Getter for the non-validated sparkline color.
     *
     * @deprecated use the utilities version instead
     * @return the non-validated sparkline color
     */
    public Color getSparklineColorNonValidated() {
        if (sparklineColorNonValidated == null) {
            sparklineColorNonValidated = new Color(255, 0, 0);
        }
        return sparklineColorNonValidated;
    }

    /**
     * Returns the color for a selected peptide.
     *
     * @deprecated use the utilities version instead
     * @return the color for a selected peptide
     */
    public Color getPeptideSelected() {
        if (peptideSelected == null) {
            peptideSelected = new Color(0, 0, 255);
        }
        return peptideSelected;
    }

    /**
     * Returns the color for a not found sparkline bar chart plots.
     *
     * @deprecated use the utilities version instead
     * @return the color for a not found sparkline bar chart plots
     */
    public Color getSparklineColorNotFound() {
        if (sparklineColorNotFound == null) {
            sparklineColorNotFound = new Color(222, 222, 222);
        }
        return sparklineColorNotFound;
    }

    /**
     * Setter for the non-validated sparkline color.
     *
     * @deprecated use the utilities version instead
     * @param sparklineColorNonValidated the non-validated sparkline color
     */
    public void setSparklineColorNonValidated(Color sparklineColorNonValidated) {
        this.sparklineColorNonValidated = sparklineColorNonValidated;
    }

    /**
     * Returns the color for a possible sparkline bar chart plots.
     *
     * @deprecated use the utilities version instead
     * @return the color for a possible sparkline bar chart plots
     */
    public Color getSparklineColorPossible() {
        if (sparklineColorPossible == null) {
            sparklineColorPossible = new Color(235, 235, 235);
        }
        return sparklineColorPossible;
    }

    /**
     * Setter for the possible sparkline color.
     *
     * @deprecated use the utilities version instead
     * @param sparklineColorPossible the possible sparkline color
     */
    public void setSparklineColorPossible(Color sparklineColorPossible) {
        this.sparklineColorPossible = sparklineColorPossible;
    }

    /**
     * Returns whether sliders should be displayed.
     *
     * @return whether sliders should be displayed
     */
    public boolean showSliders() {
        return showSliders;
    }

    /**
     * Sets whether sliders should be displayed.
     *
     * @param showSliders whether sliders should be displayed
     */
    public void setShowSliders(boolean showSliders) {
        this.showSliders = showSliders;
    }

    /**
     * Returns the paths of the recent projects.
     *
     * @return the paths of the recent projects
     */
    public ArrayList<String> getRecentProjects() {
        return recentProjects;
    }

    /**
     * Removes a recent project from the list.
     *
     * @param recentProject the recent project to remove
     */
    public void removerRecentProject(String recentProject) {
        
        // @TODO: the typo in the method name should be fixed, but results in backwards compatibility issues...
        
        recentProjects.remove(recentProject);
    }

    /**
     * Adds a recent project to the list and limits the list of recent projects
     * to a size of 20.
     *
     * @param recentProject the path of the recent project to add
     */
    public void addRecentProject(String recentProject) {
        if (recentProjects.contains(recentProject)) {
            recentProjects.remove(recentProject);
        }
        recentProjects.add(0, recentProject);
        while (recentProjects.size() > 20) {
            recentProjects.remove(recentProjects.size() - 1);
        }
    }

    /**
     * Adds a recent project to the list and limits the list of recent projects
     * to a size of 20.
     *
     * @param recentProject the recent project to add
     */
    public void addRecentProject(File recentProject) {
        addRecentProject(recentProject.getAbsolutePath());
    }

    /**
     * Returns the preferred upper memory limit.
     *
     * @deprecated use the utilities version instead
     * @return the preferred upper memory limit
     */
    public int getMemoryPreference() {
        if (memoryPreference == 0) {
            // needed for backward compatibility
            memoryPreference = 4 * 1024;
        }
        return memoryPreference;
    }

    /**
     * Sets the preferred upper memory limit.
     *
     * @deprecated use the utilities version instead
     * @param memoryPreference the preferred upper memory limit
     */
    public void setMemoryPreference(int memoryPreference) {
        this.memoryPreference = memoryPreference;
    }

    /**
     * Returns the user preferred A-score Threshold.
     *
     * @deprecated use PTM scoring preferences instead
     * @return the user preferred A-score Threshold
     */
    public Double getAScoreThreshold() {
        if (aScoreThreshold == null) {
            aScoreThreshold = 50.0;
        }
        return aScoreThreshold;
    }

    /**
     * Sets the user preferred A-score Threshold.
     *
     * @deprecated use PTM scoring preferences instead
     * @param aScoreThreshold the user preferred A-score Threshold
     */
    public void setAScoreThreshold(Double aScoreThreshold) {
        this.aScoreThreshold = aScoreThreshold;
    }

    /**
     * Returns the user preferred delta score Threshold.
     *
     * @deprecated use PTM scoring preferences instead
     * @return the user preferred delta score Threshold
     */
    public Double getDeltaScoreThreshold() {
        if (deltaScoreThreshold == null) {
            deltaScoreThreshold = 50.0;
        }
        return deltaScoreThreshold;
    }

    /**
     * Sets the user preferred delta score Threshold.
     *
     * @deprecated use PTM scoring preferences instead
     * @param deltaScoreThreshold the user preferred delta score Threshold
     */
    public void setDeltaScoreThreshold(Double deltaScoreThreshold) {
        this.deltaScoreThreshold = deltaScoreThreshold;
    }

    /**
     * Returns the color to use for the annotated peaks.
     *
     * @deprecated use the utilities version instead
     * @return the spectrumAnnotatedPeakColor
     */
    public Color getSpectrumAnnotatedPeakColor() {

        if (spectrumAnnotatedPeakColor == null) {
            spectrumAnnotatedPeakColor = Color.RED;
        }

        return spectrumAnnotatedPeakColor;
    }

    /**
     * Set the color to use for the annotated peaks.
     *
     * @deprecated use the utilities version instead
     * @param spectrumAnnotatedPeakColor the spectrumAnnotatedPeakColor to set
     */
    public void setSpectrumAnnotatedPeakColor(Color spectrumAnnotatedPeakColor) {
        this.spectrumAnnotatedPeakColor = spectrumAnnotatedPeakColor;
    }

    /**
     * Returns the color to use for the background peaks.
     *
     * @deprecated use the utilities version instead
     * @return the spectrumBackgroundPeakColor
     */
    public Color getSpectrumBackgroundPeakColor() {

        if (spectrumBackgroundPeakColor == null) {
            spectrumBackgroundPeakColor = new Color(100, 100, 100, 50);
        }

        return spectrumBackgroundPeakColor;
    }

    /**
     * Set the color to use for the background peaks.
     *
     * @deprecated use the utilities version instead
     * @param spectrumBackgroundPeakColor the spectrumBackgroundPeakColor to set
     */
    public void setSpectrumBackgroundPeakColor(Color spectrumBackgroundPeakColor) {
        this.spectrumBackgroundPeakColor = spectrumBackgroundPeakColor;
    }

    /**
     * Returns the width of the annotated peaks.
     *
     * @deprecated use the utilities version instead
     * @return the spectrumAnnotatedPeakWidth
     */
    public Float getSpectrumAnnotatedPeakWidth() {

        if (spectrumAnnotatedPeakWidth == null) {
            spectrumAnnotatedPeakWidth = 1.0f;
        }

        return spectrumAnnotatedPeakWidth;
    }

    /**
     * Set the width of the annotated peaks.
     *
     * @deprecated use the utilities version instead
     * @param spectrumAnnotatedPeakWidth the spectrumAnnotatedPeakWidth to set
     */
    public void setSpectrumAnnotatedPeakWidth(float spectrumAnnotatedPeakWidth) {
        this.spectrumAnnotatedPeakWidth = spectrumAnnotatedPeakWidth;
    }

    /**
     * Returns the width of the background peaks.
     *
     * @deprecated use the utilities version instead
     * @return the spectrumBackgroundPeakWidth
     */
    public Float getSpectrumBackgroundPeakWidth() {

        if (spectrumBackgroundPeakWidth == null) {
            spectrumBackgroundPeakWidth = 1.0f;
        }

        return spectrumBackgroundPeakWidth;
    }

    /**
     * Set the width of the background peaks.
     *
     * @deprecated use the utilities version instead
     * @param spectrumBackgroundPeakWidth the spectrumBackgroundPeakWidth to set
     */
    public void setSpectrumBackgroundPeakWidth(float spectrumBackgroundPeakWidth) {
        this.spectrumBackgroundPeakWidth = spectrumBackgroundPeakWidth;
    }

    /**
     * Returns the path to the SearchGUI installation.
     *
     * @deprecated use the utilities version instead
     * @return the path to the SearchGUI installation
     */
    public String getSearchGuiPath() {
        return searchGuiPath;
    }

    /**
     * Set the path to the SearchGUI installation.
     *
     * @deprecated use the utilities version instead
     * @param searchGuiPath the path to the SearchGUI installation
     */
    public void setSearchGuiPath(String searchGuiPath) {
        this.searchGuiPath = searchGuiPath;
    }
}
