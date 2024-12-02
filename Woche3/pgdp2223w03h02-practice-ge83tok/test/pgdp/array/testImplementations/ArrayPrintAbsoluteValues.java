package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayPrintAbsoluteValues implements ArrayInterface {

	public void print(int[] a) {
		System.out.print("{");
		if (a.length > 0) {
			System.out.print(Math.abs(a[0]));
			for (int i = 1; i < a.length; i++) {
				System.out.print(", " + Math.abs(a[i]));
			}
		} else {
			System.out.print(0);
		}
		System.out.println("}");
	}
}
