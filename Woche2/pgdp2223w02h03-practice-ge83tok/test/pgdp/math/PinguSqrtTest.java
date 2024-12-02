package pgdp.math;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;

@W02H03
public class PinguSqrtTest {

	private static boolean usesComma;
	private static boolean alwaysTwoDecimals;
	private static int countPassed;

	@BeforeAll
	private static void setup() {
		usesComma = false;
		alwaysTwoDecimals = false;
		countPassed = 0;
	}

	// hidden tests
	@HiddenTest
	@DisplayName(value = "Test 0")
	@Order(2)
	public void testZero(IOTester iot) {
		PinguSqrt.sqrt(0);
		testSubmissionOutput(iot.out().getOutputAsString(), new TestPair(0, "Wurzel aus 0.0\n\n",
				"0\n" + "--------\n" + "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 0", "Ergebnis: 0.0"));
		countPassed++;
	}

	private static final List<TestPair> negativeInputs() {
		return List.of(new TestPair(-1, "Keine negativen Wurzeln!", "", ""),
				new TestPair(Integer.MIN_VALUE + 1, "Keine negativen Wurzeln!", "", ""));
	}

	@HiddenTest
	@DisplayName(value = "Test negative")
	@Order(2)
	public void testNegative(IOTester iot) {
		for (TestPair t : negativeInputs()) {
			PinguSqrt.sqrt(t.input);
			testSubmissionOutput(iot.out().getOutputAsString(), t);
			iot.reset();
		}
		countPassed++;
	}

	private static final List<TestPair> squareInputs() {
		return List.of(
				new TestPair(1, "Wurzel aus 1.0\n" + "\n",
						"1\n" + "--------\n" + "-1\n" + "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 1",
						"Ergebnis: 1.0"),
				new TestPair(9, "Wurzel aus 9.0\n" + "\n",
						"9\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "--------\n" + "Rest: 0\n"
								+ "neue Ergebnis Ziffer: 3",
						"Ergebnis: 3.0"),
				new TestPair(16, "Wurzel aus 16.0\n" + "\n",
						"16\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "-7\n" + "--------\n" + "Rest: 0\n"
								+ "neue Ergebnis Ziffer: 4",
						"Ergebnis: 4.0"),
				new TestPair(100, "Wurzel aus 100.0\n" + "\n",
						"1\n" + "--------\n" + "-1\n" + "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 1",
						"Ergebnis: 10.0"),
				new TestPair(225, "Wurzel aus 225.0\n" + "\n",
						"2\n" + "--------\n" + "-1\n" + "--------\n" + "Rest: 1\n" + "neue Ergebnis Ziffer: 1\n" + "\n"
								+ "125\n" + "--------\n" + "-21\n" + "-23\n" + "-25\n" + "-27\n" + "-29\n"
								+ "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 5",
						"Ergebnis: 15.0"),
				new TestPair(99856, "Wurzel aus 99856.0\n" + "\n",
						"9\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "--------\n" + "Rest: 0\n"
								+ "neue Ergebnis Ziffer: 3\n" + "\n" + "98\n" + "--------\n" + "-61\n" + "--------\n"
								+ "Rest: 37\n" + "neue Ergebnis Ziffer: 1\n" + "\n" + "3756\n" + "--------\n" + "-621\n"
								+ "-623\n" + "-625\n" + "-627\n" + "-629\n" + "-631\n" + "--------\n" + "Rest: 0\n"
								+ "neue Ergebnis Ziffer: 6",
						"Ergebnis: 316.0"),
				new TestPair(999950884, "Wurzel aus 9.99950884E8\n" + "\n",
						"9\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "--------\n" + "Rest: 0\n"
								+ "neue Ergebnis Ziffer: 3\n" + "\n" + "99\n" + "--------\n" + "-61\n" + "--------\n"
								+ "Rest: 38\n" + "neue Ergebnis Ziffer: 1\n" + "\n" + "3895\n" + "--------\n" + "-621\n"
								+ "-623\n" + "-625\n" + "-627\n" + "-629\n" + "-631\n" + "--------\n" + "Rest: 139\n"
								+ "neue Ergebnis Ziffer: 6\n" + "\n" + "13908\n" + "--------\n" + "-6321\n" + "-6323\n"
								+ "--------\n" + "Rest: 1264\n" + "neue Ergebnis Ziffer: 2\n" + "\n" + "126484\n"
								+ "--------\n" + "-63241\n" + "-63243\n" + "--------\n" + "Rest: 0\n"
								+ "neue Ergebnis Ziffer: 2",
						"Ergebnis: 31622.0"));
	}

	@HiddenTest
	@DisplayName(value = "Test squares")
	@Order(2)
	public void testSquares(IOTester iot) {
		for (TestPair t : squareInputs()) {
			PinguSqrt.sqrt(t.input);
			testSubmissionOutput(iot.out().getOutputAsString(), t);
			iot.reset();
		}
		countPassed++;
	}

