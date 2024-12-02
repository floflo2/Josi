package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayInvertSwitchedOnlyOneWay implements ArrayInterface {

	public void invert(int[] a) {
		for (int i = 0; i < a.length / 2; i++) {
			a[i] = a[a.length - 1 - i];
		}
	}
}
