package eu.isas.peptideshaker.export.sections;

import com.compomics.util.Util;
import com.compomics.util.experiment.annotation.gene.GeneFactory;
import com.compomics.util.experiment.annotation.go.GOFactory;
import com.compomics.util.experiment.identification.Identification;
import com.compomics.util.experiment.identification.SearchParameters;
import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.waiting.WaitingHandler;
import com.compomics.util.preferences.AnnotationPreferences;
import eu.isas.peptideshaker.PeptideShaker;
import com.compomics.util.io.export.ExportFeature;
import static eu.isas.peptideshaker.export.OutputGenerator.SEPARATOR;
import eu.isas.peptideshaker.export.exportfeatures.FragmentFeatures;
import eu.isas.peptideshaker.export.exportfeatures.PeptideFeatures;
import eu.isas.peptideshaker.export.exportfeatures.ProteinFeatures;
import eu.isas.peptideshaker.export.exportfeatures.PsmFeatures;
import eu.isas.peptideshaker.myparameters.PSParameter;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences;
import eu.isas.peptideshaker.scoring.MatchValidationLevel;
import eu.isas.peptideshaker.utils.IdentificationFeaturesGenerator;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import uk.ac.ebi.jmzml.xml.io.MzMLUnmarshallerException;

/**
 * This class outputs the protein related export features.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class ProteinSection {

    /**
     * The protein features to export.
     */
    private ArrayList<ExportFeature> proteinFeatures = new ArrayList<ExportFeature>();
    /**
     * The peptide subsection if any.
     */
    private PeptideSection peptideSection = null;
    /**
     * The separator used to separate columns.
     */
    private String separator;
    /**
     * Boolean indicating whether the line shall be indexed.
     */
    private boolean indexes;
    /**
     * Boolean indicating whether column headers shall be included.
     */
    private boolean header;
    /**
     * The writer used to send the output to file.
     */
    private BufferedWriter writer;

    /**
     * Constructor.
     *
     * @param exportFeatures the features to export in this section.
     * ProteinFeatures as main features. If Peptide or protein features are
     * selected, they will be added as sub-sections.
     * @param separator
     * @param indexes
     * @param header
     * @param writer
     */
    public ProteinSection(ArrayList<ExportFeature> exportFeatures, String separator, boolean indexes, boolean header, BufferedWriter writer) {
        ArrayList<ExportFeature> peptideFeatures = new ArrayList<ExportFeature>();
        for (ExportFeature exportFeature : exportFeatures) {
            if (exportFeature instanceof ProteinFeatures) {
                proteinFeatures.add(exportFeature);
            } else if (exportFeature instanceof PeptideFeatures || exportFeature instanceof PsmFeatures || exportFeature instanceof FragmentFeatures) {
                peptideFeatures.add(exportFeature);
            } else {
                throw new IllegalArgumentException("Export feature of type " + exportFeature.getClass() + " not recognized.");
            }
        }
        if (!peptideFeatures.isEmpty()) {
            peptideSection = new PeptideSection(peptideFeatures, separator, indexes, header, writer);
        }
        this.separator = separator;
        this.indexes = indexes;
        this.header = header;
        this.writer = writer;
    }

    /**
     * Writes the desired section.
     *
     * @param identification the identification of the project
     * @param identificationFeaturesGenerator the identification features
     * generator of the project
     * @param searchParameters the search parameters of the project
     * @param annotationPreferences the annotation preferences
     * @param keys the keys of the protein matches to output. if null all
     * proteins will be exported.
     * @param nSurroundingAas in case a peptide export is included with
     * surrounding amino-acids, the number of surrounding amino acids to use
     * @param waitingHandler the waiting handler
     * @throws IOException exception thrown whenever an error occurred while
     * writing the file.
     * @throws IllegalArgumentException
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     * @throws MzMLUnmarshallerException
     */
    public void writeSection(Identification identification, IdentificationFeaturesGenerator identificationFeaturesGenerator,
            SearchParameters searchParameters, AnnotationPreferences annotationPreferences, ArrayList<String> keys, int nSurroundingAas, WaitingHandler waitingHandler)
            throws IOException, IllegalArgumentException, SQLException, ClassNotFoundException, InterruptedException, MzMLUnmarshallerException {

        if (waitingHandler != null) {
            waitingHandler.setSecondaryProgressCounterIndeterminate(true);
        }

        if (header) {
            writeHeader();
        }

        if (keys == null) {
            keys = identification.getProteinIdentification();
        }
        int line = 1;
        PSParameter psParameter = new PSParameter();
        ProteinMatch proteinMatch = null;

        if (peptideSection != null) {
            if (waitingHandler != null) {
                waitingHandler.setWaitingText("Loading Peptides. Please Wait...");
                waitingHandler.resetSecondaryProgressCounter();
            }
            identification.loadPeptideMatches(waitingHandler);
            if (waitingHandler != null) {
                waitingHandler.setWaitingText("Loading Peptide Details. Please Wait...");
                waitingHandler.resetSecondaryProgressCounter();
            }
            identification.loadPeptideMatchParameters(psParameter, waitingHandler);
        }

        if (waitingHandler != null) {
            waitingHandler.setWaitingText("Loading Proteins. Please Wait...");
            waitingHandler.resetSecondaryProgressCounter();
        }
        identification.loadProteinMatches(keys, waitingHandler);
        if (waitingHandler != null) {
            waitingHandler.setWaitingText("Loading Protein Details. Please Wait...");
            waitingHandler.resetSecondaryProgressCounter();
        }
        identification.loadProteinMatchParameters(keys, psParameter, waitingHandler);

        if (waitingHandler != null) {
            waitingHandler.setWaitingText("Exporting. Please Wait...");
            waitingHandler.resetSecondaryProgressCounter();
            waitingHandler.setMaxSecondaryProgressCounter(keys.size());
        }

        for (String proteinKey : keys) {

            if (waitingHandler != null) {
                if (waitingHandler.isRunCanceled()) {
                    return;
                }
                waitingHandler.increaseSecondaryProgressCounter();
            }

            if (indexes) {
                writer.write(line + separator);
            }

            proteinMatch = identification.getProteinMatch(proteinKey);
            psParameter = (PSParameter) identification.getProteinMatchParameter(proteinKey, psParameter);

            for (ExportFeature exportFeature : proteinFeatures) {
                ProteinFeatures tempProteinFeatures = (ProteinFeatures) exportFeature;
                writer.write(getFeature(identificationFeaturesGenerator, searchParameters, annotationPreferences, keys, separator, nSurroundingAas, proteinKey, proteinMatch, psParameter, tempProteinFeatures, waitingHandler) + separator);
            }
            writer.newLine();
            if (peptideSection != null) {
                peptideSection.writeSection(identification, identificationFeaturesGenerator, searchParameters, annotationPreferences, proteinMatch.getPeptideMatches(), nSurroundingAas, line + ".", null);
            }
            line++;
        }
    }

    /**
     * Returns the part of the desired section.
     *
     * @param identificationFeaturesGenerator the identification features
     * generator of the project
     * @param searchParameters the search parameters of the project
     * @param annotationPreferences the annotation preferences
     * @param keys the keys of the protein matches to output. if null all
     * proteins will be exported.
     * @param separator the column separator
     * @param nSurroundingAas in case a peptide export is included with
     * surrounding amino-acids, the number of surrounding amino acids to use
     * @param proteinKey the key of the protein match being written
     * @param proteinMatch the protein match, can be null if not needed
     * @param psParameter the protein match parameter containing the
     * PeptideShaker parameters, can be null if not needed
     * @param tempProteinFeatures the protein feature to write
     * @param waitingHandler the waiting handler
     *
     * @return the string to write
     *
     * @throws IOException exception thrown whenever an error occurred while
     * writing the file.
     * @throws IllegalArgumentException
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     * @throws MzMLUnmarshallerException
     */
    public static String getFeature(IdentificationFeaturesGenerator identificationFeaturesGenerator,
            SearchParameters searchParameters, AnnotationPreferences annotationPreferences, ArrayList<String> keys, String separator, int nSurroundingAas, String proteinKey, ProteinMatch proteinMatch, PSParameter psParameter, ProteinFeatures tempProteinFeatures, WaitingHandler waitingHandler)
            throws IOException, IllegalArgumentException, SQLException, ClassNotFoundException, InterruptedException, MzMLUnmarshallerException {

        switch (tempProteinFeatures) {
            case accession:
                return proteinMatch.getMainMatch();
            case protein_description:
                return SequenceFactory.getInstance().getHeader(proteinMatch.getMainMatch()).getSimpleProteinDescription();
            case ensembl_gene_id:
                if (!proteinMatch.isDecoy()) {
                    GeneFactory geneFactory = GeneFactory.getInstance();
                    String geneName = geneFactory.getGeneNameForUniProtProtein(proteinMatch.getMainMatch());
                    if (geneName != null) {
                        String ensemblId = geneFactory.getGeneEnsemblId(geneName);
                        if (ensemblId != null) {
                            return ensemblId;
                        }
                    }
                }
                return "";
            case gene_name:
                if (!proteinMatch.isDecoy()) {
                    GeneFactory geneFactory = GeneFactory.getInstance();
                    String geneName = geneFactory.getGeneNameForUniProtProtein(proteinMatch.getMainMatch());
                    if (geneName != null) {
                        return geneName;
                    }
                }
                return "";
            case chromosome:
                if (!proteinMatch.isDecoy()) {
                    GeneFactory geneFactory = GeneFactory.getInstance();
                    String geneName = geneFactory.getGeneNameForUniProtProtein(proteinMatch.getMainMatch());
                    if (geneName != null) {
                        String chromosome = geneFactory.getChromosomeForGeneName(geneName);
                        if (chromosome != null) {
                            return chromosome;
                        }
                    }
                }
                return "";
            case go_accession:
                StringBuilder result = new StringBuilder();
                if (!proteinMatch.isDecoy()) {
                    ArrayList<String> goTermaccessions = GOFactory.getInstance().getProteinGoAccessions(proteinKey);
                    if (goTermaccessions != null) {
                        for (String accession : goTermaccessions) {
                            if (result.length() > 0) {
                                result.append(", ");
                            }
                            result.append(accession);
                        }
                    }
                }
                return result.toString();
            case go_description:
                result = new StringBuilder();
                if (!proteinMatch.isDecoy()) {
                    ArrayList<String> goTermDescriptions = GOFactory.getInstance().getProteinGoDescriptions(proteinKey);
                    if (goTermDescriptions != null) {
                        boolean first = true;
                        for (String description : goTermDescriptions) {
                            if (result.length() > 0) {
                                result.append(", ");
                            }
                            result.append(description);
                        }
                    }
                }
                return result.toString();
            case other_proteins:
                String mainAccession = proteinMatch.getMainMatch();
                result = new StringBuilder();
                List<String> otherAccessions = Arrays.asList(ProteinMatch.getAccessions(proteinKey));
                Collections.sort(otherAccessions);
                for (String accession : otherAccessions) {
                    if (!accession.equals(mainAccession)) {
                        if (result.length() > 0) {
                            result.append(", ");
                        }
                        result.append(accession);
                    }
                }
                return result.toString();
            case protein_group:
                StringBuilder completeProteinGroup = new StringBuilder();
                List<String> allAccessions = Arrays.asList(ProteinMatch.getAccessions(proteinKey));
                Collections.sort(allAccessions);
                for (String accession : allAccessions) {
                    if (completeProteinGroup.length() > 0) {
                        completeProteinGroup.append(", ");
                    }
                    completeProteinGroup.append(accession);
                }

                return completeProteinGroup.toString();
            case confidence:
                return psParameter.getProteinConfidence() + "";
            case confident_PTMs:
                return identificationFeaturesGenerator.getPrimaryPTMSummary(proteinKey, separator);
            case other_PTMs:
                return identificationFeaturesGenerator.getSecondaryPTMSummary(proteinKey, separator);
            case confident_phosphosites:
                ArrayList<String> modifications = new ArrayList<String>();
                for (String ptm : searchParameters.getModificationProfile().getAllNotFixedModifications()) {
                    if (ptm.contains("phospho")) {
                        modifications.add(ptm);
                    }
                }
                return identificationFeaturesGenerator.getPrimaryPTMSummary(proteinKey, modifications, separator);
            case other_phosphosites:
                modifications = new ArrayList<String>();
                for (String ptm : searchParameters.getModificationProfile().getAllNotFixedModifications()) {
                    if (ptm.contains("phospho")) {
                        modifications.add(ptm);
                    }
                }
                return identificationFeaturesGenerator.getPrimaryPTMSummary(proteinKey, modifications, separator);
            case coverage:
                HashMap<Integer, Double> sequenceCoverage = identificationFeaturesGenerator.getSequenceCoverage(proteinKey, PeptideShaker.MATCHING_TYPE, searchParameters.getFragmentIonAccuracy());
                Double sequenceCoverageConfident = 100 * sequenceCoverage.get(MatchValidationLevel.confident.getIndex());
                Double sequenceCoverageDoubtful = 100 * sequenceCoverage.get(MatchValidationLevel.doubtful.getIndex());
                Double validatedCoverage = sequenceCoverageConfident + sequenceCoverageDoubtful;
                Double value = 100 * validatedCoverage;
                return Util.roundDouble(value, 2) + "";
            case possible_coverage:
                value = 100 * identificationFeaturesGenerator.getObservableCoverage(proteinKey);
                return Util.roundDouble(value, 2) + "";
            case decoy:
                if (ProteinMatch.isDecoy(proteinKey)) {
                    return 1 + "";
                } else {
                    return 0 + "";
                }
            case hidden:
                if (psParameter.isHidden()) {
                    return 1 + "";
                } else {
                    return 0 + "";
                }
            case mw:
                Double proteinMW = SequenceFactory.getInstance().computeMolecularWeight(proteinMatch.getMainMatch());
                return proteinMW.toString();
            case non_enzymatic:
                ArrayList<String> nonEnzymatic = identificationFeaturesGenerator.getNonEnzymatic(proteinKey, searchParameters.getEnzyme());
                return nonEnzymatic.size() + "";
            case pi:
                return psParameter.getProteinInferenceClassAsString();
            case peptides:
                return proteinMatch.getPeptideCount() + "";
            case psms:
                int nHits = identificationFeaturesGenerator.getNSpectra(proteinKey);
                return nHits + "";
            case validated_peptides:
                nHits = identificationFeaturesGenerator.getNValidatedPeptides(proteinKey);
                return nHits + "";
            case unique_peptides:
                nHits = identificationFeaturesGenerator.getNUniquePeptides(proteinKey);
                return nHits + "";
            case validated_psms:
                nHits = identificationFeaturesGenerator.getNValidatedSpectra(proteinKey);
                return nHits + "";
            case score:
                return psParameter.getProteinProbabilityScore() + "";
            case spectrum_counting_nsaf:
                try {
                    return identificationFeaturesGenerator.getSpectrumCounting(proteinKey,
                            SpectrumCountingPreferences.SpectralCountingMethod.NSAF) + "";
                } catch (Exception e) {
                    return "error: " + e.getLocalizedMessage();
                }
            case spectrum_counting_empai:
                try {
                    return identificationFeaturesGenerator.getSpectrumCounting(proteinKey,
                            SpectrumCountingPreferences.SpectralCountingMethod.EMPAI) + "";
                } catch (Exception e) {
                    return "error: " + e.getLocalizedMessage();
                }
            case starred:
                if (psParameter.isStarred()) {
                    return 1 + "";
                } else {
                    return 0 + "";
                }
            case validated:
                result = new StringBuilder();
                result.append(psParameter.getMatchValidationLevel().toString());
                return result.toString();
            default:
                return "Not implemented";
        }
    }

    /**
     * Writes the header of the protein section.
     *
     * @throws IOException
     */
    public void writeHeader() throws IOException {
        if (indexes) {
            writer.write(separator);
        }
        boolean firstColumn = true;
        for (ExportFeature exportFeature : proteinFeatures) {
            for (String title : exportFeature.getTitles()) {
                if (firstColumn) {
                    firstColumn = false;
                } else {
                    writer.write(separator);
                }
                writer.write(title);
            }
        }
        writer.newLine();
    }
}
