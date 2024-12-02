package pgdp;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public final class SimpleGenerics {

	private SimpleGenerics() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns a String of the given Collection.
	 * 
	 * @param collection
	 * @return String representation of the collection
	 */
	public static String toString(Collection<?> collection) {
		// TODO
		return null;
	}

	/**
	 * Returns int array of collection.
	 * 
	 * @param collection
	 * @return int array
	 */
	public static int[] toIntArray(Collection<Integer> collection) {
		// TODO
		return null;
	}

	/**
	 * Generates an generic array of type T with the given length.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param length
	 * @return reference to the generated generic array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] generateGenericArray(Class<T> clazz, int length) {
		final T[] arr = (T[]) Array.newInstance(clazz, length);
		return arr;
	}

	/**
	 * Returns the given collection in a sorted array.
	 * 
	 * @param <T>
	 * @param clazz
	 * @param collection
	 * @param comparator dictates the order of the output
	 * @return array of type T
	 */
	public static <T> T[] specialSort(Class<T> clazz, Collection<T> collection, Comparator<T> comparator) {
		// TODO
		return null;
	}

	/**
	 * Returns a collection of all elements that are contained by each Collection of
	 * collections. Collections of the input are not modified.
	 * 
	 * @param <T>
	 * @param collections not null, may not contain null values.
	 * @return intersection of all collections
	 */
	public static <T> Collection<T> intersection(Collection<T>[] collections) {
		// TODO
		return null;
	}

	/**
	 * Returns the values stored in the map. Equivalent to map.values().
	 * 
	 * @param <K> key type
	 * @param <V> value type
	 * @param map
	 * @return set of values
	 */
	public static <K, V> Set<V> getValues(Map<K, V> map) {
		// TODO
		return null;
	}

	public static void main(String... args) {

	}
}
