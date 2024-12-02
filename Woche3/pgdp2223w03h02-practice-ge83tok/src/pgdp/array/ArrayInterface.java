package pgdp.array;

public interface ArrayInterface {

	default void print(int[] a) {
		System.out.print("{");
		if (a.length > 0) {
			System.out.print(a[0]);
			for (int i = 1; i < a.length; i++) {
				System.out.print(", " + a[i]);
			}
		}
		System.out.println("}");
	}

	default void minAndMax(int[] a) {
		if (a.length == 0) {
			return;
		}
		int min = a[0];
		int max = a[0];

		for (int i = 0; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
			if (a[i] > max)
				max = a[i];
		}
		System.out.println("Minimum = " + min + ", Maximum = " + max);
	}

	default void invert(int[] a) {
		for (int i = 0; i < a.length / 2; i++) {
			int temp = a[i];
			a[i] = a[a.length - 1 - i];
			a[a.length - 1 - i] = temp;
		}
	}

	default int[] intersect(int[] a, int length) {
		if (length <= 0) {
			return new int[0];
		}

		int[] intersected = new int[length];

		for (int i = 0; i < length && i < a.length; i++) {
			intersected[i] = a[i];
		}

		return intersected;
	}

	default int[] linearize(int[][] a) {
		int length = 0;

		for (int i = 0; i < a.length; i++) {
			length = length + a[i].length;
		}

		int[] linearized = new int[length];
		int linIndex = 0;

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				linearized[linIndex++] = a[i][j];
			}
		}

		return linearized;
	}

	default void bubbleSort(int[] a) {
		for (int i = a.length - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (a[j] > a[j + 1]) {
					int temp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = temp;
				}
			}
		}
	}
}
