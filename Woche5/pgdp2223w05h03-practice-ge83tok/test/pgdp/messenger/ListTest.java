package pgdp.messenger;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.messenger.helper.ListHelper.*;
import static pgdp.messenger.helper.TestHelper.*;

import java.time.*;
import java.util.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.dynamic.*;
import pgdp.messenger.mock.*;

public class ListTest {
	private static boolean executeBehavioralTests = false;

	static void testListStructural() {
		LIST_CLASS.check(Check.PUBLIC);

		initializeListConstructorFieldsMethods();

		NEW_LIST.check(Check.PUBLIC);

		assertAll(LIST_CLASS::checkForNonPrivateFields);

		LIST_FIELDS.entrySet().stream().forEach(mf -> mf.getValue().check(Check.NOT_STATIC, Check.NOT_FINAL));

		LIST_METHODS.entrySet().stream().forEach(mm -> mm.getValue().check(Check.PUBLIC,
				mm.getKey().equals("megaMerge") ? Check.STATIC : Check.NOT_STATIC, Check.NOT_FINAL));

		executeBehavioralTests = true;
	}

	static void buildList(Object list, java.util.List<Object> oracle, int countAdd) {
		var m = new Message(countAdd + 43L, null, null, countAdd + 43 + "");
		oracle.add(0, m);
		var current = new ListElement(m, null);
		var tail = current;

		for (int i = countAdd - 2; i >= 0; i--) {
			m = new Message(i + 44L, null, null, i + 44 + "");
			oracle.add(0, m);
			current = new ListElement(m, current);
		}
		setField(LIST_FIELDS.get("size").toField(), list, countAdd);
		setField(LIST_FIELDS.get("head").toField(), list, current);
		setField(LIST_FIELDS.get("tail").toField(), list, tail);
	}

	static void testListGetByIDPublic(int count, int[] indices) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var list = NEW_LIST.newInstance();
		java.util.List<Object> oracle = new ArrayList<>();

		buildList(list, oracle, count);

		for (int i = 0; i < indices.length; i++) {
			var expected = oracle.get(indices[i]);
			var actual = LIST_METHODS.get("getByID").invokeOn(list, ((Message) expected).getId());
			assertEquals(expected, actual, methodFeedback("List.getByID"));
		}
	}

	static java.util.List<LocalDateTime> createDates(int count, Random r) {
		java.util.List<LocalDateTime> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			list.add(LocalDateTime.of(2022, r.nextInt(12) + 1, r.nextInt(28) + 1, r.nextInt(23), r.nextInt(60)));
		}
