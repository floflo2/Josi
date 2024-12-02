package pgdp.shuttle.computer;

import java.util.concurrent.LinkedBlockingQueue;

import pgdp.shuttle.tasks.ShuttleTask;

/**
 * This class evaluates tasks and then passes them to the TaskChecker until it is shut down.
 * Tasks in priorityTaskQueue are preferred over Tasks in taskQueue.
 */
public class ShuttleProcessor extends Thread implements ShuttleComputerComponent {
	private final LinkedBlockingQueue<ShuttleTask<?, ?>> taskQueue;
	private final LinkedBlockingQueue<ShuttleTask<?, ?>> priorityTaskQueue;
	private final TaskChecker checker;

	public ShuttleProcessor(TaskChecker checker) {
		taskQueue = new LinkedBlockingQueue<>();
		priorityTaskQueue = new LinkedBlockingQueue<>();
		this.checker = checker;
	}

	public void addTask(ShuttleTask<?, ?> task) throws InterruptedException {
		taskQueue.put(task);
	}

	public void addPriorityTask(ShuttleTask<?, ?> task) throws InterruptedException {
		priorityTaskQueue.put(task);
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
