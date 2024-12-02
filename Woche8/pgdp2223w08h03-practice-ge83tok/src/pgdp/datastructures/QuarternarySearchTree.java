package pgdp.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class QuarternarySearchTree<T extends Comparable<T>> implements Iterable<T> {
	private QuarternaryNode<T> root;

	public QuarternarySearchTree() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public int size() {
		return root == null ? 0 : root.size();
	}

	public boolean contains(T value) {
		if (isEmpty()) {
			return false;
		}
		return root.contains(value);
	}

	public void insert(T value) {
		if (value == null) {
			return;
		}
		if (isEmpty()) {
			root = new QuarternaryNode<T>(value);
		} else {
			root.insert(value);
		}
	}

	@Override
	public String toString() {
		if (isEmpty()) {
			return "[]";
		} else {
			return root.toString();
		}
	}

	public String toGraphvizString() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {\n");
		sb.append(root == null ? "" : root.toGraphvizStringHelper());
		sb.append("}");
		return sb.toString();
	}

	public QuarternaryNode<T> getRoot() {
		return root;
	}

	public void setRoot(QuarternaryNode<T> root) {
		this.root = root;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO
		// Throw exception using the following line of code
		// throw new NoSuchElementException("Trying to call next on an empty QuarternarySearchTreeIterator");
		return null;
	}

	public static void main(String[] args) {
		int[] values = new int[] { 8, 4, 12, 1, 5, 9, 13, 3, 7, 11, 15, 2, 6, 10, 14 };
		QuarternarySearchTree<Integer> n = new QuarternarySearchTree<Integer>();
		for (int i : values) {
			n.insert(i);
		}
		System.out.println(n.toGraphvizString());

		// uncomment after implementing the iterator for testing the large example
		//		for (int i : n) {
		//			System.out.print(i + " - ");
		//		}
	}
}
