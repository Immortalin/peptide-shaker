package eu.isas.peptideshaker.cmd;

import org.apache.commons.cli.Options;

/**
 * Enum class specifying the Command Line Parameters for mzid export.
 *
 * @author Marc Vaudel
 */
public enum MzidCLIParams {

    CPS_FILE("in", "PeptideShaker project (.cps or zip file)", true, true),
    CONTACT_FIRST_NAME("contact_first_name", "Contact first name.", true, true),
    CONTACT_LAST_NAME("contact_last_name", "Contact last name.", true, true),
    CONTACT_EMAIL("contact_email", "Contact e-mail.", true, true),
    CONTACT_ADDRESS("contact_address", "Contact address.", true, true),
    CONTACT_URL("contact_url", "Contact URL.", true, false),
    ORGANIZATION_NAME("organization_name", "Organization name.", true, true),
    ORGANIZATION_EMAIL("organization_email", "Organization e-mail.", true, true),
    ORGANIZATION_ADDRESS("organization_address", "Organization address.", true, true),
    ORGANIZATION_URL("organization_url", "Organization URL.", true, false),
    OUTPUT_FILE("output_file", "Output file.", true, true);

    /**
     * Short Id for the CLI parameter.
     */
    public final String id;
    /**
     * Explanation for the CLI parameter.
     */
    public final String description;
    /**
     * Boolean indicating whether the parameter is mandatory.
     */
    public final boolean mandatory;
    /**
     * Indicates whether user input is expected.
     */
    public final boolean hasArgument;

    /**
     * Private constructor managing the various variables for the enum
     * instances.
     *
     * @param id the id
     * @param description the description
     * @param hasArgument is input expected
     * @param mandatory is the parameter mandatory
     */
    private MzidCLIParams(String id, String description, boolean hasArgument, boolean mandatory) {
        this.id = id;
        this.description = description;
        this.mandatory = mandatory;
        this.hasArgument = hasArgument;
    }

    /**
     * Creates the options for the command line interface based on the possible
     * values.
     *
     * @param aOptions the options object where the options will be added
     */
    public static void createOptionsCLI(Options aOptions) {

        for (MzidCLIParams mzidCLIParams : values()) {
            aOptions.addOption(mzidCLIParams.id, mzidCLIParams.hasArgument, mzidCLIParams.description);
        }

        // Path setup
        aOptions.addOption(PathSettingsCLIParams.ALL.id, true, PathSettingsCLIParams.ALL.description);
    }

    /**
     * Returns the options as a string.
     *
     * @return the options as a string
     */
    public static String getOptionsAsString() {

        String output = "";
        String formatter = "%-35s";

        output += "Mandatory parameters:\n\n";
        for (MzidCLIParams mzidCLIParams : values()) {
            if (mzidCLIParams.mandatory) {
                output += "-" + String.format(formatter, mzidCLIParams.id) + mzidCLIParams.description + "\n";
            }
        }

        output += "\n\nOptional annotation parameters:\n";
        for (MzidCLIParams mzidCLIParams : values()) {
            if (!mzidCLIParams.mandatory) {
                output += "-" + String.format(formatter, mzidCLIParams.id) + mzidCLIParams.description + "\n";
            }
        }

        output += "\n\nOptional temporary folder:\n";
        output += "-" + String.format(formatter, PathSettingsCLIParams.ALL.id) + PathSettingsCLIParams.ALL.description + "\n";

        return output;
    }
}
