package eu.isas.peptideshaker.export.sections;

import com.compomics.util.waiting.WaitingHandler;
import com.compomics.util.io.export.ExportFeature;
import com.compomics.util.io.export.ExportWriter;
import eu.isas.peptideshaker.export.exportfeatures.PsSpectrumCountingFeature;
import eu.isas.peptideshaker.preferences.SpectrumCountingPreferences;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class outputs the spectrum counting related export features.
 *
 * @author Marc Vaudel
 */
public class PsSpectrumCountingSection {

    /**
     * The features to export.
     */
    private ArrayList<PsSpectrumCountingFeature> spectrumCountingFeatures;
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
    private ExportWriter writer;

    /**
     * Constructor.
     *
     * @param exportFeatures the features to export in this section
     * @param indexes indicates whether the line index should be written
     * @param header indicates whether the table header should be written
     * @param writer the writer which will write to the file
     */
    public PsSpectrumCountingSection(ArrayList<ExportFeature> exportFeatures, boolean indexes, boolean header, ExportWriter writer) {
        this.indexes = indexes;
        this.header = header;
        this.writer = writer;
        spectrumCountingFeatures = new ArrayList<PsSpectrumCountingFeature>(exportFeatures.size());
        for (ExportFeature exportFeature : exportFeatures) {
            if (exportFeature instanceof PsSpectrumCountingFeature) {
                spectrumCountingFeatures.add((PsSpectrumCountingFeature) exportFeature);
            } else {
                throw new IllegalArgumentException("Impossible to export " + exportFeature.getClass().getName() + " as spectrum counting feature.");
            }
        }
        Collections.sort(spectrumCountingFeatures);
    }

    /**
     * Writes the desired section.
     *
     * @param spectrumCountingPreferences the spectrum counting preferences of
     * this project
     * @param waitingHandler the waiting handler
     * @throws IOException exception thrown whenever an error occurred while
     * writing the file.
     */
    public void writeSection(SpectrumCountingPreferences spectrumCountingPreferences, WaitingHandler waitingHandler) throws IOException {

        if (waitingHandler != null) {
            waitingHandler.setSecondaryProgressCounterIndeterminate(true);
        }

        if (header) {
            if (indexes) {
                writer.writeHeaderText("");
                writer.addSeparator();
            }
            writer.writeHeaderText("Parameter");
            writer.addSeparator();
            writer.writeHeaderText("Value");
            writer.newLine();
        }

        int line = 1;

        for (PsSpectrumCountingFeature spectrumCountingFeature : spectrumCountingFeatures) {
            if (indexes) {
                writer.write(line + "");
                writer.addSeparator();
            }
            writer.write(spectrumCountingFeature.getTitle());
            writer.addSeparator();
            switch (spectrumCountingFeature) {
                case method:
                    switch (spectrumCountingPreferences.getSelectedMethod()) {
                        case EMPAI:
                            writer.write("emPAI");
                            break;
                        case NSAF:
                            writer.write("NSAF");
                            break;
                        default:
                            writer.write("unknown");
                    }
                    break;
                case validated:
                    if (spectrumCountingPreferences.isValidatedHits()) {
                        writer.write("Yes");
                    } else {
                        writer.write("No");
                    }
                    break;
                default:
                    writer.write("Not implemented");
            }
            writer.newLine();
            line++;
        }
    }
}
