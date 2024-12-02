package pgdp.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.DisplayName;

import de.tum.in.test.api.MirrorOutput;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.jupiter.PublicTest;

@MirrorOutput
@StrictTimeout(3)
public class CountingObjectsBehaviorTest {
	private DynamicClass<?> clazz = DynamicClass.toDynamic(CountingObjectsCollection.class);
	private DynamicField<?> map = clazz.field(HashMap.class, "map");

	@SuppressWarnings("unchecked")
	@PublicTest
	@DisplayName(value = "Test second constructor")
	public void testConstructor() {
		CountingObjectsCollection<String> c = new CountingObjectsCollection<>();
		HashMap<String, Integer> m = (HashMap<String, Integer>) map.getOf(c);
		m.put("Hello", 1);
		m.put("Penguin", 9420);
		m.put("!", 69);
		CountingObjectsCollection<String> n = new CountingObjectsCollection<>(c);
		HashMap<String, Integer> o = (HashMap<String, Integer>) map.getOf(n);
		assertTrue(m.entrySet().containsAll(o.entrySet()) && m.entrySet().size() == o.entrySet().size(),
				"The constructor with one argument does not initialize map correctly!");
	}

	@SuppressWarnings("unchecked")
	@PublicTest
	@DisplayName(value = "Test insert")
	public void testInsert() {
		CountingObjectsCollection<String> c = new CountingObjectsCollection<>();
		HashMap<String, Integer> m = (HashMap<String, Integer>) map.getOf(c);
		String s1 = "Hello";
		String s2 = "Penguin";
		c.insert(s1);
		assertEquals(1, m.get(s1), "After first insert an object should map to integer 1!");
		c.insert(s2);
		c.insert(s1);
		c.insert(s1);
		assertEquals(1, m.get(s2), "After first insert an object should map to integer 1!");
		assertEquals(3, m.get(s1), "After three inserts an object should map to integer 3!");
	}

	@SuppressWarnings("unchecked")
	@PublicTest
	@DisplayName(value = "Test getObjectCount")
	public void testGetObjectCount() {
		CountingObjectsCollection<String> c = new CountingObjectsCollection<>();
		HashMap<String, Integer> m = (HashMap<String, Integer>) map.getOf(c);
		String s1 = "Hello";
		assertEquals(0, c.getObjectCount(s1), "getObjectCount should return 0 for not inserted object!");
		m.put(s1, 5);
		assertEquals(5, c.getObjectCount(s1), "getObjectCount returns the wrong value!");
	}

	@SuppressWarnings("unchecked")
	@PublicTest
	@DisplayName(value = "Test totalObjectCount")
	public void testTotalObjectCount() {
		CountingObjectsCollection<String> c = new CountingObjectsCollection<>();
		HashMap<String, Integer> m = (HashMap<String, Integer>) map.getOf(c);
		String s1 = "Hello";
		String s2 = "Penguin";
		assertEquals(0, c.getTotalObjectCount(), "After initialization, getTotalObjectCount should return 0");
		m.put(s1, 42);
		m.put(s2, 13);
		assertEquals(55, c.getTotalObjectCount(), "getTotalObjectCount returns the wrong value!");
	}

	@SuppressWarnings("unchecked")
	@PublicTest
	@DisplayName(value = "Test getKeyList")
	public void testGetKeyList() {
		CountingObjectsCollection<String> c = new CountingObjectsCollection<>();
		HashMap<String, Integer> m = (HashMap<String, Integer>) map.getOf(c);
		String s1 = "Hello";
		String s2 = "Penguin";
		String s3 = "!";
		m.put(s1, 3);
		m.put(s2, 20);
		m.put(s3, 1);
		List<String> l = c.getKeyList();
		assertTrue(l.contains(s1), "The list returned by getKeyList does not contain all elements!");
		assertTrue(l.contains(s2), "The list returned by getKeyList does not contain all elements!");
		assertTrue(l.contains(s3), "The list returned by getKeyList does not contain all elements!");
		try {
			l.add("Test");
		} catch (UnsupportedOperationException e) {
			fail("The list returned by getKeyList is not modifiable!");
		}
	}

	@SuppressWarnings("unchecked")
	@PublicTest
	@DisplayName(value = "Test equals")
	public void testEquals() {
		CountingObjectsCollection<String> c1 = new CountingObjectsCollection<>();
		CountingObjectsCollection<String> c2 = new CountingObjectsCollection<>();
		HashMap<String, Integer> m1 = (HashMap<String, Integer>) map.getOf(c1);
		HashMap<String, Integer> m2 = (HashMap<String, Integer>) map.getOf(c2);
		String s1 = "Hello";
		assertTrue(c1.equals(c2), "Two empty map objects should be equal!");
		m1.put(s1, 1);
		assertFalse(c1.equals(c2), "Objects with different number of elements should not be equal!");
		m2.put(s1, 2);
		assertFalse(c1.equals(c2), "Objects with different values associated to the keys should not be equal!");
		m1.put(s1, 2);
		assertTrue(c1.equals(c2), "Objects with same values associated to the keys should be equal!");
	}
}
