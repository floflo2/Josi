package pgdp.pengusurvivors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import pgdp.pengusurvivors.FlowGraph.Edge;
import pgdp.pengusurvivors.FlowGraph.Vertex;

@TestClassAnnotation
public class FlowGraphBehaviorTest {

	void testResidualGeneration(Function<FlowGraph, ?> graphGenerator) {
		FlowGraph g = new FlowGraph();
		graphGenerator.apply(g);
		g.generateResidualGraph();
		for (Vertex v : g.getVertices()) {
			for (Vertex to : v.getSuccessors()) {
				assertEquals(v.getEdge(to).getCapacity(), v.getResEdge(to).getCapacity());
				assertEquals(0, to.getResEdge(v).getCapacity());
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "generateResidual - linear")
	@Order(1)
	void testGenerateResidualLinear() {
		testResidualGeneration(GraphProvider::linearChain);
	}

	@HiddenTest
	@DisplayName(value = "generateResidual - diamond")
	@Order(1)
	void testGenerateResidualDiamond() {
		testResidualGeneration(GraphProvider::diamond);
	}

	@HiddenTest
	@DisplayName(value = "generateResidual - limiter")
	@Order(1)
	void testGenerateResidualLimiter() {
		testResidualGeneration(GraphProvider::limiter);
	}

	@HiddenTest
	@DisplayName(value = "generateResidual - zigzag")
	@Order(1)
	void testGenerateResidualZigzag() {
		testResidualGeneration(GraphProvider::zigzag);
	}

	@PublicTest
	@DisplayName(value = "generateResidual - Summary")
	@Order(2)
	void testGenerateResidualSummary() {
		try {
			testGenerateResidualLinear();
			testGenerateResidualDiamond();
			testGenerateResidualLimiter();
			testGenerateResidualZigzag();
		} catch (Throwable t) {
			fail("At least one of the required tests for generateResidual failed.");
		}
	}

	@HiddenTest
	@DisplayName(value = "findPathInResidual - linear")
	@Order(3)
	void testFindResPathLinear() {
		FlowGraph g = new FlowGraph();
		Vertex[] v = GraphProvider.linearChain(g);
		Utils.generateResidualGraph(g);
		List<Vertex> path = Arrays.stream(v).toList();
		assertEquals(path, g.findPathInResidual());
		Utils.updateNetwork(g, path, 50);
		assertEquals(path, g.findPathInResidual());
		Utils.updateNetwork(g, path, 40);
		assertNull(g.findPathInResidual());
	}

	@HiddenTest
	@DisplayName(value = "findPathInResidual - diamond")
	@Order(3)
	void testFindResPathDiamond() {
		FlowGraph g = new FlowGraph();
		Vertex[] v = GraphProvider.diamond(g);
		Utils.generateResidualGraph(g);
		List<Vertex> toppath = List.of(v[0], v[1], v[3]);
		List<Vertex> midpath = List.of(v[0], v[2], v[1], v[3]);
		List<Vertex> botpath = List.of(v[0], v[2], v[3]);
		assertThat(g.findPathInResidual(), anyOf(is(toppath), is(midpath), is(botpath)));
		Utils.updateNetwork(g, midpath, 1);
		assertThat(g.findPathInResidual(), anyOf(is(toppath), is(botpath)));
		Utils.updateNetwork(g, toppath, 1);
		assertThat(g.findPathInResidual(), is(botpath));
		Utils.updateNetwork(g, botpath, 1);
		assertNull(g.findPathInResidual());
	}

	@HiddenTest
	@DisplayName(value = "findPathInResidual - limiter")
	@Order(3)
	void testFindResPathLimiter() {
		FlowGraph g = new FlowGraph();
		Vertex[] v = GraphProvider.limiter(g);
		Utils.generateResidualGraph(g);
		List<Vertex> toptop = List.of(v[0], v[1], v[3], v[4], v[5], v[7]);
		List<Vertex> topbot = List.of(v[0], v[1], v[3], v[4], v[6], v[7]);
		List<Vertex> bottop = List.of(v[0], v[2], v[3], v[4], v[5], v[7]);
		List<Vertex> botbot = List.of(v[0], v[2], v[3], v[4], v[6], v[7]);
		assertThat(g.findPathInResidual(), anyOf(is(toptop), is(topbot), is(bottop), is(botbot)));
		Utils.updateNetwork(g, toptop, 11);
		assertThat(g.findPathInResidual(), anyOf(is(topbot), is(botbot)));
		Utils.updateNetwork(g, botbot, 2);
		assertNull(g.findPathInResidual());
	}

	@HiddenTest
	@DisplayName(value = "findPathInResidual - zigzag")
	@Order(3)
	void testFindResPathZigzag() {
		FlowGraph g = new FlowGraph();
		Vertex[] v = GraphProvider.zigzag(g);
		Utils.generateResidualGraph(g);
		List<Vertex> topf = List.of(v[0], v[1], v[4], v[5]);
		List<Vertex> topb = List.of(v[0], v[1], v[4], v[2], v[3], v[5]);
		List<Vertex> botf = List.of(v[0], v[2], v[3], v[5]);
		List<Vertex> botb = List.of(v[0], v[2], v[3], v[1], v[4], v[5]);
		assertThat(g.findPathInResidual(), anyOf(is(topf), is(topb), is(botf), is(botb)));
		Utils.updateNetwork(g, botf, 1);
		assertThat(g.findPathInResidual(), anyOf(is(topf), is(botb)));
		Utils.updateNetwork(g, topf, 1);
		assertThat(g.findPathInResidual(), is(botb));
		Utils.updateNetwork(g, botb, 5);
		assertNull(g.findPathInResidual());
	}

	@PublicTest
	@DisplayName(value = "findPathInResidual - Summary")
	@Order(4)
	void testFindResPathSummary() {
		try {
			testFindResPathLinear();
			testFindResPathDiamond();
			testFindResPathLimiter();
			testFindResPathZigzag();
		} catch (Throwable t) {
			fail("At least one of the required tests for findPathInResidual failed.");
		}
	}

	@HiddenTest
	@DisplayName(value = "calcAugmentingFlow - linear")
	@Order(5)
	void testCalcAugmFlowLinear() {
		FlowGraph g = new FlowGraph();
		Vertex[] v = GraphProvider.linearChain(g);
		Utils.generateResidualGraph(g);
		List<Vertex> path = Arrays.stream(v).toList();
		assertEquals(90, g.calcAugmentingFlow(path));
	}

	@HiddenTest
	@DisplayName(value = "calcAugmentingFlow - diamond")
	@Order(5)
	void testCalcAugmFlowDiamond() {
		FlowGraph g = new FlowGraph();
		Vertex[] v = GraphProvider.diamond(g);
		Utils.generateResidualGraph(g);
		List<Vertex> toppath = List.of(v[0], v[1], v[3]);
		List<Vertex> midpath = List.of(v[0], v[2], v[1], v[3]);
		List<Vertex> botpath = List.of(v[0], v[2], v[3]);
		assertEquals(1, g.calcAugmentingFlow(midpath));
		Utils.updateNetwork(g, midpath, 1);
		assertEquals(1, g.calcAugmentingFlow(toppath));
		Utils.updateNetwork(g, toppath, 1);
		assertEquals(1, g.calcAugmentingFlow(botpath));
	}

	@HiddenTest
	@DisplayName(value = "calcAugmentingFlow - limiter")
	@Order(5)
	void testCalcAugmFlowLimiter() {
		FlowGraph g = new FlowGraph();
		Vertex[] v = GraphProvider.limiter(g);
		Utils.generateResidualGraph(g);
		List<Vertex> toptop = List.of(v[0], v[1], v[3], v[4], v[5], v[7]);
		List<Vertex> botbot = List.of(v[0], v[2], v[3], v[4], v[6], v[7]);
		assertEquals(11, g.calcAugmentingFlow(toptop));
		Utils.updateNetwork(g, toptop, 11);
		assertEquals(2, g.calcAugmentingFlow(botbot));
	}

	@HiddenTest
	@DisplayName(value = "calcAugmentingFlow - zigzag")
	@Order(5)
	void testCalcAugmFlowZigzag() {
		FlowGraph g = new FlowGraph();
		Vertex[] v = GraphProvider.zigzag(g);
		Utils.generateResidualGraph(g);
		List<Vertex> topf = List.of(v[0], v[1], v[4], v[5]);
		List<Vertex> botf = List.of(v[0], v[2], v[3], v[5]);
		List<Vertex> botb = List.of(v[0], v[2], v[3], v[1], v[4], v[5]);
		assertEquals(1, g.calcAugmentingFlow(botf));
		Utils.updateNetwork(g, botf, 1);
		assertEquals(1, g.calcAugmentingFlow(topf));
		Utils.updateNetwork(g, topf, 1);
		assertEquals(5, g.calcAugmentingFlow(botb));
	}

	@PublicTest
	@DisplayName(value = "calcAugmentingFlow - Summary")
	@Order(6)
	void testCalcAugmFlowSummary() {
		try {
			testCalcAugmFlowLinear();
			testCalcAugmFlowDiamond();
			testCalcAugmFlowLimiter();
			testCalcAugmFlowZigzag();
		} catch (Throwable t) {
			fail("At least one of the required tests for calcAugmentingFlow failed.");
		}
	}

	private void testUpdateNetworkSequence(Function<FlowGraph, Vertex[]> graphGenerator, int[][] paths, int[] flows) {
		FlowGraph g = new FlowGraph();
		Vertex[] v = graphGenerator.apply(g);
		Utils.generateResidualGraph(g);
		for (int i = 0; i < paths.length; i++) {
			testUpdateNetwork(g, Arrays.stream(paths[i]).mapToObj(j -> {
				return v[j];
			}).toList(), flows[i]);
		}
	}

	private void testUpdateNetwork(FlowGraph g, List<Vertex> path, int flow) {
		FlowGraph oracle = new FlowGraph();
		Map<Vertex, Vertex> map = Utils.copyGraph(g, oracle);
		Utils.updateNetwork(oracle, path.stream().map(map::get).toList(), flow);
		g.updateNetwork(path, flow);
		for (Vertex v : g.getVertices()) {
			final Vertex o = map.get(v);
			assertEquals(o.getSuccessors().size(), v.getSuccessors().size());
			assertFalse(v.getSuccessors().stream().filter(suc -> {
				Edge e = v.getEdge(suc);
				Edge oe = o.getEdge(map.get(suc));
				return e.getCapacity() != oe.getCapacity() || e.getFlow() != oe.getFlow();
			}).findAny().isPresent());
			assertEquals(o.getResSuccessors().size(), v.getResSuccessors().size());
			assertFalse(v.getResSuccessors().stream().filter(suc -> {
				Edge e = v.getResEdge(suc);
				Edge oe = o.getResEdge(map.get(suc));
				return e.getCapacity() != oe.getCapacity() || e.getFlow() != oe.getFlow();
			}).findAny().isPresent());
		}
	}

	@HiddenTest
	@DisplayName(value = "updateNetwork - linear")
	@Order(7)
	void testUpdateNetworkLinear() {
		int[] path = IntStream.range(0, 12).toArray();
		int[][] paths = { path, path };
		int[] flows = { 50, 40 };
		testUpdateNetworkSequence(GraphProvider::linearChain, paths, flows);
	}

	@HiddenTest
	@DisplayName(value = "updateNetwork - diamond")
	@Order(7)
	void testUpdateNetworkDiamond() {
		int[] toppath = { 0, 1, 3 };
		int[] midpath = { 0, 2, 1, 3 };
		int[] botpath = { 0, 2, 3 };
		int[][] paths = { midpath, toppath, botpath };
		int[] flows = { 1, 1, 1 };
		testUpdateNetworkSequence(GraphProvider::diamond, paths, flows);
	}

	@HiddenTest
	@DisplayName(value = "updateNetwork - limiter")
	@Order(7)
	void testUpdateNetworkLimiter() {
		int[] toptop = { 0, 1, 3, 4, 5, 7 };
		int[] botbot = { 0, 2, 3, 4, 6, 7 };
		int[][] paths = { toptop, botbot };
		int[] flows = { 11, 2 };
		testUpdateNetworkSequence(GraphProvider::limiter, paths, flows);
	}

	@HiddenTest
	@DisplayName(value = "updateNetwork - zigzag")
	@Order(7)
	void testUpdateNetworkZigzag() {
		int[] botf = { 0, 2, 3, 5 };
		int[] topf = { 0, 1, 4, 5 };
		int[] botb = { 0, 2, 3, 1, 4, 5 };
		int[][] paths = { botf, topf, botb };
		int[] flows = { 1, 1, 5 };
		testUpdateNetworkSequence(GraphProvider::zigzag, paths, flows);
	}

	@PublicTest
	@DisplayName(value = "updateNetwork - Summary")
	@Order(8)
	void testUpdateNetworkSummary() {
		try {
			testUpdateNetworkLinear();
			testUpdateNetworkDiamond();
			testUpdateNetworkLimiter();
			testUpdateNetworkZigzag();
		} catch (Throwable t) {
			fail("At least one of the required tests for updateNetwork failed.");
		}
	}

	private void testCompMaxFlowValue(Function<FlowGraph, Vertex[]> graphGenerator, int[][] paths, int[] flows,
			int expflow) {
		FlowGraph g = new FlowGraph() {
			@Override
			public void computeMaxFlow() {
			}

			@Override
			public void clearResidualGraph() {
			}

			@Override
			public void generateResidualGraph() {
			}

			@Override
			public List<Vertex> findPathInResidual() {
				return null;
			}

			@Override
			public int calcAugmentingFlow(List<Vertex> path) {
				return 0;
			}

			@Override
			public void updateNetwork(List<Vertex> path, int f) {
			}
		};
		Vertex[] v = graphGenerator.apply(g);
		Utils.generateResidualGraph(g);
		for (int i = 0; i < paths.length; i++) {
			List<Vertex> path = Arrays.stream(paths[i]).mapToObj(j -> {
				return v[j];
			}).toList();
			Utils.updateNetwork(g, path, flows[i]);
		}
		assertEquals(expflow, g.computeMaxFlowValue());
	}

	@HiddenTest
	@DisplayName(value = "computeMaxFlowValue - linear")
	@Order(9)
	void testCompMaxFlowValueLinear() {
		int[] path = IntStream.range(0, 12).toArray();
		int[][] paths = { path };
		int[] flows = { 90 };
		testCompMaxFlowValue(GraphProvider::linearChain, paths, flows, 90);
	}

	@HiddenTest
	@DisplayName(value = "computeMaxFlowValue - diamond")
	@Order(9)
	void testCompMaxFlowValueDiamond() {
		int[] toppath = { 0, 1, 3 };
		int[] midpath = { 0, 2, 1, 3 };
		int[] botpath = { 0, 2, 3 };
		int[][] paths = { midpath, toppath, botpath };
		int[] flows = { 1, 1, 1 };
		testCompMaxFlowValue(GraphProvider::diamond, paths, flows, 3);
	}

	@HiddenTest
	@DisplayName(value = "computeMaxFlowValue - limiter")
	@Order(9)
	void testCompMaxFlowValueLimiter() {
		int[] toptop = { 0, 1, 3, 4, 5, 7 };
		int[] botbot = { 0, 2, 3, 4, 6, 7 };
		int[][] paths = { toptop, botbot };
		int[] flows = { 11, 2 };
		testCompMaxFlowValue(GraphProvider::limiter, paths, flows, 13);
	}

	@HiddenTest
	@DisplayName(value = "computeMaxFlowValue - zigzag")
	@Order(9)
	void testCompMaxFlowValueZigzag() {
		int[] botf = { 0, 2, 3, 5 };
		int[] topf = { 0, 1, 4, 5 };
		int[] botb = { 0, 2, 3, 1, 4, 5 };
		int[][] paths = { botf, topf, botb };
		int[] flows = { 1, 1, 5 };
		testCompMaxFlowValue(GraphProvider::zigzag, paths, flows, 7);
	}

	@PublicTest
	@DisplayName(value = "computeMaxFlowValue - Summary")
	@Order(10)
	void testCompMaxFlowValueSummary() {
		try {
			testCompMaxFlowValueLinear();
			testCompMaxFlowValueDiamond();
			testCompMaxFlowValueLimiter();
			testCompMaxFlowValueZigzag();
		} catch (Throwable t) {
			fail("At least one of the required tests for computeMaxFlowValue failed.");
		}
	}

	@HiddenTest
	@DisplayName(value = "generateModel - simple")
	@Order(11)
	void testGenerateModelSimple() {
		FlowGraph g = PenguSurvivors.generateModel(IntStream.range(0, 10).toArray(),
				IntStream.range(100, 110).toArray(), IntStream.range(0, 10).mapToObj(i -> {
					return new int[] { i, i };
				}).toArray(int[][]::new));
		assertEquals(10, Utils.computeMaxFlowValue(g));
	}

	@HiddenTest
	@DisplayName(value = "generateModel - advanced")
	@Order(11)
	void testGenerateModelAdvanced() {
		int[] workaholics = IntStream.range(0, 6).toArray();
		int[] procrastinators = IntStream.range(100, 108).toArray();
		int[][] friendships = { { 0, 1 }, { 1, 0 }, { 2, 1 }, { 3, 3 }, { 3, 5 }, { 4, 6 }, { 5, 6 }, { 5, 7 } };
		FlowGraph g = PenguSurvivors.generateModel(workaholics, procrastinators, friendships);
		assertEquals(5, Utils.computeMaxFlowValue(g));
	}

	@PublicTest
	@DisplayName(value = "generateModel - Summary")
	@Order(10)
	void testGenerateModelSummary() {
		try {
			testGenerateModelSimple();
			testGenerateModelAdvanced();
		} catch (Throwable t) {
			fail("At least one of the required tests for generateModel failed.");
		}
	}

}
