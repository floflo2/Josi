package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayBubblesortWrongLoopCondition implements ArrayInterface {

	public void bubbleSort(int[] a) {
		for (int i = a.length - 1; i > 0; i--) {
			for (int j = 0; j <= i; j++) {
				if (a[j] > a[j + 1]) {
					int temp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = temp;
				}
			}
		}
	}
}
