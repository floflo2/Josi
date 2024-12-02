package pgdp.array;

public class Array {

	private static ArrayInterface arrayImplementation = new ArrayInterface() {
	};

	public static void print(int[] a) {
		arrayImplementation.print(a);
	}

	public static void minAndMax(int[] a) {
		arrayImplementation.minAndMax(a);
	}

	public static void invert(int[] a) {
		arrayImplementation.invert(a);
	}

	public static int[] intersect(int[] a, int length) {
		return arrayImplementation.intersect(a, length);
	}

	public static int[] linearize(int[][] a) {
		return arrayImplementation.linearize(a);
	}

	public static void bubbleSort(int[] a) {
		arrayImplementation.bubbleSort(a);
	}

	public static void setArrayImplementation(ArrayInterface arrayImplementation) {
		Array.arrayImplementation = arrayImplementation;
	}
}
