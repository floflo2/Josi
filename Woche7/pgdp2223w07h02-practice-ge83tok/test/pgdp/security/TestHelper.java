package pgdp.security;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.util.*;

import org.apiguardian.api.*;
import org.apiguardian.api.API.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.dynamic.*;

@API(status = Status.MAINTAINED)
public class TestHelper {

	public static String failStructural = "Behavioral Tests not executed. Make sure to pass the structural test of this class first.";

	// P = Public, H = Hidden, F = only for flags, L = only for LightPanels, Fi =
	// only for FinishPost
	public static List<Transition> upP = new ArrayList<>();
	public static List<Transition> upH = new ArrayList<>();
	public static List<Transition> upF = new ArrayList<>();
	public static List<Transition> upL = new ArrayList<>();
	public static List<Transition> upFi = new ArrayList<>();
	public static List<Transition> downP = new ArrayList<>();
	public static List<Transition> downH = new ArrayList<>();
	public static List<Transition> downF = new ArrayList<>();
	public static List<Transition> downL = new ArrayList<>();
	public static List<Transition> downFi = new ArrayList<>();

	public static void checkAbstract(DynamicClass<?> clazz) {
		int modifiers = clazz.toClass().getModifiers();
		if (!Modifier.isAbstract(modifiers))
			fail("Class " + clazz.getName() + " is not abstract.");
	}

	public static void checkAbstract(DynamicMethod<?> method) {
		int modifiers = method.toMethod().getModifiers();
		if (!Modifier.isAbstract(modifiers))
			fail("Method " + method.toString() + " is not abstract.");
	}

	public static void checkNotAbstract(DynamicClass<?> clazz) {
		int modifiers = clazz.toClass().getModifiers();
		if (Modifier.isAbstract(modifiers))
			fail("Class " + clazz.getName() + " is abstract.");
	}

	public static void checkNotAbstract(DynamicMethod<?> method) {
		int modifiers = method.toMethod().getModifiers();
		if (Modifier.isAbstract(modifiers))
			fail("Method " + method.toString() + " is abstract.");
	}

	public static void checkPrivate(DynamicField<?> field) {
		int modifiers = field.toField().getModifiers();
		if (!Modifier.isPrivate(modifiers))
			fail("Attribute " + field.toString() + " is not private.");
	}

	public static void setField(Field f, Object obj, Object value) {
		f.setAccessible(true);
		try {
			f.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			TestUtils.privilegedFail("Something went wrong.");
		}
	}

	public static String changeColors(String stringToColor) {
		StringBuilder sb = new StringBuilder();
		for (String s : stringToColor.split("/")) {
			sb.append(colorString(s) + "/");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	private static String colorString(String depiction) {
		return switch (depiction) {
		case "green" -> "\u001B[32m" + depiction;
		case "blue" -> "\u001B[94m" + depiction;
		case "yellow" -> "\u001B[33m" + depiction;
		case "doubleYellow" -> "\u001B[33m" + depiction;
		case "[SC]" -> "\u001B[33m[\u001B[0mSC\u001B[33m]";
		case "red" -> "\u001B[31m" + depiction;
		case "chequered" -> "\u001B[40m\u001B[97m" + "c" + "\u001B[107m\u001B[30m" + "h" + "\u001B[40m\u001B[97m" + "e"
				+ "\u001B[107m\u001B[30m" + "q" + "\u001B[40m\u001B[97m" + "u" + "\u001B[107m\u001B[30m" + "e"
				+ "\u001B[40m\u001B[97m" + "r" + "\u001B[107m\u001B[30m" + "e" + "\u001B[40m\u001B[97m" + "d";
		default -> depiction;
		} + "\u001B[0m";
	}

	public static void initTransitions() {
		upP.add(new Transition("", 0, "green"));
		upP.add(new Transition("", 0, "blue"));
		upP.add(new Transition("green", 1, "yellow"));
		upP.add(new Transition("blue", 1, "yellow"));
		upP.add(new Transition("yellow", 2, "doubleYellow"));
		upP.add(new Transition("doubleYellow", 3, "red"));
		upP.add(new Transition("red", 4, "end"));
		upP.add(new Transition("yellow", 2, "green"));
		upP.add(new Transition("red", 4, "yellow"));

		String[] state = { "", "green", "blue", "yellow", "doubleYellow", "red" };
		String[] stateFlagOnly = { "green/blue", "doubleYellow/[SC]", "green/yellow/red/blue" };
		String[] stateFinishOnly = { "green/blue", "doubleYellow/[SC]", "chequered" };
		String[] statePanelOnly = { "[SC]", "yellow" };
		int[] level = { 0, 1, 1, 2, 3, 4, 5 };
		String[] typeUp = { "clear", "green", "blue", "yellow", "doubleYellow", "[SC]", "red", "end", "trashInput" };

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < typeUp.length; j++) {
				upH.add(new Transition(state[i], level[i], typeUp[j]));
			}
		}
		for (int i = 0; i < stateFlagOnly.length; i++) {
			for (int j = 0; j < typeUp.length; j++) {
				upF.add(new Transition(stateFlagOnly[i], i * 2 + 1, typeUp[j]));
			}
		}
		for (int i = 0; i < statePanelOnly.length; i++) {
			for (int j = 0; j < typeUp.length; j++) {
				upL.add(new Transition(statePanelOnly[i], i * 2 + 3, typeUp[j]));
			}
		}
		for (int i = 0; i < stateFinishOnly.length; i++) {
			for (int j = 0; j < typeUp.length; j++) {
				upFi.add(new Transition(stateFinishOnly[i], i * 2 + 1, typeUp[j]));
			}
		}

		downP.add(new Transition("red", 4, "clear"));
		downP.add(new Transition("yellow", 2, "clear"));
		downP.add(new Transition("green", 1, "clear"));
		downP.add(new Transition("green", 1, "green"));
		downP.add(new Transition("blue", 1, "blue"));
		downP.add(new Transition("yellow", 2, "danger"));
		downP.add(new Transition("red", 4, "danger"));
		downP.add(new Transition("", 0, "clear"));
		downP.add(new Transition("red", 4, "green"));

		String[] typeDown = { "clear", "green", "blue", "danger", "trashInput" };

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < typeDown.length; j++) {
				downH.add(new Transition(state[i], level[i], typeDown[j]));
			}
		}
		for (int i = 0; i < stateFlagOnly.length; i++) {
			for (int j = 0; j < typeDown.length; j++) {
				downF.add(new Transition(stateFlagOnly[i], i * 2 + 1, typeDown[j]));
			}
		}
		for (int i = 0; i < statePanelOnly.length; i++) {
			for (int j = 0; j < typeDown.length; j++) {
				downL.add(new Transition(statePanelOnly[i], i * 2 + 3, typeDown[j]));
			}
		}
		for (int i = 0; i < stateFinishOnly.length; i++) {
			for (int j = 0; j < typeDown.length; j++) {
				downFi.add(new Transition(stateFinishOnly[i], i * 2 + 1, typeDown[j]));
			}
		}
	}
}
