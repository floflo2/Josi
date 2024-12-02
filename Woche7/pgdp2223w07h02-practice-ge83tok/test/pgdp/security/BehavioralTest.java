package pgdp.security;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.security.StructureHelper.*;
import static pgdp.security.TestHelper.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import de.tum.in.test.api.*;
import de.tum.in.test.api.dynamic.*;
import de.tum.in.test.api.io.*;
import de.tum.in.test.api.jupiter.*;
import pgdp.security.oracles.*;

@W07H02
public class BehavioralTest {

	private static boolean executeConsGetSetTests;
	private static boolean conSuc;
	private static boolean getSuc;
	private static boolean executeBehavioralTests;

	private static int countStructural;
	private static int countOtherPosts;
	private static int countTrack;

	private static String[] cons = { null, "newLightPanel", "newFlagPost", "newFinishPost" };
	private static String[] classes = { "SignalPost", "LightPanel", "FlagPost", "FinishPost" };

	@DisplayName("- | Classes Structural Test ")
	@Order(1)
	@PublicTest
	void testStructural() {
		init();

		CLASSES.entrySet().stream().forEach(c -> {
			c.getValue().check(Check.PUBLIC, Check.NOT_FINAL);
			if (c.getKey().equals("SignalPost")) {
				checkAbstract(c.getValue());
			} else {
				checkNotAbstract(c.getValue());
			}
		});

		CONSTRUCTORS.entrySet().stream().forEach(c -> c.getValue().check(Check.PUBLIC));

		FIELDS.entrySet().stream().forEach(f -> checkPrivate(f.getValue()));

		METHODS.entrySet().stream().forEach(m -> {
			m.getValue().check(Check.PUBLIC, Check.NOT_STATIC);
			if (m.getKey().equals("up") || m.getKey().equals("down")) {
				checkAbstract(m.getValue());
			} else {
				checkNotAbstract(m.getValue());
			}
		});

		executeConsGetSetTests = true;
		initTransitions();
	}

	@DisplayName("- | Constructors Test Public")
	@Order(2)
	@PublicTest
	void testConstructorsPublic() {
		if (!executeConsGetSetTests)
			TestUtils.privilegedFail(failStructural);

		int[] nums = { 77, 7, 5, 4 };

		for (int i = 0; i < 4; i++) {
			Object p;
			if (cons[i] == null) {
				p = new Post(nums[i]);
			} else {
				p = CONSTRUCTORS.get(cons[i]).newInstance(nums[i]);
			}
			var pos = FIELDS.get("postNumber").getOf(p);
			var dep = FIELDS.get("depiction").getOf(p);
			var lev = FIELDS.get("level").getOf(p);
			assertEquals(nums[i], pos,
					"Field [SignalPost.postNumber] has not been initialized correctly at initialization of "
							+ classes[i] + " with parameter " + nums[i] + ".");
			assertEquals("", dep,
					"Field [SignalPost.depiction] has not been initialized correctly at initialization of " + classes[i]
							+ ".");
			assertEquals(0, lev, "Field [SignalPost.level] has not been initialized correctly at initialization of "
					+ classes[i] + ".");
		}

		var t = CONSTRUCTORS.get("newTrack").newInstance(44);
		var posts = (SignalPost[]) FIELDS.get("posts").getOf(t);
		assertEquals(44, posts.length, "Field [Track.posts] has not been initialized correctly with parameter 44.");
		for (int i = 0; i < 43; i++) {
			if (i % 3 == 0) {
				assertTrue(CLASSES.get("LightPanel").toClass().isInstance(posts[i]),
						"SignalPost at position " + i + " has the wrong type.");
			} else {
				assertTrue(CLASSES.get("FlagPost").toClass().isInstance(posts[i]),
						"SignalPost at position " + i + " has the wrong type.");
			}
		}
		assertTrue(CLASSES.get("FinishPost").toClass().isInstance(posts[43]),
				"SignalPost at the last position has the wrong type.");
		conSuc = true;
	}

