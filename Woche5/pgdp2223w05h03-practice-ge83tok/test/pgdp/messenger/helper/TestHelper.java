package pgdp.messenger.helper;

import static pgdp.messenger.helper.ListHelper.*;

import java.lang.reflect.*;
import java.util.*;

import de.tum.in.test.api.*;
import pgdp.messenger.*;
import pgdp.messenger.mock.*;

public class TestHelper {

	public static String constructorFeedback(String field) {
		return "Field [" + field + "] has not been initialized correctly.";
	}

	public static String methodFeedback(String method) {
		return "'" + method + "()' didn't work as expected.";
	}

	public static String grading(String c, int score, int min, int p) {
		return "You only passed " + score + " out of " + min + " required tests for " + p + "P in class " + c + ".";
	}

	public static String failStructural = "Behavioral Tests not executed. Make sure to pass the structural test of this class first.";

	public static void setField(Field f, Object obj, Object value) {
		f.setAccessible(true);
		try {
			f.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			TestUtils.privilegedFail("Something went wrong.");
		}
	}

	public static java.util.List<Object> listToList(ListMock list) {
		java.util.List<Object> ret = new ArrayList<>();
		for (ListElement current = list.getHeadMock(); current != null; current = (ListElement) current.getNext()) {
			ret.add(current.getMessage());
		}
		return ret;
	}

	public static java.util.List<Object> listToList(Object list) {
		java.util.List<Object> ret = new ArrayList<>();
		for (ListElement current = (ListElement) LIST_FIELDS.get("head").getOf(list); current != null; current = current
				.getNext()) {
			ret.add(current.getMessage());
		}
		return ret;
	}

	public static int differ(Object[] a, Object[] a2) {
		for (int i = 0; i < a.length; i++) {
			if (!Objects.equals(a[i], a2[i]))
				return i;
		}
		return -1;
	}
}
