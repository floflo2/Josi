package pgdp.messenger;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.messenger.helper.TestHelper.*;
import static pgdp.messenger.helper.UserArrayHelper.*;

import java.lang.reflect.*;
import java.util.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.dynamic.*;
import pgdp.messenger.mock.*;

public class UserArrayTest {
	static boolean executeBehavioralTests = false;

	static void testUserArrayStructural() {
		USER_ARRAY_CLASS.check(Check.PUBLIC);

		initializeUserArrayConstructorFieldsMethods();

		NEW_USER_ARRAY.check(Check.PUBLIC);

		assertAll(USER_ARRAY_CLASS::checkForNonPrivateFields);

		USER_ARRAY_FIELDS.entrySet().stream().forEach(ptf -> ptf.getValue().check(Check.NOT_STATIC, Check.NOT_FINAL));

		USER_ARRAY_METHODS.entrySet().stream()
				.forEach(ptf -> ptf.getValue().check(Check.PUBLIC, Check.NOT_STATIC, Check.NOT_FINAL));

		testUserArrayConstructorPublic();
		testUserArrayGetter();
		testUserArraySetter();
		executeBehavioralTests = true;
	}

	static void testUserArrayConstructorPublic() {
		testUserArrayConstructorHidden(10, 10);
	}

	static void testUserArrayGetter() {
		var userArray = NEW_USER_ARRAY.newInstance(10);
		var expected = USER_ARRAY_FIELDS.get("users").getOf(userArray);
		var actual = USER_ARRAY_METHODS.get("getUsers").invokeOn(userArray);
		assertEquals(expected, actual, methodFeedback("UserArray.getUsers"));
	}

	static void testUserArraySetter() {
		var userArray = NEW_USER_ARRAY.newInstance(10);
		var expected = new User[10];
		USER_ARRAY_METHODS.get("setUsers").invokeOn(userArray, new Object[] { expected });
		var actual = USER_ARRAY_FIELDS.get("users").getOf(userArray);
		assertEquals(expected, actual, methodFeedback("UserArray.setUsers"));
	}

	static void testUserArrayConstructorHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		int[] cap = { 10, -10, 0, 1 };
		int[] exCap = { 10, 1, 1, 1 };

