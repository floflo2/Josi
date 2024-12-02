package pgdp.math;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import de.tum.in.test.api.io.Line;
import org.junit.jupiter.api.Test;

import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.io.IOTester;

@MirrorOutput
@StrictTimeout(2)
@WhitelistPath("target/classes/pgdp/PinguLib.class")
public class ThreeAndSevenBehaviorTest {

	/* ================================ Test 3 and 7 ================================ */

	@Public
	@Test
	public void testThreeAndSeven(IOTester tester) {
		assertAll(
				() -> testThreeAndSeven(-4, -1, tester),
				() -> testThreeAndSeven(2, 0, tester),
				() -> testThreeAndSeven(7, 16, tester),
				() -> testThreeAndSeven(25, 129, tester),
				() -> testThreeAndSeven(92, 1822, tester)
		);
	}

	private void testThreeAndSeven(int n, int expectedSum, IOTester tester) {
		tester.reset();

		int actualSum = ControlStructuresII.threeAndSeven(n);

		if(n < 0) {
			List<String> consoleOutput = tester.getOutput().stream().map(Line::text).collect(Collectors.toList());
			assertEquals("Eingabe muss größer oder gleich 0 sein!", consoleOutput.get(0), "Falscher Konsolen-Output für Parameter " + n + ".");
			assertEquals(-1, actualSum, "Bei falschen Eingaben wie hier z.B. " + n + " sollte -1 zurückgegeben werden.");
		}

		assertEquals(expectedSum, actualSum, "Falsche Summe für Eingabe " + n + ".");
	}

	/* ================================ Test ASCII ================================ */

	@Public
	@Test
	public void testPrintAsciiCodes(IOTester tester) {
		assertAll(
				() -> testPrintAsciiCodes('A', 1,
						"""
								Der ASCII-Code von 'A' ist 65.
								"""
						, tester),
				() -> testPrintAsciiCodes('A', 5,
						"""
								Der ASCII-Code von 'A' ist 65.
								Der ASCII-Code von 'B' ist 66.
								Der ASCII-Code von 'C' ist 67.
								Der ASCII-Code von 'D' ist 68.
								Der ASCII-Code von 'E' ist 69.
								"""
						, tester),
				() -> testPrintAsciiCodes('e', 9,
						"""
								Der ASCII-Code von 'e' ist 101.
								Der ASCII-Code von 'f' ist 102.
								Der ASCII-Code von 'g' ist 103.
								Der ASCII-Code von 'h' ist 104.
								Der ASCII-Code von 'i' ist 105.
								Der ASCII-Code von 'j' ist 106.
								Der ASCII-Code von 'k' ist 107.
								Der ASCII-Code von 'l' ist 108.
								Der ASCII-Code von 'm' ist 109.
								"""
						, tester),
				() -> testPrintAsciiCodes('!', 7,
						"""
								Der ASCII-Code von '!' ist 33.
								Der ASCII-Code von '"' ist 34.
								Der ASCII-Code von '#' ist 35.
								Der ASCII-Code von '$' ist 36.
								Der ASCII-Code von '%' ist 37.
								Der ASCII-Code von '&' ist 38.
								Der ASCII-Code von ''' ist 39.
								"""
						, tester)
		);
	}

	private void testPrintAsciiCodes(char start, int count, String expectedConsoleOutput, IOTester tester) {
		tester.reset();
		ControlStructuresII.printAsciiCodesFor(start, count);
		String actualConsoleOutput = tester.getOutput().stream()
				.map(Line::text)
				.collect(Collectors.joining("\n"));
		assertEquals(expectedConsoleOutput, actualConsoleOutput,
				"Für Inputs '" + start + "' und " + count + " wurde der falsche Konsolen-Output produziert.");
	}

	/* ================================ Test MultTable ================================ */

	@Public
	@Test
	public void testPrintMultTable(IOTester tester) {
		assertAll(
				() -> testPrintMultTable(2,
						"""
								*   |   1   2   
								----------------
								1   |   1   2   
								2   |   2   4   
								"""
						, tester),
				() -> testPrintMultTable(15,
						"""
								*	|	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	
								--------------------------------------------------------------------
								1	|	1	2	3	4	5	6	7	8	9	10	11	12	13	14	15	
								2	|	2	4	6	8	10	12	14	16	18	20	22	24	26	28	30	
								3	|	3	6	9	12	15	18	21	24	27	30	33	36	39	42	45	
								4	|	4	8	12	16	20	24	28	32	36	40	44	48	52	56	60	
								5	|	5	10	15	20	25	30	35	40	45	50	55	60	65	70	75	
								6	|	6	12	18	24	30	36	42	48	54	60	66	72	78	84	90	
								7	|	7	14	21	28	35	42	49	56	63	70	77	84	91	98	105	
								8	|	8	16	24	32	40	48	56	64	72	80	88	96	104	112	120	
								9	|	9	18	27	36	45	54	63	72	81	90	99	108	117	126	135	
								10	|	10	20	30	40	50	60	70	80	90	100	110	120	130	140	150	
								11	|	11	22	33	44	55	66	77	88	99	110	121	132	143	154	165	
								12	|	12	24	36	48	60	72	84	96	108	120	132	144	156	168	180	
								13	|	13	26	39	52	65	78	91	104	117	130	143	156	169	182	195	
								14	|	14	28	42	56	70	84	98	112	126	140	154	168	182	196	210	
								15	|	15	30	45	60	75	90	105	120	135	150	165	180	195	210	225     
								"""
						, tester)
		);
	}

	private void testPrintMultTable(int n, String expectedConsoleOutput, IOTester tester) {
		tester.reset();
		ControlStructuresII.printMultiplicationTable(n);
		String actualConsoleOutput = tester.getOutput().stream()
				.map(Line::text)
				.collect(Collectors.joining("\n"));
		// assertEquals(expectedConsoleOutput, actualConsoleOutput,
		// 		"Für Input " + n + " wurde die falsche Tabelle ausgegeben.");
	}

	/* ================================ Test Primes ================================ */

	@Public
	@Test
	public void testPrintPrimes(IOTester tester) {
		assertAll(
				() -> testPrintPrimes(1, "", tester),
				() -> testPrintPrimes(10, "2 3 5 7", tester),
				() -> testPrintPrimes(100, "2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71 73 79 83 89 97", tester)
		);
	}

	private void testPrintPrimes(int n, String expectedPrimeList, IOTester tester) {
		tester.reset();
		ControlStructuresII.printPrimesUpTo(n);
		String actualPrimeList = tester.getOutput().stream()
				.map(Line::text)
				.collect(Collectors.joining("\n"))
				.trim();
		assertEquals(expectedPrimeList, actualPrimeList,
				"Für Eingabe " + n + " ist die ausgegebene Liste falsch.");
	}
}