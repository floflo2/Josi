package pgdp.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import org.junit.jupiter.api.Test;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.jupiter.Public;

@MirrorOutput
@StrictTimeout(50)
@WhitelistPath("target")
public class CalculatorBehaviorTest {

	private static final int HIGH_INPUT_COUNT = 50_000;
	private ByteArrayOutputStream os;
	private ByteArrayInputStream is;

	public static String generateSolution(int... input) {

		int i = 0;
		StringBuffer expected = new StringBuffer();

		while (true) {
			expected.append("Wählen Sie eine Operation:\n1) +\n2) -\n3) *\n4) /\n5) %\n6) Programm beenden\n");
			int option = input[i++];
			if (option == 6) {
				break;
			}
			if (option >= 1 && option <= 5) {
				long op1 = input[i++];
				long op2 = input[i++];
				expected.append("Ersten Operanden eingeben:\n");
				expected.append("Zweiten Operanden eingeben:\n");
				if (option == 1) {
					expected.append((op1 + op2) + "\n");
				} else if (option == 2) {
					expected.append((op1 - op2) + "\n");
				} else if (option == 3) {
					expected.append((op1 * op2) + "\n");
				} else {
					if (op2 == 0) {
						expected.append("Fehler: Division durch 0!\n");
					} else {
						if (option == 4) {
							expected.append((op1 / op2) + "\n");
						} else {
							expected.append((op1 % op2) + "\n");
						}
					}
				}
			}

		}
		return expected.toString();
	}

	@Public
	@Test
	public void testAdditionAndSubtraction() {
		testInput(1, 65, 34, 2, 34, -12, 6);

	}

	@Public
	@Test
	public void testExitProgram() {
		testInput(6);
	}

	@Public
	@Test
	public void testInvalidSelection() {
		testInput(7, 6);
	}

	@Public
	@Test
	public void testRemainder() {
		testInput(5, -11, 3, 5, 65, 0, 6);
	}

	@Public
	@Test
	public void testDivision() {
		testInput(4, -34, 5, 6);
	}

	@Public
	@Test
	public void testDivAndRemainderZero() {
		// Div by zero
		testInput(4, 65, 0, 6);
		testInput(4, -31234, 0, 6);
		testInput(4, 4523, 0, 6);
		testInput(4, -12313, 0, 6);
		// Mod zero
		testInput(5, 65, 0, 6);
		testInput(5, -31234, 0, 6);
		testInput(5, 4523, 0, 6);
		testInput(5, -1231399, 0, 6);
	}

	@Public
	@Test
	public void testMultipleOperations() {
		// 6 als Operand
		testInput(6);
		testInput(1, 6, 6, 6);
		// Teste Addition, Subtraktion, Multiplikation, Division, Modulo
		testInput(1, -443, -123, 2, 423434, 2323, 3, 232, 4343, 4, 4343, 32, 5, 1234, 43, 6);
		testInput(5, -443, -123, 4, 5423, -2323, 3, 232, -4343, 2, 4543, -32, 1, 232, -3424, 5, -443, -123, 4, 5423,
				-2323, 3, 232, -4343, 2, 4543, -32, 1, 6532, -3424, 6);
		testInput(5, -443, -123, 4, -5423, -2323, 3, -2322, -4343, 2, -4543, -32, 1, -2324, -34424, 5, -4443, -1243, 4,
				-5423, -2323, 3, -2322, -4343, 2, -4543, -32, 1, -23432, -3424, 6);
		testInput(5, -4243, -123, 4, -54323, -2323, 3, -2332, -4343, 2, -4543, -32, 1, -2332, -3424, 5, -443, 0, 4,
				-54423, 0, 3, -232, -43243, 2, -4543, -32, 1, -232, -3424, 5, -443, -123, 4, -3434, -232343, 3, -2332,
				-43443, 2, -4543, -332, 1, -232, -3424, 6);
	}

	@Public
	@Test
	public void testInvalidSelectionPrivate() {
		// test invalid input
		testInput(4, 65, 0, 3, 23, 43, 4, 65, 0, 3, 23, 43, 7, 5, 3, 4, 6);
		testInput(4, 65, 0, 3, 23, 43, 4, 65, 0, 3, 23, 43, 0, 5, 3, 4, 6);
		testInput(4, 65, 0, 3, 23, 43, 9234234, 5, 3, 4, 6);
	}

	@Public
	@Test
	public void testManyIterations() {
		// this test should fail if the calculator is implemented recursively
		int[] inputs = new int[HIGH_INPUT_COUNT * 3 + 1];
		Random random = new Random(885732);
		for (int i = 0; i < HIGH_INPUT_COUNT; i += 3) {
			// Operation in range of 1 to 5
			inputs[i] = random.nextInt(5) + 1;
			inputs[i + 1] = random.nextInt(1_000_000);
			// avoid to cause failures due to incorrect handling of division by zero
			inputs[i + 1] = random.nextInt(1_000_000) + 1;
		}
		inputs[inputs.length - 1] = 6;
		try {
			testInput(inputs);
		} catch (StackOverflowError e) {
			fail("Unerwarteter Stackoverflow. Wurde Rekursion verwendet?");
		}
	}

	/**
	 * Tests the assignment against the given inputs and the exptected outputs providede by generateSolution()
	 * @param input
	 */
	public void testInput(int... input) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < input.length; i++) {
			b.append(input[i] + "\n");
		}
		testInput(b.toString(), generateSolution(input));
	}

	public void testInput(String input, String expectedOutput) {
		os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		System.setOut(ps);
		is = new ByteArrayInputStream(input.getBytes());
		System.setIn(is);
		String[] args = {};
		Calculator.main(args);
		String out = os.toString();
		assertEquals(true, (null != out && !out.isEmpty()), "Leere Ausgabe.");
		String output[] = out.split("\n|(\r\n)");
		String[] expected = expectedOutput.split("\n");
		assertEquals(true, expected.length == output.length,
				expected.length + " Zeilen Ausgabe erwartet, aber " + output.length + " Zeilen wurden ausgegeben.");
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], output[i]);
		}
	}
}