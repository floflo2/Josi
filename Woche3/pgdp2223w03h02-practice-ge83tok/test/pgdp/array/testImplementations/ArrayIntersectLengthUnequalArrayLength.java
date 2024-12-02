package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayIntersectLengthUnequalArrayLength implements ArrayInterface {

	public int[] intersect(int[] a, int length) {
		if (length <= 0) {
			return new int[0];
		}

		int[] intersected = new int[a.length];

		for (int i = 0; i < length && i < a.length; i++) {
			intersected[i] = a[i];
		}

		return intersected;
	}
}
