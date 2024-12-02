package pgdp.algorithms;

import java.util.Arrays;

public class RecursiveSorting {

    /**	Implementation of the MergeSort algorithm
     *
     * @param array Any Integer-Array
     * @return The passed Integer-Array, but sorted in ascending order
     */
    public static int[] mergeSort(int[] array) {
        return null;
    }

    // Hilfsmethode (muss nicht verwendet werden, könnte aber hilfreich sein)
    public static int[] merge(int[] first, int[] second) {
        return null;
    }



    /**	Implementation of the StoogeSort algorithm
     *
     * @param array Any Integer-Array
     * @return The passed Integer-Array, but sorted in ascending order
     */
    public static void stoogeSort(int[] array) {
        stoogeSort(array, 0, array.length);
    }

    public static void stoogeSort(int[] array, int from, int to) {

    }



    /**	Implementation of the SelectionSort algorithm in a recursive way
     *
     * @param array Any Integer-Array
     * @return The passed Integer-Array, but sorted in ascending order
     */
    public static void selectionSortRec(int[] a) {
        selectionSortRec(a, a.length - 1);
    }

    public static void selectionSortRec(int[] a, int toIncl) {

    }

    // Hilfsmethode (muss nicht verwendet werden, könnte aber hilfreich sein)
    public static int findIndexOfLargest(int[] a, int toIncl) {
        return -1;
    }

    // Hilfsmethode (muss nicht verwendet werden, könnte aber hilfreich sein)
    public static void swap(int[] a, int firstPos, int secondPos) {

    }

    // Für Experimente
    public static void main(String[] args) {
        int[] a = {3, 4, 9, 2, 5, 0, 2, 1, 6, 4, -3};
        selectionSortRec(a);
        System.out.println(Arrays.toString(a));
    }

}
