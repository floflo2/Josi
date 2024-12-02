package pgdp.convexhull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;

@TestClassAnnotation
public class QuickHullBehaviourTest {

	/*	Bepunktung:
	 *	 - combineHulls(): 1P
	 *	 - quickHull(): 5P
	 *		 - "normal" Cases: 3P
	 * 		 - Start Edge part of Hull: 1P
	 * 		 - All Points Collinear: 1P
	 */

	// ================ Test combineHulls() ================ //

	@PublicTest
	@DisplayName(value = "Test Combine Hulls - Simple Example")
	public void publicTestCombineHulls() {
		testCombineHulls(
				new int[][] {{0, 0}, {3, 0}, {2, 3}},
				new int[][] {{2, 3}, {1, 3}, {0, 1}},
				new int[][] {{0, 0}, {3, 0}, {2, 3}, {1, 3}, {0, 1}}
		);
	}

	@HiddenTest
	@DisplayName(value = "Test All Combine Hulls Cases - Grading Test")
	public void hiddenTestCombineHulls() {
		assertAll(
				// Public Test
				() -> testCombineHulls(
						new int[][] {{0, 0}, {3, 0}, {2, 3}},
						new int[][] {{2, 3}, {1, 3}, {0, 1}},
						new int[][] {{0, 0}, {3, 0}, {2, 3}, {1, 3}, {0, 1}}
				),

				// End Second == Start First
				() -> testCombineHulls(
						new int[][] {{0, 0}, {3, 0}, {2, 3}},
						new int[][] {{2, 3}, {1, 3}, {0, 0}},
						new int[][] {{0, 0}, {3, 0}, {2, 3}, {1, 3}, {0, 0}}
				),

				// Different Lengths
				() -> testCombineHulls(
						new int[][] {{0, 0}, {3, 0}, {2, 3}},
						new int[][] {{2, 3}, {1, 3}, {0, 2}, {0, 1}},
						new int[][] {{0, 0}, {3, 0}, {2, 3}, {1, 3}, {0, 2}, {0, 1}}
				),

				// All in one line
				() -> testCombineHulls(
						new int[][] {{0, 0}, {1, 0}},
						new int[][] {{1, 0}, {2, 0}},
						new int[][] {{0, 0}, {1, 0}, {2, 0}}
				)
		);
	}

	private void testCombineHulls(int[][] firstHull, int[][] secondHull, int[][] expected) {
		int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
		String message = "Die beiden Hulls " + pointsToString(firstHull)
				+ " und " + pointsToString(secondHull) + " werden nicht korrekt kombiniert."
				+ " Die korrekte kombinierte Hull wäre " + pointsToString(expected)
				+ ", es wurde aber " + pointsToString(actual) + " zurückgegeben.";
		assertArrayEquals(expected, actual, message);
	}

	// ================ Test Quick Hull ================ //

	@PublicTest
	@DisplayName(value = "Example 1")
	public void testExample1() {
		int[][] points = { { 7, 3 }, { 1, 2 }, { 1, 4 }, { 2, 2 }, { 4, 3 }, { 3, 4 }, { 4, 5 }, { 4, 6 }, { 5, 2 },
				{ 6, 4 }, { 1, 1 }, { 7, 5 } };
		int[][] exp1 = { { 1, 1 }, { 5, 2 }, { 7, 3 }, { 7, 5 }, { 4, 6 }, { 1, 4 }, { 1, 2 }, { 1, 1 } };
		int[][] exp2 = { { 1, 1 }, { 5, 2 }, { 7, 3 }, { 7, 5 }, { 4, 6 }, { 1, 4 }, { 1, 1 } };
		int[][] exp3 = { { 1, 4 }, { 1, 2 }, { 1, 1 }, { 5, 2 }, { 7, 3 }, { 7, 5 }, { 4, 6 }, { 1, 4 } };
		int[][] exp4 = { { 1, 4 }, { 1, 1 }, { 5, 2 }, { 7, 3 }, { 7, 5 }, { 4, 6 }, { 1, 4 } };
		int[][] exp5 = { { 1, 2 }, { 1, 1 }, { 5, 2 }, { 7, 3 }, { 7, 5 }, { 4, 6 }, { 1, 4 }, { 1, 2 } };
		int[][] actual = QuickHull.quickHull(points);

		assertAnyEquals(List.of(exp1, exp2, exp3, exp4, exp5), actual, points);
	}

