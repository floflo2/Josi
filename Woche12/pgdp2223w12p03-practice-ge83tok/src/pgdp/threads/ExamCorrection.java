package pgdp.threads;

import java.util.Arrays;

public class ExamCorrection {
	public static final int TOTAL_AMOUNT = 1900;

	public static void main(String[] args) {
		// create buffers
		Buffer[] buffers = new Buffer[9];
		buffers[0] = new Buffer(TOTAL_AMOUNT);
		buffers[buffers.length - 1] = new Buffer(TOTAL_AMOUNT);
		for (int i = 1; i < buffers.length - 1; i++)
			buffers[i] = new Buffer(50);

		// create exams
		for (int i = 0; i < TOTAL_AMOUNT; i++) {
			try {
				buffers[0].produce(new Exam());
			} catch (InterruptedException e) {
				// Theoretically cannot happen since we should never wait here
				e.printStackTrace();
			}
		}

		// Create tasks
		Runnable[] tasks = new Runnable[8];
		for (int i = 0; i < tasks.length; i++) {
			tasks[i] = new Tutor(buffers[i], buffers[(i + 1) % 8], i + 1,
					buffers[buffers.length - 1]);
		}
		
		// TODO: Starte 32 Tutor-Threads - 4 pro Aufgabe, warte, bis sie durchgelaufen sind
		//  und schließe dann die Korrektur jeder Klausur noch ab.
	}

	private static void finalizeExam(Exam exam) {
		exam.setTotalPoints(Arrays.stream(exam.getPoints()).sum());
		exam.setGrade(CorrectionScheme.grade(exam.getTotalPoints()));
		System.out.println(exam);
	}
}