	private static final List<TestPair> fpLE4DPInputs() {
		return List.of(
				new TestPair(2.25, "Wurzel aus 2.25\n" + "\n",
						"2\n" + "--------\n" + "-1\n" + "--------\n" + "Rest: 1\n" + "neue Ergebnis Ziffer: 1\n" + "\n"
								+ "125\n" + "--------\n" + "-21\n" + "-23\n" + "-25\n" + "-27\n" + "-29\n"
								+ "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 5",
						"Ergebnis: 1.5"),
				new TestPair(0.0225, "Wurzel aus 0.0225\n" + "\n",
						"2\n" + "--------\n" + "-1\n" + "--------\n" + "Rest: 1\n" + "neue Ergebnis Ziffer: 1\n" + "\n"
								+ "125\n" + "--------\n" + "-21\n" + "-23\n" + "-25\n" + "-27\n" + "-29\n"
								+ "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 5",
						"Ergebnis: 0.15"),
				new TestPair(1234.5678, "Wurzel aus 1234.5678\n" + "\n",
						"12\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "--------\n" + "Rest: 3\n"
								+ "neue Ergebnis Ziffer: 3\n" + "\n" + "334\n" + "--------\n" + "-61\n" + "-63\n"
								+ "-65\n" + "-67\n" + "-69\n" + "--------\n" + "Rest: 9\n" + "neue Ergebnis Ziffer: 5\n"
								+ "\n" + "956\n" + "--------\n" + "-701\n" + "--------\n" + "Rest: 255\n"
								+ "neue Ergebnis Ziffer: 1\n" + "\n" + "25578\n" + "--------\n" + "-7021\n" + "-7023\n"
								+ "-7025\n" + "--------\n" + "Rest: 4509\n" + "neue Ergebnis Ziffer: 3",
						"Ergebnis: 35.13"),
				new TestPair(42069.1337, "Wurzel aus 42069.1337\n" + "\n",
						"4\n" + "--------\n" + "-1\n" + "-3\n" + "--------\n" + "Rest: 0\n"
								+ "neue Ergebnis Ziffer: 2\n" + "\n" + "20\n" + "--------\n" + "--------\n"
								+ "Rest: 20\n" + "neue Ergebnis Ziffer: 0\n" + "\n" + "2069\n" + "--------\n" + "-401\n"
								+ "-403\n" + "-405\n" + "-407\n" + "-409\n" + "--------\n" + "Rest: 44\n"
								+ "neue Ergebnis Ziffer: 5\n" + "\n" + "4413\n" + "--------\n" + "-4101\n"
								+ "--------\n" + "Rest: 312\n" + "neue Ergebnis Ziffer: 1\n" + "\n" + "31237\n"
								+ "--------\n" + "--------\n" + "Rest: 31237\n" + "neue Ergebnis Ziffer: 0",
						"Ergebnis: 205.1"),
				new TestPair(900000.0009, "Wurzel aus 900000.0009\n" + "\n",
						"90\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "-7\n" + "-9\n" + "-11\n" + "-13\n" + "-15\n"
								+ "-17\n" + "--------\n" + "Rest: 9\n" + "neue Ergebnis Ziffer: 9\n" + "\n" + "900\n"
								+ "--------\n" + "-181\n" + "-183\n" + "-185\n" + "-187\n" + "--------\n"
								+ "Rest: 164\n" + "neue Ergebnis Ziffer: 4\n" + "\n" + "16400\n" + "--------\n"
								+ "-1881\n" + "-1883\n" + "-1885\n" + "-1887\n" + "-1889\n" + "-1891\n" + "-1893\n"
								+ "-1895\n" + "--------\n" + "Rest: 1296\n" + "neue Ergebnis Ziffer: 8\n" + "\n"
								+ "129600\n" + "--------\n" + "-18961\n" + "-18963\n" + "-18965\n" + "-18967\n"
								+ "-18969\n" + "-18971\n" + "--------\n" + "Rest: 15804\n" + "neue Ergebnis Ziffer: 6\n"
								+ "\n" + "1580409\n" + "--------\n" + "-189721\n" + "-189723\n" + "-189725\n"
								+ "-189727\n" + "-189729\n" + "-189731\n" + "-189733\n" + "-189735\n" + "--------\n"
								+ "Rest: 62585\n" + "neue Ergebnis Ziffer: 8",
						"Ergebnis: 948.68"));
	}

	@HiddenTest
	@DisplayName(value = "Test floating point simple")
	@Order(2)
	public void testFloatingPointLE4DP(IOTester iot) {
		for (TestPair t : fpLE4DPInputs()) {
			PinguSqrt.sqrt(t.input);
			testSubmissionOutput(iot.out().getOutputAsString(), t);
			iot.reset();
		}
		countPassed++;
	}

