package pgdp.array;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.PublicTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@MirrorOutput
@StrictTimeout(2)
@WhitelistPath("target/classes/pgdp/PinguLib.class")
public class ArraysBehaviorTest {
	static enum Method {
		Print, MinMax
	}

	public static String test(int[] input, Method meth, IOTester tester) {
		tester.reset();

		String[] inputString = new String[input.length];
		for (int i = 0; i < input.length; i++)
			inputString[i] = Integer.toString(input[i]);

		tester.provideInputLines(inputString);

		if (meth == Method.MinMax)
			Array.minAndMax(input);
		else
			Array.print(input);

		return tester.out().getOutputAsString();
	}

	/* ================================ Test print() ================================ */

	@PublicTest
	public void testPrint(IOTester tester) {
		assertAll(
				() -> testPrint(new int[] { 1, 2, 3, 4, 5 }, "{1, 2, 3, 4, 5}", tester),
				() -> testPrint(new int[] { 0, 0, 0, 0, 0 }, "{0, 0, 0, 0, 0}", tester),
				() -> testPrint(new int[] {}, "{}", tester),

				() -> testPrint(new int[] { -4 }, "{-4}", tester),
				() -> testPrint(new int[] { 72, -83, 38, 78, -33, 83, -50, 71, -93, 35, 93, -73, 18, -57, 43 }, "{72, -83, 38, 78, -33, 83, -50, 71, -93, 35, 93, -73, 18, -57, 43}", tester),
				() -> testPrint(new int[] { 2_000_000_000, -2_100_000_000 }, "{2000000000, -2100000000}", tester)
		);
	}

	private static void testPrint(int[] array, String expectedOutput, IOTester tester) {
		String actualOutput = test(array, Method.Print, tester);
		assertEquals(expectedOutput, actualOutput, "Für das Eingabe-Array " + Arrays.toString(array) + " hat 'print()' nicht das korrekte Output erzeugt!");
	}

	/* ================================ Test minAndMax() ================================ */

	@PublicTest
	public void testMinAndMax(IOTester tester) {
		assertAll(
				() -> testMinAndMax(new int[] { 1, 2, 3, 4, 5 }, "Minimum = 1, Maximum = 5", tester),
				() -> testMinAndMax(new int[] { 1, 10, 25, 13, 1000 }, "Minimum = 1, Maximum = 1000", tester),
				() -> testMinAndMax(new int[] { 1, 10, 25, -13, 1000 }, "Minimum = -13, Maximum = 1000", tester),

				() -> testMinAndMax(new int[] { 5, 4, 3, 2, 1 }, "Minimum = 1, Maximum = 5", tester),
				() -> testMinAndMax(new int[] { 1, 1, 1, 1, 1 }, "Minimum = 1, Maximum = 1", tester),
				() -> testMinAndMax(new int[] { 1 }, "Minimum = 1, Maximum = 1", tester)
		);

		try {
			Array.minAndMax(new int[] {});
		} catch(Exception e) {
			fail(
					"Die Methode 'minAndMax()' erzeugt einen Fehler, wenn ihr ein leeres Array übergeben wird. "
							+ "Dieser Randfall sollte behandelt werden. "
							+ "Die Konsolenausgabe ist in diesem Falle egal."
			);
		}
	}

	private static void testMinAndMax(int[] array, String expectedOutput, IOTester tester) {
		String actualOutput = test(array, Method.MinMax, tester);
		assertEquals(expectedOutput, actualOutput, "Für das Eingabe-Array " + Arrays.toString(array) + " hat 'minAndMax()' nicht das korrekte Output erzeugt!");
	}

	/* ================================ Test isOrderedAscendingly() ================================ */

	@PublicTest
	public void testIsOrderedAscendingly() {
		assertAll(
				() -> testOrderedAsc(new int[] {1, 2, 3, 4, 5}, true),
				() -> testOrderedAsc(new int[] {1, 1, 1, 2, 2, 2, 3}, true),
				() -> testOrderedAsc(new int[] {Integer.MIN_VALUE, -416_021_146, 0, 34, 515, Integer.MAX_VALUE}, true),

				() -> testOrderedAsc(new int[] {1, 1, 1, 1, 1}, true),
				() -> testOrderedAsc(new int[] {1}, true),
				() -> testOrderedAsc(new int[] {}, true),

				() -> testOrderedAsc(new int[] {5, 4, 3, 2, 1}, false),
				() -> testOrderedAsc(new int[] {1, 2, -3, 4, 5}, false),
				() -> testOrderedAsc(new int[] {1, 2, 3, 4, 5, 4, 5, 6, 7, 8}, false)
		);
	}

	private static void testOrderedAsc(int[] a, boolean expectedOrdering) {
		boolean actualOrdering = Array.isOrderedAscendingly(a);
		String message = "Das Eingabe-Array " + Arrays.toString(a) + " wird fälschlicherweise als "
				+ (expectedOrdering ? "nicht" : "")
				+ " aufsteigend sortiert klassifiziert.";
		assertEquals(expectedOrdering, actualOrdering, message);
	}

	/* ================================ Test invert() ================================ */

