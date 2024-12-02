package pgdp.algorithms;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.stream.Stream;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.params.provider.Arguments;

@MirrorOutput
@StrictTimeout(2)
public class RecursiveSortBehaviorTest {

	private static Stream<Arguments> arrayArguments() {
    	return Stream.of(
    			Arguments.of(new int[] {1, 2, 3, 4, 5}, new int[] {1, 2, 3, 4, 5}),
    			Arguments.of(new int[] {5, 4, 3, 2, 1}, new int[] {1, 2, 3, 4, 5}),
    			Arguments.of(new int[] {3, 5, 2, 1, 4}, new int[] {1, 2, 3, 4, 5}),
    			Arguments.of(new int[] {4, 5, 1, 3, 2}, new int[] {1, 2, 3, 4, 5}),

    			Arguments.of(new int[] {3, 2, 4, 1, 4, 3, 1, 2, 1}, new int[] {1, 1, 1, 2, 2, 3, 3, 4, 4}),
    			Arguments.of(new int[] {65, -100, 13, -27, 0, 6, 13}, new int[] {-100, -27, 0, 6, 13, 13, 65}),
    			Arguments.of(new int[] {1}, new int[] {1}),
    			Arguments.of(new int[] {}, new int[] {})
		);
	}

	@PublicTest
	public void testMergeSort() {
		assertAll(
				arrayArguments().map(
						args -> () -> testMergeSort((int[]) args.get()[0], (int[]) args.get()[1])
				)
		);
	}

	private void testMergeSort(int[] array, int[] expected) {
		int[] original = Arrays.copyOf(array, array.length);
		int[] actual = RecursiveSorting.mergeSort(array);
		String message = "Das Array " + Arrays.toString(original) + " wurde von deiner MergeSort-Implementierung nicht korrekt sortiert."
				+ " Erwartet wurde das Array " + Arrays.toString(expected) + ", geliefert wurde " + Arrays.toString(actual) + ".";
		assertArrayEquals(expected, actual, message);
	}

	@PublicTest
	public void testStoogeSort() {
		assertAll(
				arrayArguments().map(
						args -> () -> testStoogeSort((int[]) args.get()[0], (int[]) args.get()[1])
				)
		);
	}

	private void testStoogeSort(int[] array, int[] expected) {
		int[] original = Arrays.copyOf(array, array.length);
		RecursiveSorting.stoogeSort(array);
		String message = "Das Array " + Arrays.toString(original) + " wurde von deiner StoogeSort-Implementierung nicht korrekt sortiert."
				+ " Erwartet wurde das Array " + Arrays.toString(expected) + ", geliefert wurde " + Arrays.toString(array) + ".";
		assertArrayEquals(expected, array, message);
	}

	@PublicTest
	public void testSelectionSortRec() {
		assertAll(
				arrayArguments().map(
						args -> () -> testSelectionSortRec((int[]) args.get()[0], (int[]) args.get()[1])
				)
		);
	}

	private void testSelectionSortRec(int[] array, int[] expected) {
		int[] original = Arrays.copyOf(array, array.length);
		RecursiveSorting.selectionSortRec(array);
		String message = "Das Array " + Arrays.toString(original) + " wurde von deiner rekursiven BubbleSort-Implementierung nicht korrekt sortiert."
				+ " Erwartet wurde das Array " + Arrays.toString(expected) + ", geliefert wurde " + Arrays.toString(array) + ".";
		assertArrayEquals(expected, array, message);
	}

}