	private static final List<TestPair> fpG4DPInputs() {
		return List.of(
				new TestPair(4.00009, "Wurzel aus 4.00009\n" + "\n",
						"4\n" + "--------\n" + "-1\n" + "-3\n" + "--------\n" + "Rest: 0\n"
								+ "neue Ergebnis Ziffer: 2\n",
						"Ergebnis: 2.0"),
				new TestPair(2950.662412345, "Wurzel aus 2950.662412345\n" + "\n",
						"29\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "-7\n" + "-9\n" + "--------\n" + "Rest: 4\n"
								+ "neue Ergebnis Ziffer: 5\n" + "\n" + "450\n" + "--------\n" + "-101\n" + "-103\n"
								+ "-105\n" + "-107\n" + "--------\n" + "Rest: 34\n" + "neue Ergebnis Ziffer: 4\n" + "\n"
								+ "3466\n" + "--------\n" + "-1081\n" + "-1083\n" + "-1085\n" + "--------\n"
								+ "Rest: 217\n" + "neue Ergebnis Ziffer: 3\n" + "\n" + "21724\n" + "--------\n"
								+ "-10861\n" + "-10863\n" + "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 2",
						"Ergebnis: 54.32"),
				new TestPair(1.23456789, "Wurzel aus 1.23456789\n" + "\n",
						"1\n" + "--------\n" + "-1\n" + "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 1\n" + "\n"
								+ "23\n" + "--------\n" + "-21\n" + "--------\n" + "Rest: 2\n"
								+ "neue Ergebnis Ziffer: 1\n" + "\n" + "245\n" + "--------\n" + "-221\n" + "--------\n"
								+ "Rest: 24\n" + "neue Ergebnis Ziffer: 1",
						"Ergebnis: 1.11"));
	}

	@HiddenTest
	@DisplayName(value = "Test floating point cut")
	@Order(2)
	public void testFloatingPointG4DP(IOTester iot) {
		for (TestPair t : fpG4DPInputs()) {
			PinguSqrt.sqrt(t.input);
			testSubmissionOutput(iot.out().getOutputAsString(), t);
			iot.reset();
		}
		countPassed++;
	}

	@HiddenTest
	@DisplayName(value = "Test max value")
	@Order(2)
	public void testMaxValue(IOTester iot) {
		PinguSqrt.sqrt(Integer.MAX_VALUE);
		testSubmissionOutput(iot.out().getOutputAsString(), new TestPair(Integer.MAX_VALUE,
				"Wurzel aus 2.147483647E9\n" + "\n",
				"21\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "-7\n" + "--------\n" + "Rest: 5\n"
						+ "neue Ergebnis Ziffer: 4\n" + "\n" + "547\n" + "--------\n" + "-81\n" + "-83\n" + "-85\n"
						+ "-87\n" + "-89\n" + "-91\n" + "--------\n" + "Rest: 31\n" + "neue Ergebnis Ziffer: 6\n" + "\n"
						+ "3148\n" + "--------\n" + "-921\n" + "-923\n" + "-925\n" + "--------\n" + "Rest: 379\n"
						+ "neue Ergebnis Ziffer: 3\n" + "\n" + "37936\n" + "--------\n" + "-9261\n" + "-9263\n"
						+ "-9265\n" + "-9267\n" + "--------\n" + "Rest: 880\n" + "neue Ergebnis Ziffer: 4\n" + "\n"
						+ "88047\n" + "--------\n" + "--------\n" + "Rest: 88047\n" + "neue Ergebnis Ziffer: 0\n" + "\n"
						+ "8804700\n" + "--------\n" + "-926801\n" + "-926803\n" + "-926805\n" + "-926807\n"
						+ "-926809\n" + "-926811\n" + "-926813\n" + "-926815\n" + "-926817\n" + "--------\n"
						+ "Rest: 463419\n" + "neue Ergebnis Ziffer: 9\n" + "\n" + "46341900\n" + "--------\n"
						+ "-9268181\n" + "-9268183\n" + "-9268185\n" + "-9268187\n" + "-9268189\n" + "--------\n"
						+ "Rest: 975\n" + "neue Ergebnis Ziffer: 5",
				"Ergebnis: 46340.95"));
		countPassed++;
	}

	// public tests
	@PublicTest
	@DisplayName(value = "Test simple example")
	@Order(1)
	public void testSimpleExample(IOTester iot) {
		PinguSqrt.sqrt(4);
		testSubmissionOutput(iot.out().getOutputAsString(),
				new TestPair(4, "Wurzel aus 4.0\n" + "\n",
						"4\n" + "--------\n" + "-1\n" + "-3\n" + "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 2",
						"Ergebnis: 2.0"));
	}

