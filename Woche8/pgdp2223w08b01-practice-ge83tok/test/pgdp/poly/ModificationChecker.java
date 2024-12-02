package pgdp.poly;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ModificationChecker {

	private final String studentFileDir;
	private final String tempFileDir;
	private final int[][] studentTaskIDX = new int[6][2];
	private final int[][] tempTaskIDX = new int[6][2];
	private String[] studentLines;
	private String[] tempLines;
	private boolean completed;
	private boolean passed;

	private String difference;

	public ModificationChecker(String studentFile, String tempFile) {
		studentFileDir = studentFile;
		tempFileDir = tempFile;
		completed = false;
		passed = false;
		try {
			startChecker();
			completed = true;
		} catch (final Exception e) {
			difference = e.getLocalizedMessage();
			e.printStackTrace();
			completed = false;
		}
	}

	public boolean completed() {
		return completed;
	}

	public boolean passed() {
		return passed;
	}

	public String getDiff() {
		return difference;
	}

	private void startChecker() throws FileNotFoundException, IOException, NoSuchElementException {
		readFiles();
		findTokens(studentTaskIDX, getLinesEnumerated(studentLines));
		findTokens(tempTaskIDX, getLinesEnumerated(tempLines));
		matchSections();
	}

	private void findTokens(int[][] taskIDX, Stream<Line> lines) throws NoSuchElementException {
		final BiFunction<Integer, String, String> token = (t, f) -> "// TASK " + t + " (" + f + ")";
		final Line[] tokenLines = lines.parallel().filter(line -> line.s().contains("// TASK ")).toArray(Line[]::new);
		for (int t = 0; t < 6; t++) {
			final int tt = t + 1;
			taskIDX[t][0] = Arrays.stream(tokenLines).parallel()
					.filter(line -> line.s().contains(token.apply(tt, "start"))).findFirst().orElseThrow().i();
			taskIDX[t][1] = Arrays.stream(tokenLines).parallel()
					.filter(line -> line.s().contains(token.apply(tt, "end"))).findFirst().orElseThrow().i();
		}
	}

	private void matchSections() {
		if (!matchSection(-1, 3)) {
			return;
		}
		if (!matchSection(3, 5)) {
			return;
		}
		if (!matchSection(5, 4)) {
			return;
		}
		if (!matchSection(4, 0)) {
			return;
		}
		if (!matchSection(0, 1)) {
			return;
		}
		if (!matchSection(1, 2)) {
			return;
		}
		if (!matchSection(2, Integer.MAX_VALUE)) {
			return;
		}
		passed = true;
	}

	private boolean matchSection(int from, int to) {
		final String[] student = getLinesEnumerated(studentLines)
				.filter(line -> (from < 0 ? 0 : studentTaskIDX[from][1]) <= line.i()
						&& line.i() <= (to == Integer.MAX_VALUE ? studentLines.length - 1 : studentTaskIDX[to][0]))
				.map(line -> line.s()).toArray(String[]::new);
		final String[] temp = getLinesEnumerated(tempLines)
				.filter(line -> (from < 0 ? 0 : tempTaskIDX[from][1]) <= line.i()
						&& line.i() <= (to == Integer.MAX_VALUE ? tempLines.length - 1 : tempTaskIDX[to][0]))
				.map(line -> line.s()).toArray(String[]::new);
		if (!Arrays.equals(Arrays.stream(student).map(line -> line.replaceAll("\\s+", "")).toArray(String[]::new),
				Arrays.stream(temp).map(line -> line.replaceAll("\\s+", "")).toArray(String[]::new))) {
			final StringBuilder sb = new StringBuilder();
			sb.append("Found modified section! - expected: <");
			for (final String l : temp) {
				sb.append(l).append("\n");
			}
			sb.append("> but was: <");
			for (final String l : student) {
				sb.append(l).append("\n");
			}
			sb.append(">.");
			difference = sb.toString();
			return false;
		}
		return true;
	}

	private void readFiles() throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(studentFileDir))) {
			studentLines = br.lines().toArray(String[]::new);
		}
		try (BufferedReader br = new BufferedReader(new FileReader(tempFileDir))) {
			tempLines = br.lines().toArray(String[]::new);
		}
	}

	private Stream<Line> getLinesEnumerated(String[] lines) {
		return IntStream.range(0, lines.length).mapToObj(i -> new Line(i, lines[i]));
	}

	private record Line(int i, String s) {
	}
}
