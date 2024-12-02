package pgdp.recursion;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RecursiveMath {

	// Für Experimente
	public static void main(String... args) {
		int[][] permutations = permutations(new int[]{1, 2, 4});
		System.out.println(twoDArrayToString(permutations));
	}

	// Hilfsmethode zum Printen eines 2D-Arrays. Code muss nicht verstanden werden. Verwende die Methode ad libitum.
	public static String twoDArrayToString(int[][] array) {
		return Arrays.stream(array).map(Arrays::toString).collect(Collectors.joining("\n"));
	}

	/* ================ Binomialkoeffizient und Fibonacci Iterativ und Rekursiv im Vergleich ================ */

	public static long binomCoeffIterative(int n, int k) {
		if (k == 0 || n == k) {
			return 1;
		}
		if (k > n) {
			return 0;
		}
		long result = 1;
		for (int i = 1; i <= k; i++) {
			result *= n + 1 - i;
			result /= i;
		}
		return result;
	}

	public static long binomCoeffRecursive(int n, int k) {
		return 0L;
	}

	public static long fibonacciIterative(int n) {
		if (n <= 0) {
			return 0;
		}
		long last = 1;
		long current = 0;
		long tmp;
		for (int i = 0; i < n; i++) {
			tmp = last + current;
			last = current;
			current = tmp;
		}
		return current;
	}

	public static long fibonacciRecursive(int n) {
		return 0L;
	}

	/* ================ Weitere Rekursive Methoden ================ */

	public static int differenceAsBs(char[] word) {
		return differenceAsBs(word, 0);
	}

	public static int differenceAsBs(char[] word, int from) {
		return 0;
	}

	public static int[][] permutations(int[] array) {
		return null;
	}

}