package pgdp.math;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import de.tum.in.test.api.io.Line;
import org.junit.jupiter.api.Test;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.Public;

@MirrorOutput
@StrictTimeout(2)
@WhitelistPath("target/classes/pgdp/PinguLib.class")
public class ControlStructuresIBehaviorTest {

	/* ================================ Test Collatz ================================ */

	@Public
	@Test
	public void testCollatz(IOTester tester) {
		assertAll(
				() -> testCollatz(-100, "", 0, tester),
				() -> testCollatz(0, "", 0, tester),
				() -> testCollatz(4, "4 2 1", 3, tester),
				() -> testCollatz(11,
						"11 34 17 52 26 13 40 20 10 5 16 8 4 2 1",
						15, tester),
				() -> testCollatz(27,
						"27 82 41 124 62 31 94 47 142 71 214 107 322 161 484 242"
						+ " 121 364 182 91 274 137 412 206 103 310 155 466"
						+ " 233 700 350 175 526 263 790 395 1186 593 1780"
						+ " 890 445 1336 668 334 167 502 251 754 377 1132"
						+ " 566 283 850 425 1276 638 319 958 479 1438 719"
						+ " 2158 1079 3238 1619 4858 2429 7288 3644 1822"
						+ " 911 2734 1367 4102 2051 6154 3077 9232 4616"
						+ " 2308 1154 577 1732 866 433 1300 650 325 976"
						+ " 488 244 122 61 184 92 46 23 70 35 106 53 160"
						+ " 80 40 20 10 5 16 8 4 2 1",
						112, tester)
		);
	}

	private void testCollatz(int n, String expectedSequence, int expectedLength, IOTester tester) {
		tester.reset();

		ControlStructuresI.printCollatz(n);

		List<String> actualConsoleOutput = tester.getOutput().stream()
				.map(Line::text)
				.collect(Collectors.toList());

		if(n < 1) {
			assertEquals("Eingabe muss größer als 0 sein!", actualConsoleOutput.get(0),
					"Bei Eingaben kleiner 1 sollte ausgegeben werden, dass die Eingabe zu klein ist!");
			return;
		}

		assertEquals(3, actualConsoleOutput.size(),
				"Die Ausgabe muss 3 Zeilen haben:" +
						"Eine mit der Collatz-Folge, " +
						"eine mit der Länge dieser " +
						"und eine leere am Schluss.");
		assertEquals(expectedSequence, actualConsoleOutput.get(0),
				"Die Collatzfolge ist nicht korrekt.");
		assertEquals("Länge: " + expectedLength, actualConsoleOutput.get(1),
				"Die Länge wurde nicht korrekt ausgegeben.");
		assertEquals("", actualConsoleOutput.get(2),
				"Die letzte Zeile sollte leer sein.");
	}

	/* ================================ Test Powers of Two ================================ */

	@Public
	@Test
	public void testPowersOfTwo(IOTester tester) {
		assertAll(
				() -> testPowersOfTwo(-100, "", tester),
				() -> testPowersOfTwo(0, "", tester),
				() -> testPowersOfTwo(1, "1", tester),
				() -> testPowersOfTwo(7, "1 2 4", tester),
				() -> testPowersOfTwo(8, "1 2 4 8", tester),
				() -> testPowersOfTwo(9, "1 2 4 8", tester),
				() -> testPowersOfTwo(1_000_000, "1 2 4 8 16 32 64 128 256 512 1024 2048 4096 8192 16384 32768 65536 131072 262144 524288", tester)
		);
	}

	private void testPowersOfTwo(int n, String expectedSequence, IOTester tester) {
		tester.reset();

		ControlStructuresI.printPowersOfTwoUpTo(n);

		List<String> actualConsoleOutput = tester.getOutput().stream()
				.map(Line::text)
				.collect(Collectors.toList());

		if(n < 1) {
			assertEquals("Eingabe muss größer als 0 sein!", actualConsoleOutput.get(0),
					"Bei Eingaben kleiner 1 sollte ausgegeben werden, dass die Eingabe zu klein ist!");
			return;
		}

		assertEquals(2, actualConsoleOutput.size(),
				"Die Ausgabe sollte aus einer Zeile mit der Sequenz an Zweierpotenzen " +
						"und einer Leerzeile bestehen!");
		assertEquals(expectedSequence, actualConsoleOutput.get(0),
				"Die Sequenz an Zweierpotenzen ist nicht korrekt!");
		assertEquals("", actualConsoleOutput.get(1),
				"Die letzte Zeile ist nicht, wie gesollt, eine Leerzeile!");
	}

