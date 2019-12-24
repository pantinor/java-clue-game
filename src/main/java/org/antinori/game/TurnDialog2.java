package org.antinori.game;

import static org.antinori.game.Card.TYPE_ROOM;

import java.util.ArrayList;

public class TurnDialog2 extends javax.swing.JDialog {

    public static final int ACTION_VALID_ACCUSATION = 200;
    public static final int ACTION_INVALID_ACCUSATION = 500;
    public static final int ACTION_MADE_SUGGESTION = 300;
    public static final int ACTION_TOOK_PASSAGE = 310;
    public static final int ACTION_ROLLED_DICE = 320;

    Player player;

    boolean showDiceButton = true;
    boolean showSuggestionButton = true;
    boolean showSecretPassageButton = false;

    int action = 0;

    public TurnDialog2(Player player, boolean showDiceButton, boolean showSecretPassageButton, boolean showSuggestionButton) {
        super(ClueMain.frame, true);

        this.player = player;
        this.showDiceButton = showDiceButton;
        this.showSuggestionButton = showSuggestionButton;
        this.showSecretPassageButton = showSecretPassageButton;

        initComponents();
    }

    // return the data after disposing
    public int showDialog() {
        setVisible(true);
        return action;
    }

    private void initComponents() {

        setUndecorated(true);

        ClueMain.setLocationInCenter(this, -200, -200);

        java.awt.GridBagConstraints gridBagConstraints;

        bg = new javax.swing.JPanel();
        fgPanel = new DropShadowPanel();
        rollDiceButton = new javax.swing.JButton();
        takeSecretPassageButton = new javax.swing.JButton();
        makeSuggestionButton = new javax.swing.JButton();
        bgLabel = new javax.swing.JLabel();

        if (!showDiceButton) {
            rollDiceButton.setEnabled(false);
        }
        if (!showSuggestionButton) {
            makeSuggestionButton.setEnabled(false);
        }
        if (!showSecretPassageButton) {
            takeSecretPassageButton.setEnabled(false);
        }

        bg.setMinimumSize(new java.awt.Dimension(1, 1));
        bg.setLayout(new java.awt.GridBagLayout());

        fgPanel.setBackground(java.awt.SystemColor.info);
        fgPanel.setPreferredSize(new java.awt.Dimension(320, 235));

        rollDiceButton.setText("Roll the Dice");
        rollDiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rollDiceButtonActionPerformed(evt);
            }
        });

        takeSecretPassageButton.setText("Take Secret Passage");
        takeSecretPassageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takeSecretPassageButtonActionPerformed(evt);
            }
        });

        makeSuggestionButton.setText("Make a Suggestion");
        makeSuggestionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeSuggestionButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout fgPanelLayout = new org.jdesktop.layout.GroupLayout(fgPanel);
        fgPanel.setLayout(fgPanelLayout);
        fgPanelLayout.setHorizontalGroup(
                fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(fgPanelLayout.createSequentialGroup()
                                .add(91, 91, 91)
                                .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(makeSuggestionButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(takeSecretPassageButton)
                                        .add(rollDiceButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(96, Short.MAX_VALUE))
        );
        fgPanelLayout.setVerticalGroup(
                fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(fgPanelLayout.createSequentialGroup()
                                .add(64, 64, 64)
                                .add(rollDiceButton)
                                .add(18, 18, 18)
                                .add(takeSecretPassageButton)
                                .add(18, 18, 18)
                                .add(makeSuggestionButton)
                                .addContainerGap(66, Short.MAX_VALUE))
        );

        bg.add(fgPanel, new java.awt.GridBagConstraints());

        bgLabel.setIcon(ClueMain.getImageIcon("TurnFrame.png")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        bg.add(bgLabel, gridBagConstraints);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(bg, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(bg, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rollDiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rollDiceButtonActionPerformed
        ClueMain.map.resetHighlights();

        int roll = ClueMain.mapView.rollDice();

        ClueMain.map.highlightReachablePaths(player.getLocation(), ClueMain.pathfinder, roll);
        ClueMain.mapView.repaint();

        action = ACTION_ROLLED_DICE;

        dispose();
    }//GEN-LAST:event_rollDiceButtonActionPerformed

    private void takeSecretPassageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeSecretPassageButtonActionPerformed
        action = ACTION_TOOK_PASSAGE;
        SoundEffect.CREAK.play();
        dispose();
    }//GEN-LAST:event_takeSecretPassageButtonActionPerformed

    private void makeSuggestionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeSuggestionButtonActionPerformed
        int room_id = player.getLocation().getRoomId();
        if (room_id == -1) {
            return;
        }

        SuggestionDialog2 suggestionDialog = new SuggestionDialog2(ClueMain.frame, new Card(TYPE_ROOM, room_id), player.getNotebook());
        ArrayList<Card> suggestion = (ArrayList<Card>) suggestionDialog.showDialog();

        //send the suggestion for multi players
        ClueMain.showcards.setSuggestion(suggestion, player, ClueMain.yourPlayer, ClueMain.clue.getPlayers());

        //for single player, continue with the showing of the cards
        //if (!ClueMain.multiplayerFrame.isConnected()) {
        ClueMain.showcards.showCards();
        //}

        action = ACTION_MADE_SUGGESTION;

        dispose();
    }//GEN-LAST:event_makeSuggestionButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel bgLabel;
    private DropShadowPanel fgPanel;
    private javax.swing.JButton makeSuggestionButton;
    private javax.swing.JButton rollDiceButton;
    private javax.swing.JButton takeSecretPassageButton;
    // End of variables declaration//GEN-END:variables

}
