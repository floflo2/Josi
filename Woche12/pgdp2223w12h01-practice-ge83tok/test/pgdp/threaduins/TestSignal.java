package pgdp.threaduins;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestSignal implements Signal {
	final private Lock l = new ReentrantLock();
	final private Condition notBlocked = l.newCondition();
	final private AtomicInteger awaitCounter = new AtomicInteger();
	final private AtomicInteger signalCounter = new AtomicInteger();

	public int getAwaitCounter() {
		return awaitCounter.get();
	}

	public int getSignalCounter() {
		return signalCounter.get();
	}

	@Override
	public void await() {
		l.lock();
		try {
			awaitCounter.incrementAndGet();
			notBlocked.await();
			signalCounter.incrementAndGet();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		} finally {
			l.unlock();
		}
	}

	public void signal() {
		l.lock();
		try {
			notBlocked.signal();
		} finally {
			l.unlock();
		}
	}
}
