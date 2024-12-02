package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayIntersectInvalidLength implements ArrayInterface {

	public int[] intersect(int[] a, int length) {
		int[] intersected = new int[length];

		for (int i = 0; i < length && i < a.length; i++) {
			intersected[i] = a[i];
		}

		return intersected;
	}
}
