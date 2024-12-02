package pgdp.pingulib.datastructures.lists;

public class List {

	private Element head;
	private Element tail;
	private int size;

	List() {
		// TODO
	}

	/*
	 * returns size/length of the list
	 */
	public int size() {
		return size;
	}

	/*
	 * returns <true> if the list is empty, otherwise <false>
	 */
	public boolean isEmpty() {
		// TODO
		return false;
	}

	/*
	 * removes all elements from the list
	 */
	public void clear() {
		// TODO
	}

	/*
	 * adds an element at the end of the list
	 */
	public void add(int element) {
		// TODO
	}

	/*
	 * adds an element at the specified index
	 */
	public boolean add(int index, int element) {
		// TODO
		return false;
	}

	/*
	 * returns the value of the element at the specified index returns default value
	 * (minimum value of an integer) iff. such an element does not exist.
	 */
	public int get(int index) {
		// TODO
		return 0;
	}

	/*
	 * removes the element at the specified index
	 */
	public void remove(int index) {
		// TODO
	}

	/*
	 * returns String representation of the list
	 */
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append("[ ");
		Element current = head;
		for (int i = 0; i < size; i++) {
			out.append(current.toString());
			if (i != size - 1) {
				out.append(", ");
			}
			current = current.next;
		}
		out.append(" ]");
		return out.toString();
	}

	private static class Element {
		private int value;
		private Element next;

		Element(int value) {
			// TODO
		}

		Element(int value, Element next) {
			// TODO
		}

		/*
		 * returns String representation of the element
		 */
		@Override
		public String toString() {
			return "" + value;
		}
	}

}