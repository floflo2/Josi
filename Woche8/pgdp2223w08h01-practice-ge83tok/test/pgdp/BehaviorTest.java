package pgdp;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

import de.tum.in.test.api.jupiter.Hidden;
import de.tum.in.test.api.jupiter.PublicTest;

@TestClassAnnotation
public class BehaviorTest {

	private final static Integer[] values1 = { -4, -1, 0, 4, 9, 16, 17 };
	private final static Set<Integer> set1 = Set.of(values1);
	private final static List<Integer> list1 = java.util.Arrays.stream(values1).collect(Collectors.toList());

	private final static Integer[] values2 = { 1, -8, -4, 3, -5, 6, -9 };
	private final static Set<Integer> set2 = Set.of(values2);
	private final static List<Integer> list2 = java.util.Arrays.stream(values2).collect(Collectors.toList());

	private final static Comparator<Integer> comp1 = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	};

	private final static Comparator<Integer> comp2 = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return o2 - o1;
		}
	};

	private final static Comparator<Integer> comp3 = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			if ((o1 < 0) == (0 <= o2)) {
				return o1 < 0 ? 1 : -1;
			}
			return o1 - o2;
		}
	};

	@SuppressWarnings("serial")
	private final static HashMap<?, ?> map11 = new HashMap<>(
			IntStream.range(0, values1.length).boxed().collect(Collectors.toMap(i -> values1[i], i -> values1[i]))) {
		@Override
		public Collection<Integer> values() {
			return null;
		}
	};

	@SuppressWarnings("serial")
	private final static HashMap<?, ?> map12 = new HashMap<>(
			IntStream.range(0, values1.length).boxed().collect(Collectors.toMap(i -> values1[i], i -> values2[i]))) {
		@Override
		public Collection<Integer> values() {
			return null;
		}
	};

	private static Stream<Arguments> collectionArguments() {
		return Stream.of(Arguments.of(new HashSet<Integer>(), toStringSorted(new HashSet<Integer>())),
				Arguments.of(new LinkedList<Integer>(), toStringSorted(new LinkedList<Integer>())),
				Arguments.of(set1, toStringSorted(set1)), Arguments.of(list1, toString(list1)),
				Arguments.of(set2, toStringSorted(set2)), Arguments.of(list2, toString(list2)));
	}

	private static Stream<Arguments> specialSortArguments() {
		return collectionArguments()
				.map(args -> Stream.of(Arguments.of(args.get()[0], comp1, args.get()[1], "increasing"),
						Arguments.of(args.get()[0], comp2, args.get()[1], "decreasing"),
						Arguments.of(args.get()[0], comp3, args.get()[1], "positive inc, negative inc")))
				.flatMap(Function.identity());
	}

	@SuppressWarnings("unchecked")
	private static String collectionArrayToString(Collection<Integer>... cols) {
		if (cols == null || cols.length == 0) {
			return "{}";
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (final var col : cols) {
			sb.append(col instanceof Set<?> ? toStringSorted(col) : toString(col)).append(", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("}");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static Stream<Arguments> intersectionArguments() {
		final Collection<?>[][] args = { {}, { set1 }, { set2 }, { set1, set2 },
				{ Set.of(1, 2, 3, 4, 5), Set.of(1, 2, 3, 4, 5), Set.of(1, 2, 3, 4, 5), Set.of(1, 2, 3, 4, 5),
						Set.of(1, 2, 3, 4, 5) },
				{ Set.of(2, 3, 4, 5), Set.of(1, 3, 4, 5), Set.of(1, 2, 4, 5), Set.of(1, 2, 3, 5), Set.of(1, 2, 3, 4) },
				{ Set.of(2, 3, 4, 5), Set.of(1, 3, 4, 5), Set.of(1, 2, 3, 5), Set.of(1, 2, 3, 4) } };
		return java.util.Arrays.stream(args)
				.map(cols -> Arguments.of(cols, collectionArrayToString((Collection<Integer>[]) cols)));
	}

	@SuppressWarnings("serial")
	private static Stream<Arguments> mapArguments() {
		return Stream.of(Arguments.of(new HashMap<Integer, Integer>() {
			@Override
			public Collection<Integer> values() {
				return null;
			}
		}, "{}"), Arguments.of(map11, StringUtils.join(map11)), Arguments.of(map12, StringUtils.join(map12)));
	}

	private static String toStringSorted(Collection<Integer> collection) {
		if (collection.isEmpty()) {
			return "{}";
		}
		int[] values = collection.stream().mapToInt(x -> x.intValue()).sorted().toArray();

		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (final int e : values) {
			sb.append(e).append(", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("}");
		return sb.toString();
	}

	private static String toString(Collection<?> collection) {
		if (collection.isEmpty()) {
			return "{}";
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (final var e : collection) {
			sb.append(e).append(", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("}");
		return sb.toString();
	}

	@Hidden
	@Order(1)
	@DisplayName(value = "Test Individual - toString(Collection)")
	@ParameterizedTest(name = "[{index}] Testing toString({1})")
	@MethodSource("collectionArguments")
	void testToString(Collection<Integer> in, String disp) {
		final String expected = toString(in);
		final String actual = SimpleGenerics.toString(in);
		assertEquals(expected, actual);
	}

	@PublicTest
	@Order(2)
	@DisplayName(value = "Test Summary - toString(Collection)")
	@SuppressWarnings("unchecked")
	void testToStringSummary() {
		collectionArguments().forEach(args -> {
			try {
				testToString((Collection<Integer>) args.get()[0], null);
			} catch (final AssertionFailedError e) {
				fail("At least one of the required tests failed.");
			} catch (final Exception e) {
				fail("An exception was thrown. Did you use something forbidden?");
			}
		});
	}

	public static int[] toIntArray(Collection<Integer> collection) {
		final int[] arr = new int[collection.size()];
		int i = 0;
		for (final int e : collection) {
			arr[i++] = e;
		}
		return arr;
	}

	@Hidden
	@Order(3)
	@DisplayName(value = "Test Individual - toIntArray(Collection<Integer>)")
	@ParameterizedTest(name = "[{index}] Testing toIntArray({1})")
	@MethodSource("collectionArguments")
	void testToIntArray(Collection<Integer> in, String disp) {
		final int[] expected = toIntArray(in);
		final int[] actual = SimpleGenerics.toIntArray(in);
		assertArrayEquals(expected, actual);
	}

	@PublicTest
	@Order(4)
	@DisplayName(value = "Test Summary - toIntArray(Collection<Integer>)")
	@SuppressWarnings("unchecked")
	public void testToIntArraySummary() {
		collectionArguments().forEach(args -> {
			try {
				testToIntArray((Collection<Integer>) args.get()[0], null);
			} catch (final AssertionFailedError e) {
				fail("At least one of the required tests failed.");
			} catch (final Exception e) {
				fail("An exception was thrown. Did you use something forbidden?");
			}
		});
	}

	@SuppressWarnings("unchecked")
	private static <T> T[] specialSort(Class<T> clazz, Collection<T> collection, Comparator<T> comparator) {
		T[] arr = (T[]) Array.newInstance(clazz, collection.size());
		arr = collection.toArray(arr);
		java.util.Arrays.sort(arr, comparator);
		return arr;
	}

	@Hidden
	@Order(5)
	@DisplayName(value = "Test Individual - specialSort(Collection, Comparator)")
	@ParameterizedTest(name = "[{index}] Testing specialSort({2}, {3})")
	@MethodSource("specialSortArguments")
	void testSpecialSort(Collection<Integer> col, Comparator<Integer> comp, String disp1, String disp2) {
		final Integer[] expected = specialSort(Integer.class, col, comp);
		final Integer[] actual = SimpleGenerics.specialSort(Integer.class, col, comp);
		assertArrayEquals(expected, actual);
	}

	@PublicTest
	@Order(6)
	@DisplayName(value = "Test Summary - specialSort(Collection, Comparator)")
	@SuppressWarnings("unchecked")
	public void testSpecialSortSummary() {
		specialSortArguments().forEach(args -> {
			try {
				testSpecialSort((Collection<Integer>) args.get()[0], (Comparator<Integer>) args.get()[1], null, null);
			} catch (final AssertionFailedError e) {
				fail("At least one of the required tests failed.");
			} catch (final Exception e) {
				fail("An exception was thrown. Did you use something forbidden?");
			}
		});
	}

	@Hidden
	@Order(7)
	@DisplayName(value = "Test Individual - intersection(Collection[])")
	@ParameterizedTest(name = "[{index}] Testing intersection({1})")
	@MethodSource("intersectionArguments")
	void testIntersection(Collection<Integer>[] cols, String disp) {
		@SuppressWarnings("unchecked")
		final Set<Integer>[] copies = (Set<Integer>[]) java.util.Arrays.stream(cols).map(col -> new HashSet<>(col))
				.toArray(Set[]::new);
		final Set<?> expected = cols.length == 0 ? Collections.emptySet()
				: java.util.Arrays.stream(cols).map(col -> new HashSet<>(col)).skip(1)
						.collect(() -> new HashSet<>(cols[0]), Set::retainAll, Set::retainAll);
		final Set<?> actual = new HashSet<>(SimpleGenerics.intersection(cols));
		assertEquals(expected.size(), actual.size(), "Size doesn't match.");
		assertThat("Elements don't match.", actual, is(expected));
		for (int i = 0; i < cols.length; i++) {
			assertThat("Input modified", cols[i], is(copies[i]));
		}
	}

	@PublicTest
	@Order(8)
	@DisplayName(value = "Test Summary - intersection(Collection[])")
	@SuppressWarnings("unchecked")
	public void testIntersectionSummary() {
		intersectionArguments().forEach(args -> {
			try {
				testIntersection((Collection<Integer>[]) args.get()[0], null);
			} catch (final AssertionFailedError e) {
				fail("At least one of the required tests failed.");
			} catch (final Exception e) {
				fail("An exception was thrown. Did you use something forbidden?");
			}
		});
	}

	@Hidden
	@Order(9)
	@DisplayName(value = "Test Individual - getValues(Collection[])")
	@ParameterizedTest(name = "[{index}] Testing getValues({1})")
	@MethodSource("mapArguments")
	void testGetValues(Map<Integer, Integer> map, String disp) {
		final Set<?> expected = map.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toSet());
		final Set<?> actual = SimpleGenerics.getValues(map);
		assertThat(actual, is(expected));
	}

	@PublicTest
	@Order(10)
	@DisplayName(value = "Test Summary - getValues(Collection[])")
	@SuppressWarnings("unchecked")
	public void testGetValuesSummary() {
		mapArguments().forEach(args -> {
			try {
				testGetValues((Map<Integer, Integer>) args.get()[0], null);
			} catch (final AssertionFailedError e) {
				fail("At least one of the required tests failed.");
			} catch (final Exception e) {
				fail("An exception was thrown. Did you use something forbidden?");
			}
		});
	}
}
