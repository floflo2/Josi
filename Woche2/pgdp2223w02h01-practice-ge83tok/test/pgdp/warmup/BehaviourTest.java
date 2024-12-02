package pgdp.warmup;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.internal.PrivilegedException;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.PublicTest;

@TestClassAnnotation
public class BehaviourTest {

	private static Function<Integer, String> pInfoNegativeOut = (x) -> "Penguin " + x + " is not a known penguin!";
	private static Function<Integer, String> pInfoEvenOut = (x) -> "Penguin: " + x + "\nThis penguin is a male.";
	private static Function<Integer, String> pInfoOddOut = (x) -> "Penguin: " + x + "\nThis penguin is a female.";

	private static BiFunction<Integer, Integer, Integer> pEvoExp = (p, y) -> {
		boolean encountered7 = false;
		for (int i = 0; i < y; i++) {
			if (p != 1 && (p != 0) && ((p & (p - 1)) == 0)) {
				p = 1;
			} else if (p % 2 == 0) {
				p /= 2;
			} else if (p % 7 == 0) {
				if (!encountered7) {
					i += 5;
				} else {
					p = 3 * p + 1;
				}
				encountered7 = !encountered7;
			} else {
				p = 3 * p + 1;
			}
		}
		return p;
	};

	private static Function<Integer, Integer> pSumExp = (i) -> String.valueOf(i).chars().map(Character::getNumericValue)
			.sum();

	private static BiFunction<Integer, Integer, Long> pPerExp = (n, k) -> {
		long res = 1;
		for (long i = k + 1; i <= n; i++)
			res *= i;
		return res;
	};

	private static BiFunction<Integer, Integer, Long> pPowExp = (x, i) -> (long) Math.pow(x, i);

	private static Stream<Arguments> penguInfoArguments() {
		return Stream.of(Arguments.of(-100, pInfoNegativeOut.apply(-100)), Arguments.of(-1, pInfoNegativeOut.apply(-1)),
				Arguments.of(0, pInfoEvenOut.apply(0)), Arguments.of(2, pInfoEvenOut.apply(2)),
				Arguments.of(10, pInfoEvenOut.apply(10)), Arguments.of(1, pInfoOddOut.apply(1)),
				Arguments.of(3, pInfoOddOut.apply(3)), Arguments.of(9, pInfoOddOut.apply(9)));
	}

	@Hidden
	@DisplayName(value = "Test Individual - penguInfoOut(int)")
	@ParameterizedTest(name = "[{index}] Testing penguInfoOut({0})")
	@MethodSource("penguInfoArguments")
	public void penguInfoTest(int in, String exp, IOTester iot) {
		PenguWarmup.penguInfoOut(in);
		String out = iot.out().getLinesAsString().stream().collect(Collectors.joining("\n"));
		if (!out.equals(exp))
			TestUtils.privilegedFail("Test failed for input <" + in + ">.\nExpected: <" + exp + ">\nWas: <" + out
					+ ">\nFirst difference at index: <" + StringUtils.indexOfDifference(exp, out) + ">");
	}

	@PublicTest
	@DisplayName(value = "Test Summary - penguInfoOut(int)")
	public void penguInfoTestAll(IOTester iot) {
		penguInfoArguments().forEach(args -> {
			try {
				iot.reset();
				penguInfoTest((int) args.get()[0], (String) args.get()[1], iot);
			} catch (PrivilegedException e) {
				TestUtils.privilegedFail("At least one of the required tests failed.");
			} catch (Exception e) {
				TestUtils.privilegedFail("The test itself failed, please contact a tutor.");
			}
		});
	}

	private static Stream<Arguments> penguEvoArguments() {
		return Stream.of(Arguments.of(128, 2), Arguments.of(9, 10), Arguments.of(9, 11), Arguments.of(4, 10),
				Arguments.of(32, 10), Arguments.of(39, 5), Arguments.of(39, 9));
	}

	@Hidden
	@DisplayName(value = "Test Individual - penguEvolution(int, int)")
	@ParameterizedTest(name = "[{index}] Testing penguEvolution({0}, {1})")
	@MethodSource("penguEvoArguments")
	public void penguEvoTest(int p, int y) {
		int student = PenguWarmup.penguEvolution(p, y);
		int exp = pEvoExp.apply(p, y);
		if (exp != student)
			TestUtils.privilegedFail(
					"Test failed for input <" + p + ", " + y + ">.\nExpected: <" + exp + ">\nWas: <" + student + ">\n");
	}

