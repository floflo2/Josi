package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayLinearizeWrongGap implements ArrayInterface {

	public int[] linearize(int[][] a) {
		int length = 0;

		for (int i = 0; i < a.length; i++) {
			if (a[i].length > length) {
				length = a[i].length;
			}
		}

		length *= a.length;

		int[] linearized = new int[length];
		int linIndex = 0;

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				linearized[linIndex++] = a[i][j];
			}
		}

		return linearized;
	}
}
