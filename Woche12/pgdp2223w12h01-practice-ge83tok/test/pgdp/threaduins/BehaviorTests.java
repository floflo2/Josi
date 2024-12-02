package pgdp.threaduins;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.State;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;

@TestClassAnnotation
public class BehaviorTests {

	private final String LINE_SEPARATOR_REGEX = "(\r)?(\n)";

	private TestSignal signal;

	private Throwable throwable;
	private final Thread.UncaughtExceptionHandler exHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread th, Throwable ex) {
			throwable = ex;
		}
	};

	@BeforeEach
	void setup() {
		// reset signal
		signal = new TestSignal();
		Threaduins.setSignal(signal);

		// reset uncaught exception
		throwable = null;
	}

	private void checkUncaughtExceptions() {
		if (throwable != null) {
			fail("Uncaught exception: " + throwable);
		}
	}

	private void testSummary(String task, Runnable... tests) {
		try {
			for (Runnable test : tests) {
				setup();
				test.run();
			}
		} catch (final AssertionError e) {
			final String msg = "At least one of the required tests for " + task + " failed.";
			fail(msg);
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// TEST getWorcaholic
	// ------------------------------------------------------------------------------------------------------

	@HiddenTest
	@DisplayName(value = "Test getWorkaholic - Stop Message")
	@Order(1)
	void testGetWorkaholicContainsStopMSG() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final PrintStream stream = new PrintStream(out);
		final Thread t = Threaduins.getWorkaholic(stream);
		assertNotNull(t, "No Thread returned.");
		t.start();
		// NEVER REMOVE THIS
		// JUST DON'T!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		System.out.println("This is a very very useless output to get some computation time.");
		t.interrupt();
		try {
			t.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		final String regex = "(" + Pattern.quote(Threaduins.WORKAHOLIC_WORKING_MSG) + LINE_SEPARATOR_REGEX + ")*"
				+ Pattern.quote(Threaduins.WORKAHOLIC_STOP_MSG) + LINE_SEPARATOR_REGEX;
		assertThat("Messages sent to the given PrintStream don't match.", out.toString(),
				Matchers.matchesPattern(regex));
	}

	private void testGetWorkaholicContainsWorkingMSGSingleRun() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final PrintStream stream = new PrintStream(out);
		final Thread t = Threaduins.getWorkaholic(stream);
		assertNotNull(t, "No Thread returned.");
		t.start();
		// NEVER REMOVE THIS
		// JUST DON'T!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		System.out.println("This is a very very useless output to get some computation time.");
		t.interrupt();
		try {
			t.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		final String regex = "(" + Pattern.quote(Threaduins.WORKAHOLIC_WORKING_MSG) + LINE_SEPARATOR_REGEX + ")+"
				+ Pattern.quote(Threaduins.WORKAHOLIC_STOP_MSG) + LINE_SEPARATOR_REGEX;
		assertThat("Messages sent to the given PrintStream don't match.", out.toString(),
				Matchers.matchesPattern(regex));
	}

	@HiddenTest
	@DisplayName(value = "Test getWorkaholic - Working Message")
	@Order(2)
	void testGetWorkaholicContainsWorkingMSG() {
		assertNotNull(Threaduins.getWorkaholic(null), "No Thread returned.");
		boolean passedOnce = false;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				testGetWorkaholicContainsWorkingMSGSingleRun();
				passedOnce = true;
				break;
			} catch (final AssertionError e) {

				continue;
			}
		}
		if (!passedOnce) {
			fail("None of the Workaholic penguins sent his work message to the given PrintStream.");
		}
	}

	@PublicTest
	@DisplayName(value = "Test getWorkaholic - Summary")
	@Order(3)
	void testGetWorkaholicSummary() {
		try {
			testGetWorkaholicContainsStopMSG();
			testGetWorkaholicContainsWorkingMSG();
		} catch (final AssertionError e) {
			String msg = "At least one of the required tests for getWorkaholic failed.";
			if (e.getMessage()
					.equals("None of the Workaholic penguins sent his work message to the given PrintStream.")) {
				msg += "\nCAUTION:\n"
						+ "One of the failing tests is non deterministic. Check if the tests passed before and try "
						+ "to resubmit your solution. If this test reports a false negative, you must send us an "
						+ "complaint after the deadline.";
			}
			fail(msg);
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// TEST stopWorcaholic
	// ------------------------------------------------------------------------------------------------------

	@HiddenTest
	@DisplayName(value = "Test stopWorkaholic - Started")
	@Order(4)
	void testStopWorkaholicStarted() {
		final AtomicBoolean threadStarted = new AtomicBoolean();
		final Thread t = new Thread() {
			@Override
			public void start() {
				threadStarted.set(true);
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopWorkaholic(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			signal.signal();
			assertTrue(threadStarted.get(), "Thread was not started.");
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopWorkaholic - Interrupted")
	@Order(5)
	void testStopWorkaholicInterrupt() {
		final AtomicBoolean threadInterrupted = new AtomicBoolean();
		final Thread t = new Thread() {
			@Override
			public void interrupt() {
				threadInterrupted.set(true);
				System.out.println("Thread interrupted!");
				super.interrupt();
			}

			@Override
			public void run() {
				while (!isInterrupted()) {
					System.out.println("Not Interrupted 1");
				}
				System.out.println("Interrupted");
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopWorkaholic(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			signal.signal();
			Thread.sleep(1000);
			assertTrue(threadInterrupted.get(), "Thread was not interrupted.");
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopWorkaholic - Terminated")
	@Order(6)
	void testStopWorkaholicTerminated() {
		final Thread t = new Thread() {
			@Override
			public void run() {
				while (!isInterrupted()) {
					System.out.println("Not Interrupted 2");
				}
				System.out.println("Interrupted");
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopWorkaholic(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			signal.signal();
			Thread.sleep(1000);
			assertTrue(t.getState() == State.TERMINATED, "Thread was not terminated correctly.");
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopWorkaholic - await Signal")
	@Order(7)
	void testStopWorkaholicAwaitSignal() {
		final Thread t = new Thread() {
			@Override
			public void run() {
				while (!isInterrupted()) {
					System.out.println("Not Interrupted 3");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						interrupt();
					}
				}
				System.out.println("Interrupted");
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopWorkaholic(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			assertEquals(1, signal.getAwaitCounter(), "Not waiting for KillSignal.");
			assertEquals(0, signal.getSignalCounter(), "KillSignal was not sent yet.");
			signal.signal();
			Thread.sleep(1000);
			assertEquals(1, signal.getAwaitCounter(), "Not waiting for KillSignal.");
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopWorkaholic - Joined")
	@Order(8)
	void testStopWorkaholicJoined(IOTester iot) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				while (!isInterrupted()) {
					System.out.println("Not Interrupted");
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
							interrupt();
						}
					}
				}
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Interrupted");
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopWorkaholic(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			for (int i = 0; i < 3; i++) {
				synchronized (t) {
					t.notify();
				}
				Thread.sleep(500);
			}
			signal.signal();
			Thread.sleep(500);
			synchronized (t) {
				t.notify();
			}
			s.join();
			iot.out().assertLinesMatch("Workaholic is not properly joined.", Threaduins.STOP_MSG, "Not Interrupted",
					"Not Interrupted", "Not Interrupted", "Not Interrupted", "Interrupted", Threaduins.STOPPED_MSG);
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopWorkaholic - Console Output")
	@Order(9)
	void testStopWorkaholicOutput(IOTester iot) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				while (!isInterrupted()) {
				}
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopWorkaholic(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			iot.out().assertLinesMatch("Output doesn't match.", Threaduins.STOP_MSG);
			signal.signal();
			Thread.sleep(1000);
			iot.out().assertLinesMatch("Output doesn't match.", Threaduins.STOP_MSG, Threaduins.STOPPED_MSG);
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@PublicTest
	@DisplayName(value = "Test stopWorkaholic - Summary")
	@Order(10)
	@StrictTimeout(20)
	void testStopWorkaholicSummary(IOTester iot) {
		Runnable[] tests = { () -> testStopWorkaholicStarted(), () -> testStopWorkaholicInterrupt(),
				() -> testStopWorkaholicTerminated(), () -> testStopWorkaholicAwaitSignal(), () -> {
					iot.reset();
					testStopWorkaholicJoined(iot);
				}, () -> {
					iot.reset();
					testStopWorkaholicOutput(iot);
				} };
		testSummary("stopWorkaholic", tests);
	}

	// ------------------------------------------------------------------------------------------------------
	// TEST getLuckyProcrastinator
	// ------------------------------------------------------------------------------------------------------

	@HiddenTest
	@DisplayName(value = "Test getLuckyProcrastinator - Messages")
	@Order(11)
	@StrictTimeout(10)
	void testGetLuckyProcrastinatorIsProcrastinating() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final PrintStream stream = new PrintStream(out);
		final Thread t = Threaduins.getLuckyProcrastinator(stream);
		assertNotNull(t, "No Thread returned.");
		t.start();
		String regex = Pattern.quote(Threaduins.PROCRASTINATOR_PROCRASTINATING_MSG) + LINE_SEPARATOR_REGEX;
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			assertThat("Messages sent to the given PrintStream don't match.", out.toString(),
					Matchers.matchesPattern(regex));
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (t) {
			t.notify();
		}
		try {
			t.join();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
		regex += Pattern.quote(Threaduins.LUCKY_PROCRASTINATOR_WORKING_MSG) + LINE_SEPARATOR_REGEX;
		assertThat("Messages sent to the given PrintStream don't match.", out.toString(),
				Matchers.matchesPattern(regex));
	}

	@PublicTest
	@DisplayName(value = "Test getLuckyProcrastinator - Summary")
	@Order(12)
	@StrictTimeout(10)
	void testGetLuckyProcrastinatorSummary() {
		try {
			testGetLuckyProcrastinatorIsProcrastinating();
		} catch (final AssertionError e) {
			String msg = "At least one of the required tests for getLuckyProcrastinator failed.";
			fail(msg);
		}
	}

	// ------------------------------------------------------------------------------------------------------
	// TEST stopProcrastinator
	// ------------------------------------------------------------------------------------------------------

	@HiddenTest
	@DisplayName(value = "Test stopProcrastinator - Started")
	@Order(13)
	void testStopProcrastinatorStarted() {
		final AtomicBoolean threadStarted = new AtomicBoolean();
		final Thread t = new Thread() {
			@Override
			public void start() {
				threadStarted.set(true);
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopProcrastinator(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			signal.signal();
			assertTrue(threadStarted.get(), "Thread was not started.");
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopProcrastinator - Terminated")
	@Order(14)
	void testStopProcrastinatorTerminated() {
		final AtomicBoolean threadInterrupted = new AtomicBoolean();
		final Thread t = new Thread() {
			@Override
			public void run() {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						threadInterrupted.set(true);
						e.printStackTrace();
						interrupt();
					}
				}
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopProcrastinator(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			signal.signal();
			Thread.sleep(1000);
			assertFalse(threadInterrupted.get(), "Thread should be notified not forcefully interrupted.");
			assertTrue(t.getState() == State.TERMINATED, "Thread was not terminated correctly.");
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopProcrastinator - await Signal")
	@Order(15)
	void testStopProcrastinatorAwaitSignal() {
		final Thread t = new Thread() {
			@Override
			public void run() {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						interrupt();
					}
				}
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopProcrastinator(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			assertEquals(1, signal.getAwaitCounter(), "Not waiting for KillSignal.");
			assertEquals(0, signal.getSignalCounter(), "KillSignal was not sent yet.");
			signal.signal();
			Thread.sleep(1000);
			assertEquals(1, signal.getAwaitCounter(), "Not waiting for KillSignal.");
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopProcrastinator - Joined")
	@Order(16)
	void testStopProcrastinatorJoined(IOTester iot) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						wait();
					}
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Let me finish!");
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopProcrastinator(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(500);
			signal.signal();
			s.join();
			iot.out().assertLinesMatch("Procrastinator is not properly joined.", Threaduins.STOP_MSG, "Let me finish!",
					Threaduins.STOPPED_MSG);
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@HiddenTest
	@DisplayName(value = "Test stopProcrastinator - Console Output")
	@Order(17)
	void testStopProcrastinatorOutput(IOTester iot) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						interrupt();
					}
				}
			}
		};
		final Thread s = new Thread() {
			@Override
			public void run() {
				Threaduins.stopProcrastinator(t);
			}
		};
		s.setUncaughtExceptionHandler(exHandler);
		try {
			s.start();
			Thread.sleep(1000);
			iot.out().assertLinesMatch("Output doesn't match.", Threaduins.STOP_MSG);
			signal.signal();
			Thread.sleep(1000);
			iot.out().assertLinesMatch("Output doesn't match.", Threaduins.STOP_MSG, Threaduins.STOPPED_MSG);
		} catch (final InterruptedException e) {
			fail("Test was interrupted: " + e);
		} finally {
			if (t.isAlive()) {
				t.interrupt();
			}
			checkUncaughtExceptions();
			if (s.isAlive()) {
				s.interrupt();
			}
		}
	}

	@PublicTest
	@DisplayName(value = "Test stopProcrastinator - Summary")
	@Order(18)
	@StrictTimeout(10)
	void testStopProcrastinatorSummary(IOTester iot) {
		Runnable[] tests = { () -> testStopProcrastinatorStarted(), () -> testStopProcrastinatorTerminated(),
				() -> testStopProcrastinatorAwaitSignal(), () -> {
					iot.reset();
					testStopProcrastinatorJoined(iot);
				}, () -> {
					iot.reset();
					testStopProcrastinatorOutput(iot);
				} };
		testSummary("stopProcrastinator", tests);
	}
}
