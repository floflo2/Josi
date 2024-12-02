package pgdp.array;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.PublicTest;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@MirrorOutput
@StrictTimeout(2)
@WhitelistPath("target/classes/pgdp/PinguLib.class")
public class ArraysBehaviorTest {

	private static String twoDIntArrayToString(int[][] array) {
		return "[" + Arrays.stream(array).map(Arrays::toString).collect(Collectors.joining(", ")) + "]";
	}

	private static String twoDCharArrayToString(char[][] array) {
		return "\n" + Arrays.stream(array).map(Arrays::toString).collect(Collectors.joining(",\n")) + "\n";
	}

	/* ================================ Test minsAndMaxs() ================================ */

	@PublicTest
	public void testMinsAndMaxs() {
		assertAll(
				() -> testMinsAndMaxs(
						new int[][]{
								{1, 2, 3, 4, 5},
								{5, 4, 3, 2, 1},
								{1, 2, 3, 2, 1},
								{1, 2, -3, 4, 5},
								{1, 1, 1, 1, 1},
								{1},
								{-4, 7, 6, 0, -3, 6, 3, 3, -5, 2, 7, 2, 1}
						},
						new int[][]{
								{1, 5},
								{1, 5},
								{1, 3},
								{-3, 5},
								{1, 1},
								{1, 1},
								{-5, 7}
						}
				)
		);
	}

	private void testMinsAndMaxs(int[][] a, int[][] expected) {
		int[][] actual = Array.minsAndMaxs(a);
		if(expected.length != actual.length) {
			fail(
					"Die Anzahl an Minimum-Maximum-Paaren im Output sollte der Länge des Arrays 'a' entsprechen. "
							+ "Dies ist hier aber nicht der Fall: 'a' hat Länge " + a.length + ", es wurden aber " + actual.length
							+ " Minimum-Maximum-Paare zurückgegeben."
			);
		}
		for(int i = 0; i < a.length; i++) {
			String message = "Die Minima bzw. Maxima der Eingabe " + twoDIntArrayToString(a) + " wurden nicht korrekt berechnet: "
					+ "Für das " + i + "-te innere Array " + Arrays.toString(a[i]) + " sollte das Minimum " + expected[i][0]
					+ " und das Maximum " + expected[i][1] + " sein, berechnet wurde aber Minimum " + actual[i][0] + " und Maximum "
					+ actual[i][1];
			assertArrayEquals(expected[i], actual[i], message);
		}
	}

	/* ================================ Test transpose() ================================ */

	@PublicTest
	public void testTranspose() {
		assertAll(
				() -> testTranspose(
						new int[][]{
								{1, 2, 3},
								{4, 5, 6},
								{7, 8, 9}
						},
						new int[][]{
								{1, 4, 7},
								{2, 5, 8},
								{3, 6, 9}
						}
				),
				() -> testTranspose(
						new int[][]{
								{1, 2, 3},
								{4, 5, 6}
						},
						new int[][] {
								{1, 4},
								{2, 5},
								{3, 6}
						}
				),
				() -> testTranspose(
						new int[][]{
								{1, 2, 3}
						},
						new int[][]{
								{1},
								{2},
								{3}
						}
				),
				() -> testTranspose(
						new int[][]{
								{1},
								{2},
								{3}
						},
						new int[][]{
								{1, 2, 3}
						}
				)
		);
	}

	private void testTranspose(int[][] a, int[][] expected) {
		int[][] actual = Array.transpose(a);
		String message = "Die Eingabe " + twoDIntArrayToString(a) + " wurde nicht korrekt transponiert: "
				+ "Erwartet wurde " + twoDIntArrayToString(expected) + ", geliefert wurde " + twoDIntArrayToString(actual) + ".";
		assertArrayEquals(expected, actual, message);
	}

	/* ================================ Test linearize() ================================ */

