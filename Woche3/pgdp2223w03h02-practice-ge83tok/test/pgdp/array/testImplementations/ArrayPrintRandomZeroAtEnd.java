package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayPrintRandomZeroAtEnd implements ArrayInterface {

	public void print(int[] a) {
		System.out.print("{");
		if (a.length > 0) {
			for (int i = 0; i < a.length; i++) {
				System.out.print(a[i] + ", ");
			}
			System.out.print(0);
		}
		System.out.println("}");
	}
}
