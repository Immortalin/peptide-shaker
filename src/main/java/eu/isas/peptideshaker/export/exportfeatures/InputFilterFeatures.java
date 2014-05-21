package eu.isas.peptideshaker.export.exportfeatures;

import com.compomics.util.io.export.ExportFeature;
import static eu.isas.peptideshaker.export.exportfeatures.ValidationFeatures.values;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This enum lists the export features related to the import features.
 *
 * @author Marc
 */
public enum InputFilterFeatures implements ExportFeature {

    min_peptide_length("Minimal Peptide Length", "The minimal peptide length."),
    max_peptide_length("Maximal Peptide Length", "The maximal peptide length."),
    max_mz_deviation("Precursor m/z Tolerance", "The maximal precursor m/z error tolerance allowed."),
    max_mz_deviation_unit("Precursor m/z Tolerance Unit", "The unit of the maximal precursor m/z error tolerance allowed."),
    unknown_PTM("Unrecognized Modifications Discarded", "Indicates whether the Peptide Spectrum Matches (PSMs) presenting PTMs which do not match the search parameters were discarded.");
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
    public final static String type = "Input Filters";

    /**
     * Constructor.
     *
     * @param title title of the feature
     * @param description description of the feature
     */
    private InputFilterFeatures(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public ArrayList<ExportFeature> getExportFeatures() {
        ArrayList<ExportFeature> result = new ArrayList<ExportFeature>();
        result.addAll(Arrays.asList(values()));
        return result;
    }

    @Override
    public String[] getTitles() {
        return new String[]{title};
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
