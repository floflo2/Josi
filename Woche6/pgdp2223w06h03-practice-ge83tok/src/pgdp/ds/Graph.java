package pgdp.ds;

public interface Graph {

	int getNumberOfNodes();

	void addEdge(int from, int to);

	boolean isAdj(int from, int to);

	int[] getAdj(int id);

	default String toGraphviz() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph G {\n");
		for (int from = 0; from < getNumberOfNodes(); from++) {
			sb.append(from).append(" [shape=circle]\n");
			for (int to : getAdj(from)) {
				sb.append(from).append(" -> ").append(to).append('\n');
			}
		}
		sb.append("}\n");
		return sb.toString();
	}

}