	@DisplayName("- | Getter Test Public")
	@Order(3)
	@PublicTest
	void testGetter() {
		if (!executeConsGetSetTests)
			TestUtils.privilegedFail(failStructural);

		var t = CONSTRUCTORS.get("newTrack").newInstance(2);
		var ex = FIELDS.get("posts").getOf(t);
		var ac = METHODS.get("getPosts").invokeOn(t);
		assertEquals(ex, ac, "'Track.getPost()' didn't work as expected.");

		for (int i = 0; i < 4; i++) {
			Object p;
			if (cons[i] == null) {
				p = new Post(i);
			} else {
				p = CONSTRUCTORS.get(cons[i]).newInstance(i);
			}
			setField(FIELDS.get("postNumber").toField(), p, 1337);
			setField(FIELDS.get("depiction").toField(), p, "yellow");
			setField(FIELDS.get("level").toField(), p, 4);

			ex = FIELDS.get("postNumber").getOf(p);
			ac = METHODS.get("getPostNumber").invokeOn(p);
			assertEquals(ex, ac, "'SignalPost.getPostNumber()' didn't work as expected for class " + classes[i] + ".");

			ex = FIELDS.get("depiction").getOf(p);
			ac = METHODS.get("getDepiction").invokeOn(p);
			assertEquals(ex, ac, "'SignalPost.getDepiction()' didn't work as expected for class " + classes[i] + ".");

			ex = FIELDS.get("level").getOf(p);
			ac = METHODS.get("getLevel").invokeOn(p);
			assertEquals(ex, ac, "'SignalPost.getLevel()' didn't work as expected for class " + classes[i] + ".");
		}
		getSuc = true;
	}

	@DisplayName("- | Setter Test Public")
	@Order(4)
	@PublicTest
	void testSetter() {
		if (!executeConsGetSetTests)
			TestUtils.privilegedFail(failStructural);

		var t = CONSTRUCTORS.get("newTrack").newInstance(5);
		var ex = Array.newInstance(CLASSES.get("SignalPost").toClass(), 2);
		Array.set(ex, 0, CONSTRUCTORS.get("newLightPanel").newInstance(0));
		Array.set(ex, 1, CONSTRUCTORS.get("newFlagPost").newInstance(1));
		METHODS.get("setPosts").invokeOn(t, new Object[] { ex });
		var ac = FIELDS.get("posts").getOf(t);
		assertArrayEquals((Object[]) ex, (Object[]) ac, "'Track.setPosts()' didn't work as expected.");

		for (int i = 0; i < 4; i++) {
			Object p;
			if (cons[i] == null) {
				p = new Post(i);
			} else {
				p = CONSTRUCTORS.get(cons[i]).newInstance(i);
			}

			METHODS.get("setPostNumber").invokeOn(p, 1337);
			ac = FIELDS.get("postNumber").getOf(p);
			assertEquals(1337, ac,
					"'SignalPost.setPostNumber()' didn't work as expected for class " + classes[i] + ".");

			METHODS.get("setDepiction").invokeOn(p, "red");
			ac = FIELDS.get("depiction").getOf(p);
			assertEquals("red", ac,
					"'SignalPost.setDepiction()' didn't work as expected for class " + classes[i] + ".");

			METHODS.get("setLevel").invokeOn(p, 3);
			ac = FIELDS.get("level").getOf(p);
			assertEquals(3, ac, "'SignalPost.setLevel()' didn't work as expected for class " + classes[i] + ".");
		}
		executeBehavioralTests = conSuc && getSuc;
	}

	static void constructorHelper(int amount) {
		var track = CONSTRUCTORS.get("newTrack").newInstance(amount);
		var oracle = new TrackOracle(amount);

		var posts = (SignalPost[]) FIELDS.get("posts").getOf(track);
		assertEquals(oracle.getPosts().length, posts.length,
				"Field [Track.posts] has not been initialized correctly with parameter " + amount + ".");
		for (int i = 0; i < oracle.getPosts().length - 1; i++) {
			if (i % 3 == 0) {
				assertTrue(CLASSES.get("LightPanel").toClass().isInstance(posts[i]),
						"SignalPost at position " + i + " has the wrong type.");
			} else {
				assertTrue(CLASSES.get("FlagPost").toClass().isInstance(posts[i]),
						"SignalPost at position " + i + " has the wrong type.");
			}
		}
		assertTrue(CLASSES.get("FinishPost").toClass().isInstance(posts[oracle.getPosts().length - 1]),
				"SignalPost at the last position has the wrong type.");
	}

