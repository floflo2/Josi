package pgdp.trials;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.jupiter.HiddenTest;
import pgdp.trials.TrialOfTheMountains.Mountain;

@TestClassAnnotation
public class TrialOfTheMountainsBehaviorTest {

	private static void test(Mountain[] mountains) {
		Map<Long, Set<Long>> g = parseGraph(mountains);
		TrialOfTheMountains.assignBeasts(mountains);
		checkModified(mountains, g);
		allAssigned(mountains);
		checkNumberOfSpecies(mountains);
		isValidAssignment(mountains);
	}

	private static Map<Long, Set<Long>> parseGraph(Mountain[] mountains) {
		HashMap<Long, Set<Long>> g = new HashMap<>();
		for (Mountain m : mountains) {
			g.put(m.getLabel(), m.getNeighbours().stream().map(Mountain::getLabel).collect(Collectors.toSet()));
		}
		return g;
	}

	private static void checkModified(Mountain[] mountains, Map<Long, Set<Long>> g) {
		for (Mountain m : mountains) {
			assertEquals(g.get(m.getLabel()),
					m.getNeighbours().stream().map(Mountain::getLabel).collect(Collectors.toSet()),
					"The graph was modified.");
		}
	}

	private static void allAssigned(Mountain[] mountains) {
		assertEquals(0,
				Arrays.stream(mountains).map(Mountain::getBeast).filter(Predicate.not(Objects::nonNull)).count(),
				"There should be no Mountains without an assigned beast.");
	}

	private static void checkNumberOfSpecies(Mountain[] mountains) {
		int maxDegree = Arrays.stream(mountains).map(Mountain::getNeighbours).mapToInt(Set::size).max().orElse(0);
		long numBeasts = Arrays.stream(mountains).map(Mountain::getBeast).distinct().count();
		if (numBeasts > maxDegree + 1) {
			fail("The assignment uses too many different species of beasts.");
		}
	}

	private static void isValidAssignment(Mountain[] mountains) {
		assertEquals(0, Arrays.stream(mountains).mapToLong(m -> {
			return m.getNeighbours().stream().map(Mountain::getBeast).filter(b -> {
				return b == m.getBeast();
			}).count();
		}).sum(), "Found neighbours with equal assignment. (Each pair is counted twice.)");
	}

	@HiddenTest
	@DisplayName(value = "Mountain - Single Mountain")
	@Order(1)
	void testSingleMountain() {
		test(new Mountain[] { new Mountain() });
	}

	@HiddenTest
	@DisplayName(value = "Mountain - Mountains not Connected")
	@Order(2)
	void testMountainsNotConnected() {
		test(new Mountain[] { new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain() });
	}

	@HiddenTest
	@DisplayName(value = "Mountain - Simple Chain")
	@Order(3)
	void testSimpleChain() {
		Mountain[] mountains = { new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain() };
		for (int i = 1; i < mountains.length; i++) {
			mountains[i].addNeighbour(mountains[i - 1]);
			mountains[i - 1].addNeighbour(mountains[i]);
		}
		test(mountains);
	}

	@HiddenTest
	@DisplayName(value = "Mountain - Cycle")
	@Order(4)
	void testCycle() {
		Mountain[] mountains = { new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain() };
		for (int i = 1; i <= mountains.length; i++) {
			mountains[i % mountains.length].addNeighbour(mountains[i - 1]);
			mountains[i - 1].addNeighbour(mountains[i % mountains.length]);
		}
		test(mountains);
	}

	@HiddenTest
	@DisplayName(value = "Mountain - Fully Connected")
	@Order(5)
	void testFullyConnected() {
		Mountain[] mountains = { new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain(),
				new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain() };
		for (var mi : mountains) {
			for (var mj : mountains) {
				if (mi != mj) {
					mi.addNeighbour(mj);
				}
			}
		}
		test(mountains);
	}

	@HiddenTest
	@DisplayName(value = "Mountain - Complex Network")
	@Order(6)
	void testComplexNetwork() {
		Mountain[] mountains = { new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain(),
				new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain(),
				new Mountain(), new Mountain(), new Mountain(), new Mountain(), new Mountain() };
		int[][] edges = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 4, 5 }, { 4, 6 }, { 4, 7 }, { 4, 8 }, { 9, 10 }, { 10, 11 },
				{ 11, 12 }, { 9, 13 }, { 13, 14 }, { 13, 15 } };
		for (var edge : edges) {
			mountains[edge[0]].addNeighbour(mountains[edge[1]]);
			mountains[edge[1]].addNeighbour(mountains[edge[0]]);
		}
		test(mountains);
	}

}
