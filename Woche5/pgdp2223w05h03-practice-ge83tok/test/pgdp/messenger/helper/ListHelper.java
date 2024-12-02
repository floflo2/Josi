package pgdp.messenger.helper;

import java.lang.reflect.*;
import java.time.*;
import java.util.*;

import de.tum.in.test.api.dynamic.*;
import pgdp.messenger.*;

public class ListHelper {

	public static DynamicClass<?> LIST_CLASS = DynamicClass.toDynamic("pgdp.messenger.List");

	public static DynamicConstructor<?> NEW_LIST;

	public static Map<String, DynamicField<?>> LIST_FIELDS = new HashMap<>();

	public static Map<String, DynamicMethod<?>> LIST_METHODS = new HashMap<>();

	public static void initializeListConstructorFieldsMethods() {
		NEW_LIST = LIST_CLASS.constructor();
		initializeFields();
		initializeMethods();
	}

	public static void initializeFields() {
		LIST_FIELDS.put("head", LIST_CLASS.field(ListElement.class, "head"));
		LIST_FIELDS.put("tail", LIST_CLASS.field(ListElement.class, "tail"));
		LIST_FIELDS.put("size", LIST_CLASS.field(int.class, "size"));
	}

	public static void initializeMethods() {
		LIST_METHODS.put("insertAt", LIST_CLASS.method(void.class, "insertAt", int.class, Message.class));
		LIST_METHODS.put("add", LIST_CLASS.method(void.class, "add", Message.class));
		LIST_METHODS.put("delete", LIST_CLASS.method(void.class, "delete", Message.class));
		LIST_METHODS.put("size", LIST_CLASS.method(int.class, "size"));
		LIST_METHODS.put("getByIndex", LIST_CLASS.method(Message.class, "getByIndex", int.class));
		LIST_METHODS.put("getByID", LIST_CLASS.method(Message.class, "getByID", long.class));
		LIST_METHODS.put("megaMerge", LIST_CLASS.method(LIST_CLASS.toClass(), "megaMerge",
				Array.newInstance(LIST_CLASS.toClass(), 0).getClass()));
		LIST_METHODS.put("filterDays",
				LIST_CLASS.method(LIST_CLASS.toClass(), "filterDays", LocalDateTime.class, LocalDateTime.class));
		LIST_METHODS.put("filterUser", LIST_CLASS.method(LIST_CLASS.toClass(), "filterUser", User.class));
		LIST_METHODS.put("toString", LIST_CLASS.method(String.class, "toString"));
	}
}