//		System.out.println(!(new HashSet<>(list).size() < list.size()));
		return list.stream().sorted().toList();
	}

	static java.util.List<Message> createMessages(java.util.List<LocalDateTime> dates) {
		java.util.List<Message> list = new ArrayList<>();
		for (LocalDateTime d : dates) {
			list.add(new Message(id++, d, new User(77L, "Pinguin", null), "Message " + id));
		}
		return list;
	}

	static void createList(List list, java.util.List<Message> messages) {
		for (Message m : messages) {
			list.add(m);
		}
	}

	static long id = 77L;

	static void testListMegaMergePublic(int[] arrayLength) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		ListMock[] oracles = new ListMock[arrayLength.length];
		List[] actuals = new List[arrayLength.length];
		Random r = new Random(1111);
		for (int i = 0; i < arrayLength.length; i++) {
			java.util.List<LocalDateTime> l = createDates(arrayLength[i], r);
			java.util.List<Message> m = createMessages(l);
			oracles[i] = new ListMock();
			actuals[i] = new List();
			createList(oracles[i], m);
			createList(actuals[i], m);
		}
		var ret = (List) LIST_METHODS.get("megaMerge").invokeStatic(new Object[] { actuals });
		assertNotNull(ret, methodFeedback("List.megaMerge") + " Return value is null.");
		var retO = ListMock.megaMerge(oracles);
		assertEquals(listToList(retO), listToList(ret), methodFeedback("List.megaMerge"));
	}

	static void testListFilterDaysPublic(int count, LocalDateTime start, LocalDateTime end, boolean hidden) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		ListMock oracle = new ListMock();
		List actual = new List();
		id = 1337L;
		Random r = new Random(1234);
		java.util.List<LocalDateTime> l = createDates(count, r);
		java.util.List<Message> m = createMessages(l);
		createList(oracle, m);
		createList(actual, m);

		var ret = (List) LIST_METHODS.get("filterDays").invokeOn(actual, start, end);
		assertNotNull(ret, methodFeedback("List.filterDays") + " Return value is null.");
		var retO = oracle.filterDays(start, end);
		assertEquals(listToList(retO), listToList(ret), methodFeedback("List.filterDays"));
		if (hidden) {
			assertEquals(listToList(oracle), listToList(actual),
					methodFeedback("List.filterDays") + " You modified the original list.");
		}
	}

	static void testListFilterUserPublic(int count, User u, int otherUsers, boolean hidden) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		ListMock oracle = new ListMock();
		List actual = new List();
		Random r = new Random(69);
		java.util.List<User> others = new ArrayList<>();
		for (int i = 0; i < otherUsers; i++) {
			others.add(new User(id++, "Some other user", null));
		}

		id = 7654L;
		for (int i = 0; i < count; i++) {
			if (r.nextBoolean() || u == null) {
				var m = new Message(id++, null, others.get(r.nextInt(others.size())), "I don't want these messages");
				oracle.add(m);
				actual.add(m);
			} else {
				var m = new Message(id++, null, u, "I want messages of this user to be filtered");
				oracle.add(m);
				actual.add(m);
			}
		}

		var ret = (List) LIST_METHODS.get("filterUser").invokeOn(actual, u);
		assertNotNull(ret, methodFeedback("List.filterUser") + " Return value is null.");
		var retO = oracle.filterUser(u);
		assertEquals(listToList(retO), listToList(ret), methodFeedback("List.filterUser"));
		if (hidden) {
			assertEquals(listToList(oracle), listToList(actual),
					methodFeedback("List.filterUser") + " You modified the original list.");
		}
	}

	static void testListToStringPublic(int count) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var list = NEW_LIST.newInstance();
		java.util.List<Object> oracle = new ArrayList<>();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < count; i++) {
			var m = new Message(i + 69, LocalDateTime.of(2022, 10, 20, 13, 53), new User(77L, "Olli", null),
					"Building...");
			LIST_METHODS.get("add").invokeOn(list, m);
			oracle.add(m);
			sb.append((i + 69) + "; 77; " + LocalDateTime.of(2022, 10, 20, 13, 53) + ": Building...\n");
		}

		var expected = sb.toString();
		var actual = LIST_METHODS.get("toString").invokeOn(list);
		assertEquals(expected, actual, methodFeedback("List.toString"));
	}

	static void testListGetByIDEdgeCasesHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		testListGetByIDEdgeCasesHidden(68);
		testListGetByIDEdgeCasesHidden(174);
	}

	static void testListGetByIDEdgeCasesHidden(int id) {
		var list = NEW_LIST.newInstance();

		var actual = LIST_METHODS.get("getByID").invokeOn(list, 0);
		assertNull(actual, methodFeedback("List.getByID"));

		for (int i = 0; i < 100; i++) {
			var m = new Message(i + 69, null, null, "");
			LIST_METHODS.get("add").invokeOn(list, m);
		}

		actual = LIST_METHODS.get("getByID").invokeOn(list, id);
		assertNull(actual, methodFeedback("List.getByID"));
	}

	static void testListToStringEdgeCaseHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var list = NEW_LIST.newInstance();
		var actual = LIST_METHODS.get("toString").invokeOn(list);
		assertEquals("", actual, methodFeedback("List.toString"));
	}
}
