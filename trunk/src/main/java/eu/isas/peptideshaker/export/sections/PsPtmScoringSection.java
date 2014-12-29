package eu.isas.peptideshaker.export.sections;

import com.compomics.util.waiting.WaitingHandler;
import com.compomics.util.preferences.PTMScoringPreferences;
import com.compomics.util.io.export.ExportFeature;
import com.compomics.util.io.export.ExportWriter;
import eu.isas.peptideshaker.export.exportfeatures.PsPtmScoringFeature;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class outputs the project related export features.
 *
 * @author Marc Vaudel
 */
public class PsPtmScoringSection {

    /**
     * The features to export.
     */
    private ArrayList<PsPtmScoringFeature> ptmScoringFeatures;
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
    public PsPtmScoringSection(ArrayList<ExportFeature> exportFeatures, boolean indexes, boolean header, ExportWriter writer) {
        this.indexes = indexes;
        this.header = header;
        this.writer = writer;
        ptmScoringFeatures = new ArrayList<PsPtmScoringFeature>(exportFeatures.size());
        for (ExportFeature exportFeature : exportFeatures) {
            if (exportFeature instanceof PsPtmScoringFeature) {
                ptmScoringFeatures.add((PsPtmScoringFeature) exportFeature);
            } else {
                throw new IllegalArgumentException("Impossible to export " + exportFeature.getClass().getName() + " as PTM scoring feature.");
            }
        }
        Collections.sort(ptmScoringFeatures);
    }

    /**
     * Writes the desired section.
     *
     * @param ptmcoringPreferences the PTM scoring preferences of this project
     * @param waitingHandler the waiting handler
     * @throws IOException exception thrown whenever an error occurred while
     * writing the file
     */
    public void writeSection(PTMScoringPreferences ptmcoringPreferences, WaitingHandler waitingHandler) throws IOException {

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

        for (PsPtmScoringFeature ptmScoringFeature : ptmScoringFeatures) {
            if (indexes) {
                writer.write(line + "");
                writer.addSeparator();
            }
            writer.write(ptmScoringFeature.getTitle() + "");
            writer.addSeparator();
            switch (ptmScoringFeature) {
                case aScore:
                    if (ptmcoringPreferences.isProbabilitsticScoreCalculation()) {
                        writer.write("Yes");
                    } else {
                        writer.write("No");
                    }
                    break;
                case flr:
                    writer.write(ptmcoringPreferences.getFlrThreshold() + "");
                    break;
                case neutral_losses:
                    if (ptmcoringPreferences.isProbabilisticScoreNeutralLosses()) {
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
