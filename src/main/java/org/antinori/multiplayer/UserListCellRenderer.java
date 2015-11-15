package org.antinori.multiplayer;

import sfs2x.client.entities.User;
import sfs2x.client.entities.variables.UserVariable;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * User list custom ListCellRenderer.
 */
public class UserListCellRenderer extends JLabel implements ListCellRenderer {

    public UserListCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        User user = (User) value;

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        UserVariable ch = user.getVariable("character");

        setText(user.getName() + " (" + (ch == null ? "none" : ch.getStringValue()) + ") ");

        return this;
    }
}
