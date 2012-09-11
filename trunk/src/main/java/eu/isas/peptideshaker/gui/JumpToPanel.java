package eu.isas.peptideshaker.gui;

import com.compomics.util.experiment.identification.SequenceFactory;
import com.compomics.util.experiment.identification.matches.PeptideMatch;
import com.compomics.util.experiment.identification.matches.ProteinMatch;
import com.compomics.util.experiment.massspectrometry.Precursor;
import com.compomics.util.experiment.massspectrometry.Spectrum;
import com.compomics.util.experiment.massspectrometry.SpectrumFactory;
import eu.isas.peptideshaker.myparameters.PSParameter;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Jump To panel for use in the menu bar in the main frame.
 *
 * @author Marc Vaudel
 * @author Harald Barsnes
 */
public class JumpToPanel extends javax.swing.JPanel {

    /**
     * Enum of the types of data to jump to.
     */
    public enum JumpType {

        proteinAndPeptides, spectrum
    }
    /**
     * the type of data to jump to in that panel.
     */
    private JumpType jumpType = JumpType.proteinAndPeptides;
    /**
     * Instance of the main GUI class.
     */
    private PeptideShakerGUI peptideShakerGUI;
    /**
     * Items matching the criterion for each type.
     */
    private HashMap<JumpType, ArrayList<String>> possibilities = new HashMap<JumpType, ArrayList<String>>();
    /**
     * currently selected item
     */
    private HashMap<JumpType, Integer> currentSelection = new HashMap<JumpType, Integer>();
    /**
     * the text to display by default.
     */
    private HashMap<JumpType, String> lastInput = new HashMap<JumpType, String>();
    /**
     * the text to display by default.
     */
    private HashMap<JumpType, String> lastLabel = new HashMap<JumpType, String>();
    /**
     * Instance of the sequence factory.
     */
    private SequenceFactory sequenceFactory = SequenceFactory.getInstance();
    /**
     * the text to display by default.
     */
    private HashMap<JumpType, String> welcomeText;
    /**
     * The spectrum factory
     */
    private SpectrumFactory spectrumFactory = SpectrumFactory.getInstance();
    /**
     * The spectrum file inspected when jumping to spectra.
     */
    private String spectrumfile;
    /**
     * Counts the number of times the users has pressed a key on the keyboard in
     * the search field.
     */
    private int keyPressedCounter = 0;
    /**
     * The time to wait between keys typed before updating the search.
     */
    private int waitingTime = 1000;

    /**
     * Type of item selected
     */
    private enum Type {

        PROTEIN,
        PEPTIDE,
        SPECTRUM
    }
    /**
     * Type of each possible item.
     */
    private HashMap<JumpType, ArrayList<Type>> types = new HashMap<JumpType, ArrayList<Type>>();

    /**
     * Creates a new JumpToPanel
     *
     * @param peptideShakerGUI the parent
     */
    public JumpToPanel(PeptideShakerGUI peptideShakerGUI) {
        initComponents();

        this.peptideShakerGUI = peptideShakerGUI;

        welcomeText = new HashMap<JumpType, String>();
        welcomeText.put(JumpType.proteinAndPeptides, "(protein or peptide)");
        welcomeText.put(JumpType.spectrum, "(title, m/z or RT)");
        inputTxt.setText(welcomeText.get(jumpType));
        indexLabel.setText("");
        previousButton.setEnabled(false);
        nextButton.setEnabled(false);
    }

    /**
     * Moce the focus to the Jump To text field and select all the content.
     */
    public void selectTextField() {
        inputTxt.requestFocus();
        inputTxt.selectAll();
    }

    /**
     * Set the color for the hits.
     *
     * @param color
     */
    public void setColor(Color color) {
        indexLabel.setForeground(color);
    }

