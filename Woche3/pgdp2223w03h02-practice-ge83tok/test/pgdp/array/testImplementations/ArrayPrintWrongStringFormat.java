package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayPrintWrongStringFormat implements ArrayInterface {

	public void print(int[] a) {
		if (a.length > 0) {
			System.out.print(a[0]);
			for (int i = 1; i < a.length; i++) {
				System.out.print(" " + a[i]);
			}
		}
		System.out.println();
	}
}
