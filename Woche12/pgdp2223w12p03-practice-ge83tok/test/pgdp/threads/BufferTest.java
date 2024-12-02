package pgdp.threads;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.Thread.State;

import de.tum.in.test.api.jupiter.PublicTest;
import de.tum.in.test.api.AllowThreads;
import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import org.junit.jupiter.api.DisplayName;

@MirrorOutput
@StrictTimeout(5)
@WhitelistPath("target")
@AllowThreads
public class BufferTest {
	@PublicTest
	@DisplayName(value = "Buffer Works Correctly as Exam Queue - 1 Exam")
	public void testInOut1() throws InterruptedException {
		Buffer b = new Buffer(5);
		Exam e1 = new Exam();
		b.produce(e1);
		assertSame(e1, b.consume(),
				"Ein- und Ausgabe des Puffers stimmen nicht überein");
	}

	@PublicTest
	@DisplayName(value = "Buffer Works Correctly as Exam Queue - 3 Exams")
	public void testInOut2() throws InterruptedException {
		Buffer b = new Buffer(5);
		Exam e1 = new Exam();
		Exam e2 = new Exam();
		Exam e3 = new Exam();
		b.produce(e1);
		b.produce(e2);
		b.produce(e3);
		assertSame(e1, b.consume(),
				"Ein- und Ausgabe des Puffers stimmen nicht überein");
		assertSame(e2, b.consume(),
				"Ein- und Ausgabe des Puffers stimmen nicht überein");
		assertSame(e3, b.consume(),
				"Ein- und Ausgabe des Puffers stimmen nicht überein");
	}

	@PublicTest
	@DisplayName(value = "Consume Waits on Empty Buffers")
	public void testWaitEmpty() throws InterruptedException {
		Buffer b = new Buffer(5);
		Thread t = new Thread(() -> {
			try {
				b.consume();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		t.start();
		Thread.sleep(500);
		assertEquals(State.WAITING, t.getState(),
				"Thread wartet nicht bei consume");
	}

	@PublicTest
	@DisplayName(value = "Produce Waits on Full Buffers")
	public void testWaitFull() throws InterruptedException {
		Buffer b = new Buffer(1);
		b.produce(new Exam());
		Thread t = new Thread(() -> {
			try {
				b.produce(new Exam());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		t.start();
		Thread.sleep(500);
		assertEquals(State.WAITING, t.getState(),
				"Thread wartet nicht bei produce");
	}

	@PublicTest
	@DisplayName(value = "Simultaneous Producing and Consuming Terminates")
	public void testLong() throws InterruptedException {
		Buffer b = new Buffer(50);
		Thread prod = new Thread(() -> {
			try {
				for (int i = 0; i < 10000; i++)
					b.produce(new Exam());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		Thread cons = new Thread(() -> {
			try {
				for (int i = 0; i < 10000; i++)
					b.consume();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		prod.start();
		cons.start();
		Thread.sleep(500);
		assertEquals(State.TERMINATED, prod.getState(), "Something went wrong");
		assertEquals(State.TERMINATED, cons.getState(), "Something went wrong");
	}
}