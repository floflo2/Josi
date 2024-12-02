package pgdp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * This class provides convenience methods to make the barrier of entry into
 * programming easier. In addition to that it realizes an educational
 * sub-language to Java called "Mini Java" with simpler grammar.
 * <p>
 * <i>Diese Klasse stellt simple Methoden zur Verfügung um den Einstieg in die
 * Programmierung zu vereinfachen. Zusätzlich dazu wird damit eine für die Lehre
 * zusammengestellte Sub-Programmiersprache von Java umgesetzt, die sich "Mini
 * Java" nennt und eine einfachere Grammatik besitzt.</i>
 */
public class PinguLib {
	private static ByteArrayOutputStream baos;
	private static PrintStream ps;
	private static PrintStream old;

	static {
		setup();
	}

	/**
	 * Prints the {@link String} to the console and breaks the line.
	 * <p>
	 * <i>Gibt den {@link String} auf der Konsole aus und beginnt eine neue
	 * Zeile.</i>
	 */
	public static void write(String output) {
		System.setOut(old);
		System.out.println(output);
		System.setOut(ps);
	}

	/**
	 * Prints the <code>int</code> to the console and breaks the line.
	 * <p>
	 * <i>Gibt den <code>int</code>-Wert auf der Konsole aus und beginnt eine neue
	 * Zeile.</i>
	 */
	public static void write(int output) {
		write(String.valueOf(output));
	}

	/**
	 * Prints the <code>double</code> to the console and breaks the line.
	 * <p>
	 * <i>Gibt den <code>double</code>-Wert auf der Konsole aus und beginnt eine
	 * neue Zeile.</i>
	 */
	public static void write(double output) {
		write(String.valueOf(output));
	}

	/**
	 * Prints the {@link String} to the console. Does not end with a line break.
	 * <p>
	 * <i>Gibt den {@link String} auf der Konsole aus. Endet nicht mit einem
	 * Zeilenumbruch.</i>
	 */
	public static void writeConsole(String output) {
		System.setOut(old);
		System.out.print(output);
		System.setOut(ps);
	}

	/**
	 * Prints the <code>int</code> to the console. Does not end with a line break.
	 * <p>
	 * <i>Gibt den <code>int</code>-Wert auf der Konsole aus. Endet nicht mit einem
	 * Zeilenumbruch.</i>
	 */
	public static void writeConsole(int output) {
		writeConsole(String.valueOf(output));
	}

	/**
	 * Prints the <code>double</code> to the console. Does not end with a line
	 * break.
	 * <p>
	 * <i>Gibt den <code>double</code>-Wert auf der Konsole aus. Endet nicht mit
	 * einem Zeilenumbruch.</i>
	 */
	public static void writeConsole(double output) {
		writeConsole(String.valueOf(output));
	}

	/**
	 * Setup for reading console output. Needs to be executed before each read
	 */
	public static void setup() {
		baos = new ByteArrayOutputStream();
		ps = new PrintStream(baos);
		old = System.out;
		System.setOut(ps);
	}

	/**
	 * use this function to read the console output
	 * 
	 * @return console output
	 */
	public static String getConsoleOutput() {
		String output = baos.toString().trim();
		baos.reset();
		return output;
	}

	/**
	 * Reset for reading console output. Needs to be executed after each read
	 */
	public static void reset() {
		System.setOut(old);
	}
}