	@DisplayName("- | Constructor Test Hidden")
	@Order(5)
	@HiddenTest
	void testConstructorHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		countStructural++;
		constructorHelper(100);
		constructorHelper(1);
		constructorHelper(0);
		constructorHelper(-1);
		constructorHelper(-10);
		countTrack++;
	}

	static void toStringHelper(Object post, SignalPostOracle oracle, int p, int max, String end) {
		String[] dep = { "", "green", "blue", "yellow", "doubleYellow", "red", "green/blue", "doubleYellow/[SC]", "" };
		dep[8] = end;
		int[] lev = { 0, 1, 1, 2, 3, 4, 1, 3, 5 };

		for (int i = 0; i < max; i++) {
			setField(FIELDS.get("depiction").toField(), post, dep[i]);
			setField(FIELDS.get("level").toField(), post, lev[i]);
			setField(FIELDS.get("postNumber").toField(), post, i);
			oracle.setDepiction(dep[i]);
			oracle.setLevel(lev[i]);
			oracle.setPostNumber(i);

			String ac = (String) METHODS.get("toString").invokeOn(post);
			String ex = oracle.toString();
			String exC = oracle.toStringColored();
			if (!ac.equals(ex) && !ac.equals(exC)) {
				fail("'SignalPost.toString()' didn't work as expected for class " + classes[p] + ". ==> expected: <"
						+ ex + (i != 0 ? "> or <" + exC : "") + "> but was: <" + ac + ">");
			}
		}

	}

	@DisplayName("- | SignalPost toString Public")
	@Order(6)
	@PublicTest
	void testSignalPostToStringPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		toStringHelper(new Post(0), new PostOracle(0), 0, 6, "");
	}

	@DisplayName("- | SignalPost toString Hidden")
	@Order(7)
	@HiddenTest
	void testSignalPostToStringHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		toStringHelper(new Post(0), new PostOracle(0), 0, 9, "");
		countOtherPosts++;
	}

	@DisplayName("- | LightPanel toString Public")
	@Order(8)
	@PublicTest
	void testLightPanelToStringPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		toStringHelper(CONSTRUCTORS.get("newLightPanel").newInstance(0), new LightPanelOracle(0), 1, 6, "yellow");
	}

	@DisplayName("- | LightPanel toString Hidden")
	@Order(9)
	@HiddenTest
	void testLightPanelToStringHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		toStringHelper(CONSTRUCTORS.get("newLightPanel").newInstance(0), new LightPanelOracle(0), 1, 9, "yellow");
		countOtherPosts++;
	}

	@DisplayName("- | FlagPost toString Public")
	@Order(10)
	@PublicTest
	void testFlagPostToStringPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		toStringHelper(CONSTRUCTORS.get("newFlagPost").newInstance(0), new FlagPostOracle(0), 2, 6,
				"green/yellow/red/blue");
	}

	@DisplayName("- | FlagPost toString Hidden")
	@Order(11)
	@HiddenTest
	void testFlagPostToStringHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		toStringHelper(CONSTRUCTORS.get("newFlagPost").newInstance(0), new FlagPostOracle(0), 2, 9,
				"green/yellow/red/blue");
		countOtherPosts++;
	}

	@DisplayName("- | FinishPost toString Public")
	@Order(12)
	@PublicTest
	void testFinishPostToStringPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		toStringHelper(CONSTRUCTORS.get("newFinishPost").newInstance(0), new FinishPostOracle(0), 3, 6, "chequered");
	}

	@DisplayName("- | FinishPost toString Hidden")
	@Order(13)
	@HiddenTest
	void testFinishPostToStringHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		toStringHelper(CONSTRUCTORS.get("newFinishPost").newInstance(0), new FinishPostOracle(0), 3, 9, "chequered");
		countOtherPosts++;
	}

	static void upHelper(Object post, SignalPostOracle oracle, int p, List<Transition> list) {
		for (Transition t : list) {
			setField(FIELDS.get("level").toField(), post, t.level());
			setField(FIELDS.get("depiction").toField(), post, t.state());
			oracle.setLevel(t.level());
			oracle.setDepiction(t.state());

			var ret = METHODS.get("up").invokeOn(post, t.type());
			var retO = oracle.up(t.type());

			assertEquals(retO, ret,
					"'SignalPost.up()' didn't work as expected for class " + classes[p] + " in state {level="
							+ t.level() + ", depiction=\"" + t.state() + "\"} and with parameter \"" + t.type()
							+ "\". Wrong value returned.");

			String ac = (String) FIELDS.get("depiction").getOf(post);
			String ex = oracle.getDepiction();
			assertEquals(ex, ac,
					"'SignalPost.up()' didn't work as expected for class " + classes[p] + " in state {level="
							+ t.level() + ", depiction=\"" + t.state() + "\"} and with parameter \"" + t.type()
							+ "\". Wrong value in [SignalPost.depiction].");

			int lev = (int) FIELDS.get("level").getOf(post);
			int levEx = oracle.getLevel();
			assertEquals(levEx, lev,
					"'SignalPost.up()' didn't work as expected for class " + classes[p] + " in state {level="
							+ t.level() + ", depiction=\"" + t.state() + "\"} and with parameter \"" + t.type()
							+ "\". Wrong value in [SignalPost.level].");
		}
	}

	@DisplayName("- | LightPanel up Public")
	@Order(14)
	@PublicTest
	void testLightPanelUpPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		upHelper(CONSTRUCTORS.get("newLightPanel").newInstance(0), new LightPanelOracle(0), 1, upP);
	}

	@DisplayName("- | LightPanel up Hidden")
	@Order(15)
	@HiddenTest
	void testLightPanelUpHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		upHelper(CONSTRUCTORS.get("newLightPanel").newInstance(0), new LightPanelOracle(0), 1, upH);
		upHelper(CONSTRUCTORS.get("newLightPanel").newInstance(0), new LightPanelOracle(0), 1, upL);
		countOtherPosts++;
	}

	@DisplayName("- | FlagPost up Public")
	@Order(16)
	@PublicTest
	void testFlagPostUpPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		upHelper(CONSTRUCTORS.get("newFlagPost").newInstance(0), new FlagPostOracle(0), 2, upP);
	}

	@DisplayName("- | FlagPost up Hidden")
	@Order(17)
	@HiddenTest
	void testFlagPostUpHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		upHelper(CONSTRUCTORS.get("newFlagPost").newInstance(0), new FlagPostOracle(0), 2, upH);
		upHelper(CONSTRUCTORS.get("newFlagPost").newInstance(0), new FlagPostOracle(0), 2, upF);
		countOtherPosts++;
	}

	@DisplayName("- | FinishPost up Public")
	@Order(18)
	@PublicTest
	void testFinishPostUpPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		upHelper(CONSTRUCTORS.get("newFinishPost").newInstance(0), new FinishPostOracle(0), 3, upP);
	}

	@DisplayName("- | FinishPost up Hidden")
	@Order(19)
	@HiddenTest
	void testFinishPostUpHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		upHelper(CONSTRUCTORS.get("newFinishPost").newInstance(0), new FinishPostOracle(0), 3, upH);
		upHelper(CONSTRUCTORS.get("newFinishPost").newInstance(0), new FinishPostOracle(0), 3, upFi);
		countOtherPosts++;
	}

	static void downHelper(Object post, SignalPostOracle oracle, int p, List<Transition> list) {
		for (Transition t : list) {
			setField(FIELDS.get("level").toField(), post, t.level());
			setField(FIELDS.get("depiction").toField(), post, t.state());
			oracle.setLevel(t.level());
			oracle.setDepiction(t.state());

			var ret = METHODS.get("down").invokeOn(post, t.type());
			var retO = oracle.down(t.type());

			assertEquals(retO, ret,
					"'SignalPost.down()' didn't work as expected for class " + classes[p] + " in state {level="
							+ t.level() + ", depiction=\"" + t.state() + "\"} and with parameter \"" + t.type()
							+ "\". Wrong value returned.");

			String ac = (String) FIELDS.get("depiction").getOf(post);
			String ex = oracle.getDepiction();
			assertEquals(ex, ac,
					"'SignalPost.down()' didn't work as expected for class " + classes[p] + " in state {level="
							+ t.level() + ", depiction=\"" + t.state() + "\"} and with parameter \"" + t.type()
							+ "\". Wrong value in [SignalPost.depiction].");

			int lev = (int) FIELDS.get("level").getOf(post);
			int levEx = oracle.getLevel();
			assertEquals(levEx, lev,
					"'SignalPost.down()' didn't work as expected for class " + classes[p] + " in state {level="
							+ t.level() + ", depiction=\"" + t.state() + "\"} and with parameter \"" + t.type()
							+ "\". Wrong value in [SignalPost.level].");
		}
	}

	@DisplayName("- | LightPanel down Public")
	@Order(20)
	@PublicTest
	void testLightPanelDownPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		downHelper(CONSTRUCTORS.get("newLightPanel").newInstance(0), new LightPanelOracle(0), 1, downP);
	}

	@DisplayName("- | LightPanel down Hidden")
	@Order(21)
	@HiddenTest
	void testLightPanelDownHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		downHelper(CONSTRUCTORS.get("newLightPanel").newInstance(0), new LightPanelOracle(0), 1, downH);
		downHelper(CONSTRUCTORS.get("newLightPanel").newInstance(0), new LightPanelOracle(0), 1, downL);
		countOtherPosts++;
	}

	@DisplayName("- | FlagPost down Public")
	@Order(22)
	@PublicTest
	void testFlagPostDownPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		downHelper(CONSTRUCTORS.get("newFlagPost").newInstance(0), new FlagPostOracle(0), 2, downP);
	}

	@DisplayName("- | FlagPost down Hidden")
	@Order(23)
	@HiddenTest
	void testFlagPostDownHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		downHelper(CONSTRUCTORS.get("newFlagPost").newInstance(0), new FlagPostOracle(0), 2, downH);
		downHelper(CONSTRUCTORS.get("newFlagPost").newInstance(0), new FlagPostOracle(0), 2, downF);
		countOtherPosts++;
	}

	@DisplayName("- | FinishPost down Public")
	@Order(24)
	@PublicTest
	void testFinishPostDownPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		downHelper(CONSTRUCTORS.get("newFinishPost").newInstance(0), new FinishPostOracle(0), 3, downP);
	}

	@DisplayName("- | FinishPost down Hidden")
	@Order(25)
	@HiddenTest
	void testFinishPostDownHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		downHelper(CONSTRUCTORS.get("newFinishPost").newInstance(0), new FinishPostOracle(0), 3, downH);
		downHelper(CONSTRUCTORS.get("newFinishPost").newInstance(0), new FinishPostOracle(0), 3, downFi);
		countOtherPosts++;
	}

	static void setArray(Object track, int size) {
		SignalPostOracle[] array = new SignalPostOracle[size];
		for (int i = 0; i < array.length - 1; i++) {
			array[i] = i % 3 == 0 ? new LightPanelOracle(i) : new FlagPostOracle(i);
		}
		array[array.length - 1] = new FinishPostOracle(array.length - 1);
		setField(FIELDS.get("posts").toField(), track, array);
	}

	static void setAllHelper(Object track, TrackOracle oracle, String type, boolean up) {
		oracle.setAll(type, up);
		METHODS.get("setAll").invokeOn(track, type, up);

		var ex = oracle.getPosts();
		var ac = (SignalPostOracle[]) FIELDS.get("posts").getOf(track);
		for (int i = 0; i < ex.length; i++) {
			assertEquals(ex[i].getLevel(), ac[i].getLevel(),
					"'Track.setAll()' didn't work as expected with parameter {type=\"" + type + "\", up=" + up
							+ "}. Wrong level at post " + i + ".");
			assertEquals(ex[i].getDepiction(), ac[i].getDepiction(),
					"'Track.setAll()' didn't work as expected with parameter {type=\"" + type + "\", up=" + up
							+ "}. Wrong depiction at post " + i + ".");
		}
	}

	@DisplayName("- | Track setAll Public")
	@Order(26)
	@PublicTest
	void testTrackSetAllPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(10);
		var track = CONSTRUCTORS.get("newTrack").newInstance(10);
		setArray(track, 10);
		setAllHelper(track, oracle, "green", true);
		setAllHelper(track, oracle, "green", false);
		setAllHelper(track, oracle, "red", true);
		setAllHelper(track, oracle, "clear", false);
	}

	@DisplayName("- | Track setAll Hidden")
	@Order(27)
	@HiddenTest
	void testTrackSetAllHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(30);
		var track = CONSTRUCTORS.get("newTrack").newInstance(30);
		setArray(track, 30);
		oracle.getPosts()[16] = new LightPanelOracle(16);
		Array.set(FIELDS.get("posts").getOf(track), 16, new LightPanelOracle(16));
		setAllHelper(track, oracle, "blue", true);
		setAllHelper(track, oracle, "green", true);
		setAllHelper(track, oracle, "blue", false);
		setAllHelper(track, oracle, "yellow", true);
		setAllHelper(track, oracle, "doubleYellow", true);
		setAllHelper(track, oracle, "[SC]", true);
		setAllHelper(track, oracle, "danger", false);
		setAllHelper(track, oracle, "end", true);
		countTrack++;
	}

	static void setRangeHelper(Object track, TrackOracle oracle, String type, boolean up, int start, int end) {
		oracle.setRange(type, up, start, end);
		METHODS.get("setRange").invokeOn(track, type, up, start, end);

		var ex = oracle.getPosts();
		var ac = (SignalPostOracle[]) FIELDS.get("posts").getOf(track);
		for (int i = 0; i < ex.length; i++) {
			assertEquals(ex[i].getLevel(), ac[i].getLevel(),
					"'Track.setRange()' didn't work as expected with parameter {type=" + type + ", up=" + up
							+ ", start=" + start + ", end=" + end + "}. Wrong level at post " + i + ".");
			assertEquals(ex[i].getDepiction(), ac[i].getDepiction(),
					"'Track.setRange()' didn't work as expected with parameter {type=" + type + ", up=" + up
							+ ", start=" + start + ", end=" + end + "}. Wrong depiction at post " + i + ".");
		}
	}

	@DisplayName("- | Track setRange Public")
	@Order(28)
	@PublicTest
	void testTrackSetRangePublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(10);
		var track = CONSTRUCTORS.get("newTrack").newInstance(10);
		setArray(track, 10);
		setRangeHelper(track, oracle, "green", true, 1, 5);
		setRangeHelper(track, oracle, "green", false, 1, 5);
		setRangeHelper(track, oracle, "red", true, 4, 9);
		setRangeHelper(track, oracle, "clear", false, 4, 9);
	}

	@DisplayName("- | Track setRange Hidden")
	@Order(29)
	@HiddenTest
	void testTrackSetRangeHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(30);
		var track = CONSTRUCTORS.get("newTrack").newInstance(30);
		setArray(track, 30);
		oracle.getPosts()[16] = new LightPanelOracle(16);
		Array.set(FIELDS.get("posts").getOf(track), 16, new LightPanelOracle(16));
		setRangeHelper(track, oracle, "blue", true, 15, 2);
		setRangeHelper(track, oracle, "green", true, 4, 8);
		setRangeHelper(track, oracle, "blue", false, 3, 7);
		setRangeHelper(track, oracle, "yellow", true, 29, 0);
		setRangeHelper(track, oracle, "doubleYellow", true, 28, 1);
		setRangeHelper(track, oracle, "[SC]", true, 0, 29);
		setRangeHelper(track, oracle, "danger", false, 29, 28);
		setRangeHelper(track, oracle, "end", true, 26, 25);
		countTrack++;
	}

	static void hazardHelper(Object track, TrackOracle oracle, int start, int end, boolean create) {
		if (create) {
			oracle.createHazardAt(start, end);
			METHODS.get("createHazardAt").invokeOn(track, start, end);
		} else {
			oracle.removeHazardAt(start, end);
			METHODS.get("removeHazardAt").invokeOn(track, start, end);
		}

		var ex = oracle.getPosts();
		var ac = (SignalPostOracle[]) FIELDS.get("posts").getOf(track);
		for (int i = 0; i < ex.length; i++) {
			assertEquals(ex[i].getLevel(), ac[i].getLevel(),
					"'Track." + (create ? "create" : "remove")
							+ "HazardAt()' didn't work as expected with parameter {start=" + start + ", end=" + end
							+ "}. Wrong level at post " + i + ".");
			assertEquals(ex[i].getDepiction(), ac[i].getDepiction(),
					"'Track." + (create ? "create" : "remove")
							+ "HazardAt()' didn't work as expected with parameter {start=" + start + ", end=" + end
							+ "}. Wrong depiction at post " + i + ".");
		}
	}

	@DisplayName("- | Track create-/removeHazardAt Public")
	@Order(30)
	@PublicTest
	void testTrackCreateHazardAtPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(10);
		var track = CONSTRUCTORS.get("newTrack").newInstance(10);
		setArray(track, 10);
		hazardHelper(track, oracle, 1, 5, true);
		hazardHelper(track, oracle, 1, 5, false);
	}

	@DisplayName("- | Track create-/removeHazardAt Hidden")
	@Order(31)
	@HiddenTest
	void testTrackCreateHazardAtHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(30);
		var track = CONSTRUCTORS.get("newTrack").newInstance(30);
		setArray(track, 30);
		oracle.getPosts()[16] = new LightPanelOracle(16);
		Array.set(FIELDS.get("posts").getOf(track), 16, new LightPanelOracle(16));
		hazardHelper(track, oracle, 15, 3, true);
		hazardHelper(track, oracle, 15, 3, false);
		countTrack++;
	}

	static void lappedHelper(Object track, TrackOracle oracle, int post, boolean create) {
		if (create) {
			oracle.createLappedCarAt(post);
			METHODS.get("createLappedCarAt").invokeOn(track, post);
		} else {
			oracle.removeLappedCarAt(post);
			METHODS.get("removeLappedCarAt").invokeOn(track, post);
		}

		var ex = oracle.getPosts();
		var ac = (SignalPostOracle[]) FIELDS.get("posts").getOf(track);
		for (int i = 0; i < ex.length; i++) {
			assertEquals(ex[i].getLevel(), ac[i].getLevel(),
					"'Track." + (create ? "create" : "remove")
							+ "LappedCarAt()' didn't work as expected with parameter {post=" + post
							+ "}. Wrong level at post " + i + ".");
			assertEquals(ex[i].getDepiction(), ac[i].getDepiction(),
					"'Track." + (create ? "create" : "remove")
							+ "LappedCarAt()' didn't work as expected with parameter {start=" + post
							+ "}. Wrong depiction at post " + i + ".");
		}
	}

	@DisplayName("- | Track create-/removeLappedCarAt Public")
	@Order(32)
	@PublicTest
	void testTrackCreateLappedCarAtPublic() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(10);
		var track = CONSTRUCTORS.get("newTrack").newInstance(10);
		setArray(track, 10);
		lappedHelper(track, oracle, 1, true);
		lappedHelper(track, oracle, 1, false);
	}

	@DisplayName("- | Track create-/removeLappedCarAt Hidden")
	@Order(33)
	@HiddenTest
	void testTrackCreateLappedCarAtHidden() {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(30);
		var track = CONSTRUCTORS.get("newTrack").newInstance(30);
		setArray(track, 30);
		oracle.getPosts()[16] = new LightPanelOracle(16);
		Array.set(FIELDS.get("posts").getOf(track), 16, new LightPanelOracle(16));
		lappedHelper(track, oracle, 28, true);
		lappedHelper(track, oracle, 28, false);
		countTrack++;
	}

	static void printHelper(Object track, TrackOracle oracle, IOTester iot) {
		iot.reset();
		METHODS.get("printStatus").invokeOn(track);
		String ac = iot.out().getLinesAsString().stream().collect(Collectors.joining("\n"));
		String ex = oracle.printStatus();
		String exC = oracle.printStatusColored();
		if (!ex.equals(ac) && !exC.equals(ac)) {
			fail("'Track.printStatus()' didn't work as expected. ==> expected: <" + ex + "> or <" + exC
					+ ">, but was: <" + ac + ">");
		}
	}

	@DisplayName("- | Track printStatus Public")
	@Order(34)
	@PublicTest
	void testTrackPrintStatusPublic(IOTester iot) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(5);
		var track = CONSTRUCTORS.get("newTrack").newInstance(5);
		setArray(track, 5);
		printHelper(track, oracle, iot);
		oracle.setRange("green", true, 1, 3);
		METHODS.get("setRange").invokeOn(track, "green", true, 1, 3);
		printHelper(track, oracle, iot);
	}

	@DisplayName("- | Track printStatus Hidden")
	@Order(35)
	@HiddenTest
	void testTrackPrintStatusHidden(IOTester iot) {
		if (!executeBehavioralTests)
			TestUtils.privilegedFail(failStructural);

		var oracle = new TrackOracle(30);
		var track = CONSTRUCTORS.get("newTrack").newInstance(30);
		setArray(track, 30);
		oracle.getPosts()[16] = new LightPanelOracle(16);
		Array.set(FIELDS.get("posts").getOf(track), 16, new LightPanelOracle(16));
		printHelper(track, oracle, iot);

		oracle.setRange("blue", true, 25, 10);
		METHODS.get("setRange").invokeOn(track, "blue", true, 25, 10);
		printHelper(track, oracle, iot);

		oracle.createHazardAt(5, 10);
		METHODS.get("createHazardAt").invokeOn(track, 5, 10);
		printHelper(track, oracle, iot);

		oracle.setAll("[SC]", true);
		METHODS.get("setAll").invokeOn(track, "[SC]", true);
		printHelper(track, oracle, iot);

		oracle.setAll("red", true);
		METHODS.get("setAll").invokeOn(track, "red", true);
		printHelper(track, oracle, iot);

		oracle.setAll("danger", false);
		METHODS.get("setAll").invokeOn(track, "danger", false);
		printHelper(track, oracle, iot);

		oracle.setAll("clear", false);
		METHODS.get("setAll").invokeOn(track, "clear", false);
		printHelper(track, oracle, iot);

		oracle.setAll("end", true);
		METHODS.get("setAll").invokeOn(track, "end", true);
		printHelper(track, oracle, iot);

		countTrack++;
	}

	@DisplayName("- | Grading Structural for 1 Point")
	@Order(36)
	@HiddenTest
	void gradingStructural1P() {
		assertTrue(countStructural == 1, "You only passed 0 out of 1 required tests for 1 Point in structural tests.");
	}

	@DisplayName("- | Grading Lightpanel/FlagPost/FinishPost for 1st Point")
	@Order(37)
	@HiddenTest
	void gradingPosts1P() {
		assertTrue(countOtherPosts >= 2,
				"You only passed " + countOtherPosts + " out of 2 required tests for 1st Point in class SignalPost/Lightpanel/FlagPost/FinishPost.");
	}

	@DisplayName("- | Grading Lightpanel/FlagPost/FinishPost for 2nd Point")
	@Order(38)
	@HiddenTest
	void gradingPosts2P() {
		assertTrue(countOtherPosts >= 4,
				"You only passed " + countOtherPosts + " out of 4 required tests for 2nd Point in class SignalPost/Lightpanel/FlagPost/FinishPost.");
	}

	@DisplayName("- | Grading Lightpanel/FlagPost/FinishPost for 3rd Point")
	@Order(39)
	@HiddenTest
	void gradingPosts3P() {
		assertTrue(countOtherPosts >= 7,
				"You only passed " + countOtherPosts + " out of 7 required tests for 3rd Point in class SignalPost/Lightpanel/FlagPost/FinishPost.");
	}

	@DisplayName("- | Grading Lightpanel/FlagPost/FinishPost for 4th Point")
	@Order(40)
	@HiddenTest
	void gradingPosts4P() {
		assertTrue(countOtherPosts == 10,
				"You only passed " + countOtherPosts + " out of 10 required tests for 4th Point in class SignalPost/Lightpanel/FlagPost/FinishPost.");
	}

	@DisplayName("- | Grading Track for 1 Point")
	@Order(41)
	@HiddenTest
	void gradingTrack1P() {
		assertTrue(countTrack == 6,
				"You only passed " + countTrack + " out of 6 required tests for 1 Point in class Track.");
	}
}
