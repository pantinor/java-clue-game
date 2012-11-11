package org.antinori.multiplayer;

import sfs2x.client.entities.Room;
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
		Room room = (Room) value;

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		setText(room.getName());// + "(" + room.getUserCount() + "/" + room.getMaxUsers() + ")");

		return this;
	}
}
