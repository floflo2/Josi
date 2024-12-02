package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayIntersectWrongLoopConditionIntersectedLength implements ArrayInterface {

	public int[] intersect(int[] a, int length) {
		if (length <= 0) {
			return new int[0];
		}

		int[] intersected = new int[length];

		for (int i = 0; i < a.length; i++) {
			intersected[i] = a[i];
		}

		return intersected;
	}
}
