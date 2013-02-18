package eu.isas.peptideshaker.export.exportfeatures;

import eu.isas.peptideshaker.export.ExportFeature;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class lists the PSM identification features.
 *
 * @author Marc Vaudel
 */
public enum ProjectFeatures implements ExportFeature, Serializable {

    peptide_shaker("PeptideShaker Version", "Software version used to create the project."),
    date("Date", "Date of project creation."),
    experiment("Experiment", "Experiment name."),
    sample("Sample", "Sample name."),
    replicate("Replicate number", "Replicate number.");
    /**
     * The title of the feature which will be used for column heading.
     */
    public String title;
    /**
     * The description of the feature.
     */
    public String description;
    /**
     * The type of export feature.
     */
    public final static String type = "Project Details";

    /**
     * Constructor.
     *
     * @param title title of the feature
     * @param description description of the feature
     */
    private ProjectFeatures(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public ArrayList<ExportFeature> getExportFeatures() {
        ArrayList<ExportFeature> result = new ArrayList<ExportFeature>();
        result.add(peptide_shaker);
        result.add(date);
        result.add(experiment);
        result.add(sample);
        result.add(replicate);
        return result;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getFeatureFamily() {
        return type;
    }
}
