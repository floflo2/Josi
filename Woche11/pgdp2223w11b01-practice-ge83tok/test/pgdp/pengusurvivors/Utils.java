package pgdp.pengusurvivors;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicField;
import pgdp.pengusurvivors.FlowGraph.Edge;
import pgdp.pengusurvivors.FlowGraph.Vertex;

public class Utils {

	public static Map<Vertex, Vertex> copyGraph(FlowGraph g, FlowGraph copy) {
		Map<Vertex, Vertex> map = new HashMap<>();
		for (Vertex v : g.getVertices()) {
			map.put(v, copy.addVertex());
		}
		for (Vertex v : g.getVertices()) {
			v.getSuccessors().stream().forEach(suc -> {
				Edge e = map.get(v).addEdge(map.get(suc), v.getEdge(suc).getCapacity());
				setFlow(e, v.getEdge(suc).getFlow());
			});
			v.getResSuccessors().stream().forEach(suc -> {
				Edge e = map.get(v).addResEdge(map.get(suc), v.getResEdge(suc).getCapacity());
				setFlow(e, v.getResEdge(suc).getFlow());
			});
		}
		return map;
	}

	public static void setCapacity(Edge e, int c) {
		DynamicClass<?> sclazz = new DynamicClass<>("pgdp.pengusurvivors.FlowGraph$Edge");
		DynamicField<?> cfield = sclazz.field(int.class, "c");
		try {
			cfield.toField().setInt(e, c);
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
			fail("Unable to set edge capacity.");
		}
	}

	public static void setFlow(Edge e, int f) {
		DynamicClass<?> sclazz = new DynamicClass<>("pgdp.pengusurvivors.FlowGraph$Edge");
		DynamicField<?> ffield = sclazz.field(int.class, "f");
		try {
			ffield.toField().setInt(e, f);
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
			fail("Unable to set edge flow.");
		}
	}

	public static void computeMaxFlow(FlowGraph g) {
		generateResidualGraph(g);
		List<Vertex> augPath;
		while ((augPath = findPathInResidual(g)) != null) {
			int augFlow = calcAugmentingFlow(augPath);
			updateNetwork(g, augPath, augFlow);
		}
	}

	public static int computeMaxFlowValue(FlowGraph g) {
		computeMaxFlow(g);
		return g.getSource().getSuccessors().stream().map(suc -> {
			return g.getSource().getEdge(suc);
		}).mapToInt(Edge::getFlow).sum();
	}

	public static void generateResidualGraph(FlowGraph g) {
		g.clearResidualGraph();
		LinkedList<Vertex> q = new LinkedList<>();
		q.add(g.getSource());
		Vertex current;
		while (!q.isEmpty()) {
			current = q.pop();
			for (Vertex v : current.getSuccessors()) {
				if (v.getResSuccessors().isEmpty()) {
					q.add(v);
				}
				current.addResEdge(v, current.getEdge(v).getCapacity());
				v.addResEdge(current, 0);
			}
		}
	}

	public static List<Vertex> findPathInResidual(FlowGraph g) {
		LinkedList<Vertex> q = new LinkedList<>();
		Map<Vertex, Vertex> prev = new HashMap<>();
		Set<Vertex> visited = new HashSet<>();
		Vertex current;
		q.push(g.getSource());
		visited.add(g.getSource());
		while (!q.isEmpty()) {
			current = q.poll();

			for (Vertex suc : current.getResSuccessors()) {
				if (current.getResEdge(suc).getCapacity() > 0 && suc.equals(g.getSink())) {
					prev.put(suc, current);
					return constructPath(g, prev);
				}
				if (!visited.contains(suc) && current.getResEdge(suc).getCapacity() > 0) {
					q.push(suc);
					visited.add(suc);
					prev.put(suc, current);
				}
			}
		}
		return null;
	}

	private static List<Vertex> constructPath(FlowGraph g, Map<Vertex, Vertex> prev) {
		LinkedList<Vertex> path = new LinkedList<>();
		Vertex current = g.getSink();
		path.add(g.getSink());
		while (prev.containsKey(current)) {
			path.add(0, prev.get(current));
			current = prev.get(current);
		}
		return path;
	}

	public static int calcAugmentingFlow(List<Vertex> path) {
		int f = Integer.MAX_VALUE;
		for (int i = 0; i < path.size() - 1; i++) {
			Edge e = path.get(i).getResEdge(path.get(i + 1));
			f = Math.min(f, e.getCapacity() - e.getFlow());
		}
		return f;
	}

	public static void updateNetwork(FlowGraph g, List<Vertex> path, int f) {
		for (int i = 0; i < path.size() - 1; i++) {
			Vertex from = path.get(i);
			Vertex to = path.get(i + 1);
			Utils.setCapacity(from.getResEdge(to), from.getResEdge(to).getCapacity() - f);
			Utils.setCapacity(to.getResEdge(from), to.getResEdge(from).getCapacity() + f);
			if (from.hasSuccessor(to)) {
				Utils.setFlow(from.getEdge(to), from.getEdge(to).getFlow() + f);
			} else {
				Utils.setFlow(to.getEdge(from), to.getEdge(from).getFlow() - f);
			}
		}
	}
}
