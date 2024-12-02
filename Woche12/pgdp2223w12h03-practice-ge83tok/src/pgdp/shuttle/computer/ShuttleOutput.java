package pgdp.shuttle.computer;

import java.util.concurrent.LinkedBlockingQueue;

import pgdp.shuttle.tasks.ShuttleTask;

/**
 *	This class outputs Task results until it's shut down.
 */
public class ShuttleOutput extends Thread implements ShuttleComputerComponent {
	private final LinkedBlockingQueue<ShuttleTask<?, ?>> taskQueue;

	public ShuttleOutput() {
		taskQueue = new LinkedBlockingQueue<>();
	}

	public void addTask(ShuttleTask<?, ?> task) throws InterruptedException {
		taskQueue.put(task);
	}

	@Override
	public void run() {
		// TODO
	}

	@Override
	public void shutDown() {
		// TODO
	}
}
