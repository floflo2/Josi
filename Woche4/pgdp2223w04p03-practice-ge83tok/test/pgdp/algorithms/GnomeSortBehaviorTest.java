package pgdp.algorithms;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.jupiter.PublicTest;

@MirrorOutput
@StrictTimeout(2)
public class GnomeSortBehaviorTest {

	@PublicTest
	public void testEmpty() {
		int[] a = new int[0];
		GnomeSort.gnomeSort(a);
		assertArrayEquals(new int[0], a, "Ein Array der Länge 0 hat sich nach gnomeSort geändert.");
	}

	@PublicTest
	public void testSorted() {
		int[] a = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] res = Arrays.copyOf(a, a.length);
		GnomeSort.gnomeSort(a);
		assertArrayEquals(res, a, "Ein sortiertes Array ist nach gnomeSort nicht mehr sortiert.");
	}

	@PublicTest
	public void testSortedReversed() {
		int[] a = new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
		int[] res = Arrays.copyOf(a, a.length);
		GnomeSort.gnomeSort(a);
		Arrays.sort(res);
		assertArrayEquals(res, a, "Ein rückwärts sortiertes Array ist nach gnomeSort nicht sortiert.");
	}

	@PublicTest
	public void testUnsorted() {
		int[] a = new int[] { 1337, 42, 69, 420, 3, 0, 1, 4090, 7 };
		int[] res = Arrays.copyOf(a, a.length);
		GnomeSort.gnomeSort(a);
		Arrays.sort(res);
		assertArrayEquals(res, a, "Ein Array ist nach gnomeSort nicht sortiert.");
	}

	@PublicTest
	public void testUnsortedDuplicates() {
		int[] a = new int[] { 1337, 1337, 42, 69, 420, 3, 42, 0, 1, 4090, 7, 0, 420 };
		int[] res = Arrays.copyOf(a, a.length);
		GnomeSort.gnomeSort(a);
		Arrays.sort(res);
		assertArrayEquals(res, a, "Ein Array ist nach gnomeSort nicht sortiert.");
	}
}
