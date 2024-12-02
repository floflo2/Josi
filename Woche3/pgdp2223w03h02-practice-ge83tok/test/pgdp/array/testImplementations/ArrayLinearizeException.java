package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayLinearizeException implements ArrayInterface {

	public int[] linearize(int[][] a) {
		int length = a.length * a[0].length;

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
