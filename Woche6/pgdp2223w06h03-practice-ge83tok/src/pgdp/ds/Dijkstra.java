package pgdp.ds;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.PriorityQueue;

public final class Dijkstra {

	private Dijkstra() {
	}

	public static void main(String[] args) {
		Graph g = new DenseGraph(7);

		g.addEdge(0, 1);
		g.addEdge(0, 3);
		g.addEdge(0, 2);
		g.addEdge(2, 1);
		g.addEdge(2, 0);
		g.addEdge(2, 4);
		g.addEdge(1, 3);
		g.addEdge(3, 0);
		g.addEdge(3, 4);
		g.addEdge(3, 6);
		g.addEdge(4, 5);
		g.addEdge(5, 3);
		g.addEdge(6, 5);

		System.out.println(Arrays.toString(dijkstra(g, 0, 5)));
	}

	public static int[] dijkstra(Graph g, int s, int t) {

		// init
		PriorityQueue<Pair> q = new PriorityQueue<>();
		Pair[] vert = new Pair[g.getNumberOfNodes()];
		for (int i = 0; i < vert.length; i++) {
			if (i != s) {
				vert[i] = new Pair(i, Integer.MAX_VALUE, -1);
			} else {
				vert[s] = new Pair(s, 0, -1);
				q.add(vert[s]);
			}
		}

		// exec dijkstra
		Pair current;
		while (!q.isEmpty()) {
			current = q.poll();
			if (current.dist() >= vert[t].dist()) {
				break;
			}
			for (int n : g.getAdj(current.node)) {
				if (current.dist() + 1 < vert[n].dist()) {
					q.remove(vert[n]);
					vert[n] = new Pair(n, current.dist() + 1, current.node());
					q.add(vert[n]);
				}
			}
		}

		// if no path exists return empty array
		if (s != t && vert[t].prev() == -1) {
			return new int[0];
		}

		// generate SSP
		LinkedList<Integer> path = new LinkedList<>();
		int c = t;
		while (c != -1) {
			path.addFirst(c);
			c = vert[c].prev();
		}
		return path.stream().mapToInt(x -> x.intValue()).toArray();
	}

	private record Pair(int node, int dist, int prev) implements Comparable<Pair> {

		@Override
		public int compareTo(Pair o) {
			if (dist - o.dist == 0) {
				return node - o.node;
			}
			return dist - o.dist;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Pair other = (Pair) obj;
			return dist == other.dist && node == other.node;
		}

		@Override
		public int hashCode() {
			return Objects.hash(dist, node);
		}

	}

}