	@PublicTest
	public void testLinearize() {
		assertAll(
				() -> testLinearize(
						new int[][] {
								{1, 2},
								{3},
								{4, 5, 6}
						},
						new int[] {1, 2, 3, 4, 5, 6}
				),
				() -> testLinearize(
						new int[][] {
								{1, 2},
								{},
								{3, 4, 5, 6}
						},
						new int[] {1, 2, 3, 4, 5, 6}
				),
				() -> testLinearize(
						new int[][]{
								{1, 1},
								{1},
								{1, 1, 1},
								{2, 1}
						},
						new int[] {1, 1, 1, 1, 1, 1, 2, 1}
				),
				() -> testLinearize(
						new int[][] {},
						new int[] {}
				)
		);
	}

	private void testLinearize(int[][] a, int[] expected) {
		int[] actual = Array.linearize(a);
		String message = "Die Eingabe " + twoDIntArrayToString(a) + " wurde nicht korrekt linearisiert: "
				+ "Erwartet wurde " + Arrays.toString(expected) + ", geliefert wurde " + Arrays.toString(actual) + ".";
		assertArrayEquals(expected, actual, message);
	}

	/* ================================ Test crossword() ================================ */

	@PublicTest
	public void testCrossword() {
		assertAll(
				() -> testCrossword(
						new char[][]{
								{'A'}
						},
						new char[] {'A'},
						true
				),
				() -> testCrossword(
						new char[][]{
								{'A'}
						},
						new char[] {'B'},
						false
				),
				() -> testCrossword(
						new char[][] {
								{'A', 'P', 'A', 'N', 'G', 'U'},
								{'P', 'P', 'I', 'N', 'G', 'E'},
								{'E', 'I', 'I', 'N', 'A', 'A'},
								{'N', 'N', 'N', 'N', 'K', 'A'},
								{'G', 'G', 'Q', 'Q', 'G', 'U'},
								{'U', 'O', 'Q', 'Q', 'U', 'U'}
						},
						new char[] {'P', 'I', 'N', 'G', 'U'},
						true
				),
				() -> testCrossword(
						new char[][] {
								{'A', 'P', 'A', 'N', 'G', 'U'},
								{'P', 'P', 'I', 'N', 'G', 'U'},
								{'E', 'I', 'I', 'N', 'A', 'A'},
								{'N', 'N', 'N', 'N', 'K', 'A'},
								{'G', 'G', 'Q', 'Q', 'G', 'U'},
								{'U', 'O', 'Q', 'Q', 'U', 'O'}
						},
						new char[] {'P', 'I', 'N', 'G', 'U'},
						true
				),
				() -> testCrossword(
						new char[][] {
								{'A', 'P', 'A', 'N', 'G', 'U'},
								{'P', 'P', 'I', 'N', 'G', 'E'},
								{'E', 'I', 'I', 'N', 'A', 'A'},
								{'N', 'N', 'N', 'N', 'K', 'A'},
								{'G', 'G', 'Q', 'Q', 'G', 'U'},
								{'U', 'U', 'Q', 'Q', 'U', 'O'}
						},
						new char[] {'P', 'I', 'N', 'G', 'U'},
						true
				),
				() -> testCrossword(
						new char[][] {
								{'A', 'P', 'A', 'N', 'G', 'U'},
								{'P', 'P', 'I', 'N', 'G', 'E'},
								{'E', 'I', 'I', 'N', 'A', 'A'},
								{'N', 'N', 'N', 'N', 'K', 'A'},
								{'G', 'G', 'Q', 'Q', 'G', 'U'},
								{'U', 'O', 'Q', 'Q', 'U', 'O'}
						},
						new char[] {'P', 'I', 'N', 'G', 'U'},
						false
				)
		);
	}

	private void testCrossword(char[][] letterGrid, char[] word, boolean expected) {
		boolean actual = Array.crossword(letterGrid, word);
		String message = "Für das Buchstabengitter " + twoDCharArrayToString(letterGrid) + " wurde fälschlicherweise behauptet, "
				+ "das Wort " + Arrays.toString(word) + " käme " + (actual ? "" : "nicht ") + "darin vor.";
		assertEquals(expected, actual, message);
	}


}