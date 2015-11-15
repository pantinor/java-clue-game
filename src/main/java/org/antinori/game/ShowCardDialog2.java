package org.antinori.game;

import java.awt.Dialog;

public class ShowCardDialog2 extends javax.swing.JDialog {

    String text;

    public ShowCardDialog2(String text) {

        super(ClueMain.frame, true);

        this.text = text;

        initComponents();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        ClueMain.setLocationInCenter(this, -200, -200);
        setUndecorated(true);

        bg = new javax.swing.JPanel();
        fgPanel = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();
        okButton = new javax.swing.JButton();
        bgLabel = new javax.swing.JLabel();

        bg.setMinimumSize(new java.awt.Dimension(1, 1));
        bg.setLayout(new java.awt.GridBagLayout());

        fgPanel.setBackground(java.awt.SystemColor.info);
        fgPanel.setPreferredSize(new java.awt.Dimension(340, 223));

        jTextArea1.setBackground(java.awt.SystemColor.info);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Berlin Sans FB Demi", 0, 18)); // NOI18N
        jTextArea1.setLineWrap(true);

        jTextArea1.setEditable(false); //uneditable
        jTextArea1.setHighlighter(null); //unselectable

        jTextArea1.setRows(5);
        jTextArea1.setText(text);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout fgPanelLayout = new org.jdesktop.layout.GroupLayout(fgPanel);
        fgPanel.setLayout(fgPanelLayout);
        fgPanelLayout.setHorizontalGroup(
                fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(fgPanelLayout.createSequentialGroup()
                        .add(fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(fgPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .add(jTextArea1))
                                .add(fgPanelLayout.createSequentialGroup()
                                        .add(108, 108, 108)
                                        .add(okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
        );
        fgPanelLayout.setVerticalGroup(
                fgPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(fgPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jTextArea1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 168, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(okButton)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bg.add(fgPanel, new java.awt.GridBagConstraints());

        bgLabel.setIcon(ClueMain.getImageIcon("showcardframe.png")); // NOI18N

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

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JLabel bgLabel;
    private javax.swing.JPanel fgPanel;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables

}