	@PublicTest
	@DisplayName(value = "Test complex example")
	@Order(1)
	public void testComplexExample(IOTester iot) {
		PinguSqrt.sqrt(1049.76);
		testSubmissionOutput(iot.out().getOutputAsString(), new TestPair(1049.76, "Wurzel aus 1049.76\n" + "\n",
				"10\n" + "--------\n" + "-1\n" + "-3\n" + "-5\n" + "--------\n" + "Rest: 1\n"
						+ "neue Ergebnis Ziffer: 3\n" + "\n" + "149\n" + "--------\n" + "-61\n" + "-63\n" + "--------\n"
						+ "Rest: 25\n" + "neue Ergebnis Ziffer: 2\n" + "\n" + "2576\n" + "--------\n" + "-641\n"
						+ "-643\n" + "-645\n" + "-647\n" + "--------\n" + "Rest: 0\n" + "neue Ergebnis Ziffer: 4",
				"Ergebnis: 32.4"));
	}

	private static void testSubmissionOutput(String studentString, TestPair solution) {
		studentString = studentString.trim();
		if (studentString.length() < solution.outputStart.length() + solution.outputMid.length()
				+ solution.outputEnd.length()) {
			fail("Student output is too short!\nexpected:\n" + solution.outputStart + "\n" + solution.outputMid + "\n"
					+ solution.outputEnd + "\nbut was:\n" + studentString);
		}
		if (!studentString.startsWith(solution.outputStart)) {
			if (studentString.startsWith(solution.outputStart.replace('.', ','))) {
				usesComma = true;
			} else {
				fail("Student output does not start with correct calculations!\nExpected: " + solution.outputStart
						+ "\nWas: " + studentString.substring(0, solution.outputStart.length())
						+ "\ncomplete student output:\n" + studentString);
			}
		}
		if (!studentString.contains(solution.outputMid)) {
			fail("Student output does not contain the following substring:\n" + solution.outputMid + "\nexpected:\n"
					+ solution.outputStart + "\n" + solution.outputMid + "\n" + solution.outputEnd + "\nbut was:\n"
					+ studentString);
		}
		if (!studentString.endsWith(solution.outputEnd)) {
			if (studentString.endsWith(solution.outputEnd.replace('.', ','))) {
				usesComma = true;
			} else if (studentString.substring(0, studentString.length() - 1).endsWith(solution.outputEnd)
					&& studentString.charAt(studentString.length() - 1) == '0') {
				alwaysTwoDecimals = true;
			} else if (studentString.substring(0, studentString.length() - 1).endsWith(
					solution.outputEnd.replace('.', ',')) && studentString.charAt(studentString.length() - 1) == '0') {
				usesComma = true;
				alwaysTwoDecimals = true;
			} else {
				fail("Student output does not end correctly!\nExpected: " + solution.outputEnd + "\nWas: "
						+ studentString.substring(studentString.length() - solution.outputEnd.length() - 1)
						+ "\ncomplete student output:\n" + studentString);
			}
		}
	}

	private static record TestPair(double input, String outputStart, String outputMid, String outputEnd) {
	}

	// grading
	private String getGradingString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Not enough points for this test:\n");
		sb.append("Hidden tests passed (1 point each): ").append(countPassed).append("\n");
		sb.append("Deduction: \",\" used instead of \".\"? ").append(usesComma ? "Yes, -1 point" : "No").append("\n");
		sb.append("Deduction: result with second decimal point equal to zero? ")
				.append(alwaysTwoDecimals ? "Yes, -1 point" : "No").append("\n");
		sb.append("Total points: ").append(getTotalPoints());
		return sb.toString();
	}

	private int getTotalPoints() {
		return Math.max(0, countPassed - (usesComma ? 1 : 0) - (alwaysTwoDecimals ? 1 : 0));
	}

	@HiddenTest
	@DisplayName("Grading Test 1")
	public void pointTest1() {
		if (getTotalPoints() < 1) {
			fail(getGradingString());
		}
	}

	@HiddenTest
	@DisplayName("Grading Test 2")
	public void pointTest2() {
		if (getTotalPoints() < 2) {
			fail(getGradingString());
		}
	}

	@HiddenTest
	@DisplayName("Grading Test 3")
	public void pointTest3() {
		if (getTotalPoints() < 3) {
			fail(getGradingString());
		}
	}

	@HiddenTest
	@DisplayName("Grading Test 4")
	public void pointTest4() {
		if (getTotalPoints() < 4) {
			fail(getGradingString());
		}
	}

	@HiddenTest
	@DisplayName("Grading Test 5")
	public void pointTest5() {
		if (getTotalPoints() < 5) {
			fail(getGradingString());
		}
	}

	@HiddenTest
	@DisplayName("Grading Test 6")
	public void pointTest6() {
		if (getTotalPoints() < 6) {
			fail(getGradingString());
		}
	}
}
