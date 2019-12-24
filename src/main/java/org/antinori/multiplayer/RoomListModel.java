package org.antinori.multiplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;

public class RoomListModel extends AbstractListModel {

    private final List<String> roomList;

    public RoomListModel() {
        roomList = new ArrayList();
    }

    public int getSize() {
        return roomList.size();
    }

    public String getElementAt(int index) {
        return roomList.get(index);
    }

    public void setRoomList(Map<Integer, String> rooms) {
        if (roomList.size() > 0) {
            final int lastIndex = roomList.size() - 1;
            roomList.clear();
            fireIntervalRemoved(this, 0, lastIndex);
        }

        for (Map.Entry<Integer, String> e : rooms.entrySet()) {
            roomList.add(e.getValue());
        }
        if (roomList.size() > 0) {
            fireIntervalAdded(this, 0, roomList.size() - 1);
        }
    }

    public void addRoom(String room) {
        roomList.add(room);
        fireIntervalAdded(this, roomList.size() - 1, roomList.size() - 1);
    }

    public void removeRoom(String room) {
        for (int i = 0; i < roomList.size(); i++) {
            if (room.equals(roomList.get(i))) {
                roomList.remove(i);
                fireIntervalRemoved(this, i, i);
                break;
            }
        }
    }

    public void removeRooms() {
        for (int i = 0; i < roomList.size(); i++) {
            roomList.remove(i);
            fireIntervalRemoved(this, i, i);
        }
    }

    public void updateRoom(String room) {
        for (int i = 0; i < roomList.size(); i++) {
            if (room.equals(roomList.get(i))) {
                fireContentsChanged(this, i, i);
                break;
            }
        }
    }
}