    /**
     * Updates the item selection in the selected tab.
     */
    public void updateSelectionInTab() {

        indexLabel.setForeground(Color.BLACK);

        if (types.get(jumpType).get(currentSelection.get(jumpType)) == Type.PROTEIN) {
            peptideShakerGUI.setSelectedItems(possibilities.get(jumpType).get(currentSelection.get(jumpType)), PeptideShakerGUI.NO_SELECTION, PeptideShakerGUI.NO_SELECTION);
            peptideShakerGUI.updateSelectionInCurrentTab();
        } else if (types.get(jumpType).get(currentSelection.get(jumpType)) == Type.PEPTIDE) {
            peptideShakerGUI.setSelectedItems(PeptideShakerGUI.NO_SELECTION, possibilities.get(jumpType).get(currentSelection.get(jumpType)), PeptideShakerGUI.NO_SELECTION);
            if (peptideShakerGUI.getSelectedTab() == PeptideShakerGUI.MODIFICATIONS_TAB_INDEX
                    && !peptideShakerGUI.getDisplayedPeptides().contains(possibilities.get(jumpType).get(currentSelection.get(jumpType)))) {
                // warn the user that the current selection is not in the tab
                indexLabel.setForeground(Color.RED);
            } else {
                peptideShakerGUI.updateSelectionInCurrentTab();
            }
        } else {
            peptideShakerGUI.setSelectedItems(PeptideShakerGUI.NO_SELECTION, PeptideShakerGUI.NO_SELECTION, possibilities.get(jumpType).get(currentSelection.get(jumpType)));
            peptideShakerGUI.updateSelectionInCurrentTab();
        }
        String label = "(" + (currentSelection.get(jumpType) + 1) + " of " + possibilities.get(jumpType).size() + ")";
        indexLabel.setText(label);
        lastLabel.put(jumpType, label);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        findJLabel = new javax.swing.JLabel();
        inputTxt = new javax.swing.JTextField();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        indexLabel = new javax.swing.JLabel();

        setOpaque(false);

        findJLabel.setText("Find:");

        inputTxt.setForeground(new java.awt.Color(204, 204, 204));
        inputTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        inputTxt.setText("(peptide or protein)");
        inputTxt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        inputTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                inputTxtMouseReleased(evt);
            }
        });
        inputTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputTxtKeyReleased(evt);
            }
        });

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/previous_grey.png"))); // NOI18N
        previousButton.setToolTipText("Previous");
        previousButton.setBorder(null);
        previousButton.setBorderPainted(false);
        previousButton.setContentAreaFilled(false);
        previousButton.setIconTextGap(0);
        previousButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/previous.png"))); // NOI18N
        previousButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                previousButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                previousButtonMouseExited(evt);
            }
        });
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/next_grey.png"))); // NOI18N
        nextButton.setToolTipText("Next");
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/next.png"))); // NOI18N
        nextButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                nextButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                nextButtonMouseExited(evt);
            }
        });
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        indexLabel.setFont(indexLabel.getFont().deriveFont((indexLabel.getFont().getStyle() | java.awt.Font.ITALIC)));
        indexLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        indexLabel.setText(" ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(findJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(indexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {nextButton, previousButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(findJLabel)
                    .addComponent(inputTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(indexLabel)
                    .addComponent(previousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Update the jump to filter.
     *
     * @param evt
     */
    private void inputTxtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTxtKeyReleased

        final KeyEvent event = evt;
        keyPressedCounter++;

        new Thread("FindThread") {

            @Override
            public synchronized void run() {

                try {
                    wait(waitingTime);
                } catch (InterruptedException e) {
                }

                // see if the gui is to be updated or not
                if (peptideShakerGUI.getIdentification() != null && keyPressedCounter == 1) {

                    if (!inputTxt.getText().equalsIgnoreCase(welcomeText.get(jumpType))) {
                        inputTxt.setForeground(Color.black);
                    } else {
                        inputTxt.setForeground(new Color(204, 204, 204));
                    }

                    if (event.getKeyCode() == KeyEvent.VK_UP && previousButton.isEnabled()) {
                        previousButtonActionPerformed(null);
                    } else if (event.getKeyCode() == KeyEvent.VK_DOWN & nextButton.isEnabled()) {
                        nextButtonActionPerformed(null);
                    } else {
                        if (!possibilities.containsKey(jumpType)) {
                            possibilities.put(jumpType, new ArrayList<String>());
                            types.put(jumpType, new ArrayList<Type>());
                        } else {
                            possibilities.get(jumpType).clear();
                            types.get(jumpType).clear();
                        }
                        currentSelection.put(jumpType, 0);
                        String doubleString, input = inputTxt.getText().trim().toLowerCase();
                        lastInput.put(jumpType, input);

                        if (!input.equals("")) {

                            peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                            inputTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));

                            if (jumpType == JumpType.proteinAndPeptides) {
                                PSParameter psParameter = new PSParameter();

                                for (String proteinKey : peptideShakerGUI.getIdentification().getProteinIdentification()) {
                                    if (!ProteinMatch.isDecoy(proteinKey)) {
                                        try {
                                            psParameter = (PSParameter) peptideShakerGUI.getIdentification().getProteinMatchParameter(proteinKey, psParameter);
                                        } catch (Exception e) {
                                            peptideShakerGUI.catchException(e);
                                        }
                                        if (!psParameter.isHidden()) {
                                            if (proteinKey.toLowerCase().contains(input)) {
                                                possibilities.get(jumpType).add(proteinKey);
                                                types.get(jumpType).add(Type.PROTEIN);
                                            } else {
                                                try {
                                                    for (String accession : ProteinMatch.getAccessions(proteinKey)) {
                                                        if (sequenceFactory.getHeader(accession).getDescription().toLowerCase().contains(input)) {
                                                            possibilities.get(jumpType).add(proteinKey);
                                                            types.get(jumpType).add(Type.PROTEIN);
                                                            break;
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    // cannot get description, ignore
                                                }
                                            }
                                        }
                                    }
                                }

                                ArrayList<String> secondaryCandidates = new ArrayList<String>();
                                PeptideMatch peptideMatch;

                                for (String peptideKey : peptideShakerGUI.getIdentification().getPeptideIdentification()) {
                                    try {
                                        psParameter = (PSParameter) peptideShakerGUI.getIdentification().getPeptideMatchParameter(peptideKey, psParameter);

                                    } catch (Exception e) {
                                        peptideShakerGUI.catchException(e);
                                        return;
                                    }
                                    if (!psParameter.isHidden()) {
                                        if (peptideKey.toLowerCase().startsWith(input)) {
                                            possibilities.get(jumpType).add(peptideKey);
                                            types.get(jumpType).add(Type.PEPTIDE);
                                        } else if (peptideKey.toLowerCase().contains(input)) {
                                            secondaryCandidates.add(peptideKey);
                                        }
                                    }
                                }

                                for (String secondaryCandidate : secondaryCandidates) {
                                    try {
                                        peptideMatch = peptideShakerGUI.getIdentification().getPeptideMatch(secondaryCandidate);

                                    } catch (Exception e) {
                                        peptideShakerGUI.catchException(e);
                                        return;
                                    }
                                    for (String protein : peptideMatch.getTheoreticPeptide().getParentProteins()) {
                                        if (!ProteinMatch.isDecoy(protein)) {
                                            possibilities.get(jumpType).add(secondaryCandidate);
                                            types.get(jumpType).add(Type.PEPTIDE);
                                            break;
                                        }
                                    }
                                }
                            } else {
                                String spectrumKey;
                                for (String spectrumTitle : spectrumFactory.getSpectrumTitles(spectrumfile)) {
                                    spectrumKey = Spectrum.getSpectrumKey(spectrumfile, spectrumTitle);
                                    if (spectrumKey.contains(input)) {
                                        possibilities.get(jumpType).add(spectrumKey);
                                        types.get(jumpType).add(Type.SPECTRUM);
                                    } else {
                                        try {
                                            Precursor precursor = peptideShakerGUI.getPrecursor(spectrumKey, false);
                                            doubleString = precursor.getMz() + "";
                                            if (doubleString.startsWith(input)) {
                                                possibilities.get(jumpType).add(spectrumKey);
                                                types.get(jumpType).add(Type.SPECTRUM);
                                            } else {
                                                doubleString = precursor.getRt() + "";
                                                if (doubleString.startsWith(input)) {
                                                    possibilities.get(jumpType).add(spectrumKey);
                                                    types.get(jumpType).add(Type.SPECTRUM);
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            if (possibilities.get(jumpType).size() > 0) {

                                if (possibilities.get(jumpType).size() > 1) {
                                    previousButton.setEnabled(true);
                                    nextButton.setEnabled(true);
                                } else { // possibilities.size() == 1
                                    previousButton.setEnabled(false);
                                    nextButton.setEnabled(false);
                                }

                                updateSelectionInTab();
                            } else {
                                previousButton.setEnabled(false);
                                nextButton.setEnabled(false);

                                if (!input.equalsIgnoreCase(welcomeText.get(jumpType))) {
                                    indexLabel.setText("(no matches)");
                                } else {
                                    indexLabel.setText("");
                                }
                            }

                            peptideShakerGUI.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                            inputTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

                        } else {
                            indexLabel.setText("");
                            previousButton.setEnabled(false);
                            nextButton.setEnabled(false);
                            inputTxt.setText(welcomeText.get(jumpType));
                            inputTxt.selectAll();
                        }
                    }

                    lastLabel.put(jumpType, indexLabel.getText());

                    // gui updated, reset the counter
                    keyPressedCounter = 0;
                } else {
                    // gui not updated, decrease the counter
                    keyPressedCounter--;
                }
            }
        }.start();
    }//GEN-LAST:event_inputTxtKeyReleased

    /**
     * Display the previous match in the list.
     *
     * @param evt
     */
    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        if (currentSelection.get(jumpType) == 0) {
            currentSelection.put(jumpType, possibilities.get(jumpType).size() - 1);
        } else {
            currentSelection.put(jumpType, currentSelection.get(jumpType) - 1);
        }
        updateSelectionInTab();
    }//GEN-LAST:event_previousButtonActionPerformed

    /**
     * Display the next match in the list.
     *
     * @param evt
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        if (currentSelection.get(jumpType) == possibilities.get(jumpType).size() - 1) {
            currentSelection.put(jumpType, 0);
        } else {
            currentSelection.put(jumpType, currentSelection.get(jumpType) + 1);
        }
        updateSelectionInTab();
    }//GEN-LAST:event_nextButtonActionPerformed

    /**
     * Select all text in the search field.
     *
     * @param evt
     */
    private void inputTxtMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inputTxtMouseReleased
        if (inputTxt.getText().equals(welcomeText.get(jumpType))) {
            inputTxt.selectAll();
        }
    }//GEN-LAST:event_inputTxtMouseReleased

    /**
     * Change the icon to a hand icon.
     *
     * @param evt
     */
    private void previousButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousButtonMouseEntered
        if (previousButton.isEnabled()) {
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }
    }//GEN-LAST:event_previousButtonMouseEntered

    /**
     * Change the icon back to the default icon.
     *
     * @param evt
     */
    private void previousButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previousButtonMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_previousButtonMouseExited

    /**
     * Change the icon back to the default icon.
     *
     * @param evt
     */
    private void nextButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextButtonMouseExited
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_nextButtonMouseExited

    /**
     * Change the icon to a hand icon.
     *
     * @param evt
     */
    private void nextButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextButtonMouseEntered
        if (nextButton.isEnabled()) {
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        }
    }//GEN-LAST:event_nextButtonMouseEntered

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel findJLabel;
    private javax.swing.JLabel indexLabel;
    private javax.swing.JTextField inputTxt;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    // End of variables declaration//GEN-END:variables

    /**
     * Override to set the input text field enabled or not.
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {

        inputTxt.setEnabled(enabled);
        indexLabel.setEnabled(enabled);

        if (possibilities.size() > 0 && enabled) {
            previousButton.setEnabled(true);
            nextButton.setEnabled(true);
        } else {
            previousButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }

    /**
     * Changes the type of jumpToPanel.
     *
     * @param jumpType the new type of jump to panel
     */
    public void setType(JumpType jumpType) {
        this.jumpType = jumpType;
        if (lastInput.get(jumpType) != null && !lastInput.get(jumpType).equals("")) {
            inputTxt.setText(lastInput.get(jumpType));
            indexLabel.setText(lastLabel.get(jumpType));
        } else {
            inputTxt.setText(welcomeText.get(jumpType));
            indexLabel.setText("");
        }
    }

    /**
     * Sets the spectrum file inspected.
     *
     * @param spectrumFile the name of the spectrum file inspected
     */
    public void setSpectrumFile(String spectrumFile) {
        this.spectrumfile = spectrumFile;
    }
}