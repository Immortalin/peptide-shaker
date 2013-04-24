package eu.isas.peptideshaker.export;

import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.SearchParameters;
import com.compomics.util.gui.waiting.WaitingHandler;
import com.compomics.util.gui.waiting.waitinghandlers.ProgressDialogX;
import com.compomics.util.io.SerializationUtils;
import com.compomics.util.preferences.AnnotationPreferences;
import com.compomics.util.preferences.IdFilter;
import com.compomics.util.preferences.ModificationProfile;
import com.compomics.util.preferences.PTMScoringPreferences;
import eu.isas.peptideshaker.export.exportfeatures.AnnotationFeatures;
import eu.isas.peptideshaker.export.exportfeatures.InputFilterFeatures;
import eu.isas.peptideshaker.export.exportfeatures.PeptideFeatures;
import eu.isas.peptideshaker.export.exportfeatures.ProjectFeatures;
import eu.isas.peptideshaker.export.exportfeatures.ProteinFeatures;
import eu.isas.peptideshaker.export.exportfeatures.PsmFeatures;
import eu.isas.peptideshaker.export.exportfeatures.PtmScoringFeatures;
import eu.isas.peptideshaker.export.exportfeatures.SearchFeatures;
import eu.isas.peptideshaker.export.exportfeatures.SpectrumCountingFeatures;
import eu.isas.peptideshaker.export.exportfeatures.ValidationFeatures;
import eu.isas.peptideshaker.export.sections.AnnotationSection;
import eu.isas.peptideshaker.export.sections.InputFilterSection;
import eu.isas.peptideshaker.export.sections.PeptideSection;
import eu.isas.peptideshaker.export.sections.ProjectSection;
import eu.isas.peptideshaker.export.sections.ProteinSection;
import eu.isas.peptideshaker.export.sections.PsmSection;
import eu.isas.peptideshaker.export.sections.PtmScoringSection;
import eu.isas.peptideshaker.export.sections.SearchParametersSection;
import eu.isas.peptideshaker.export.sections.SpectrumCountingSection;
import eu.isas.peptideshaker.export.sections.ValidationSection;
import eu.isas.peptideshaker.myparameters.PSMaps;
import eu.isas.peptideshaker.preferences.ProjectDetails;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences;
import eu.isas.peptideshaker.utils.IdentificationFeaturesGenerator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

/**
 * This factory is used to manage and generate reports.
 *
 * @author Marc Vaudel
 */
public class ExportFactory implements Serializable {

    /**
     * Serial number for backward compatibility.
     */
    static final long serialVersionUID = 1979509878742026942L;
    /**
     * The instance of the factory.
     */
    private static ExportFactory instance = null;
    /**
     * User defined factory containing the user schemes.
     */
    private static final String SERIALIZATION_FILE = System.getProperty("user.home") + "/.peptideshaker/exportFactory.cus";
    /**
     * The user export schemes.
     */
    private HashMap<String, ExportScheme> userSchemes = new HashMap<String, ExportScheme>();

    /**
     * Constructor.
     */
    private ExportFactory() {
    }

    /**
     * Static method to get the instance of the factory.
     *
     * @return the instance of the factory
     */
    public static ExportFactory getInstance() {
        if (instance == null) {
            try {
                File savedFile = new File(SERIALIZATION_FILE);
                instance = (ExportFactory) SerializationUtils.readObject(savedFile);
            } catch (Exception e) {
                instance = new ExportFactory();
                try {
                    instance.saveFactory();
                } catch (IOException ioe) {
                    // cancel save
                    ioe.printStackTrace();
                }
            }
        }
        return instance;
    }

    /**
     * Saves the factory in the user folder.
     *
     * @throws IOException exception thrown whenever an error occurred while
     * saving the ptmFactory
     */
    public void saveFactory() throws IOException {
        File factoryFile = new File(SERIALIZATION_FILE);
        if (!factoryFile.getParentFile().exists()) {
            factoryFile.getParentFile().mkdir();
        }
        SerializationUtils.writeObject(instance, factoryFile);
    }

