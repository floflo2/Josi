package pgdp.security;

import java.lang.reflect.*;
import java.util.*;

import de.tum.in.test.api.dynamic.*;

public class StructureHelper {

	public static Map<String, DynamicClass<?>> CLASSES = new HashMap<>();
	public static Map<String, DynamicConstructor<?>> CONSTRUCTORS = new HashMap<>();
	public static Map<String, DynamicField<?>> FIELDS = new HashMap<>();
	public static Map<String, DynamicMethod<?>> METHODS = new HashMap<>();

	public static void init() {
		initClasses();
		initConstructors();
		initFields();
		initMethods();
	}

	private static void initClasses() {
		CLASSES.put("Track", DynamicClass.toDynamic("pgdp.security.Track"));
		CLASSES.put("SignalPost", DynamicClass.toDynamic("pgdp.security.SignalPost"));
		CLASSES.put("LightPanel", DynamicClass.toDynamic("pgdp.security.LightPanel"));
		CLASSES.put("FlagPost", DynamicClass.toDynamic("pgdp.security.FlagPost"));
		CLASSES.put("FinishPost", DynamicClass.toDynamic("pgdp.security.FinishPost"));
	}

	private static void initConstructors() {
		CONSTRUCTORS.put("newTrack", CLASSES.get("Track").constructor(int.class));
		CONSTRUCTORS.put("newSignalPost", CLASSES.get("SignalPost").constructor(int.class));
		CONSTRUCTORS.put("newLightPanel", CLASSES.get("LightPanel").constructor(int.class));
		CONSTRUCTORS.put("newFlagPost", CLASSES.get("FlagPost").constructor(int.class));
		CONSTRUCTORS.put("newFinishPost", CLASSES.get("FinishPost").constructor(int.class));
	}

	private static void initFields() {
		FIELDS.put("posts", CLASSES.get("Track")
				.field(Array.newInstance(CLASSES.get("SignalPost").toClass(), 0).getClass(), "posts"));
		FIELDS.put("postNumber", CLASSES.get("SignalPost").field(int.class, "postNumber"));
		FIELDS.put("depiction", CLASSES.get("SignalPost").field(String.class, "depiction"));
		FIELDS.put("level", CLASSES.get("SignalPost").field(int.class, "level"));
	}

	private static void initMethods() {
		// Track
		METHODS.put("setAll", CLASSES.get("Track").method(void.class, "setAll", String.class, boolean.class));
		METHODS.put("setRange",
				CLASSES.get("Track").method(void.class, "setRange", String.class, boolean.class, int.class, int.class));
		METHODS.put("createHazardAt", CLASSES.get("Track").method(void.class, "createHazardAt", int.class, int.class));
		METHODS.put("removeHazardAt", CLASSES.get("Track").method(void.class, "removeHazardAt", int.class, int.class));
		METHODS.put("createLappedCarAt", CLASSES.get("Track").method(void.class, "createLappedCarAt", int.class));
		METHODS.put("removeLappedCarAt", CLASSES.get("Track").method(void.class, "removeLappedCarAt", int.class));
		METHODS.put("printStatus", CLASSES.get("Track").method(void.class, "printStatus"));
		METHODS.put("getPosts", CLASSES.get("Track")
				.method(Array.newInstance(CLASSES.get("SignalPost").toClass(), 0).getClass(), "getPosts"));
		METHODS.put("setPosts", CLASSES.get("Track").method(void.class, "setPosts",
				Array.newInstance(CLASSES.get("SignalPost").toClass(), 0).getClass()));

		// SignalPost
		METHODS.put("up", CLASSES.get("SignalPost").method(boolean.class, "up", String.class));
		METHODS.put("down", CLASSES.get("SignalPost").method(boolean.class, "down", String.class));
		METHODS.put("toString", CLASSES.get("SignalPost").method(String.class, "toString"));
		METHODS.put("getPostNumber", CLASSES.get("SignalPost").method(int.class, "getPostNumber"));
		METHODS.put("setPostNumber", CLASSES.get("SignalPost").method(void.class, "setPostNumber", int.class));
		METHODS.put("getDepiction", CLASSES.get("SignalPost").method(String.class, "getDepiction"));
		METHODS.put("setDepiction", CLASSES.get("SignalPost").method(void.class, "setDepiction", String.class));
		METHODS.put("getLevel", CLASSES.get("SignalPost").method(int.class, "getLevel"));
		METHODS.put("setLevel", CLASSES.get("SignalPost").method(void.class, "setLevel", int.class));
	}
}