	@PublicTest
	public void testInvert() {
		assertAll(
				() -> testInvert(new int[] {1, 2, 3, 4, 5}, new int[] {5, 4, 3, 2, 1}),
				() -> testInvert(new int[] {1, 2, 3, 2, 3}, new int[] {3, 2, 3, 2, 1}),
				() -> testInvert(new int[] {1, 1, 1, 1, 1}, new int[] {1, 1, 1, 1, 1}),

				() -> testInvert(new int[] {1}, new int[] {1}),
				() -> testInvert(new int[] {}, new int[] {}),
				() -> testInvert(new int[] {Integer.MAX_VALUE, -400, 38, Integer.MIN_VALUE, Integer.MAX_VALUE, -38, 55}, new int[] {55, -38, Integer.MAX_VALUE, Integer.MIN_VALUE, 38, -400, Integer.MAX_VALUE})
		);
	}

	private static void testInvert(int[] a, int[] expected) {
		int[] copyOfA = a.clone();
		Array.invert(a);
		String message = "Das Eingabe-Array " + Arrays.toString(copyOfA) + " wurde nicht korrekt invertiert: "
				+ "Erwartet wurde " + Arrays.toString(expected) + " , geliefert wurde " + Arrays.toString(a) + ".";
		assertArrayEquals(expected, a, message);
	}

	/* ================================ Test intersect() ================================ */

	@PublicTest
	public void testIntersect() {
		assertAll(
				() -> testIntersect(new int[] {1, 2, 3, 4, 5}, 3, new int[] {1, 2, 3}),
				() -> testIntersect(new int[] {1, 1, 1, 1, 1}, 3, new int[] {1, 1, 1}),
				() -> testIntersect(new int[] {Integer.MAX_VALUE, -30, 0, 25, Integer.MAX_VALUE}, 3, new int[] {Integer.MAX_VALUE, -30, 0}),

				() -> testIntersect(new int[] {1, 2, 3, 4, 5}, 0, new int[] {}),
				() -> testIntersect(new int[] {1, 2, 3, 4, 5}, -4, new int[] {}),
				() -> testIntersect(new int[] {1, 2, 3, 4, 5}, Integer.MIN_VALUE, new int[] {}),

				() -> testIntersect(new int[] {1, 2, 3, 4, 5}, 8, new int[] {1, 2, 3, 4, 5, 0, 0, 0}),
				() -> testIntersect(new int[] {1}, 8, new int[] {1, 0, 0, 0, 0, 0, 0, 0}),
				() -> testIntersect(new int[] {}, 8, new int[] {0, 0, 0, 0, 0, 0, 0, 0})
		);
	}

	private static void testIntersect(int[] a, int length, int[] expected) {
		int[] actual = Array.intersect(a, length);
		String message = "Das Eingabe-Array " + Arrays.toString(a) + " mit 'length' gleich " + length + " wurde nicht korrekt behandelt: "
				+ "Erwartet wurde " + Arrays.toString(expected) + ", geliefert wurde " + Arrays.toString(actual);
		assertArrayEquals(expected, actual, message);
	}

	/* ================================ Test filterEvenNumbersFrom() ================================ */

	@PublicTest
	public void testFilterEvenNumbersFrom() {
		assertAll(
				() -> testFilterEven(new int[] {1, 2, 3, 4, 5}, new int[] {2, 4}),
				() -> testFilterEven(new int[] {1, 1, 1, 1, 1}, new int[] {}),
				() -> testFilterEven(new int[] {2, 2, 2, 2, 2}, new int[] {2, 2, 2, 2, 2}),
				() -> testFilterEven(new int[] {-2, 0, -4, Integer.MIN_VALUE, Integer.MAX_VALUE}, new int[] {-2, 0, -4, Integer.MIN_VALUE}),
				() -> testFilterEven(new int[] {}, new int[] {})
		);
	}

	private static void testFilterEven(int[] a, int[] expected) {
		int[] actual = Array.filterEvenNumbersFrom(a);
		String message = "Das Eingabe-Array " + Arrays.toString(a) + " wurde nicht korrekt gefiltert: "
				+ "Erwartet wurde " + Arrays.toString(expected) + " , geliefert wurde " + Arrays.toString(actual) + ".";
		assertArrayEquals(expected, actual, message);
	}

	/* ================================ Test distinct() ================================ */

	@PublicTest
	public void testDistinct() {
		assertAll(
				() -> testDistinct(new int[] {1, 2, 3, 4, 5}, new int[] {1, 2, 3, 4, 5}),
				() -> testDistinct(new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0}, new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0}),
				() -> testDistinct(new int[] {1}, new int[] {1}),
				() -> testDistinct(new int[] {}, new int[] {}),

				() -> testDistinct(new int[] {1, 1, 2, 3, 3, 3, 4, 4, 5}, new int[] {1, 2, 3, 4, 5}),
				() -> testDistinct(new int[] {1, 1, 1, 1, 1}, new int[] {1}),
				() -> testDistinct(new int[] {1, 0, 1, 0, 1, 0, 2, 1, 0, 1, 0, 1}, new int[] {1, 0, 2}),
				() -> testDistinct(new int[] {5, 5, 0, -4, 0, 5, 2, 6, 0, 5}, new int[] {5, 0, -4, 2, 6})
		);
	}

	private static void testDistinct(int[] a, int[] expected) {
		int[] actual = Array.distinct(a);
		String message = "Das Eingabe-Array " + Arrays.toString(a) + " wurde nicht korrekt behandelt: "
				+ "Erwartet wurde " + Arrays.toString(expected) + " , geliefert wurde " + Arrays.toString(actual) + ".";
		assertArrayEquals(expected, actual, message);
	}

}