	@PublicTest
	@DisplayName(value = "Test Summary - penguEvolution(int, int)")
	public void penguEvoTestAll() {
		penguEvoArguments().forEach(args -> {
			try {
				penguEvoTest((int) args.get()[0], (int) args.get()[1]);
			} catch (PrivilegedException e) {
				TestUtils.privilegedFail("At least one of the required tests failed.");
			} catch (Exception e) {
				TestUtils.privilegedFail("The test itself failed, please contact a tutor.");
			}
		});
	}

	private static Stream<Arguments> penguSumArguments() {
		return Stream.of(Arguments.of(128), Arguments.of(1337), Arguments.of(123456789), Arguments.of(987654321),
				Arguments.of(345012634));
	}

	@Hidden
	@DisplayName(value = "Test Individual - penguSum(int)")
	@ParameterizedTest(name = "[{index}] Testing penguSum({0})")
	@MethodSource("penguSumArguments")
	public void penguSumTest(int in) {
		int student = PenguWarmup.penguSum(in);
		int exp = pSumExp.apply(in);
		if (exp != student)
			TestUtils.privilegedFail(
					"Test failed for input <" + in + ">.\nExpected: <" + exp + ">\nWas: <" + student + ">\n");
	}

	@PublicTest
	@DisplayName(value = "Test Summary - penguSum(int)")
	public void penguSumTestAll() {
		penguSumArguments().forEach(args -> {
			try {
				penguSumTest((int) args.get()[0]);
			} catch (PrivilegedException e) {
				TestUtils.privilegedFail("At least one of the required tests failed.");
			} catch (Exception e) {
				TestUtils.privilegedFail("The test itself failed, please contact a tutor.");
			}
		});
	}

	private static Stream<Arguments> penguPerArguments() {
		return Stream.of(Arguments.of(1, 1), Arguments.of(6, 3), Arguments.of(14, 10), Arguments.of(21, 19),
				Arguments.of(20, 1));
	}

	@Hidden
	@DisplayName(value = "Test Individual - penguPermutation(int, int)")
	@ParameterizedTest(name = "[{index}] Testing penguPermutation({0}, {1})")
	@MethodSource("penguPerArguments")
	public void penguPerTest(int n, int k) {
		long student = PenguWarmup.penguPermutation(n, k);
		long exp = pPerExp.apply(n, k);
		if (exp != student)
			TestUtils.privilegedFail(
					"Test failed for input <" + n + ", " + k + ">.\nExpected: <" + exp + ">\nWas: <" + student + ">\n");
	}

	@PublicTest
	@DisplayName(value = "Test Summary - penguPermutation(int, int)")
	public void penguPerTestAll() {
		penguPerArguments().forEach(args -> {
			try {
				penguPerTest((int) args.get()[0], (int) args.get()[1]);
			} catch (PrivilegedException e) {
				TestUtils.privilegedFail("At least one of the required tests failed.");
			} catch (Exception e) {
				TestUtils.privilegedFail("The test itself failed, please contact a tutor.");
			}
		});
	}

	private static Stream<Arguments> penguPowArguments() {
		return Stream.of(Arguments.of(128, 1), Arguments.of(1337, 2), Arguments.of(3, 4), Arguments.of(46341, 2),
				Arguments.of(4, 8), Arguments.of(128, 0), Arguments.of(0, 0));
	}

	@Hidden
	@DisplayName(value = "Test Individual - penguPowers(int, int)")
	@ParameterizedTest(name = "[{index}] Testing penguPowers({0}, {1})")
	@MethodSource("penguPowArguments")
	public void penguPowTest(int x, int i) {
		long student = PenguWarmup.penguPowers(x, i);
		long exp = pPowExp.apply(x, i);
		if (exp != student)
			TestUtils.privilegedFail(
					"Test failed for input <" + x + ", " + i + ">.\nExpected: <" + exp + ">\nWas: <" + student + ">\n");
	}

	@PublicTest
	@DisplayName(value = "Test Summary - penguPowers(int, int)")
	public void penguPowTestAll() {
		penguPowArguments().forEach(args -> {
			try {
				penguPowTest((int) args.get()[0], (int) args.get()[1]);
			} catch (PrivilegedException e) {
				TestUtils.privilegedFail("At least one of the required tests failed.");
			} catch (Exception e) {
				TestUtils.privilegedFail("The test itself failed, please contact a tutor.");
			}
		});
	}
}