		for (int i = 0; i < cap.length; i++) {
			testUserArrayConstructorHidden(cap[i], exCap[i]);
		}
	}

	static void testUserArrayConstructorHidden(int initCapacity, int expectedCap) {
		var userArray = NEW_USER_ARRAY.newInstance(initCapacity);
		var u = USER_ARRAY_FIELDS.get("users").getOf(userArray);
		assertNotNull(u, constructorFeedback("UserArray.users"));
		var actualCap = Array.getLength(u);
		assertEquals(expectedCap, actualCap, constructorFeedback("UserArray.users"));
	}

	static void testUserArrayAddUserPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var expected = new User[10];
		var actual = new User[10];
		var userArray = NEW_USER_ARRAY.newInstance(10);
		setField(USER_ARRAY_FIELDS.get("users").toField(), userArray, actual);

		for (int i = 0; i < 10; i++) {
			var u = new User(i + 5, "U" + (i + 5), null);
			Array.set(expected, i, u);
			USER_ARRAY_METHODS.get("addUser").invokeOn(userArray, u);
			assertArrayEquals((Object[]) expected, (Object[]) actual, methodFeedback("UserArray.addUser"));
		}
	}

	static void testUserArrayDeleteUserPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new UserArrayMock(10);

		var actual = new User[10];
		for (int i = 0; i < 10; i++) {
			var u = new User(i + 42, "U" + (i + 42), null);
			oracle.addUser(u);
			Array.set(actual, i, u);
		}

		var userArray = NEW_USER_ARRAY.newInstance(10);
		setField(USER_ARRAY_FIELDS.get("users").toField(), userArray, actual);

		Random random = new Random(1337);
		for (int i = 0; i < 10; i++) {
			int r = random.nextInt(10) + 69;
			var ret = USER_ARRAY_METHODS.get("deleteUser").invokeOn(userArray, r);
			var retO = oracle.deleteUser(r);
			assertSame(retO, ret, methodFeedback("UserArray.deleteUser"));
			assertArrayEquals(oracle.getUsers(), (Object[]) actual, methodFeedback("UserArray.deleteUser"));
		}
	}

	static void testUserArraySizePublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var actual = new User[10];
		var userArray = NEW_USER_ARRAY.newInstance(10);
		setField(USER_ARRAY_FIELDS.get("users").toField(), userArray, actual);

		for (int i = 0; i < 5; i++) {
			var u = new User(i + 420, "U" + 420, null);
			USER_ARRAY_METHODS.get("addUser").invokeOn(userArray, u);
		}
		var num = USER_ARRAY_METHODS.get("size").invokeOn(userArray);
		assertEquals(5, num, methodFeedback("UserArray.size"));
	}

	static void testUserArrayAddUserHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new UserArrayMock(10);

		var actual = new User[10];
		var userArray = NEW_USER_ARRAY.newInstance(10);
		setField(USER_ARRAY_FIELDS.get("users").toField(), userArray, actual);

		oracle.addUser(null);
		USER_ARRAY_METHODS.get("addUser").invokeOn(userArray, new Object[] { null });
		assertArrayEquals(oracle.getUsers(), (Object[]) actual, methodFeedback("UserArray.addUser"));

		for (int i = 0; i < 41; i++) {
			var u = new User(i + 69, "U" + i + 69, null);
			oracle.addUser(u);
			USER_ARRAY_METHODS.get("addUser").invokeOn(userArray, u);
			actual = (User[]) USER_ARRAY_FIELDS.get("users").getOf(userArray);
			assertArrayEquals(oracle.getUsers(), (Object[]) actual, methodFeedback("UserArray.addUser"));
		}
	}

	static void testUserArrayAddAndDeleteHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		Random random = new Random(1337);
		var oracle = new UserArrayMock(10);

		var actual = new User[10];
		var userArray = NEW_USER_ARRAY.newInstance(10);
		setField(USER_ARRAY_FIELDS.get("users").toField(), userArray, actual);

		oracle.addUser(null);
		USER_ARRAY_METHODS.get("addUser").invokeOn(userArray, new Object[] { null });
		assertArrayEquals(oracle.getUsers(), (Object[]) actual, methodFeedback("UserArray.addUser"));

		for (int i = 0; i < 500; i++) {
			int r = random.nextInt(100);
			if (oracle.getIndex(r) == -1) {
				var u = new User(r, "U" + r, null);
				oracle.addUser(u);
				USER_ARRAY_METHODS.get("addUser").invokeOn(userArray, u);
			} else {
				var retO = oracle.deleteUser(r);
				var ret = USER_ARRAY_METHODS.get("deleteUser").invokeOn(userArray, ((User) retO).getId());
				assertEquals(retO, ret, methodFeedback("UserArray.deleteUser") + i);
			}
			actual = (User[]) USER_ARRAY_FIELDS.get("users").getOf(userArray);
			assertArrayEquals(oracle.getUsers(), (Object[]) actual,
					methodFeedback(r == -1 ? "UserArray.addUser" : "UserArray.deleteUser"));
		}
	}

	public static void main(String[] args) {
		testUserArrayStructural();
		testUserArrayAddAndDeleteHidden();
	}

	static void testUserArraySizeHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var actual = new User[10];
		var userArray = NEW_USER_ARRAY.newInstance(10);
		setField(USER_ARRAY_FIELDS.get("users").toField(), userArray, actual);

		for (int i = 0; i < 100; i++) {
			var u = new User(i + 420, "U" + 420, null);
			USER_ARRAY_METHODS.get("addUser").invokeOn(userArray, u);
		}
		var num = USER_ARRAY_METHODS.get("size").invokeOn(userArray);
		assertEquals(100, num, methodFeedback("UserArray.size"));

		for (int i = 0; i < 20; i++) {
			USER_ARRAY_METHODS.get("deleteUser").invokeOn(userArray, i + 420);
		}
		num = USER_ARRAY_METHODS.get("size").invokeOn(userArray);
		assertEquals(80, num, methodFeedback("UserArray.size"));
	}
}
