package eu.isas.peptideshaker.gui;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * A dialog displaying progress details when the identification files are being
 * analyzed.
 *
 * @author  Marc Vaudel
 * @author  Harald Barsnes
 */
public class WaitingDialog extends javax.swing.JDialog {

    /**
     * Needed for the shaking feature.
     */
    private JDialog dialog;
    /**
     * Used in the shaking feature.
     */
    private Point naturalLocation;
    /**
     * Timer for the shaking feature.
     */
    private Timer shakeTimer;
    /**
     * A reference to the main frame.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * Boolean indicating whether the run is finished
     */
    private boolean runFinished = false;
    /**
     * Boolean indicating whether the run is canceled
     */
    private boolean runCanceled = false;
    /**
     * Convenience date format
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
    /**
     * The tab space to add when using tab.
     */
    private String tab = "        "; // tab could be used, but is location dependent


    /**
     * Creates a new WaitingDialog.
     *
     * @param peptideShaker a reference to the main frame
     * @param modal
     * @param experimentReference the experiment reference
     */
    public WaitingDialog(PeptideShakerGUI peptideShaker, boolean modal, String experimentReference) {
        super(peptideShaker, modal);
        initComponents();
        this.setLocationRelativeTo(peptideShaker);
        this.peptideShakerGUI = peptideShaker;

        // change the peptide shaker icon to a "waiting version"
        peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker-orange.gif")));
    }

    /**
     * Set the maximum value of the progress bar.
     *
     * @param maxProgressValue the max value
     */
    public void setMaxProgressValue(int maxProgressValue) {
        progressBar.setMaximum(maxProgressValue);
    }

    /**
     * Increase the progress bar value by one "counter".
     */
    public void increaseProgressValue() {
        progressBar.setValue(progressBar.getValue() + 1);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        reportArea = new javax.swing.JTextArea();
        okButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Importing Data - Please Wait...");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Import Progress"));

        reportArea.setBackground(new java.awt.Color(254, 254, 254));
        reportArea.setColumns(20);
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setRows(5);
        jScrollPane1.setViewportView(reportArea);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addContainerGap())
        );

        okButton.setText("Cancel");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save Report");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(406, 406, 406)
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {okButton, saveButton});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(saveButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Saves the progress report to file.
     *
     * @param evt
     */
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        File outputFile = null;
        JFileChooser fc = new JFileChooser();
        int result = fc.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            outputFile = fc.getSelectedFile();
            if (outputFile.exists()) {
                int choice = JOptionPane.showConfirmDialog(this,
                        new String[]{"The file " + outputFile.getName() + " already exists!", "Overwrite?"},
                        "File Already Exists", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION) {
                    return;
                }
            } else {
                return;
            }
        }
        if (outputFile != null) {
            saveReport(outputFile);
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    /**
     * Cancels the analysis if ongoing or opens the results if finished.
     *
     * @param evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if (runFinished || runCanceled) {
            this.dispose();
        } else {
            setRunCanceled();
        }
    }//GEN-LAST:event_okButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea reportArea;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Set the analysis as finished.
     */
    public void setRunFinished() {
        runFinished = true;
        saveButton.setEnabled(true);
        okButton.setText("OK");
        progressBar.setIndeterminate(false);
        progressBar.setValue(progressBar.getMaximum());
        progressBar.setStringPainted(true);
        progressBar.setString("Import Completed.");
        this.setTitle("Importing Data - Completed");
        
        // change the peptide shaker icon back to the default version
        peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")));

        // make the dialog shake for a couple of seconds
        startShake();
    }

    /**
     * Set the analysis as canceled.
     */
    public void setRunCanceled() {
        runCanceled = true;
        appendReportEndLine();
        appendReport("Import canceled.");
        saveButton.setEnabled(true);
        okButton.setText("OK");
        progressBar.setIndeterminate(false);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("Calculation Canceled!");
        this.setTitle("Importing Data - Canceled");
    }

    /**
     * Append text to the report.
     *
     * @param report the text to append
     */
    public void appendReport(String report) {
        Date date = new Date();
        reportArea.append(date + tab + report + "\n");
    }

    /**
     * Append two tabs to the report. No new line.
     */
    public void appendReportNewLineNoDate() {
        reportArea.append(tab);
    }

    /**
     * Append a new line to the report.
     */
    public void appendReportEndLine() {
        reportArea.append("\n");
    }

    /**
     * Returns true if the run is canceled.
     *
     * @return true if the run is canceled
     */
    public boolean isRunCanceled() {
        return runCanceled;
    }

    /**
     * Saves the report in the given file
     *
     * @param aFile file to save the report in
     */
    private void saveReport(File aFile) {
        StringBuffer output = new StringBuffer();
        String host = " @ ";

        try {
            host += InetAddress.getLocalHost().getHostName();


        } catch (UnknownHostException uhe) {
            // Disregard. It's not so bad if we can not report this.
        }

        // Write the file header.
        output.append("# ------------------------------------------------------------------"
                + "\n# SearchGUI Report File"
                + "\n#"
                + "\n# Originally saved by: " + System.getProperty("user.name") + host
                + "\n#                  on: " + sdf.format(new Date())
                + "\n#                  as: " + aFile.getName()
                + "\n# ------------------------------------------------------------------\n");

        output.append(reportArea.getText() + "\n");

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(aFile));
            bw.write(output.toString());
            bw.flush();
            JOptionPane.showMessageDialog(this, "Settings written to file '" + aFile.getAbsolutePath() + "'.", "Settings Saved", JOptionPane.INFORMATION_MESSAGE);


        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this, new String[]{"Error writing report to file:", ioe.getMessage()}, "Save Failed", JOptionPane.ERROR_MESSAGE);

        } finally {
            if (bw != null) {
                try {
                    bw.close();

                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(this, new String[]{"Error writing report to file:", ioe.getMessage()}, "Save Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Make the dialog shake when the analysis has completed.
     */
    private void startShake() {
        final long startTime;

        naturalLocation = this.getLocation();
        startTime = System.currentTimeMillis();

        dialog = this;

        shakeTimer = new Timer(5, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                double TWO_PI = Math.PI * 2.0;
                double SHAKE_CYCLE = 50;

                long elapsed = System.currentTimeMillis() - startTime;
                double waveOffset = (elapsed % SHAKE_CYCLE) / SHAKE_CYCLE;
                double angle = waveOffset * TWO_PI;

                int SHAKE_DISTANCE = 10;

                int shakenX = (int) ((Math.sin(angle) * SHAKE_DISTANCE) + naturalLocation.x);
                dialog.setLocation(shakenX, naturalLocation.y);
                dialog.repaint();

                int SHAKE_DURATION = 1000;

                if (elapsed >= SHAKE_DURATION) {
                    stopShake();
                }
            }
        });
        shakeTimer.start();
    }

    /**
     * Stop the dialog shake.
     */
    private void stopShake() {
        shakeTimer.stop();
        dialog.setLocation(naturalLocation);
        dialog.repaint();

        appendReport("Your peptides have been shaken!");

        // return the peptide shaker icon to the standard version
        peptideShakerGUI.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/peptide-shaker.gif")));
    }
}