	/* ================================ Test Triangle ================================ */

	@Public
	@Test
	public void testTriangle(IOTester tester) {
		assertAll(
				() -> testTriangle(-100, "", tester),
				() -> testTriangle(0, "", tester),
				() -> testTriangle(1, "*\n", tester),
				() -> testTriangle(3,
						"""
								***
								**
								*
								"""
						, tester),
				() -> testTriangle(6,
						"""
								******
								*****
								****
								***
								**
								*
								"""
						,  tester)
		);
	}

	private void testTriangle(int n, String expected, IOTester tester) {
		tester.reset();

		ControlStructuresI.printTriangle(n);

		String actualConsoleOutput = tester.getOutput().stream()
				.map(Line::text)
				.collect(Collectors.joining("\n"));

		if(n < 1) {
			assertEquals("Eingabe muss größer als 0 sein!\n", actualConsoleOutput,
					"Bei Eingaben kleiner 1 sollte ausgegeben werden, dass die Eingabe zu klein ist!");
			return;
		}

		assertEquals(expected, actualConsoleOutput);
	}

	/* ================================ Test Number of Digits ================================ */

	@Public
	@Test
	public void testNumberOfDigits() {
		assertAll(
				() -> testNumberOfDigits(0, 0),
				() -> testNumberOfDigits(1, 1),
				() -> testNumberOfDigits(5, 1),
				() -> testNumberOfDigits(22, 2),
				() -> testNumberOfDigits(82_397, 5),
				() -> testNumberOfDigits(153_152_745, 9),
				() -> testNumberOfDigits(Integer.MAX_VALUE, 10)
		);
	}

	private void testNumberOfDigits(int number, int expectedNumberOfDigits) {
		if(number < 0) {
			return;
		}

		assertEquals(expectedNumberOfDigits, ControlStructuresI.calculateNumberOfDigits(number),
				"Anzahl an Ziffern für die Zahl " + number + " nicht korrekt berechnet!");
	}

	/* ================================ Test Reverse Number ================================ */

	@Public
	@Test
	public void testReverseNumber() {
		assertAll(
				() -> testReverseNumber(0, 0),
				() -> testReverseNumber(4, 4),
				() -> testReverseNumber(1_002, 2_001),
				() -> testReverseNumber(1_234_567, 7_654_321),
				() -> testReverseNumber(999_999_999, 999_999_999),
				() -> testReverseNumber(987_656_789, 987_656_789),
				() -> testReverseNumber(1_000_000, 1)
		);
	}

	private void testReverseNumber(int number, int expectedReversedNumber) {
		if(number < 0 || number > 999_999_999) {
			return;
		}

		assertEquals(expectedReversedNumber, ControlStructuresI.reverseNumber(number),
				"Die Zahl " + number + " wird nicht korrekt umgekehrt!");
	}

	/* ================================ Test Palindrome ================================ */

	@Public
	@Test
	public void testPalindrome() {
		assertAll(
				() -> testPalindrome(0, true),
				() -> testPalindrome(4, true),
				() -> testPalindrome(1_002, false),
				() -> testPalindrome(1_234_567, false),
				() -> testPalindrome(999_999_999, true),
				() -> testPalindrome(987_656_789, true),
				() -> testPalindrome(1_000_000, false)
		);
	}

	private void testPalindrome(int number, boolean expectedResult) {
		if(number < 0 || number > 999_999_999) {
			return;
		}

		assertEquals(expectedResult, ControlStructuresI.isPalindrome(number),
				expectedResult
						? "Die Zahl " + number + " wurde nicht korrekt als Palindrom erkannt."
						: "Die Zahl " + number + " wurde fälschlicherweise als Palindrom klassifiziert"
		);
	}

}