    /**
     * Returns a list of the name of the available user schemes.
     *
     * @return a list of the implemented user schemes
     */
    public ArrayList<String> getUserSchemesNames() {
        return new ArrayList<String>(userSchemes.keySet());
    }

    /**
     * Returns the export scheme indexed by the given name.
     *
     * @param schemeName the name of the desired export scheme
     * @return the desired export scheme
     */
    public ExportScheme getExportScheme(String schemeName) {
        ExportScheme exportScheme = userSchemes.get(schemeName);
        if (exportScheme == null) {
            exportScheme = getDefaultExportSchemes().get(schemeName);
        }
        return exportScheme;
    }

    /**
     * Removes a user scheme.
     *
     * @param schemeName the name of the scheme to remove
     */
    public void removeExportScheme(String schemeName) {
        userSchemes.remove(schemeName);
    }

    /**
     * Adds an export scheme to the map of user schemes.
     *
     * @param exportScheme the new export scheme, will be accessible via its
     * name
     */
    public void addExportScheme(ExportScheme exportScheme) {
        userSchemes.put(exportScheme.getName(), exportScheme);
    }

    /**
     * Returns a list of the default export schemes.
     *
     * @return a list of the default export schemes
     */
    public ArrayList<String> getDefaultExportSchemesNames() {
        return new ArrayList<String>(getDefaultExportSchemes().keySet());
    }

