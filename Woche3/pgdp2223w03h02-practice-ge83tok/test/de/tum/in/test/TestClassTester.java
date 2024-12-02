package de.tum.in.test;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId.Segment;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Event;
import org.junit.platform.testkit.engine.Events;

/**
 * Required gradle dependencies:
 *
 * <pre>
 * testImplementation group: 'org.junit.platform', name: 'junit-platform-testkit', version: '1.9.0'
 * </pre>
 *
 * @author Christian Femers
 *
 */
public final class TestClassTester {

	private static final String ENGINE = "junit-jupiter";

	private final Class<?> testClass;
	private TestClassTester.TestResults results;

	public TestClassTester(Class<?> testClass) {
		this.testClass = Objects.requireNonNull(testClass, "testClass");
	}

	public TestClassTester.TestResults runTest() {
		results = TestResults.of(EngineTestKit.engine(ENGINE).selectors(selectClass(testClass)).execute());
		return results;
	}

	public TestClassTester.TestResults getResults() {
		return results;
	}

	public static TestClassTester forClass(Class<?> testClass) {
		return new TestClassTester(testClass);
	}

	public static final class TestResults {
		private static final BiPredicate<String, Event> HAS_NAME = (name, e) -> cleanMethodName(getMethodOf(e))
				.equals(cleanMethodName(name));
		private final Events tests;

		private TestResults(Events tests) {
			this.tests = tests;
		}

		public long countTotal() {
			return tests.finished().count();
		}

		public long countSuccessful() {
			return tests.succeeded().count();
		}

		public long countFailed() {
			return tests.failed().count();
		}

		public boolean allSuccessful() {
			return countTotal() == countSuccessful();
		}

		public boolean allFailed() {
			return countTotal() == countFailed();
		}

		/**
		 * @param methodName completely qualified method signature, e.g.
		 *                   <code>"testSomething()"</code> or
		 *                   <code>"otherTest(java.lang.String)"</code>
		 */
		public boolean testSuccessful(String methodName) {
			Objects.requireNonNull(methodName, "methodName");
			return tests.succeeded().stream().anyMatch(e -> HAS_NAME.test(methodName, e));
		}

		/**
		 * @param methodName completely qualified method signature, e.g.
		 *                   <code>"testSomething()"</code> or
		 *                   <code>"otherTest(java.lang.String)"</code>
		 */
		public boolean testFailed(String methodName) {
			Objects.requireNonNull(methodName, "methodName");
			return tests.failed().stream().anyMatch(e -> HAS_NAME.test(methodName, e));
		}

		/**
		 * @param methodName completely qualified method signature, e.g.
		 *                   <code>"testSomething()"</code> or
		 *                   <code>"otherTest(java.lang.String)"</code>
		 */
		public Optional<TestExecutionResult> getTestExecutionResult(String methodName) {
			Objects.requireNonNull(methodName, "methodName");
			return tests.finished().stream().filter(e -> HAS_NAME.test(methodName, e)).findFirst()
					.flatMap(e -> e.getPayload(TestExecutionResult.class));
		}

		/**
		 * @param methodName completely qualified method signature, e.g.
		 *                   <code>"testSomething()"</code> or
		 *                   <code>"otherTest(java.lang.String)"</code>
		 */
		public Optional<Throwable> getThrown(String methodName) {
			Objects.requireNonNull(methodName, "methodName");
			return getTestExecutionResult(methodName).flatMap(TestExecutionResult::getThrowable);
		}

		/**
		 * @param methodName completely qualified method signature, e.g.
		 *                   <code>"testSomething()"</code> or
		 *                   <code>"otherTest(java.lang.String)"</code>
		 */
		public boolean testFailedWith(String methodName, Class<? extends Throwable> thrown) {
			Objects.requireNonNull(methodName, "methodName");
			Objects.requireNonNull(thrown, "thrown");
			return getTestExecutionResult(methodName).flatMap(TestExecutionResult::getThrowable)
					.map(t -> thrown.isInstance(t)).orElse(false);
		}

		public Map<String, TestExecutionResult> getAllResults() {
			return tests.finished().stream().filter(Event.byPayload(TestExecutionResult.class, p -> true)).collect(
					Collectors.toMap(TestResults::getMethodOf, e -> e.getRequiredPayload(TestExecutionResult.class)));
		}

		private static String getMethodOf(Event e) {
			return e.getTestDescriptor().getUniqueId().getSegments().stream()
					.dropWhile(s -> !"class".equals(s.getType())).skip(1).map(Segment::getValue)
					.collect(Collectors.joining());
		}

		private static String cleanMethodName(String s) {
			return s.replaceAll("\\(\\)", "");
		}

		static TestClassTester.TestResults of(EngineExecutionResults engineRes) {
			return new TestResults(engineRes.testEvents());
		}

		@Override
		public String toString() {
			return getAllResults().toString();
		}
	}
}