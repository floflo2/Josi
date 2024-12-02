package pgdp.datastructures;

import java.util.ArrayList;
import java.util.List;

public class QuarternaryNode<T extends Comparable<T>> {

	private List<T> values;
	private List<QuarternaryNode<T>> children;
	private int nodeSize;

	public QuarternaryNode(T value) {
		values = new ArrayList<T>(3);
		values.add(value);
		children = new ArrayList<QuarternaryNode<T>>(4);
		for (int i = 0; i < 4; i++) {
			children.add(null);
		}
		nodeSize = value != null ? 1 : 0;
	}

	public T getValue(int i) {
		if (i < 0 || i >= nodeSize) {
			return null;
		}
		return values.get(i);
	}

	public QuarternaryNode<T> getChild(int i) {
		if (i < 0 || i >= nodeSize + 1) {
			return null;
		}
		return children.get(i);
	}

	public List<T> getValues() {
		return values;
	}

	public void setValues(List<T> values) {
		this.values = values;
	}

	public List<QuarternaryNode<T>> getChildren() {
		return children;
	}

	public void setChildren(List<QuarternaryNode<T>> children) {
		this.children = children;
	}

	public void setNodeSize(int nodeSize) {
		this.nodeSize = nodeSize;
	}

	public int getNodeSize() {
		return nodeSize;
	}

	public int height() {
		int height = 0;
		for (QuarternaryNode<T> node : children) {
			if (node != null) {
				int nodeHeight = node.height();
				height = height < nodeHeight ? nodeHeight : height;
			}
		}
		return height + 1;
	}

	public int size() {
		int size = nodeSize;
		for (QuarternaryNode<T> node : children) {
			if (node != null) {
				size += node.size();
			}
		}
		return size;
	}

	public void insert(T value) {
		if (nodeSize < 3) {
			for (int i = 0; i < nodeSize; i++) {
				if (values.get(i).compareTo(value) > 0) {
					values.add(i, value);
					nodeSize++;
					return;
				}
			}
			values.add(value);
			nodeSize++;
		} else {
			for (int i = 0; i < 3; i++) {
				if (values.get(i).compareTo(value) > 0) {
					if (children.get(i) != null) {
						children.get(i).insert(value);
					} else {
						children.set(i, new QuarternaryNode<T>(value));
					}
					return;
				}
			}
			if (children.get(3) != null) {
				children.get(3).insert(value);
			} else {
				children.set(3, new QuarternaryNode<T>(value));
			}
		}
	}

	public boolean contains(T value) {
		for (int i = 0; i < nodeSize; i++) {
			if (values.get(i).compareTo(value) == 0) {
				return true;
			} else if (values.get(i).compareTo(value) > 0) {
				return children.get(i) != null && children.get(i).contains(value);
			}
		}
		return children.get(3) != null && children.get(3).contains(value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < nodeSize; i++) {
			if (children.get(i) != null) {
				sb.append(children.get(i).toString());
			}
			sb.append("- ").append(values.get(i)).append(" -");
		}
		if (children.get(3) != null) {
			sb.append(children.get(3).toString());
		}
		sb.append("]");
		return sb.toString();
	}

	public String toGraphvizStringHelper() {
		StringBuilder sb = new StringBuilder();
		sb.append("subgraph ");
		sb.append(getGraphvizName());
		sb.append(" {\n{rank=same ");
		for (int i = 0; i < nodeSize; i++) {
			sb.append("\"").append(values.get(i)).append("\"");
			if (i != nodeSize - 1) {
				sb.append(" -> ");
			}
		}
		if (nodeSize > 1) {
			sb.append("[style=invis]}");
		} else {
			sb.append("}");
		}
		sb.append("\ncolor=green");
		sb.append("\n}\n");

		for (int i = 0; i <= nodeSize; i++) {
			if (children.get(i) != null) {
				String tmp = children.get(i).toGraphvizStringHelper();
				sb.append("\n").append(tmp).append("\n");
				if (i == nodeSize) {
					sb.append("\"").append(values.get(nodeSize - 1)).append("\"").append(" -> ").append("\"")
							.append(children.get(i).values.get(children.get(i).nodeSize / 2)).append("\"").append("\n");
				} else {
					sb.append("\"").append(values.get(i)).append("\"").append(" -> ").append("\"")
							.append(children.get(i).values.get(children.get(i).nodeSize / 2)).append("\"").append("\n");
				}
			}
		}

		return sb.toString();
	}

	private String getGraphvizName() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"cluster_");
		for (int i = 0; i < nodeSize; i++) {
			sb.append(values.get(i)).append("_");
		}
		sb.append("\"");
		return sb.toString();
	}
}
