package pgdp.threads;

public class Tutor implements Runnable {
	private Buffer in;
	private Buffer out;
	private int taskNumber;
	private Buffer finished;

	public Tutor(Buffer in, Buffer out, int taskNumber, Buffer finished) {
		this.in = in;
		this.out = out;
		this.taskNumber = taskNumber;
		this.finished = finished;
	}

	@Override
	public void run() {
		while (true) {
			// TODO: Implementiere die run-Methode, sodass der Tutor je eine neue Exam aus seinem in-Buffer nimmt,
			//  korrigiert und auf den out-Buffer bzw. den finished-Buffer ablegt.
		}
	}

	private boolean correctExam(Exam exam, int exercise) {
		int currentPoints = exam.getPoints()[taskNumber - 1];
		if (currentPoints < 0) {
			// first correction
			exam.setPoints(taskNumber, CorrectionScheme.points(taskNumber,
					exam.getAnswer(taskNumber)));
			return false;
		} else {
			// second correction
			if (Math.random() < 0.1) {
				if (currentPoints == 0 || Math.random() < 0.5) {
					exam.updateCorrection(taskNumber, 1);
				} else {
					exam.updateCorrection(taskNumber, -1);
				}
			}
			return taskNumber == 8;
		}
	}
}