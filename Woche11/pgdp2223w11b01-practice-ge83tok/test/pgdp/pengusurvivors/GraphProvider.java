package pgdp.pengusurvivors;

import pgdp.pengusurvivors.FlowGraph.Vertex;

public class GraphProvider {

	private static void initVertices(FlowGraph g, Vertex[] v) {
		for (int i = 0; i < v.length; i++) {
			v[i] = g.addVertex();
		}
	}

	public static Vertex[] linearChain(FlowGraph g) {
		Vertex[] v = new Vertex[12];
		initVertices(g, v);
		for (int i = 0; i < v.length - 1; i++) {
			v[i].addEdge(v[i + 1], 100 - i);
		}
		g.setSource(v[0]);
		g.setSink(v[v.length - 1]);
		return v;
	}

	public static Vertex[] diamond(FlowGraph g) {
		Vertex[] v = new Vertex[4];
		initVertices(g, v);
		v[0].addEdge(v[1], 1);
		v[0].addEdge(v[2], 2);
		v[1].addEdge(v[3], 2);
		v[2].addEdge(v[1], 1);
		v[2].addEdge(v[3], 1);
		g.setSource(v[0]);
		g.setSink(v[3]);
		return v;
	}

	public static Vertex[] limiter(FlowGraph g) {
		Vertex[] v = new Vertex[8];
		initVertices(g, v);
		v[0].addEdge(v[1], 69);
		v[0].addEdge(v[2], 420);
		v[1].addEdge(v[3], 187);
		v[2].addEdge(v[3], 1337);
		v[3].addEdge(v[4], 13);
		v[4].addEdge(v[5], 121);
		v[4].addEdge(v[6], 42);
		v[5].addEdge(v[7], 11);
		v[6].addEdge(v[7], 550);
		g.setSource(v[0]);
		g.setSink(v[7]);
		return v;
	}

	public static Vertex[] zigzag(FlowGraph g) {
		Vertex[] v = new Vertex[6];
		initVertices(g, v);
		v[0].addEdge(v[1], 1);
		v[0].addEdge(v[2], 10);
		v[1].addEdge(v[4], 6);
		v[2].addEdge(v[3], 6);
		v[3].addEdge(v[1], 10);
		v[3].addEdge(v[5], 1);
		v[4].addEdge(v[2], 10);
		v[4].addEdge(v[5], 10);
		g.setSource(v[0]);
		g.setSink(v[5]);
		return v;
	}
}
