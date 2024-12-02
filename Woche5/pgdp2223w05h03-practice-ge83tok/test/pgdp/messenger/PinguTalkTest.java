package pgdp.messenger;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.messenger.helper.PinguTalkHelper.*;
import static pgdp.messenger.helper.TestHelper.*;
import static pgdp.messenger.helper.UserArrayHelper.*;

import java.lang.reflect.*;
import java.util.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.dynamic.*;
import pgdp.messenger.mock.*;

public class PinguTalkTest {
	static boolean executeBehavioralTests = false;
	private static String feedback = "";

	static void testPinguTalkStructural() {
		PINGU_TALK_CLASS.check(Check.PUBLIC);
		USER_ARRAY_CLASS.check(Check.PUBLIC);

		initializePinguTalkConstructorFieldsMethods();
		initializeUserArrayConstructorFieldsMethods();

		NEW_PINGU_TALK.check(Check.PUBLIC);

		assertAll(PINGU_TALK_CLASS::checkForNonPrivateFields);

		PINGU_TALK_FIELDS.entrySet().stream().forEach(ptf -> {
			if (ptf.getKey().equals("members") || ptf.getKey().equals("topics")) {
				ptf.getValue().check(Check.NOT_FINAL, Check.NOT_STATIC);
			} else {
				ptf.getValue().check(Check.NOT_FINAL, Check.STATIC);
			}
		});

		PINGU_TALK_METHODS.entrySet().stream()
				.forEach(ptf -> ptf.getValue().check(Check.PUBLIC, Check.NOT_STATIC, Check.NOT_FINAL));

		testPinguTalkConstructor(10, 10, 10, 10);
		testPinguTalkGetter("getMembers", "members");
		testPinguTalkGetter("getTopics", "topics");
		executeBehavioralTests = true;
	}

	static void testPinguTalkGetter(String getter, String field) {
		var pinguTalk = NEW_PINGU_TALK.newInstance(10, 10);
		var expected = PINGU_TALK_FIELDS.get(field).getOf(pinguTalk);
		var actual = PINGU_TALK_METHODS.get(getter).invokeOn(pinguTalk);
		assertEquals(expected, actual, methodFeedback(getter));
	}

	static void testPinguTalkConstructor(int initCapacityMembers, int maxCapacityTopics, int expectedMembersCap,
			int expectedTopicsCap) {
		var pinguTalk = NEW_PINGU_TALK.newInstance(initCapacityMembers, maxCapacityTopics);

		var actualTopicID = PINGU_TALK_FIELDS.get("topicID").getOf(pinguTalk);
		var actualUserID = PINGU_TALK_FIELDS.get("userID").getOf(pinguTalk);
		assertEquals(0L, actualTopicID, constructorFeedback("PinguTalk.topicID"));
		assertEquals(0L, actualUserID, constructorFeedback("PinguTalk.userID"));

		var members = PINGU_TALK_FIELDS.get("members").getOf(pinguTalk);
		assertNotNull(members, constructorFeedback("PinguTalk.members"));

		if (!USER_ARRAY_FIELDS.get("users").exists())
			TestUtils.privilegedFail("Attribut [UserArray.users] konnte nicht gefunden werden.");
		var users = USER_ARRAY_FIELDS.get("users").getOf(members);
		assertNotNull(users, constructorFeedback("UserArray.users"));
		var actualMembersCap = Array.getLength(users);
		assertEquals(expectedMembersCap, actualMembersCap, constructorFeedback("PinguTalk.members"));

		var topics = PINGU_TALK_FIELDS.get("topics").getOf(pinguTalk);
		assertNotNull(topics, constructorFeedback("PinguTalk.topics"));
		var actualTopicsCap = Array.getLength(topics);
		assertEquals(expectedTopicsCap, actualTopicsCap, constructorFeedback("PinguTalk.topics"));
	}

	static void testPinguTalkAddMemberPublic(int size, int count) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var ua = NEW_USER_ARRAY.newInstance(size);
		var pingu = NEW_PINGU_TALK.newInstance(size, 0);
		setField(PINGU_TALK_FIELDS.get("userID").toField(), null, 7);
		setField(PINGU_TALK_FIELDS.get("members").toField(), pingu, ua);
		var oracle = new UserArrayMock(size);

