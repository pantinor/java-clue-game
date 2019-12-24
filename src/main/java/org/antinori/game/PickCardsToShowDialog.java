package org.antinori.game;

import java.util.ArrayList;
import javax.swing.JDialog;
import static org.antinori.game.Card.*;

public class PickCardsToShowDialog extends JDialog {

    public PickCardsToShowDialog(ArrayList<Card> suggestion, String suggestion_text, Player player) {

        this.suggestion = suggestion;
        this.player = player;

        setModal(true);
        setUndecorated(true);
        ClueMain.setLocationInCenter(this, -200, -200);

        initComponents();

        suggestion_ta.setText(player.getPlayerName() + ", pick a card to show\naccording to the suggestion.\n\n" + suggestion_text);

        ArrayList<Card> cards_in_hand = player.getCardsInHand();

        boolean has_a_card = false;

        for (Card card : cards_in_hand) {

            if (!suggestion.contains(card)) {
                continue;
            }

            has_a_card = true;

            int type = card.getType();
            int value = card.getValue();

            if (type == TYPE_SUSPECT) {
                if (value == SUSPECT_SCARLET) {
                    scarlet_cb.setEnabled(true);
                }
                if (value == SUSPECT_MUSTARD) {
                    mustard_cb.setEnabled(true);
                }
                if (value == SUSPECT_GREEN) {
                    green_cb.setEnabled(true);
                }
                if (value == SUSPECT_PLUM) {
                    plum_cb.setEnabled(true);
                }
                if (value == SUSPECT_WHITE) {
                    white_cb.setEnabled(true);
                }
                if (value == SUSPECT_PEACOCK) {
                    peacock_cb.setEnabled(true);
                }
            } else if (type == TYPE_WEAPON) {
                if (value == WEAPON_REVOLVER) {
                    revolver_cb.setEnabled(true);
                }
                if (value == WEAPON_PIPE) {
                    pipe_cb.setEnabled(true);
                }
                if (value == WEAPON_ROPE) {
                    rope_cb.setEnabled(true);
                }
                if (value == WEAPON_CANDLE) {
                    candlestick_cb.setEnabled(true);
                }
                if (value == WEAPON_WRENCH) {
                    wrench_cb.setEnabled(true);
                }
                if (value == WEAPON_KNIFE) {
                    knife_cb.setEnabled(true);
                }
            } else {
                if (value == ROOM_KITCHEN) {
                    kitchen_cb.setEnabled(true);
                }
                if (value == ROOM_BALLROOM) {
                    ballroom_cb.setEnabled(true);
                }
                if (value == ROOM_CONSERVATORY) {
                    conservatory_cb.setEnabled(true);
                }
                if (value == ROOM_BILLIARD) {
                    billiard_cb.setEnabled(true);
                }
                if (value == ROOM_LIBRARY) {
                    library_cb.setEnabled(true);
                }
                if (value == ROOM_STUDY) {
                    study_cb.setEnabled(true);
                }
                if (value == ROOM_HALL) {
                    hall_cb.setEnabled(true);
                }
                if (value == ROOM_LOUNGE) {
                    lounge_cb.setEnabled(true);
                }
                if (value == ROOM_DINING) {
                    dining_cb.setEnabled(true);
                }
            }

        }

        //let them click OK if they have no cards to show
        if (!has_a_card) {
            okButton.setEnabled(true);
        }

    }

