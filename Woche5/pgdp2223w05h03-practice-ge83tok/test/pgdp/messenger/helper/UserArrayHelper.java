package pgdp.messenger.helper;

import java.lang.reflect.*;
import java.util.*;

import de.tum.in.test.api.dynamic.*;
import pgdp.messenger.*;

public class UserArrayHelper {

	public static DynamicClass<?> USER_ARRAY_CLASS = DynamicClass.toDynamic("pgdp.messenger.UserArray");

	public static DynamicConstructor<?> NEW_USER_ARRAY;

	public static Map<String, DynamicField<?>> USER_ARRAY_FIELDS = new HashMap<>();

	public static Map<String, DynamicMethod<?>> USER_ARRAY_METHODS = new HashMap<>();

	public static void initializeUserArrayConstructorFieldsMethods() {
		NEW_USER_ARRAY = USER_ARRAY_CLASS.constructor(int.class);
		initializeFields();
		initializeMethods();
	}

	public static void initializeFields() {
		USER_ARRAY_FIELDS.put("users", USER_ARRAY_CLASS.field(Array.newInstance(User.class, 0).getClass(), "users"));
	}

	public static void initializeMethods() {
		USER_ARRAY_METHODS.put("addUser", USER_ARRAY_CLASS.method(void.class, "addUser", User.class));
		USER_ARRAY_METHODS.put("deleteUser", USER_ARRAY_CLASS.method(User.class, "deleteUser", long.class));
		USER_ARRAY_METHODS.put("size", USER_ARRAY_CLASS.method(int.class, "size"));
		USER_ARRAY_METHODS.put("setUsers",
				USER_ARRAY_CLASS.method(void.class, "setUsers", Array.newInstance(User.class, 0).getClass()));
		USER_ARRAY_METHODS.put("getUsers",
				USER_ARRAY_CLASS.method(Array.newInstance(User.class, 0).getClass(), "getUsers"));
	}
}
