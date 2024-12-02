package pgdp.messenger.mock;

import pgdp.messenger.*;

public class UserArrayMock extends UserArray {
	private User[] users;
	private int numberOfUsers;

	public UserArrayMock(int initCapacity) {
		super(initCapacity);
		users = new User[initCapacity < 1 ? 1 : initCapacity];
	}

	public void addUser(User user) {
		if (user == null) {
			return;
		}
		for (int i = 0; i < users.length; i++) {
			if (users[i] == null) {
				users[i] = user;
				numberOfUsers++;
				return;
			}
		}
		User[] temp = new User[users.length * 2];
		for (int i = 0; i < users.length; i++) {
			temp[i] = users[i];
		}
		users = temp;
		users[numberOfUsers++] = user;
	}

	public User deleteUser(long id) {
		for (int i = 0; i < users.length; i++) {
			if (users[i] != null && users[i].getId() == id) {
				User temp = users[i];
				users[i] = null;
				numberOfUsers--;
				return temp;
			}
		}
		return null;
	}

	public int size() {
		return numberOfUsers;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}

	public User[] getUsers() {
		return users;
	}

	public int getIndex(long id) {
		for (int i = 0; i < users.length; i++) {
			if (users[i] != null && users[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}
}
