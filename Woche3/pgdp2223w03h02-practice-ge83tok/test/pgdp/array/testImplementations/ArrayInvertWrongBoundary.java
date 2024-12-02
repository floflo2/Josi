package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayInvertWrongBoundary implements ArrayInterface {

	public void invert(int[] a) {
		for (int i = 0; i < a.length; i++) {
			int temp = a[i];
			a[i] = a[a.length - 1 - i];
			a[a.length - 1 - i] = temp;
		}
	}
}