	@PublicTest
	@DisplayName(value = "Large Input")
	public void testLarge() {
		int[][] points = IntStream.range(0, 101).mapToObj(i -> IntStream.range(0, 101).mapToObj(j -> {
			if (i == 0 && j != 0 && j != 100 || j == 0 && i != 0 && i != 100 || i == 100 && j != 0 && j != 100
					|| j == 100 && i != 0 && i != 100)
				return null;
			return new int[] { i, j };
		}).filter(Objects::nonNull)).flatMap(x -> x).toArray(int[][]::new);
		int[][] exp1 = { { 0, 0 }, { 100, 0 }, { 100, 100 }, { 0, 100 }, { 0, 0 } };
		int[][] exp2 = { { 0, 100 }, { 0, 0 }, { 100, 0 }, { 100, 100 }, { 0, 100 } };
		int[][] actual = QuickHull.quickHull(points);

		assertAnyEquals(List.of(exp1, exp2), actual, points);
	}

	@HiddenTest
	@DisplayName(value = "All Normal Cases - Grading Test")
	public void testNormalCases() {
		try {
			assertAll(
					this::testExample1,
					this::testLarge
			);
		} catch(Throwable t) {
			fail("Nicht alle \"normalen\" Fälle werden korrekt behandelt.");
		}
	}

	@HiddenTest
	@DisplayName(value = "Edge Case - Start Edge Part of Convex Hull - Grading Test")
	public void testECStartEdgePartOfHull() {
		int[][] points = { { 1, 1 }, { 3, 1 }, { 2, 3 }, { 2, 2 } };
		int[][] exp = { { 1, 1 }, { 3, 1 }, { 2, 3 }, { 1, 1 } };
		int[][] actual = QuickHull.quickHull(points);

		List<int[][]> l = new LinkedList<>();
		l.add(exp);
		assertAnyEquals(l, actual, points);
	}

	@HiddenTest
	@DisplayName(value = "Edge Case - All Points Collinear - Grading Test")
	public void testECAllCollinear() {
		int[] p1 = { 1, 1 };
		int[] p2 = { 2, 1 };
		int[] p3 = { 3, 1 };
		int[] p4 = { 4, 1 };

		int[][] points = { p1, p2, p3, p4 };

		int[][] exp1 = { p1, p4, p1 };
		int[][] exp2 = { p1, p4, p3, p1 };
		int[][] exp3 = { p1, p4, p2, p1 };
		int[][] exp4 = { p1, p4, p3, p2, p1 };

		int[][] exp5 = { p1, p2, p4, p1 };
		int[][] exp6 = { p1, p2, p4, p3, p1 };
		int[][] exp7 = { p1, p2, p4, p2, p1 };
		int[][] exp8 = { p1, p2, p4, p3, p2, p1 };

		int[][] exp9 = { p1, p3, p4, p1 };
		int[][] exp10 = { p1, p3, p4, p3, p1 };
		int[][] exp11 = { p1, p3, p4, p2, p1 };
		int[][] exp12 = { p1, p3, p4, p3, p2, p1 };

		int[][] exp13 = { p1, p2, p3, p4, p1 };
		int[][] exp14 = { p1, p2, p3, p4, p3, p1 };
		int[][] exp15 = { p1, p2, p3, p4, p2, p1 };
		int[][] exp16 = { p1, p2, p3, p4, p3, p2, p1 };

		int[][] actual = QuickHull.quickHull(points);

		assertAnyEquals(
				List.of(
						exp1, exp2, exp3, exp4, exp5, exp6, exp7, exp8,
						exp9, exp10, exp11, exp12, exp13, exp14, exp15, exp16
				),
				actual,
				points
		);
	}

	// ================ Helper Methods ================ //

	private static void assertAnyEquals(List<int[][]> listOfExpected, int[][] actual, int[][] points) {
		assertTrue(
				listOfExpected.stream().anyMatch(expected -> Arrays.deepEquals(expected, actual)),
				"Für die Punktwolke " + pointsToString(points)  + " wird die Hülle " + pointsToString(actual)
						+ " ausgegeben. Diese ist jedoch nicht korrekt."
						+ " Denke daran, dass die Hülle am (an einem der) linkesten Punkt(e) beginnen und enden"
						+ " und die einzelnen Punkte gegen den Uhrzeigersinn aufgelistet sein müssen."
		);
	}

	private static String pointsToString(int[][] points) {
		return "{" + Arrays.stream(points)
				.map(Arrays::toString)
				.collect(Collectors.joining(", "))
				+ "}";
	}

}
