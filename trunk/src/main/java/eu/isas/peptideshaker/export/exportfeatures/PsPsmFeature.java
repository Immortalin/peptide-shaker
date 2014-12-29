package eu.isas.peptideshaker.export.exportfeatures;

import com.compomics.util.io.export.ExportFeature;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class lists the PSM identification features.
 *
 * @author Marc Vaudel
 */
public enum PsPsmFeature implements ExportFeature {

    localization_confidence("Localization Confidence", "The confidence in variable PTM localization.", false),
    probabilistic_score("probabilistic PTM score", "The probabilistic score (e.g. A-score or PhosphoRS) used for variable PTM localization.", false),
    d_score("D-score", "D-score for variable PTM localization.", false),
    algorithm_score("Algorithm Score", "Best score given by the identification algorithm to the hit retained by PeptideShaker independent of modification localization.", false),
    score("Score", "Score of the retained peptide as a combination of the algorithm scores (used to rank PSMs).", true),
    raw_score("Raw score", "Score before log transformation.", true),
    confidence("Confidence", "Confidence in percent associated to the retained PSM.", false),
    validated("Validation", "Indicates the validation level of the protein group.", false),
    starred("Starred", "Indicates whether the match was starred in the interface (1: yes, 0: no).", false),
    hidden("Hidden", "Indicates whether the match was hidden in the interface (1: yes, 0: no).", false);

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
    public final static String type = "Peptide Spectrum Matching Summary";
    /**
     * Indicates whether a feature is for advanced user only.
     */
    private final boolean advanced;

    /**
     * Constructor.
     *
     * @param title title of the feature
     * @param description description of the feature
     * @param advanced indicates whether a feature is for advanced user only
     */
    private PsPsmFeature(String title, String description, boolean advanced) {
        this.title = title;
        this.description = description;
        this.advanced = advanced;
    }

    @Override
    public ArrayList<ExportFeature> getExportFeatures(boolean includeSubFeatures) {
        ArrayList<ExportFeature> result = new ArrayList<ExportFeature>();
        result.addAll(PsIdentificationAlgorithmMatchesFeature.values()[0].getExportFeatures(includeSubFeatures));
        result.addAll(Arrays.asList(values()));
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

    @Override
    public boolean isAdvanced() {
        return advanced;
    }
}
