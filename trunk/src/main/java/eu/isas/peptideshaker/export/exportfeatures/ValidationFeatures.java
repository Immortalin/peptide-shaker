package eu.isas.peptideshaker.export.exportfeatures;

import eu.isas.peptideshaker.export.ExportFeature;
import java.util.ArrayList;

/**
 * This enum lists the export features linked to the validation process.
 *
 * @author Marc Vaudel
 */
public enum ValidationFeatures implements ExportFeature {

    validated_protein("Proteins Validated", "The number of validated proteins."),
    total_protein("Proteins Total", "The estimated total number of proteins."),
    protein_fdr("Proteins FDR Limit", "The estimated protein False Discovery Rate (FDR)."),
    protein_fnr("Proteins FNR Limit", "The estimated protein False Negative Rate (FNR)."),
    protein_confidence("Proteins Confidence Limit", "The lowest confidence among validated proteins."),
    protein_pep("Proteins PEP Limit", "The highest Posterior Error Probability (PEP) among validated proteins."),
    protein_accuracy("Proteins Confidence Accuracy", "The estimated protein Posterior Error Probability (PEP) and confidence estimation accuracy."),
    validated_peptide("Peptides Validated", "The number of validated peptides. Note that peptides are grouped by modification status when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    total_peptide("Peptides Total", "The estimated total number of peptides. Note that peptides are grouped by modification status when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    peptide_fdr("Peptides FDR Limit", "The estimated peptide False Discovery Rate (FDR). Note that peptides are grouped by modification status when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    peptide_fnr("Peptides FNR Limit", "The estimated peptide False Negative Rate (FNR). Note that peptides are grouped by modification status when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    peptide_confidence("Peptides Confidence Limit", "The lowest confidence among validated peptides. Note that peptides are grouped by modification status when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    peptide_pep("Peptides PEP Limit", "The highest Posterior Error Probability (PEP) among validated peptides. Note that peptides are grouped by modification status when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    peptide_accuracy("Peptides Confidence Accuracy", "The estimated peptide Posterior Error Probability (PEP) and confidence estimation accuracy. Note that peptides are grouped by modification status when statistical significance is ensured based on this parameter: \"Confidence accuracy\" < 1%."),
    validated_psm("PSM Validated", "The number of validated Peptide Spectrum Matches (PSMs). Note that PSMs are grouped by identified charge when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    total_psm("PSM Total", "The estimated total number of Peptide Spectrum Matches (PSMs). Note that PSMs are grouped by identified charge when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    psm_fdr("PSM FDR Limit", "The estimated Peptide Spectrum Match (PSM) False Discovery Rate (FDR). Note that PSMs are grouped by identified charge when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    psm_fnr("PSM FNR Limit", "The estimated Peptide Spectrum Match (PSM) False Negative Rate (FNR). Note that PSMs are grouped by identified charge when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    psm_confidence("PSM Confidence Limit", "The lowest confidence among validated Peptide Spectrum Matches (PSMs). Note that PSMs are grouped by identified charge when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    psm_pep("PSM PEP Limit", "The highest Posterior Error Probability (PEP) among validated Peptide Spectrum Matches (PSMs). Note that PSMs are grouped by identified charge when statistical significance is ensured, i.e. \"Confidence accuracy\" < 1%."),
    psm_accuracy("PSM Confidence Accuracy", "The estimated Peptide Spectrum Match (PSM) Posterior Error Probability (PEP) and confidence estimation accuracy. Note that PSMs are grouped by identified charge when statistical significance is ensured based on this parameter: \"Confidence accuracy\" < 1%.");
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
    public final static String type = "Target/Decoy Validation Summary";

    /**
     * Constructor.
     *
     * @param title title of the feature
     * @param description description of the feature
     */
    private ValidationFeatures(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public ArrayList<ExportFeature> getExportFeatures() {
        ArrayList<ExportFeature> result = new ArrayList<ExportFeature>();
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
