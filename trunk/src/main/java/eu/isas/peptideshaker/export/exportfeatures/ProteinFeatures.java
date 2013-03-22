package eu.isas.peptideshaker.export.exportfeatures;

import eu.isas.peptideshaker.export.ExportFeature;
import java.util.ArrayList;

/**
 * This class lists the protein identification features.
 *
 * @author Marc Vaudel
 */
public enum ProteinFeatures implements ExportFeature {

    accession("Main Accession", "Main accession of the protein group."),
    protein_description("Description", "Description of the protein designed by the main accession."),
    mw("MW (kDa)", "Molecular Weight."),
    coverage("Coverage (%)", "Sequence coverage in percent of the protein designed by the main accession."),
    possible_coverage("Possible Coverage (%)", "Possible sequence coverage in percent of the protein designed by the main accession according to the search settings."),
    non_enzymatic("Non-Enzymatic", "Indicates how many non-enzymatic peptides were found for this protein match."),
    spectrum_counting("Spectrum Counting", "Spectrum counting index"),
    confident_PTMs("Confident Modification Sites", "List of the sites where a variable modification was confidently localized."),
    other_PTMs("Other Modification Sites", "List of the non-confident sites where a variable modification was localized."),
    confident_phosphosites("Confident Phosphosites", "List of the sites where a phosphorylation was confidently localized."),
    other_phosphosites("Other Phosphosites", "List of the non-confident sites where a phosphorylation was localized."),
    pi("PI", "Protein Inference status of the protein group."),
    other_proteins("Secondary Accessions", "Other accessions in the protein group (alphabetical order)."),
    protein_group("Protein Group", "The complete protein group (alphabetical order)."),
    validated_peptides("#Validated Peptides", "Number of validated peptides."),
    peptides("#Peptides", "Total number of peptides."),
    unique_peptides("#Unique", "Total number of peptides unique to this protein group."),
    validated_psms("#Validated PSMs", "Number of validated PSMs"),
    psms("#PSMs", "Number of PSMs"),
    score("Score", "Score of the protein group."),
    confidence("Confidence", "Confidence in percent associated to the protein group."),
    decoy("Decoy", "Indicates whether the protein group is a decoy (1: yes, 0: no)."),
    validated("Validated", "Indicates whether the protein group passed the validation process (1: yes, 0: no)."),
    starred("Starred", "Indicates whether the match was starred in the interface (1: yes, 0: no)."),
    hidden("Hidden", "Indicates whether the match was hidden in the interface (1: yes, 0: no).");
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
    public final static String type = "Protein Identification Summary";

    /**
     * Constructor.
     *
     * @param title title of the feature
     * @param description description of the feature
     */
    private ProteinFeatures(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public ArrayList<ExportFeature> getExportFeatures() {
        ArrayList<ExportFeature> result = new ArrayList<ExportFeature>();
        result.add(accession);
        result.add(protein_description);
        result.add(mw);
        result.add(coverage);
        result.add(possible_coverage);
        result.add(spectrum_counting);
        result.add(confident_PTMs);
        result.add(other_PTMs);
        result.add(confident_phosphosites);
        result.add(other_phosphosites);
        result.add(pi);
        result.add(other_proteins);
        result.add(protein_group);
        result.add(validated_peptides);
        result.add(peptides);
        result.add(validated_psms);
        result.add(psms);
        result.add(score);
        result.add(confidence);
        result.add(decoy);
        result.add(validated);
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
