package pgdp.messenger.mock;

import java.time.*;

import pgdp.messenger.*;

public class ListMock extends List {
	private ListElement headMock;
	private ListElement tailMock;
	private int sizeMock;

	public boolean isEmpty() {
		return headMock == null;
	}

	public void insertAt(int index, Message message) {
		if (index > sizeMock || index < 0 || message == null) {
			return;
		}
		if (headMock == null) {
			headMock = tailMock = new ListElement(message, null);
		} else if (index == 0) {
			headMock = new ListElement(message, headMock);
		} else if (index == sizeMock) {
			add(message);
			return;
		} else {
			ListElement prev = null;
			ListElement current = headMock;
			for (int i = 0; i < index; i++) {
				prev = current;
				current = current.getNext();
			}
			prev.setNext(new ListElement(message, current));
		}
		sizeMock++;
	}

	public void add(Message message) {
		if (message == null) {
			return;
		}
		if (tailMock == null) {
			headMock = tailMock = new ListElement(message, null);
		} else {
			tailMock.setNext(new ListElement(message, null));
			tailMock = tailMock.getNext();
		}
		sizeMock++;
	}

	public void delete(Message message) {
		ListElement prev = null;
		ListElement current = headMock;
		while (current != null) {
			if (current.getMessage() == message) {
				if (prev == null) {
					headMock = headMock.getNext();
				} else {
					prev.setNext(current.getNext());
					if (current.getNext() == null) {
						tailMock = prev;
					}
				}
				sizeMock--;
				return;
			}
			prev = current;
			current = current.getNext();
		}
	}

	public int size() {
		return sizeMock;
	}

	public Message getByIndex(int index) {
		if (index >= sizeMock || index < 0) {
			return null;
		}
		ListElement current = headMock;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		return current.getMessage();
	}

	public Message getByID(long id) {
		for (ListElement current = headMock; current != null; current = current.getNext()) {
			if (current.getMessage().getId() == id) {
				return current.getMessage();
			}
		}
		return null;
	}

	public static ListMock megaMerge(ListMock... input) {
		int combinedSize = 0;
		for (ListMock l : input) {
			combinedSize += l.sizeMock;
		}

		ListMock result = new ListMock();
		for (int i = 0; i < combinedSize; i++) {
			Message min = null;
			int index = -1;
			for (int l = 0; l < input.length; l++) {
				if (!input[l].isEmpty() && (min == null
						|| input[l].headMock.getMessage().getTimestamp().isBefore(min.getTimestamp()))) {
					min = input[l].headMock.getMessage();
					index = l;
				}
			}
			input[index].delete(min);
			result.add(min);
		}

		return result;
	}

	public ListMock filterDays(LocalDateTime start, LocalDateTime end) {
		ListMock result = new ListMock();
		if (start != null && end != null && start.isBefore(end)) {
			for (ListElement current = headMock; current != null; current = current.getNext()) {
				LocalDateTime date = current.getMessage().getTimestamp();
				if (date.isEqual(start) || date.isAfter(start) && date.isBefore(end)) {
					result.add(current.getMessage());
				}
			}
		}
		return result;
	}

	public ListMock filterUser(User user) {
		ListMock result = new ListMock();
		for (ListElement current = headMock; current != null; current = current.getNext()) {
			if (current.getMessage().getAuthor() == user) {
				result.add(current.getMessage());
			}
		}
		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ListElement current = headMock; current != null; current = current.getNext()) {
			sb.append(current.getMessage().toString() + "\n");
		}
		return sb.toString();
	}

	public ListElement getHeadMock() {
		return headMock;
	}

	public ListElement getTailMock() {
		return tailMock;
	}

}