    /**
     * Writes the desired export in text format. If an argument is not needed,
     * provide null (at your own risks).
     *
     * @TODO: implement other formats, put sometimes text instead of tables
     *
     * @param exportScheme the scheme of the export
     * @param destinationFile the destination file
     * @param experiment the experiment corresponding to this project (mandatory
     * for the Project section)
     * @param sample the sample of the project (mandatory for the Project
     * section)
     * @param replicateNumber the replicate number of the project (mandatory for
     * the Project section)
     * @param projectDetails the project details (mandatory for the Project
     * section)
     * @param identification the identification (mandatory for the Protein,
     * Peptide and PSM sections)
     * @param identificationFeaturesGenerator the identification features
     * generator (mandatory for the Protein, Peptide and PSM sections)
     * @param searchParameters the search parameters (mandatory for the Protein,
     * Peptide, PSM and search parameters sections)
     * @param proteinKeys the protein keys to export (mandatory for the Protein
     * section)
     * @param peptideKeys the peptide keys to export (mandatory for the Peptide
     * section)
     * @param psmKeys the keys of the PSMs to export (mandatory for the PSM
     * section)
     * @param proteinMatchKey the protein match key when exporting peptides from
     * a single protein match (optional for the Peptide sections)
     * @param nSurroundingAA the number of surrounding amino acids to export
     * (mandatory for the Peptide section)
     * @param annotationPreferences the annotation preferences (mandatory for
     * the Annotation section)
     * @param idFilter the identification filer (mandatory for the Input Filter
     * section)
     * @param ptmcoringPreferences the PTM scoring preferences (mandatory for
     * the PTM scoring section)
     * @param spectrumCountingPreferences the spectrum counting preferences
     * (mandatory for the spectrum counting section)
     * @param psMaps the PeptideShaker validation maps (mandatory for the
     * Validation section)
     * @param progressDialog the progress dialog
     * @param modificationProfile the current modification profile
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     * @throws MzMLUnmarshallerException
     */
    public static void writeExport(ExportScheme exportScheme, File destinationFile, String experiment, String sample, int replicateNumber,
            ProjectDetails projectDetails, Identification identification, IdentificationFeaturesGenerator identificationFeaturesGenerator,
            SearchParameters searchParameters, ArrayList<String> proteinKeys, ArrayList<String> peptideKeys, ArrayList<String> psmKeys,
            String proteinMatchKey, int nSurroundingAA, AnnotationPreferences annotationPreferences, IdFilter idFilter,
            PTMScoringPreferences ptmcoringPreferences, SpectrumCountingPreferences spectrumCountingPreferences, PSMaps psMaps,
            ModificationProfile modificationProfile, WaitingHandler waitingHandler) throws IOException, IllegalArgumentException, SQLException, ClassNotFoundException,
            InterruptedException, MzMLUnmarshallerException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));

        String mainTitle = exportScheme.getMainTitle();
        if (mainTitle != null) {
            writer.write(mainTitle);
            writeSeparationLines(writer, exportScheme.getSeparationLines());
        }

        for (String sectionName : exportScheme.getSections()) {
            if (exportScheme.isIncludeSectionTitles()) {
                writer.write(sectionName);
                writer.newLine();
            }
            if (sectionName.equals(AnnotationFeatures.type)) {
                AnnotationSection section = new AnnotationSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(annotationPreferences, waitingHandler);
            } else if (sectionName.equals(InputFilterFeatures.type)) {
                InputFilterSection section = new InputFilterSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(idFilter, waitingHandler);
            } else if (sectionName.equals(PeptideFeatures.type)) {
                PeptideSection section = new PeptideSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer, modificationProfile);
                section.writeSection(identification, identificationFeaturesGenerator, searchParameters, psmKeys, proteinMatchKey, nSurroundingAA, waitingHandler);
            } else if (sectionName.equals(ProjectFeatures.type)) {
                ProjectSection section = new ProjectSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(experiment, sample, replicateNumber, projectDetails, waitingHandler);
            } else if (sectionName.equals(ProteinFeatures.type)) {
                ProteinSection section = new ProteinSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(identification, identificationFeaturesGenerator, searchParameters, psmKeys, waitingHandler);
            } else if (sectionName.equals(PsmFeatures.type)) {
                PsmSection section = new PsmSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(identification, identificationFeaturesGenerator, searchParameters, psmKeys, waitingHandler);
            } else if (sectionName.equals(PtmScoringFeatures.type)) {
                PtmScoringSection section = new PtmScoringSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(ptmcoringPreferences, waitingHandler);
            } else if (sectionName.equals(SearchFeatures.type)) {
                SearchParametersSection section = new SearchParametersSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(searchParameters, waitingHandler);
            } else if (sectionName.equals(SpectrumCountingFeatures.type)) {
                SpectrumCountingSection section = new SpectrumCountingSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(spectrumCountingPreferences, waitingHandler);
            } else if (sectionName.equals(ValidationFeatures.type)) {
                ValidationSection section = new ValidationSection(exportScheme.getExportFeatures(sectionName), exportScheme.getSeparator(), exportScheme.isIndexes(), exportScheme.isHeader(), writer);
                section.writeSection(psMaps, waitingHandler);
            } else {
                writer.write("Section " + sectionName + " not implemented in the ExportFactory.");
            }

            writeSeparationLines(writer, exportScheme.getSeparationLines());
        }

        writer.close();
    }

    /**
     * Writes the documentation related to a report.
     *
     * @param exportScheme the export scheme of the report
     * @param destinationFile the destination file where to write the
     * documentation
     * @throws IOException
     */
    public static void writeDocumentation(ExportScheme exportScheme, File destinationFile) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));

        String mainTitle = exportScheme.getMainTitle();
        if (mainTitle != null) {
            writer.write(mainTitle);
            writeSeparationLines(writer, exportScheme.getSeparationLines());
        }
        for (String sectionName : exportScheme.getSections()) {
            if (exportScheme.isIncludeSectionTitles()) {
                writer.write(sectionName);
                writer.newLine();
            }
            for (ExportFeature exportFeature : exportScheme.getExportFeatures(sectionName)) {
                writer.write(exportFeature.getTitle() + exportScheme.getSeparator() + exportFeature.getDescription());
                writer.newLine();
            }
            writeSeparationLines(writer, exportScheme.getSeparationLines());
        }
        writer.close();
    }

    /**
     * Writes section separation lines using the given writer.
     *
     * @param writer the writer
     * @param nSeparationLines the number of separation lines to write
     * @throws IOException
     */
    private static void writeSeparationLines(BufferedWriter writer, int nSeparationLines) throws IOException {
        for (int i = 1; i <= nSeparationLines; i++) {
            writer.newLine();
        }
    }

    /**
     * Returns the implemented sections.
     *
     * @return the implemented sections
     */
    public static ArrayList<String> getImplementedSections() {
        ArrayList<String> result = new ArrayList<String>();
        result.add(AnnotationFeatures.type);
        result.add(InputFilterFeatures.type);
        result.add(PeptideFeatures.type);
        result.add(PsmFeatures.type);
        result.add(ProjectFeatures.type);
        result.add(ProteinFeatures.type);
        result.add(PtmScoringFeatures.type);
        result.add(SearchFeatures.type);
        result.add(SpectrumCountingFeatures.type);
        result.add(ValidationFeatures.type);
        return result;
    }

    /**
     * Returns the default schemes available.
     *
     * @return a list containing the default schemes
     */
    private static HashMap<String, ExportScheme> getDefaultExportSchemes() {

        ///////////////////////////
        // Default protein report
        ///////////////////////////
        ArrayList<ExportFeature> exportFeatures = new ArrayList<ExportFeature>();

        // protein accessions and protein inferences 
        exportFeatures.add(ProteinFeatures.accession);
        exportFeatures.add(ProteinFeatures.protein_description);
        exportFeatures.add(ProteinFeatures.pi);
        exportFeatures.add(ProteinFeatures.other_proteins);
        exportFeatures.add(ProteinFeatures.protein_group);

        // peptide and spectrum counts
        exportFeatures.add(ProteinFeatures.peptides);
        exportFeatures.add(ProteinFeatures.validated_peptides);
        exportFeatures.add(ProteinFeatures.unique_peptides);
        exportFeatures.add(ProteinFeatures.psms);
        exportFeatures.add(ProteinFeatures.validated_psms);

        // protein coverage
        exportFeatures.add(ProteinFeatures.coverage);
        exportFeatures.add(ProteinFeatures.possible_coverage);

        // molecular weight and spectrum counting
        exportFeatures.add(ProteinFeatures.mw);
        exportFeatures.add(ProteinFeatures.spectrum_counting_nsaf);
        exportFeatures.add(ProteinFeatures.spectrum_counting_empai);

        // variable_ptms
        exportFeatures.add(ProteinFeatures.confident_PTMs);
        exportFeatures.add(ProteinFeatures.other_PTMs);

        // protein scores
        exportFeatures.add(ProteinFeatures.score);
        exportFeatures.add(ProteinFeatures.confidence);
        exportFeatures.add(ProteinFeatures.decoy);
        exportFeatures.add(ProteinFeatures.validated);

        ExportScheme proteinReport = new ExportScheme("Default Protein Report", false, exportFeatures, "\t", true, true, 0, false);


        ///////////////////////////
        // Default peptide report
        ///////////////////////////
        exportFeatures = new ArrayList<ExportFeature>();

        // accessions
        exportFeatures.add(PeptideFeatures.accessions);

        // peptide sequence
        exportFeatures.add(PeptideFeatures.aaBefore);
        exportFeatures.add(PeptideFeatures.sequence);
        exportFeatures.add(PeptideFeatures.aaAfter);

        // variable_ptms
        exportFeatures.add(PeptideFeatures.variable_ptms);
        exportFeatures.add(PeptideFeatures.localization_confidence);
        exportFeatures.add(PeptideFeatures.fixed_ptms);

        // psms
        exportFeatures.add(PeptideFeatures.validated_psms);
        exportFeatures.add(PeptideFeatures.psms);

        // peptide scores
        exportFeatures.add(PeptideFeatures.score);
        exportFeatures.add(PeptideFeatures.confidence);
        exportFeatures.add(PeptideFeatures.decoy);
        exportFeatures.add(PeptideFeatures.validated);

        ExportScheme peptideReport = new ExportScheme("Default Peptide Report", false, exportFeatures, "\t", true, true, 0, false);


        ///////////////////////////
        // Default PSM report
        ///////////////////////////
        exportFeatures = new ArrayList<ExportFeature>();

        // protein accessions
        exportFeatures.add(PsmFeatures.accessions);
        exportFeatures.add(PsmFeatures.sequence);

        // ptms
        exportFeatures.add(PsmFeatures.variable_ptms);
        exportFeatures.add(PsmFeatures.d_score);
        exportFeatures.add(PsmFeatures.a_score);
        exportFeatures.add(PsmFeatures.localization_confidence);
        exportFeatures.add(PsmFeatures.fixed_ptms);

        // spectrum file
        exportFeatures.add(PsmFeatures.spectrum_file);
        exportFeatures.add(PsmFeatures.spectrum_title);
        exportFeatures.add(PsmFeatures.spectrum_scan_number);

        // spectrum details
        exportFeatures.add(PsmFeatures.rt);
        exportFeatures.add(PsmFeatures.mz);
        exportFeatures.add(PsmFeatures.spectrum_charge);
        exportFeatures.add(PsmFeatures.identification_charge);
        exportFeatures.add(PsmFeatures.theoretical_mass);
        exportFeatures.add(PsmFeatures.isotope);
        exportFeatures.add(PsmFeatures.mz_error);

        // psm scores
        exportFeatures.add(PsmFeatures.score);
        exportFeatures.add(PsmFeatures.confidence);
        exportFeatures.add(PsmFeatures.decoy);
        exportFeatures.add(PsmFeatures.validated);

        ExportScheme psmReport = new ExportScheme("Default PSM Report", false, exportFeatures, "\t", true, true, 0, false);


        ///////////////////////////
        // Certificate of analysis
        ///////////////////////////
        ArrayList<String> sectionsList = new ArrayList<String>();
        exportFeatures = new ArrayList<ExportFeature>();
        
        // project details
        sectionsList.add(ProjectFeatures.type);
        exportFeatures.add(ProjectFeatures.peptide_shaker);
        exportFeatures.add(ProjectFeatures.date);
        exportFeatures.add(ProjectFeatures.experiment);
        exportFeatures.add(ProjectFeatures.sample);
        exportFeatures.add(ProjectFeatures.replicate);
        
        // search parameters
        sectionsList.add(SearchFeatures.type);
        exportFeatures.addAll(Arrays.asList(SearchFeatures.values()));
        
        // input filters
        sectionsList.add(InputFilterFeatures.type);
        exportFeatures.addAll(Arrays.asList(InputFilterFeatures.values()));
        
        // validation details
        sectionsList.add(ValidationFeatures.type);
        exportFeatures.addAll(Arrays.asList(ValidationFeatures.values()));
        
        // ptms
        sectionsList.add(PtmScoringFeatures.type);
        exportFeatures.addAll(Arrays.asList(PtmScoringFeatures.values()));
        
        // spectrum counting details
        sectionsList.add(SpectrumCountingFeatures.type);
        exportFeatures.addAll(Arrays.asList(SpectrumCountingFeatures.values()));
        
        // annotation settings
        sectionsList.add(AnnotationFeatures.type);
        exportFeatures.addAll(Arrays.asList(AnnotationFeatures.values()));
        ExportScheme coa = new ExportScheme("Certificate of Analysis", false, sectionsList, exportFeatures, ": ", true, false, 2, true, "Certificate of Analysis");

        HashMap<String, ExportScheme> defaultSchemes = new HashMap<String, ExportScheme>();
        defaultSchemes.put(proteinReport.getName(), proteinReport);
        defaultSchemes.put(peptideReport.getName(), peptideReport);
        defaultSchemes.put(psmReport.getName(), psmReport);
        defaultSchemes.put(coa.getName(), coa);
        return defaultSchemes;
    }
}
