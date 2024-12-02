package pgdp.messenger.helper;

import static pgdp.messenger.helper.UserArrayHelper.*;

import java.lang.reflect.*;
import java.util.*;

import de.tum.in.test.api.dynamic.*;
import pgdp.messenger.*;

public class PinguTalkHelper {

	public static DynamicClass<?> PINGU_TALK_CLASS = DynamicClass.toDynamic("pgdp.messenger.PinguTalk");

	public static DynamicConstructor<?> NEW_PINGU_TALK;

	public static Map<String, DynamicField<?>> PINGU_TALK_FIELDS = new HashMap<>();

	public static Map<String, DynamicMethod<?>> PINGU_TALK_METHODS = new HashMap<>();

	public static void initializePinguTalkConstructorFieldsMethods() {
		NEW_PINGU_TALK = PINGU_TALK_CLASS.constructor(int.class, int.class);
		initializeFields();
		initializeMethods();
	}

	public static void initializeFields() {
		PINGU_TALK_FIELDS.put("members", PINGU_TALK_CLASS.field(USER_ARRAY_CLASS.toClass(), "members"));
		PINGU_TALK_FIELDS.put("topics", PINGU_TALK_CLASS.field(Array.newInstance(Topic.class, 0).getClass(), "topics"));
		PINGU_TALK_FIELDS.put("topicID", PINGU_TALK_CLASS.field(long.class, "topicID"));
		PINGU_TALK_FIELDS.put("userID", PINGU_TALK_CLASS.field(long.class, "userID"));
	}

	public static void initializeMethods() {
		PINGU_TALK_METHODS.put("addMember", PINGU_TALK_CLASS.method(User.class, "addMember", String.class, User.class));
		PINGU_TALK_METHODS.put("deleteMember", PINGU_TALK_CLASS.method(User.class, "deleteMember", long.class));
		PINGU_TALK_METHODS.put("createNewTopic", PINGU_TALK_CLASS.method(Topic.class, "createNewTopic", String.class));
		PINGU_TALK_METHODS.put("deleteTopic", PINGU_TALK_CLASS.method(Topic.class, "deleteTopic", long.class));
		PINGU_TALK_METHODS.put("getMembers", PINGU_TALK_CLASS.method(USER_ARRAY_CLASS.toClass(), "getMembers"));
		PINGU_TALK_METHODS.put("getTopics",
				PINGU_TALK_CLASS.method(Array.newInstance(Topic.class, 0).getClass(), "getTopics"));
	}
}
