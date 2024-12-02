package pgdp.recursion;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicMethod;
import de.tum.in.test.api.jupiter.Public;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@MirrorOutput
@StrictTimeout(10)
@WhitelistPath("target")
public class RecursiveMathTest {
	DynamicClass<?> recursiveMath = new DynamicClass<>("pgdp.recursion.RecursiveMath");
	DynamicMethod<?> binCoeffRec = recursiveMath.method(long.class, "binomCoeffRecursive", int.class, int.class);
	DynamicMethod<?> binCoeffEndRec = recursiveMath.method(long.class, "binomCoeffEndRec", int.class, int.class);
	DynamicMethod<?> fibonacciRec = recursiveMath.method(long.class, "fibonacciRecursive", int.class);
	DynamicMethod<?> fibonacciEndRec = recursiveMath.method(long.class, "fibonacciEndRec", int.class);

	@Public
	@Test
	public void test_binomCoeff_rec() {
		assertEquals(1l, binCoeffRec.invokeStatic(1, 0), "Bin(1 0) sollte 1 sein.");
		assertEquals(1l, binCoeffRec.invokeStatic(1, 1), "Bin(1 1) sollte 1 sein.");
		assertEquals(1l, binCoeffRec.invokeStatic(5, 5), "Bin(5,5) sollte 1 sein.");
		assertEquals(0l, binCoeffRec.invokeStatic(1, 2), "Bin(1,2) sollte 0 sein.");
		assertEquals(10l, binCoeffRec.invokeStatic(5, 3), "Bin(5,3) sollte 10 sein.");
		assertEquals(10l, binCoeffRec.invokeStatic(5, 2), "Bin(5,2) sollte 10 sein.");
	}

	@Public
	@Test
	public void test_fibonacci_rec() {
		long[] solutions = new long[] { 0, 1, 1, 2, 3, 5, 8, 13, 21, 34 };
		for (int i = 0; i < 10; i++) {
			assertEquals(solutions[i], fibonacciRec.invokeStatic(i),
					"Die " + (i + 1) + ". Zahl der Fibonacci-Reihe sollte " + solutions[i] + " sein.");
		}
	}

	@Public
	@Test
	public void testAsBs() {
		assertAll(
				() -> testAsBs(new char[] {'A', 'B', 'B', 'A'}, 0),
				() -> testAsBs(new char[] {'A', 'B', 'A', 'A'}, 2),
				() -> testAsBs(new char[] {'A', 'B', 'B', 'B'}, -2),
				() -> testAsBs(new char[] {'A', 'A', 'A', 'B', 'B', 'B', 'B', 'A', 'A'}, 1),
				() -> testAsBs(new char[] {'A', 'A', 'A', 'A', 'A'}, 5),
				() -> testAsBs(new char[] {'B', 'B', 'B', 'B', 'B'}, -5),
				() -> testAsBs(new char[] {}, 0)
		);
	}

	private void testAsBs(char[] word, int expected) {
		int actual = RecursiveMath.differenceAsBs(word);
		String message = "Für das Wort " + Arrays.toString(word) + " wird nicht die korrekte Differenz berechnet!";
		assertEquals(expected, actual, message);
	}

	@Public
	@Test
	public void testPermutations() {
		assertAll(
				() -> testPermutations(
						new int[]{1, 2},
						new int[][]{
								{1, 2},
								{2, 1}
						}
				),
				() -> testPermutations(
						new int[]{1, 2, 4},
						new int[][]{
								{1, 2, 4},
								{2, 1, 4},
								{1, 4, 2},
								{2, 4, 1},
								{4, 1, 2},
								{4, 2, 1}
						}
				),
				() -> testPermutations(
						new int[]{1, 2, 3, 4},
						new int[][]{
								{1, 2, 3, 4},
								{1, 2, 4, 3},
								{1, 3, 2, 4},
								{1, 3, 4, 2},
								{1, 4, 2, 3},
								{1, 4, 3, 2},

								{2, 1, 4, 3},
								{2, 1, 3, 4},
								{2, 3, 1, 4},
								{2, 3, 4, 1},
								{2, 4, 1, 3},
								{2, 4, 3, 1},

								{3, 1, 2, 4},
								{3, 1, 4, 2},
								{3, 2, 1, 4},
								{3, 2, 4, 1},
								{3, 4, 1, 2},
								{3, 4, 2, 1},

								{4, 1, 2, 3},
								{4, 1, 3, 2},
								{4, 2, 1, 3},
								{4, 2, 3, 1},
								{4, 3, 1, 2},
								{4, 3, 2, 1}
						}
				),
				() -> testPermutations(
						new int[]{},
						new int[][]{
								{}
						}
				)
		);
	}

	private void testPermutations(int[] array, int[][] expected) {
		int[][] actual = RecursiveMath.permutations(array);
		assertEquals(expected.length, actual.length, "Die Anzahl an berechneten Permutationen stimmt nicht.");
		for(int i = 0; i < expected.length; i++) {
			boolean isInActual = false;
			for(int j = 0; j < actual.length; j++) {
				isInActual |= Arrays.equals(expected[i], actual[j]);
				if(isInActual)
					break;
			}
			assertTrue(isInActual, "Für das Array " + Arrays.toString(array) + " wurde die Permutation " + Arrays.toString(expected[i]) + " vergessen.");
		}
	}

}