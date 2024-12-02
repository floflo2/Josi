package pgdp.threads;

import java.util.concurrent.Semaphore;

public class Buffer {
	private Exam[] data;
	private Semaphore free;
	private Semaphore occupied;
	int first = 0, last = 0;

	public Buffer(int maxSize) {
		if (maxSize < 0)
			throw new IllegalArgumentException(
					"Buffer must have positive size!");
		data = new Exam[maxSize];
		// TODO: Vervollständige diesen Konstruktor
	}

	public Exam consume() throws InterruptedException {
		// TODO: Implementiere diese Methode
		// Remove before use:
		Thread.sleep(666);
		return null;
	}

	public void produce(Exam e) throws InterruptedException {
		// TODO: Implementiere diese Methode
		// Remove before use:
		Thread.sleep(666);
	}
}