		for (int i = 0; i < count; i++) {
			var u = (User) PINGU_TALK_METHODS.get("addMember").invokeOn(pingu, "U" + (i + 7), null);
			oracle.addUser(u);
		}

		ua = PINGU_TALK_FIELDS.get("members").getOf(pingu);
		var array = USER_ARRAY_FIELDS.get("users").getOf(ua);
		assertArrayEquals(oracle.getUsers(), (Object[]) array, methodFeedback("PinguTalk.addMember"));

		for (int i = 0; i < count; i++) {
			var actual = (User) Array.get(array, i);

			var actualID = actual.getId();
			var actualName = actual.getName();
			var actualSup = actual.getSupervisor();

			assertEquals((long) i + 7, actualID, methodFeedback("PinguTalk.addMember"));
			assertEquals("U" + (i + 7), actualName, methodFeedback("PinguTalk.addMember"));
			assertNull(actualSup, methodFeedback("PinguTalk.addMember"));
		}
	}

	static void testPinguTalkDeleteMemberPublic(int size, int count) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		if (tryDeleteMember(new UserArrayMock(size), size, count)) {
			if (tryDeleteMember(NEW_USER_ARRAY.newInstance(size), size, count)) {
				TestUtils.privilegedFail(methodFeedback("PinguTalk.deleteMember") + feedback);
			}
		}
		assertTrue(true);
	}

	static boolean tryDeleteMember(Object ua, int size, int count) {
		var pingu = NEW_PINGU_TALK.newInstance(size, 0);
		setField(PINGU_TALK_FIELDS.get("members").toField(), pingu, ua);
		var oracle = new UserArrayMock(size);

		Object[] array = ua instanceof UserArrayMock ? ((UserArrayMock) ua).getUsers()
				: (Object[]) USER_ARRAY_FIELDS.get("users").getOf(ua);
		for (int i = 0; i < size; i++) {
			var u = new User(i + 69, "U" + (i + 69), null);
			Array.set(array, i, u);
			oracle.addUser(u);
		}

		Random random = new Random(1337);
		boolean failed = false, failedRet = false;
		for (int i = 0; i < count && !failed && !failedRet; i++) {
			long r = random.nextLong(count) + 69;
			var ret = PINGU_TALK_METHODS.get("deleteMember").invokeOn(pingu, r);
			var retO = oracle.deleteUser(r);
			failedRet = !(retO == ret);

			array = ua instanceof UserArrayMock ? ((UserArrayMock) ua).getUsers()
					: (Object[]) USER_ARRAY_FIELDS.get("users").getOf(ua);
			failed = !Arrays.equals(oracle.getUsers(), array);

			if (failedRet) {
				feedback = " Wrong object returend. ==> expected: <" + retO + ">, but was: <" + ret + ">";
			} else if (failed) {
				int d = differ(oracle.getUsers(), array);
				feedback = " ==> array contents differ at index [" + d + "], expected: <" + oracle.getUsers()[d]
						+ "> but was: <" + array[d] + ">";
			}
		}
		return failed || failedRet;
	}

	static void testPinguTalkCreateNewTopicPublic(int size, int count) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var pingu = NEW_PINGU_TALK.newInstance(0, size);

		var array = new Topic[size];
		setField(PINGU_TALK_FIELDS.get("topics").toField(), pingu, array);

		for (int i = 0; i < count; i++) {
			var ret = PINGU_TALK_METHODS.get("createNewTopic").invokeOn(pingu, "Topic" + (i + 69));
			var topicID = PINGU_TALK_FIELDS.get("topicID").getOf(pingu);
			if (i < size) {
				assertNotNull(ret, methodFeedback("PinguTalk.createNewTopic")
						+ " Wrong object for partially filled array returned.");
				assertEquals((long) i + 1, topicID, methodFeedback("PinguTalk.createNewTopic")
						+ "There seems to be an issue with your 'PinguTalk.topicID.'");
			} else {
				assertNull(ret, methodFeedback("PinguTalk.createNewTopic") + " Wrong object for full arary returned.");
			}
		}

		for (int i = 0; i < size; i++) {
			var topic = (Topic) Array.get(array, i);
			assertNotNull(topic, methodFeedback("PinguTalk.createNewTopic") + " No Topic was created.");

			var actualName = topic.getName();

			var actualID = topic.getId();

			assertEquals("Topic" + (i + 69), actualName,
					methodFeedback("PinguTalk.createNewTopic") + " Wrong name for new Topic.");
			assertEquals((long) i, actualID, methodFeedback("PinguTalk.createNewTopic") + " Wrong id for new Topic");
		}
	}

	static void testPinguTalkDeleteTopicPublic(int size, int count) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var pingu = NEW_PINGU_TALK.newInstance(0, size);

		var array = new Topic[size];
		var expected = new Topic[size];
		setField(PINGU_TALK_FIELDS.get("topics").toField(), pingu, array);

		for (int i = 0; i < size; i++) {
			var t = new Topic(i + 42, "T" + (i + 42));
			Array.set(array, i, t);
			Array.set(expected, i, t);
		}

		Random random = new Random(1337);
		for (int i = 0; i < count; i++) {
			int r = random.nextInt(size);
			var e = Array.get(expected, r);
			Array.set(expected, r, null);
			var a = PINGU_TALK_METHODS.get("deleteTopic").invokeOn(pingu, r + 42);
			assertEquals(e, a, methodFeedback("PinguTalk.deleteTopic"));
		}
		assertArrayEquals((Object[]) expected, (Object[]) array, methodFeedback("PinguTalk.deleteTopic"));
	}

	static void testPinguTalkConstructorHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		setField(PINGU_TALK_FIELDS.get("userID").toField(), null, 0L);
		setField(PINGU_TALK_FIELDS.get("topicID").toField(), null, 0L);

		testPinguTalkConstructor(10, 4, 10, 4);
		testPinguTalkConstructor(-10, 5, 1, 5);
		testPinguTalkConstructor(10, -3, 10, 1);
		testPinguTalkConstructor(0, -1, 1, 1);
	}

	static void testPinguTalkCreateAndDeleteHidden(int size, int countAdd1, int countDelete1, int countAdd2,
			int expectedId) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var pingu = NEW_PINGU_TALK.newInstance(0, size);

		var array = new Topic[size];
		var expected = new Topic[size];
		setField(PINGU_TALK_FIELDS.get("topics").toField(), pingu, array);
		setField(PINGU_TALK_FIELDS.get("topicID").toField(), null, 69L);

		for (int i = 0; i < countAdd1; i++) {
			PINGU_TALK_METHODS.get("createNewTopic").invokeOn(pingu, "Topic" + (i + 69));
			var t = Array.get(array, i);
			assertNotNull(t, methodFeedback("PinguTalk.createNewTopic") + " No Topic was created.");
			Array.set(expected, i, t);
			var topicID = PINGU_TALK_FIELDS.get("topicID").getOf(pingu);
			assertEquals((long) i + 70, topicID, methodFeedback("PinguTalk.createNewTopic")
					+ "There seems to be an issue with your 'PinguTalk.topicID.'");
		}
		assertArrayEquals((Object[]) expected, (Object[]) array, methodFeedback("PinguTalk.deleteTopic"));

		Random random = new Random(1337);
		for (int i = 0; i < countDelete1; i++) {
			int r = random.nextInt(size);
			var e = Array.get(expected, r);
			Array.set(expected, r, null);
			var a = PINGU_TALK_METHODS.get("deleteTopic").invokeOn(pingu, r + 69);
			assertEquals(e, a, methodFeedback("PinguTalk.deleteTopic"));
		}
		assertArrayEquals((Object[]) expected, (Object[]) array, methodFeedback("PinguTalk.deleteTopic"));

		for (int i = 0; i < countAdd2; i++) {
			var t = PINGU_TALK_METHODS.get("createNewTopic").invokeOn(pingu, "Topic" + (i + 69));
			boolean arrayFull = true;
			for (int j = 0; j < size; j++) {
				if (Array.get(expected, j) == null) {
					Array.set(expected, j, t);
					assertNotNull(t, methodFeedback("PinguTalk.createNewTopic") + " No Topic was created.");
					arrayFull = false;
					break;
				}
			}
			if (!arrayFull) {
				var topicID = PINGU_TALK_FIELDS.get("topicID").getOf(pingu);
				assertEquals((long) i + expectedId, topicID, methodFeedback("PinguTalk.createNewTopic")
						+ "There seems to be an issue with your 'PinguTalk.topicID.'");
			}
		}
	}
}
