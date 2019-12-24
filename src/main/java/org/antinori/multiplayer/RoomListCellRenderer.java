package org.antinori.multiplayer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Room list custom ListCellRenderer.
 */
public class RoomListCellRenderer extends JLabel implements ListCellRenderer {

    public RoomListCellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String room = (String) value;

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setText(room);// + "(" + room.getUserCount() + "/" + room.getMaxUsers() + ")");

        return this;
    }
}
