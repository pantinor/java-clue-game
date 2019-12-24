package org.antinori.multiplayer;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

public class UserListModel extends AbstractListModel {

    private final List<String> userList;

    public UserListModel() {
        userList = new ArrayList<>();
    }

    public int getSize() {
        return userList.size();
    }

    public String getElementAt(int index) {
        return userList.get(index);
    }

    public void setUserList(List<String> users) {
        if (userList.size() > 0) {
            final int lastIndex = userList.size() - 1;
            userList.clear();
            fireIntervalRemoved(this, 0, lastIndex);
        }

        for (String e : users) {
            userList.add(e);
        }
        if (userList.size() > 0) {
            fireIntervalAdded(this, 0, userList.size() - 1);
        }
    }

    public void addUser(String user) {
        userList.add(user);
        fireIntervalAdded(this, userList.size() - 1, userList.size() - 1);
    }

    public void removeUser(String userId) {
        for (int i = 0; i < userList.size(); i++) {
            if (userId.equals(userList.get(i))) {
                userList.remove(i);
                fireIntervalRemoved(this, i, i);
                break;
            }
        }
    }

    public void removeUsers() {
        for (int i = 0; i < userList.size(); i++) {
            userList.remove(i);
            fireIntervalRemoved(this, i, i);
        }
    }
}
