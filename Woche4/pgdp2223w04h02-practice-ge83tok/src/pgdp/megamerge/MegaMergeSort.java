package pgdp.megamerge;

import java.util.Arrays;

public class MegaMergeSort {

	/**
	 * Sorts the array using mega merge sort with div splits
	 * @param array the array to be sorted
	 * @param div the split factor
	 * @return the sorted array
	 */
	protected int[] megaMergeSort(int[] array, int div) {
		return megaMergeSort(array, div, 0, array.length);
	}

	/**
	 * Sorts the array using mega merge sort with div splits in the defined range
	 * @param array the array to be sorted
	 * @param div the split factor
	 * @param from the lower bound (inclusive)
	 * @param to the upper bound (exclusive)
	 * @return the sorted array
	 */
	protected int[] megaMergeSort(int[] array, int div, int from, int to) {
		// TODO
		return null;
	}

	/**
	 * Merges all arrays in the given range
	 * @param arrays to be merged
	 * @param from lower bound (inclusive)
	 * @param to upper bound (exclusive)
	 * @return the merged array
	 */
	protected int[] merge(int[][] arrays, int from, int to) {
		// TODO
		return null;
	}

	/**
	 * Merges the given arrays into one
	 * @param arr1 the first array
	 * @param arr2 the second array
	 * @return the resulting array
	 */
	protected int[] merge(int[] arr1, int[] arr2) {
		// TODO
		return null;
	}

	public static void main(String[] args) {
		MegaMergeSort mms = new MegaMergeSort();
		int[] arr = new int[] { 1, 2, 6, 7, 4, 3, 8, 9, 0, 5 };
		int[] res = mms.megaMergeSort(arr, 4);
		System.out.println(Arrays.toString(res));
	}
}