    //return the data after clicking OK
    public Card showDialog() {
        setVisible(true);
        return picked_card;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bg = new javax.swing.JPanel();
        fgPanel = new javax.swing.JPanel();
        scarlet_cb = new javax.swing.JRadioButton();
        mustard_cb = new javax.swing.JRadioButton();
        green_cb = new javax.swing.JRadioButton();
        plum_cb = new javax.swing.JRadioButton();
        white_cb = new javax.swing.JRadioButton();
        peacock_cb = new javax.swing.JRadioButton();
        revolver_cb = new javax.swing.JRadioButton();
        candlestick_cb = new javax.swing.JRadioButton();
        rope_cb = new javax.swing.JRadioButton();
        pipe_cb = new javax.swing.JRadioButton();
        wrench_cb = new javax.swing.JRadioButton();
        knife_cb = new javax.swing.JRadioButton();
        kitchen_cb = new javax.swing.JRadioButton();
        ballroom_cb = new javax.swing.JRadioButton();
        conservatory_cb = new javax.swing.JRadioButton();
        billiard_cb = new javax.swing.JRadioButton();
        library_cb = new javax.swing.JRadioButton();
        study_cb = new javax.swing.JRadioButton();
        hall_cb = new javax.swing.JRadioButton();
        lounge_cb = new javax.swing.JRadioButton();
        dining_cb = new javax.swing.JRadioButton();
        okButton = new javax.swing.JButton();
        suggestion_ta = new javax.swing.JTextArea();
        bgLabel = new javax.swing.JLabel();
        buttonSelectGroup = new javax.swing.ButtonGroup();

        buttonSelectGroup.add(scarlet_cb);
        buttonSelectGroup.add(mustard_cb);
        buttonSelectGroup.add(green_cb);
        buttonSelectGroup.add(plum_cb);
        buttonSelectGroup.add(white_cb);
        buttonSelectGroup.add(peacock_cb);

        buttonSelectGroup.add(revolver_cb);
        buttonSelectGroup.add(candlestick_cb);
        buttonSelectGroup.add(rope_cb);
        buttonSelectGroup.add(pipe_cb);
        buttonSelectGroup.add(wrench_cb);
        buttonSelectGroup.add(knife_cb);

        buttonSelectGroup.add(kitchen_cb);
        buttonSelectGroup.add(ballroom_cb);
        buttonSelectGroup.add(conservatory_cb);
        buttonSelectGroup.add(billiard_cb);
        buttonSelectGroup.add(library_cb);
        buttonSelectGroup.add(study_cb);
        buttonSelectGroup.add(hall_cb);
        buttonSelectGroup.add(lounge_cb);
        buttonSelectGroup.add(dining_cb);

        scarlet_cb.addItemListener(new PickItemListener());
        mustard_cb.addItemListener(new PickItemListener());
        green_cb.addItemListener(new PickItemListener());
        plum_cb.addItemListener(new PickItemListener());
        white_cb.addItemListener(new PickItemListener());
        peacock_cb.addItemListener(new PickItemListener());

        revolver_cb.addItemListener(new PickItemListener());
        candlestick_cb.addItemListener(new PickItemListener());
        rope_cb.addItemListener(new PickItemListener());
        pipe_cb.addItemListener(new PickItemListener());
        wrench_cb.addItemListener(new PickItemListener());
        knife_cb.addItemListener(new PickItemListener());

        kitchen_cb.addItemListener(new PickItemListener());
        ballroom_cb.addItemListener(new PickItemListener());
        conservatory_cb.addItemListener(new PickItemListener());
        billiard_cb.addItemListener(new PickItemListener());
        library_cb.addItemListener(new PickItemListener());
        study_cb.addItemListener(new PickItemListener());
        hall_cb.addItemListener(new PickItemListener());
        lounge_cb.addItemListener(new PickItemListener());
        dining_cb.addItemListener(new PickItemListener());

        scarlet_cb.setEnabled(false);
        mustard_cb.setEnabled(false);
        green_cb.setEnabled(false);
        plum_cb.setEnabled(false);
        white_cb.setEnabled(false);
        peacock_cb.setEnabled(false);

        revolver_cb.setEnabled(false);
        candlestick_cb.setEnabled(false);
        rope_cb.setEnabled(false);
        pipe_cb.setEnabled(false);
        wrench_cb.setEnabled(false);
        knife_cb.setEnabled(false);

        kitchen_cb.setEnabled(false);
        ballroom_cb.setEnabled(false);
        conservatory_cb.setEnabled(false);
        billiard_cb.setEnabled(false);
        library_cb.setEnabled(false);
        study_cb.setEnabled(false);
        hall_cb.setEnabled(false);
        lounge_cb.setEnabled(false);
        dining_cb.setEnabled(false);

        okButton.setEnabled(false);

        bg.setMinimumSize(new java.awt.Dimension(1, 1));
        bg.setLayout(new java.awt.GridBagLayout());

        fgPanel.setOpaque(false);

        scarlet_cb.setText("Miss Scarlet");
        scarlet_cb.setOpaque(false);

        mustard_cb.setText("Colonel Mustard");
        mustard_cb.setOpaque(false);

        green_cb.setText("Mr. Green");
        green_cb.setOpaque(false);

        plum_cb.setText("Professor Plum");
        plum_cb.setOpaque(false);

        white_cb.setText("Mrs. White");
        white_cb.setOpaque(false);

        peacock_cb.setText("Mrs. Peacock");
        peacock_cb.setOpaque(false);

        revolver_cb.setText("Revolver");
        revolver_cb.setOpaque(false);

        candlestick_cb.setText("Candlelabra");
        candlestick_cb.setOpaque(false);

        rope_cb.setText("Rope");
        rope_cb.setOpaque(false);

        pipe_cb.setText("Lead Pipe");
        pipe_cb.setOpaque(false);

        wrench_cb.setText("Wrench");
        wrench_cb.setOpaque(false);

        knife_cb.setText("Knife");
        knife_cb.setOpaque(false);

        kitchen_cb.setText("Kitchen");
        kitchen_cb.setOpaque(false);

        ballroom_cb.setText("Ballroom");
        ballroom_cb.setOpaque(false);

        conservatory_cb.setText("Conservatory");
        conservatory_cb.setOpaque(false);

        billiard_cb.setText("Billiard Room");
        billiard_cb.setOpaque(false);

        library_cb.setText("Library");
        library_cb.setOpaque(false);

        study_cb.setText("Study");
        study_cb.setOpaque(false);

        hall_cb.setText("Hall");
        hall_cb.setOpaque(false);

        lounge_cb.setText("Lounge");
        lounge_cb.setOpaque(false);

        dining_cb.setText("Dining Room");
        dining_cb.setOpaque(false);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        suggestion_ta.setColumns(20);
        suggestion_ta.setFont(ClueMain.FONT_14); // NOI18N
        suggestion_ta.setForeground(new java.awt.Color(0, 0, 255));
        suggestion_ta.setRows(5);
        suggestion_ta.setBorder(null);
        suggestion_ta.setOpaque(false);
        suggestion_ta.setEditable(false); //uneditable
        suggestion_ta.setLineWrap(true);
        suggestion_ta.setHighlighter(null); //unselectable

        org.jdesktop.layout.GroupLayout fgPanelLayout = new org.jdesktop.layout.GroupLayout(fgPanel);
        fgPanel.setLayout(fgPanelLayout);
        fgPanelLayout.setHorizontalGroup(
                fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(fgPanelLayout.createSequentialGroup()
                                .add(18, 18, 18)
                                .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(fgPanelLayout.createSequentialGroup()
                                                .add(scarlet_cb)
                                                .add(55, 55, 55)
                                                .add(revolver_cb))
                                        .add(fgPanelLayout.createSequentialGroup()
                                                .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(mustard_cb)
                                                        .add(green_cb)
                                                        .add(white_cb)
                                                        .add(peacock_cb)
                                                        .add(plum_cb))
                                                .add(33, 33, 33)
                                                .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(candlestick_cb)
                                                        .add(rope_cb)
                                                        .add(pipe_cb)
                                                        .add(wrench_cb)
                                                        .add(knife_cb)))
                                        .add(suggestion_ta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 185, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(31, 31, 31)
                                .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                                .add(kitchen_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(study_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(library_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(billiard_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(ballroom_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(conservatory_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(hall_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(lounge_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .add(dining_cb, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(53, Short.MAX_VALUE))
        );
        fgPanelLayout.setVerticalGroup(
                fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(fgPanelLayout.createSequentialGroup()
                                .add(16, 16, 16)
                                .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(fgPanelLayout.createSequentialGroup()
                                                .add(kitchen_cb)
                                                .add(0, 0, 0)
                                                .add(ballroom_cb)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(conservatory_cb)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(billiard_cb)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(library_cb)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(study_cb)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(hall_cb)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(lounge_cb)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(dining_cb)
                                                .add(39, 39, 39)
                                                .add(okButton))
                                        .add(fgPanelLayout.createSequentialGroup()
                                                .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                                        .add(scarlet_cb)
                                                        .add(revolver_cb))
                                                .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(fgPanelLayout.createSequentialGroup()
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(mustard_cb)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(green_cb)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(plum_cb)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(white_cb)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(peacock_cb))
                                                        .add(fgPanelLayout.createSequentialGroup()
                                                                .add(candlestick_cb)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(rope_cb)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(pipe_cb)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(wrench_cb)
                                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                                .add(knife_cb)))
                                                .add(18, 18, 18)
                                                .add(suggestion_ta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 128, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(34, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        bg.add(fgPanel, gridBagConstraints);

        bgLabel.setIcon(ClueMain.getImageIcon("orange-gradient.jpg")); // NOI18N
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
                        .add(bg, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(bg, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    class PickItemListener implements java.awt.event.ItemListener {

        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            okButton.setEnabled(true);
        }
    }

    public void okButtonActionPerformed(java.awt.event.ActionEvent evt) {

        if (scarlet_cb.isSelected()) {
            picked_card = new Card(TYPE_SUSPECT, SUSPECT_SCARLET);
        }
        if (mustard_cb.isSelected()) {
            picked_card = new Card(TYPE_SUSPECT, SUSPECT_MUSTARD);
        }
        if (green_cb.isSelected()) {
            picked_card = new Card(TYPE_SUSPECT, SUSPECT_GREEN);
        }
        if (plum_cb.isSelected()) {
            picked_card = new Card(TYPE_SUSPECT, SUSPECT_PLUM);
        }
        if (white_cb.isSelected()) {
            picked_card = new Card(TYPE_SUSPECT, SUSPECT_WHITE);
        }
        if (peacock_cb.isSelected()) {
            picked_card = new Card(TYPE_SUSPECT, SUSPECT_PEACOCK);
        }

        if (revolver_cb.isSelected()) {
            picked_card = new Card(TYPE_WEAPON, WEAPON_REVOLVER);
        }
        if (candlestick_cb.isSelected()) {
            picked_card = new Card(TYPE_WEAPON, WEAPON_CANDLE);
        }
        if (rope_cb.isSelected()) {
            picked_card = new Card(TYPE_WEAPON, WEAPON_ROPE);
        }
        if (pipe_cb.isSelected()) {
            picked_card = new Card(TYPE_WEAPON, WEAPON_PIPE);
        }
        if (wrench_cb.isSelected()) {
            picked_card = new Card(TYPE_WEAPON, WEAPON_WRENCH);
        }
        if (knife_cb.isSelected()) {
            picked_card = new Card(TYPE_WEAPON, WEAPON_KNIFE);
        }

        if (kitchen_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_KITCHEN);
        }
        if (ballroom_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_BALLROOM);
        }
        if (conservatory_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_CONSERVATORY);
        }
        if (billiard_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_BILLIARD);
        }
        if (library_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_LIBRARY);
        }
        if (study_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_STUDY);
        }
        if (hall_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_HALL);
        }
        if (lounge_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_LOUNGE);
        }
        if (dining_cb.isSelected()) {
            picked_card = new Card(TYPE_ROOM, ROOM_DINING);
        }

        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ArrayList<Card> suggestion;
    private Player player;
    private Card picked_card = null;

    private javax.swing.JRadioButton ballroom_cb;
    private javax.swing.JPanel bg;
    private javax.swing.JLabel bgLabel;
    private javax.swing.JRadioButton billiard_cb;
    private javax.swing.JRadioButton candlestick_cb;
    private javax.swing.JRadioButton conservatory_cb;
    private javax.swing.JRadioButton dining_cb;
    private javax.swing.JPanel fgPanel;
    private javax.swing.JRadioButton green_cb;
    private javax.swing.JRadioButton hall_cb;
    private javax.swing.JRadioButton kitchen_cb;
    private javax.swing.JRadioButton knife_cb;
    private javax.swing.JRadioButton library_cb;
    private javax.swing.JRadioButton lounge_cb;
    private javax.swing.JRadioButton mustard_cb;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton peacock_cb;
    private javax.swing.JRadioButton pipe_cb;
    private javax.swing.JRadioButton plum_cb;
    private javax.swing.JRadioButton revolver_cb;
    private javax.swing.JRadioButton rope_cb;
    private javax.swing.JRadioButton scarlet_cb;
    private javax.swing.JRadioButton study_cb;
    private javax.swing.JRadioButton white_cb;
    private javax.swing.JRadioButton wrench_cb;
    private javax.swing.JTextArea suggestion_ta;
    private javax.swing.ButtonGroup buttonSelectGroup;

    // End of variables declaration//GEN-END:variables
}
