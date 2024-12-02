package pgdp.array;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.platform.engine.TestExecutionResult;

import de.tum.in.test.TestClassTester;
import de.tum.in.test.TestClassTester.TestResults;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import pgdp.PinguLib;
import pgdp.array.testImplementations.*;
import pgdp.tests.ArrayTest;

@W03H02
class ArrayTestTest {
	private static boolean correctWasAccepted = false;

	@DisplayName("- | Public Test Correct Implementation")
	@PublicTest
	@Order(1)
	void testCorrectImplementation() {
		for(int i = 0; i < 2; i++) {
			TestResults results = executeArrayTest(new ArrayCorrect());
			assertTrue(results.allSuccessful(),
					"Deine Tests akzeptieren keine korrekte Implementierung:\n" + getTestResult(results));
		}
		correctWasAccepted = true;
	}

	@DisplayName("- | Print Wrong String Format")
	@HiddenTest
	@Order(2)
	void testPrintMissingBrackets() {
		assertFalse(executeArrayTest(new ArrayPrintWrongStringFormat()).allSuccessful(),
				"Du testest nicht, ob das Array mit Klammern ausgegeben wird.");
		assertFalse(executeArrayTest(new ArrayPrintTooManyCommas()).allSuccessful(),
				"Du testest nicht, dass am Ende kein Komma + Leerzeichen eingefügt wird.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Print Absolute Values")
	@HiddenTest
	@Order(3)
	void testPrintAbsoluteValues() {
		assertFalse(executeArrayTest(new ArrayPrintAbsoluteValues()).allSuccessful(),
				"Du testest nicht, ob negative Zahlen richtig geprintet werden.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Print Random Zero At End")
	@HiddenTest
	@Order(4)
	void testPrintRandomZeroAtEnd() {
		assertFalse(executeArrayTest(new ArrayPrintRandomZeroAtEnd()).allSuccessful(),
				"Du testest nicht, ob fälschlicherweise Zahlen angefügt werden.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Print Wrong Empty Array")
	@HiddenTest
	@Order(5)
	void testPrintWrongEmptyArray() {
		assertFalse(executeArrayTest(new ArrayPrintWrongEmptyArray()).allSuccessful(),
				"Du testest nicht, was bei einem leeren Array passiert.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Min and Max Missed Last Number")
	@HiddenTest
	@Order(6)
	void testMinAndMaxMissedLastNumber() {
		assertFalse(executeArrayTest(new ArrayMinAndMaxMissedLastNumber()).allSuccessful(),
				"Du testest nicht, ob alle Elemente verglichen werden.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Min and Max Mixed Up Min And Max")
	@HiddenTest
	@Order(7)
	void testMinAndMaxMixedUpMinAndMax() {
		assertFalse(executeArrayTest(new ArrayMinAndMaxMixedUpMinMax()).allSuccessful(),
				"Du testest nicht, ob Min und Max in der richtigen Reihenfolge ausgegeben werden.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Min and Max Wrong Empty Array")
	@HiddenTest
	@Order(8)
	void testMinAndMaxWrongEmptyArray() {
		assertFalse(executeArrayTest(new ArrayMinAndMaxWrongEmptyArray()).allSuccessful(),
				"Du testest nicht, ob das leere Array richtig behandelt wird.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Min and Max Wrong String Format")
	@HiddenTest
	@Order(9)
	void testMinAndMaxWrongStringFormat() {
		assertFalse(executeArrayTest(new ArrayMinAndMaxWrongStringFormat()).allSuccessful(),
				"Du testest nicht, ob der String das richtige Format hat.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Invert Even Arrays")
	@HiddenTest
	@Order(10)
	void testInvertEvenArrays() {
		assertFalse(executeArrayTest(new ArrayInvertEvenArrays()).allSuccessful(),
				"Du testest nicht auf einen Fehler, der nur bei Arrays gerader Länge passiert.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Invert Switched Only One Way")
	@HiddenTest
	@Order(11)
	void testInvertSwitchedOnlyOneWay() {
		assertFalse(executeArrayTest(new ArrayInvertSwitchedOnlyOneWay()).allSuccessful(),
				"Du testest nicht, ob die Elemente wirklich vertauscht, sondern nur von hinten nach vorne überschrieben werden.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Invert Wrong Boundary")
	@HiddenTest
	@Order(12)
	void testInvertWrongBoundary() {
		assertFalse(executeArrayTest(new ArrayInvertWrongBoundary()).allSuccessful(),
				"Du testest nicht, ob an der richtigen Stelle in der Schleife abgebrochen wird.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Intersect Invalid Length")
	@HiddenTest
	@Order(13)
	void testIntersectInvalidLength() {
		assertFalse(executeArrayTest(new ArrayIntersectInvalidLength()).allSuccessful(),
				"Du testest nicht, was bei einer length <= 0 passiert");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Intersect Length Unequal Array Length")
	@HiddenTest
	@Order(14)
	void testIntersectLengthUnequalArrayLength() {
		assertFalse(executeArrayTest(new ArrayIntersectLengthUnequalArrayLength()).allSuccessful(),
				"Du testest nicht, was bei einer length != a.length passiert");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Intersect Wrong Loop Conditions")
	@HiddenTest
	@Order(15)
	void testIntersectWrongLoopCondition() {
		assertFalse(executeArrayTest(new ArrayIntersectWrongLoopConditionArrayLength()).allSuccessful(),
				"Du testest nicht, was bei einer falschen Loop-Condition passiert");
		assertFalse(executeArrayTest(new ArrayIntersectWrongLoopConditionIntersectedLength()).allSuccessful(),
				"Du testest nicht, was bei einer falschen Loop-Condition passiert");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Linearize Exception")
	@HiddenTest
	@Order(16)
	void testLinearizeException() {
		assertFalse(executeArrayTest(new ArrayLinearizeException()).allSuccessful(),
				"Du testest nicht, was bei einem zu kleinen Array passiert.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Linearize Wrong Gap")
	@HiddenTest
	@Order(17)
	void testLinearizeWrongGap() {
		assertFalse(executeArrayTest(new ArrayLinearizeWrongGap()).allSuccessful(),
				"Du testest nicht, ob die neue Array-Länge die Summe der Längen der Input-Arrays ist.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Bubblesort Switching Order")
	@HiddenTest
	@Order(18)
	void testBubblesortSwitchingOrder() {
		assertFalse(executeArrayTest(new ArrayBubblesortSwitchingOrder()).allSuccessful(),
				"Du testest nicht, ob die Elemente korrekt vertauscht werden.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Bubblesort Wrong Loop Condition")
	@HiddenTest
	@Order(19)
	void testBubblesortWrongLoopCondition() {
		assertFalse(executeArrayTest(new ArrayBubblesortWrongLoopCondition()).allSuccessful(),
				"Du testest nicht, ob die Schleife richtig ist.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName("- | Bubblesort Wrong Starting Index")
	@HiddenTest
	@Order(20)
	void testBubblesortWrongStartingIndex() {
		assertFalse(executeArrayTest(new ArrayBubblesortWrongStratingIndex()).allSuccessful(),
				"Du testest nicht, ob die Schleife richtig initialisiert wird.");
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	/* ================================ GRADING ================================ */

	@DisplayName(value = "Grading - Print")
	@PublicTest
	@Order(21)
	void testGradingPrint() {
		try{
			executeTests(this::testPrintMissingBrackets, this::testPrintAbsoluteValues, this::testPrintRandomZeroAtEnd, this::testPrintWrongEmptyArray);
		} catch (AssertionError e) {
			fail("Deine Tests für die Methode 'print()' finden nicht alle falschen Implementierungen.");
		}
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName(value = "Grading - Min and Max")
	@PublicTest
	@Order(22)
	void testGradingMinAndMax() {
		try{
			executeTests(this::testMinAndMaxMixedUpMinAndMax, this::testMinAndMaxMissedLastNumber, this::testMinAndMaxWrongEmptyArray, this::testMinAndMaxWrongStringFormat);
		} catch (AssertionError e) {
			fail("Deine Tests für die Methode 'minAndMax()' finden nicht alle falschen Implementierungen.");
		}
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName(value = "Grading - Invert")
	@PublicTest
	@Order(23)
	void testGradingInvert() {
		try{
			executeTests(this::testInvertEvenArrays, this::testInvertWrongBoundary, this::testInvertSwitchedOnlyOneWay);
		} catch (AssertionError e) {
			fail("Deine Tests für die Methode 'invert()' finden nicht alle falschen Implementierungen.");
		}
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName(value = "Grading - Intersect")
	@PublicTest
	@Order(24)
	void testGradingIntersect() {
		try{
			executeTests(this::testIntersectInvalidLength, this::testIntersectLengthUnequalArrayLength, this::testIntersectWrongLoopCondition);
		} catch (AssertionError e) {
			fail("Deine Tests für die Methode 'intersect()' finden nicht alle falschen Implementierungen.");
		}
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName(value = "Grading - Linearize")
	@PublicTest
	@Order(25)
	void testGradingLinearize() {
		try{
			executeTests(this::testLinearizeException, this::testLinearizeWrongGap);
		} catch (AssertionError e) {
			fail("Deine Tests für die Methode 'linearize()' finden nicht alle falschen Implementierungen.");
		}
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	@DisplayName(value = "Grading - BubbleSort")
	@PublicTest
	@Order(23)
	void testGradingBubbleSort() {
		try{
			executeTests(this::testBubblesortSwitchingOrder, this::testBubblesortWrongLoopCondition, this::testBubblesortWrongStartingIndex);
		} catch (AssertionError e) {
			fail("Deine Tests für die Methode 'bubbleSort()' finden nicht alle falschen Implementierungen.");
		}
		assertTrue(correctWasAccepted,
				"Deine Tests akzeptieren keine korrekte Implementierung, wodurch keine Benotung stattfindet.");
	}

	/* ================================ HELPERS ================================ */

	private static TestResults executeArrayTest(ArrayInterface arrayImplementation) {
		Array.setArrayImplementation(arrayImplementation);
		TestClassTester tester = TestClassTester.forClass(ArrayTest.class);
		TestResults results = tester.runTest();
		if (results.countTotal() == 0) {
			fail("Tests wurden nicht ausgeführt, da JUnit keine gefunden hat... geh sicher, dass du @Test vor jedem Test benutzt hast!");
		}
		System.out.println(results.getAllResults().entrySet().stream()
				.map(ter -> ter.getKey() + ": " + ter.getValue().getThrowable().map(Throwable::toString))
				.collect(Collectors.joining(",\n ", "[", "]")));

		System.out.println("==== Finished, " + results.countSuccessful() + " successful out of " + results.countTotal()
				+ " Tests,  ====");
		return results;
	}

	private static String getTestResult(TestResults results) {
		return results.getAllResults().entrySet().stream()
				.map(result -> result.getKey() + " " + result.getValue().getStatus()
						+ (result.getValue().getStatus() == TestExecutionResult.Status.SUCCESSFUL ? ""
								: ": " + result.getValue().getThrowable().orElse(new Throwable("no error found???"))))
				.collect(Collectors.joining("\n")) + "\n";
	}

	private void executeTests(Runnable... tests) {
		for(Runnable test: tests) {
			test.run();
		}
		for(Runnable test: tests) {
			PinguLib.setup();
			test.run();
		}
	}
}
