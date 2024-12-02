package pgdp.array.testImplementations;

import pgdp.array.ArrayInterface;

public class ArrayMinAndMaxWrongEmptyArray implements ArrayInterface {

	public void minAndMax(int[] a) {
		int min = a[0];
		int max = a[0];

		for (int i = 0; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
			if (a[i] > max)
				max = a[i];
		}
		System.out.println("Minimum = " + min + ", Maximum = " + max);
	}
}
