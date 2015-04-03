# `PeptideShaker Command Line Interface` #

The PeptideShaker command line interface can be used to process identification files and output identification results in various formats.

There are six main sections to this page:

  * [A) PeptideShakerCLI](#A)_PeptideShakerCLI.md) - data processing
  * [B) ReportCLI](#B)_ReportCLI.md) - identification results exports
  * [C) FollowUpCLI](#C)_FollowUpCLI.md) - export for follow up analysis
  * [C) MzidCLI](#D)_MzidCLI.md) - export as mzIdentML
  * [D) PathSettingsCLI](#E)_PathSettingsCLI.md) - set the paths to use
  * [E) General](#F)_General.md) - general command line help

Note that ReportCLI, FollowUpCLI, MzidCLI and PathSettingsCLI options can also be appended to PeptideShakerCLI command lines.

All command line options have the same overall structure and only differ in the features and parameters available.

The recommended way to generate an identification parameters file for use in PeptideShakerCLI, is via the SearchGUI graphical user interface. But the file can also be created using the [IdentificationParametersCLI](http://code.google.com/p/compomics-utilities/wiki/IdentificationParametersCLI).


---


## A) PeptideShakerCLI ##

**Standard command line**

```
java -cp PeptideShaker-X.Y.Z.jar eu.isas.peptideshaker.cmd.PeptideShakerCLI [parameters]
```

**Mandatory parameters**

```
-experiment                Specifies the experiment name.

-sample                    Specifies the sample name.

-replicate                 The replicate number.

-identification_files      Identification files (X!Tandem .t.xml, mzIdentML .mzid, 
                           MS Amanda .cvs, OMSSA .omx, Mascot .dat files, or .zip) 
                           in a comma separated list or an entire folder.
                           Example: "file1.omx, file1.dat, file1.t.xml".

-out                       PeptideShaker output file. If the file already exists 
                           it will be silently overwritten.

-spectrum_files (*)        The spectrum files (mgf format) in a comma
                           separated list or an entire folder.
                           Example: "file1.mgf, file2.mgf".

-id_params (*)             The identification parameters file. 
                           Generated using SeachGUI or via IdentificationParametersCLI. 
                           This file is automatically saved by SearchGUI along with the 
                           identification files.

(*) Not mandatory if these files are part of a zip file input with the identification files. (from v0.36.0)
```

**Optional gene annotation parameter**

```
-species                   The species to use for the gene annotation, e.g., 'Homo sapiens'. 
                           Supported species are listed in the GUI.

-species_type              The species type to use for the gene annotation, e.g., 'Vertebrates' or 'Plants'. 
                           Supported species types are listed in the GUI.

-species_update            Check for new species information in Ensembl and update if possible. 
                           (1: true, 0: false, default is '0').
```


**Optional validation parameters**

```
-protein_FDR               FDR at the protein level in percent 
                           (default 1% FDR: '1').

-peptide_FDR               FDR at the peptide level in percent 
                           (default 1% FDR: '1').

-psm_FDR                   FDR at the PSM level in percent (default 1%
                           FDR: '1').

-protein_fraction_mw_confidence     
                           Minimum confidence required for a protein in
                           the fraction MW plot (default 95%: '95.0').
```

**Optional PTM localization scoring parameters**

```
-ptm_score                 The PTM probabilistic score to use if any (0: A-score, 1: phosphoRS). 
                           If no score is given, no probabilistic score will be used.

-ptm_threshold             The threshold to use for the PTM scores. If none set, 
                           an automatic threshold will be used.

-score_neutral_losses      Include neutral losses of mass different from the PTM 
                           in spectrum annotation of the PTM score 
                           (1: true, 0: false, default is '0').
```

**Optional filtering parameters**

```
-min_peptide_length        Minimim peptide length filter (default is '4').

-max_peptide_length        Maximum peptide length filter (default is '30').

-max_precursor_error       Maximum precursor error filter (no filter is used by default). 
                           See also max_precursor_error_type.

-max_precursor_error_type  Maximum precursor error type (0: ppm, 1: Da, default is '0'). 
                           See also max_precursor_error.

-exclude_unknown_ptms      Exclude unknown PTMs (1: true, 0: false, default is '1').
```

**Optional export parameters**

```
-zip                       Exports the entire project as a zip file in the file specified.
```

**Optional processing parameters**

```
-threads                   The number of threads to use. Defaults to the number of available CPUs.

-gui                       Use a dialog to display the progress (1: true, 0: false, default is '0').
```


**PeptideShakerCLI Example**

PeptideShakerCLI example where _X_, _Y_ and _Z_ have to be replaced by the actual version of PeptideShaker and _my folder_ by the folder containing the desired files:

```
java -cp PeptideShaker-X.Y.Z.jar eu.isas.peptideshaker.cmd.PeptideShakerCLI 
-experiment myExperiment -sample mySample -replicate 1 
-identification_files "C:\my folder" -spectrum_files "C:\my folder" 
-id_params "C:\my folder\my_search_params.parameters" 
-out "C:\my folder\myCpsFile.cps"
```

_Note that for readability the command is here split over multiple lines. When used the command should of course be a single line._

[Go to top of page](#PeptideShaker_Command_Line_Interface.md)


---


## B) ReportCLI ##

**Standard command line**

```
java -cp PeptideShaker-X.Y.Z.jar eu.isas.peptideshaker.cmd.ReportCLI [parameters]
```

**Mandatory parameters**

```
-in                        PeptideShaker project (.cps file)

-out_reports               Output folder for report files. (Existing files will be overwritten.)
```

**Optional report options**

```
-reports                   Comma separated list of types of report to export. 
                           0: Certificate of Analysis, 
                           1: Default Hierarchical Report,
                           2: Default PSM Report, 
                           3: Default Peptide Report, 
                           4: Default Protein Report, 
                           5-n: Your own custom reports.

-documentation             Comma separated list of types of report documentation to export. 
                           0: Certificate of Analysis, 
                           1: Default Hierarchical Report,
                           2: Default PSM Report, 
                           3: Default Peptide Report, 
                           4: Default Protein Report,
                           5-n: Your own custom reports.
```

To add custom reports see Export > Identification Features > Reports in PeptideShaker.

**ReportCLI Example**

ReportCLI example where _X_, _Y_ and _Z_ have to be replaced by the actual version of PeptideShaker:

```
java -cp PeptideShaker-X.Y.Z.jar eu.isas.peptideshaker.cmd.ReportCLI 
-in "C:\my folder\myCpsFile.cps" -out_reports "C:\my folder" -reports "0, 3"
```

[Go to top of page](#PeptideShaker_Command_Line_Interface.md)


---


## C) FollowUpCLI ##

**Standard command line**

```
java -cp PeptideShaker-X.Y.Z.jar eu.isas.peptideshaker.cmd.FollowUpCLI [parameters]
```

**Mandatory parameters**

```
-in                        PeptideShaker project (.cps file)
```

**Optional recalibration parameters**

```
-recalibration_folder      Output folder for the recalibrated files. (Existing files will be overwritten.)

-recalibration_mode        Recalibration type. 
                           0: precursor and fragment ions (default), 
                           1: precursor only, 
                           2: fragment ions only.
```

**Optional spectrum export parameters**

```
-spectrum_folder           Output folder for the spectra. (Existing files will be overwritten.)

-psm_type                  Type of PSMs. 
                           0: Spectra of Non-Validated PSMs (default),  
                           1: Spectra of Non-Validated Peptides, 
                           2: Spectra of Non-Validated Proteins, 
                           3: Spectra of Validated PSMs, 
                           4: Spectra of Validated PSMs of Validated Peptides, 
                           5: Spectra of validated PSMs of Validated Peptides of Validated Proteins.
```

**Optional Progenesis export parameters**

```
-progenesis_file           Output file for identification results in Progenesis LC-MS compatible format. 
                           (Existing files will be overwritten.)

-progenesis_type           Type of hits to export to Progenesis. 
                           0: Validated PSMs of Validated Peptides of Validated Proteins.
                           1: Validated PSMs of Validated Peptides, 
                           2: Validated PSMs,
                           3: Validated PSMs containing confidently localized PTMs.

-progenesis_ptms           Comma separated list of PTMs to include in reports of Type 3.
```

**Optional protein accessions export parameters**

```
-accessions_file           Output file to export the protein accessions in text format. 
                           (Existing files will be overwritten.)

-accessions_type           When exporting accessions, select a category of proteins. 
                           0: Main Accession of Validated Protein Groups (default),
                           1: All Accessions of Validated Protein Groups, 
                           2: Non-Validated Accessions.
```

**Optional FASTA export parameters**

```
-fasta_file                File where to export the protein details in fasta format. 
                           (Existing files will be overwritten.)

-fasta_type                When exporting protein details, select a category of proteins. 
                           0: Main Accession of Validated Protein Groups (default), 
                           1: All Accessions of Validated Protein Groups, 
                           2: Non-Validated Accessions.
```

**Optional inclusion list generation parameters**

```
-inclusion_list_file       Output file for an inclusion list of validated hits. 
                           (Existing files will be overwritten.)

-inclusion_list_format     Format for the inclusion list. 
                           0: Thermo (default), 
                           1: ABI, 
                           2: Bruker, 
                           3: MassLynx.

-inclusion_list_peptide_filters     
                           Peptide filters to be used for the inclusion list export (comma separated). 
                           0: Miscleaved Peptides, 
                           1: Reactive Peptides, 
                           2: Degenerated Peptides.

-inclusion_list_protein_filters     
                           Protein inference filters to be used for the inclusion list export (comma separated). 
                           1: Related Proteins, 
                           2: Related and Unrelated Proteins, 
                           3: Unrelated Proteins.

-inclusion_list_rt_window  Retention time window for the inclusion list export (in seconds).
```

**FollowUpCLI Example**

FollowUpCLI example where _X_, _Y_ and _Z_ have to be replaced by the actual version of PeptideShaker and _my folder_ by the folder containing the desired files:

```
java -cp PeptideShaker-X.Y.Z.jar eu.isas.peptideshaker.cmd.FollowUpCLI -in "C:\my folder\myCpsFile.cps" -spectrum_folder "C:\my folder" -psm_type 0
```

[Go to top of page](#PeptideShaker_Command_Line_Interface.md)


---


## D) MzidCLI ##

Available from PeptideShaker v0.30.0.

**Standard command line**

```
java -cp PeptideShaker-X.Y.Z.jar eu.isas.peptideshaker.cmd.MzidCLI [parameters]
```

**Mandatory parameters**

```
-in                        PeptideShaker project (.cps file)

-output_file               Output file.

-contact_first_name        Contact first name.

-contact_last_name         Contact last name.

-contact_email             Contact e-mail.

-contact_address           Contact address.

-organization_name         Organization name.

-organization_email        Organization e-mail.

-organization_address      Organization address.
```

**Optional parameters:**

```
-contact_url               Contact URL.

-organization_url          Organization URL.             
```

[Go to top of page](#PeptideShaker_Command_Line_Interface.md)


---


## E) PathSettingsCLI ##

**Generic temporary folder**

```
-temp_folder               A folder for temporary file storage. Use only if 
                           you encounter problems with the default configuration.
```

**Specific path setting**

```
-peptideshaker_matches_directory    
                           Directory where identification matches are temporarily 
                           saved to reduce the memory footprint.

-peptideshaker_user_preferences     
                           Folder containing the PeptideShaker user preferences file.

-peptideshaker_exports     Folder containing the user custom exports file.

-utilities_user_preferences         
                           Folder containing the compomics utilities user preferences file.

-ptm_configuration         Folder containing the PTM user preferences file.

-fasta_indexes             Folder containing the indexes of the protein sequences databases.

-gene_mapping              Folder containing the gene mapping files.

-pride_annotation          Folder containing the PRIDE annotation preferences.
```

[Go to top of page](#PeptideShaker_Command_Line_Interface.md)


---


## F) General ##

**Comma Separated Lists**

When using comma separated lists as input please pay attention to the quotes required. Surround the full content of the option in quotes and not the individual items:

```
-spectrum_files "C:\..\file_1.mgf, C:\..\file_2.mgf"
```

**Absolute Paths**

In general it is recommended to use absolute paths.


**Memory Settings**

Remember that big datasets require more than the default memory provided to the Java virtual machine, so for larger dataset please increase the maximum memory setting. Example, for a maximum of 2GB of memory:

```
java -Xmx2048M -cp PeptideShaker-X.Y.Z.jar eu.isas.peptideshaker.cmd.PeptideShakerCLI [parameters]
```

See also: [JavaTroubleShooting](http://code.google.com/p/compomics-utilities/wiki/JavaTroubleShooting).


**Opening PeptideShaker Projects**

To open a PeptideShaker project (a cps file) from the command line (for display in PetpideShaker) use the following command:

```
java -jar PeptideShaker-X.Y.Z.jar -cps "C:\my folder\myCpsFile.cps"
```


**Help**

If you experience any problems with the command line or have any suggestion please contact us via the [PeptideShaker mailing list](https://groups.google.com/group/peptide-shaker) or set up an issue using the [issue tracking system](http://code.google.com/p/peptide-shaker/issues/list).

[Go to top of page](#PeptideShaker_Command_Line_Interface.md)