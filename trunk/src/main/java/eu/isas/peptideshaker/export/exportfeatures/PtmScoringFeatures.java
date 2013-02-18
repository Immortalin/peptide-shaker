/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.isas.peptideshaker.export.exportfeatures;

import eu.isas.peptideshaker.export.ExportFeature;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This enum groups the export features related to PTM scoring
 *
 * @author Marc
 */
public enum PtmScoringFeatures implements ExportFeature {
    
    aScore("A-score", "Indicates whether the A-score was computed for PTM localization."),
    neutral_losses("Accounting for Neutral Losses", "Indicates whether the neutral losses are accounted for in the A-score calculation."),
    flr("FLR", "For peptides presenting a single modification of a kind and more than one modification site, the site is marked as confident if the A-score passes this estimated FLR.");
    
    /**
     * The title of the feature which will be used for column heading
     */
    public String title;
    /**
     * The description of the feature
     */
    public String description;
    /**
     * The type of export feature
     */
    public final static String type = "Postranslational Modification Scoring Settings";

    /**
     * Constructor
     *
     * @param title title of the feature
     * @param description description of the feature
     */
    private PtmScoringFeatures(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public ArrayList<ExportFeature> getExportFeatures() {
        ArrayList<ExportFeature> result = new ArrayList<ExportFeature>();
        result.add(aScore);
        result.add(neutral_losses);
        result.add(flr);
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
