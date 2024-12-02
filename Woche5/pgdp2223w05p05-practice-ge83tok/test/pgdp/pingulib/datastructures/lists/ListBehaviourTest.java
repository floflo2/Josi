package pgdp.pingulib.datastructures.lists;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import de.tum.in.test.api.dynamic.*;
import de.tum.in.test.api.jupiter.*;

@W05P04
public class ListBehaviourTest {

	DynamicClass<?> list = new DynamicClass<>("pgdp.pingulib.datastructures.lists.List");
	DynamicConstructor<?> newList = list.constructor();

	DynamicClass<?> element = new DynamicClass<>("pgdp.pingulib.datastructures.lists.List$Element");
	DynamicConstructor<?> newElement1 = element.constructor(int.class);
	DynamicConstructor<?> newElement2 = element.constructor(int.class, element.toClass());

	DynamicField<?> head = list.field(element.toClass(), "head");
	DynamicField<?> tail = list.field(element.toClass(), "tail");
	DynamicField<?> size = list.field(int.class, "size");

	DynamicField<?> value = element.field(int.class, "value");
	DynamicField<?> next = element.field(element.toClass(), "next");

	@DisplayName("- | Public Test List-Constructor")
	@PublicTest
	@Order(1)
	void testListConstructor() {
		var l = newList.newInstance();
		if (head.getOf(l) != null)
			fail("<head> is not <null>");
		if (tail.getOf(l) != null)
			fail("<tail> is not <null>");
		if (((List) l).size() != 0)
			fail("<size> is not <0>");
	}

	@DisplayName("- | Public Test Element-Constructor 1")
	@PublicTest
	@Order(2)
	void testElementConstructor1() {
		var e1 = newElement1.newInstance(0);
		var e2 = newElement1.newInstance(5);

		if (!value.getOf(e1).equals(0) || !value.getOf(e2).equals(5))
			fail("<value> is not set correctly");

		if (next.getOf(e1) != null)
			fail("<next> is not <null>");
	}

	@DisplayName("- | Public Test Element-Constructor 2")
	@PublicTest
	@Order(3)
	void testElementConstructor2() {
		var e2 = newElement2.newInstance(5, null);
		var e1 = newElement2.newInstance(0, e2);

		if (!value.getOf(e1).equals(0) || !value.getOf(e2).equals(5))
			fail("<value> is not set correctly");

		if (next.getOf(e2) != null || !next.getOf(e1).equals(e2))
			fail("<next> is not set correctly");
	}

	@DisplayName("- | Public Test isEmpty")
	@PublicTest
	@Order(4)
	void testIsEmpty() throws IllegalArgumentException, IllegalAccessException {
		var l = newList.newInstance();
		var e = newElement1.newInstance(0);

		if (!((List) l).isEmpty())
			fail("isEmpty on new instance of List should return <true>");

		size.toField().set(l, 1);
		head.toField().set(l, e);
		tail.toField().set(l, e);
		if (((List) l).isEmpty())
			fail("isEmpty on instance of List with size != 0, head != null, or tail != null should return <false>");
	}

	@DisplayName("- | Public Test clear")
	@PublicTest
	@Order(5)
	void testClear() throws IllegalArgumentException, IllegalAccessException {
		var l = newList.newInstance();
		var e = newElement1.newInstance(0);

		size.toField().set(l, 1);
		head.toField().set(l, e);
		tail.toField().set(l, e);

		((List) l).clear();

		if (head.getOf(l) != null)
			fail("<head> is not <null>");
		if (tail.getOf(l) != null)
			fail("<tail> is not <null>");
		if (((List) l).size() != 0)
			fail("<size> is not <0>");
	}

	@DisplayName("- | Public Test add(int)")
	@PublicTest
	@Order(6)
	void testAdd1() {
		var l = newList.newInstance();
		// empty
		((List) l).add(0);
		if (head.getOf(l) == null)
			fail("add on new instance: <head> is still <null>");
		if (tail.getOf(l) == null)
			fail("add on new instance: <tail> is still <null>");
		if (head.getOf(l) != tail.getOf(l))
			fail("add on new instance: <tail> != <head>");
		if (((List) l).size() != 1)
			fail("add on new instance: <size> is not <1>");

		// not empty
		for (int i = 1; i < 10; i++) {
			var oldTail = tail.getOf(l);
			String oldList = ((List) l).toString();
			((List) l).add(i);
			String currentList = ((List) l).toString();
			String expectedList = oldList.substring(0, oldList.length() - 2) + ", " + i + " ]";
			if (head.getOf(l) == null)
				fail("add(int): <head> is <null>");
			if (tail.getOf(l) == null)
				fail("add(int): <tail> is <null>");
			if (oldTail == tail.getOf(l))
				fail("add(int): <tail> was not updated");
			if (((List) l).size() != i + 1)
				fail("add(int): <size> is not updated");
			if (!currentList.equals(expectedList))
				fail("add(int): something went horribly wrong\nexpected: " + expectedList + "\nwas: " + currentList);
		}
	}

