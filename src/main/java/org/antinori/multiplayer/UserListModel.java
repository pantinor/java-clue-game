package org.antinori.multiplayer;

import sfs2x.client.entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractListModel;

/**
 * User list model
 */
public class UserListModel extends AbstractListModel {
	private final List<User> userList;

	public UserListModel() {
		userList = new ArrayList<User>();
	}

	public int getSize() {
		return userList.size();
	}

	public User getElementAt(int index) {
		return userList.get(index);
	}

	public void setUserList(List<User> users) {
		if (userList.size() > 0) {
			final int lastIndex = userList.size() - 1;
			userList.clear();
			fireIntervalRemoved(this, 0, lastIndex);
		}

		for (User e : users) {
			userList.add(e);
		}
		if (userList.size() > 0) {
			fireIntervalAdded(this, 0, userList.size() - 1);
		}
	}

	public void addUser(User user) {
		userList.add(user);
		fireIntervalAdded(this, userList.size() - 1, userList.size() - 1);
	}

	public void removeUser(int userId) {
		for (int i = 0; i < userList.size(); i++) {
			if (userId == userList.get(i).getId()) {
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
