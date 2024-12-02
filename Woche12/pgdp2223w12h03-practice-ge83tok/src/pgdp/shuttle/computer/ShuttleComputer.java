package pgdp.shuttle.computer;

import pgdp.shuttle.tasks.ErrorProneTaskGenerator;
import pgdp.shuttle.tasks.ErrorlessTaskGenerator;
import pgdp.shuttle.tasks.TaskGenerator;

/**
 * This class assembles all ShuttleComputerComponents together
 */
public class ShuttleComputer extends Thread {

	private final long tasksToGenerate;
	private final TaskGenerator generator;
	private final long sleepTime;

	public ShuttleComputer(long tasksToGenerate, TaskGenerator generator, long sleepTime) {
		this.tasksToGenerate = tasksToGenerate;
		this.generator = generator;
		this.sleepTime = sleepTime;
	}

	@Override
	public void run() {
		// TODO
	}

	public static void main(String[] args) {
		boolean errorless = false;
		int tasks = 1000000;
		long sleepTime = 100;

		if (errorless) {
			// Random tasks with deterministic results
			ErrorlessTaskGenerator g = new ErrorlessTaskGenerator(42);
			ShuttleComputer sG = new ShuttleComputer(tasks, g, sleepTime);
			sG.start();
			try {
				sG.join();
			} catch (InterruptedException i) {
				i.printStackTrace();
			}
		} else {
			// Equal tasks with random result
			ErrorProneTaskGenerator e = new ErrorProneTaskGenerator(20);
			ShuttleComputer sE = new ShuttleComputer(tasks, e, sleepTime);
			sE.start();
			try {
				sE.join();
			} catch (InterruptedException i) {
				i.printStackTrace();
			}
			// task count output for ErrorProneTaskGenerator (may give a hint, if everything works as intended)
			System.out.println(e.getCount());
		}
	}
}