	@DisplayName("- | Public Test add(int, int) - depending on add(int)")
	@PublicTest
	@Order(7)
	void testAdd2() {
		// empty
		var l = newList.newInstance();
		if (((List) l).add(-1, 0))
			fail("add on index=-1: return value is not <false>");
		if (head.getOf(l) != null)
			fail("add on index=-1: <head> is not <null>");
		if (tail.getOf(l) != null)
			fail("add on index=-1: <tail> is not <null>");
		if (((List) l).size() != 0)
			fail("add on index=-1: <size> is not <0>");

		l = newList.newInstance();
		if (((List) l).add(1, 0))
			fail("add on index=1: return value is not <false>");
		if (head.getOf(l) != null)
			fail("add on index=1: <head> is not <null>");
		if (tail.getOf(l) != null)
			fail("add on index=1: <tail> is not <null>");
		if (((List) l).size() != 0)
			fail("add on index=1: <size> is not <0>");

		l = newList.newInstance();
		if (!((List) l).add(0, 0))
			fail("add on index=0: return value is not <false>");
		if (head.getOf(l) == null)
			fail("add on index=0: <head> is <null>");
		if (tail.getOf(l) == null)
			fail("add on index=0: <tail> is <null>");
		if (head.getOf(l) != tail.getOf(l))
			fail("add on index=0: <head> != <tail>");
		if (((List) l).size() != 1)
			fail("add on index=0: <size> is not <1>");

		// center element
		l = newList.newInstance();
		for (int i = 0; i < 5; i++)
			if (i != 2)
				((List) l).add(i);
		if (!((List) l).add(2, -1))
			fail("add on center: return value is not <true>");
		if (!((List) l).toString().equals("[ 0, 1, -1, 3, 4 ]"))
			fail("add(int): something went horribly wrong\nexpected: [ 0, 1, -1, 3, 4 ]\nwas: "
					+ ((List) l).toString());
		if (((List) l).size() != 5)
			fail("add on center: <size> is not correct");

		// first element
		l = newList.newInstance();
		for (int i = 1; i < 5; i++)
			((List) l).add(i);
		if (!((List) l).add(0, 0))
			fail("add on index=0: return value is not <true>");
		if (!((List) l).toString().equals("[ 0, 1, 2, 3, 4 ]"))
			fail("add(int): something went horribly wrong\nexpected: [ 0, 1, 2, 3, 4 ]\nwas: " + ((List) l).toString());
		if (((List) l).size() != 5)
			fail("add on index=0: <size> is not correct");

		// last element
		l = newList.newInstance();
		for (int i = 0; i < 4; i++)
			((List) l).add(i);
		if (!((List) l).add(4, -4))
			fail("add on index=size: return value is not <true>");
		if (!((List) l).toString().equals("[ 0, 1, 2, 3, -4 ]"))
			fail("add(int): something went horribly wrong\nexpected: [ 0, 1, 2, 3, -4 ]\nwas: "
					+ ((List) l).toString());
		if (((List) l).size() != 5)
			fail("add on index=size: <size> is not correct");

		// out of bounds
		l = newList.newInstance();
		for (int i = 0; i < 5; i++)
			((List) l).add(i);
		if (((List) l).add(-1, -1))
			fail("add on index=-1: return value is not <false>");
		if (((List) l).add(-1, 6))
			fail("add on index=size+1: return value is not <false>");
		if (((List) l).size() != 5)
			fail("add on index=-1: <size> is not correct");
	}

	@DisplayName("- | Public Test get - depending on add(int)")
	@PublicTest
	@Order(8)
	void testGet() {
		// empty
		var l = newList.newInstance();
		if (((List) l).get(-1) != Integer.MIN_VALUE || ((List) l).get(0) != Integer.MIN_VALUE
				|| ((List) l).get(1) != Integer.MIN_VALUE)
			fail("get on empty list: doesn't return correct default value");

		// get element
		for (int i = 0; i < 5; i++)
			((List) l).add(-i);

		for (int i = 0; i < 5; i++)
			if (((List) l).get(i) != -i)
				fail("get(" + i + ") on [ 0, -1, -2, -3, -4 ] returned: <" + ((List) l).get(i) + "> expected: <" + (-i)
						+ ">");

		// get modifies references
		if (!((List) l).toString().equals("[ 0, -1, -2, -3, -4 ]"))
			fail("get modifies the list!");
	}

	@DisplayName("- | Public Test remove - depending on add(int)")
	@PublicTest
	@Order(8)
	void testRemove() {
		// center element
		var l = newList.newInstance();
		for (int i = 0; i < 5; i++)
			((List) l).add(-i);
		((List) l).remove(2);
		if (!((List) l).toString().equals("[ 0, -1, -3, -4 ]"))
			fail("remove(2) on [ 0, -1, -2, -3, -4 ] expected: [ 0, -1, -3, -4 ] was: " + ((List) l).toString());

		// first element
		l = newList.newInstance();
		for (int i = 0; i < 5; i++)
			((List) l).add(-i);
		((List) l).remove(0);
		if (!((List) l).toString().equals("[ -1, -2, -3, -4 ]"))
			fail("remove(0) on [ 0, -1, -2, -3, -4 ] expected: [ -1, -2, -3, -4 ] was: " + ((List) l).toString());

		// last element
		l = newList.newInstance();
		for (int i = 0; i < 5; i++)
			((List) l).add(-i);
		((List) l).remove(4);
		if (!((List) l).toString().equals("[ 0, -1, -2, -3 ]"))
			fail("remove(4) on [ 0, -1, -2, -3, -4 ] expected: [ 0, -1, -2, -3 ] was: " + ((List) l).toString());

		// out of bounds
		l = newList.newInstance();
		for (int i = 0; i < 5; i++)
			((List) l).add(-i);
		((List) l).remove(5);
		((List) l).remove(-1);
		if (!((List) l).toString().equals("[ 0, -1, -2, -3, -4 ]"))
			fail("remove on index < 0 or size <= index should not modify the list");
	}
}