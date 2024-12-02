package pgdp.trials;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.jupiter.PublicTest;

@TestClassAnnotation
public class GradingTests {

	private static boolean tog, tos, tom, tod;

	@PublicTest
	@DisplayName(value = "TrialOfTheGrasses - Grading")
	@Order(1)
	void testTrialOfTheGrassesGrading() {
		try {
			TrialOfTheGrassesBehaviorTest tests = new TrialOfTheGrassesBehaviorTest();
			tests.testLeaf();
			tests.testTree();
			tog = true;
		} catch (AssertionError ae) {
			fail("At least one of the required cases failed.");
		} catch (Exception ex) {
			fail("An unexpected Exception was thrown.");
		}
	}

	@PublicTest
	@DisplayName(value = "TrialOfTheSword - Grading")
	@Order(2)
	void testTrialOfTheSwordGrading() {
		try {
			TrialOfTheSwordBehaviorTest tests = new TrialOfTheSwordBehaviorTest();
			tests.testFlatArr1();
			tests.testFlatArr2();
			tos = true;
		} catch (AssertionError ae) {
			fail("At least one of the required cases failed.");
		} catch (Exception ex) {
			fail("An unexpected Exception was thrown.");
		}
	}

	@PublicTest
	@DisplayName(value = "TrialOfTheMountains - Grading")
	@Order(3)
	void testTrialOfTheMountainsGrading() {
		try {
			TrialOfTheMountainsBehaviorTest tests = new TrialOfTheMountainsBehaviorTest();
			tests.testSingleMountain();
			tests.testMountainsNotConnected();
			tests.testSimpleChain();
			tests.testCycle();
			tests.testFullyConnected();
			tests.testComplexNetwork();
			tom = true;
		} catch (AssertionError ae) {
			fail("At least one of the required cases failed.");
		} catch (Exception ex) {
			fail("An unexpected Exception was thrown.");
		}
	}

	@PublicTest
	@DisplayName(value = "TrialOfTheDreams - Grading")
	@Order(4)
	void testTrialOfTheDreamsGrading() {
		try {
			TrialOfTheDreamsBehaviorTest tests = new TrialOfTheDreamsBehaviorTest();
			tests.testLockPickSimple();
			tests.testLockPickHard();
			tests.testLockPickEmpty();
			tod = true;
		} catch (AssertionError ae) {
			fail("At least one of the required cases failed.");
		} catch (Exception ex) {
			fail("An unexpected Exception was thrown.");
		}
	}

	@PublicTest
	@DisplayName(value = "Story - This test will always fail.")
	@Order(5)
	void testForStory() {
		StringBuilder msg = new StringBuilder();
		msg.append("THIS TEST WILL ALWAYS FAIL! It's purely part of the story.\n");
		msg.append("   ___________________________________________________\n");
		msg.append(" / \\                                                  \\\n");
		msg.append("|   |                                                 |\n");
		msg.append(" \\_ |                                                 |\n");
		msg.append("    | ...                                             |\n");
		msg.append("    | Eve: \"Hey! Deine erste Prüfung sind die         |\n");
		msg.append("    | 'Trials of the Grasses'. Mach dir keine Sorgen, |\n");
		msg.append("    | du schaffst das!\"                               |\n");
		msg.append("    | ...                                             |\n");
		if (tog) {
			msg.append("    | Eve: \"Super! Schnapp dir dein Schwert und lass  |\n");
			msg.append("    | uns direkt weiter gehen zur nächsten Prüfung.\"  |\n");
			msg.append("    | ...                                             |\n");
			if (tos) {
				msg.append("    | Eve: \"Yay! Die Hälfte schon geschafft. Keine    |\n");
				msg.append("    | Zeit für Erholung, wir müssen direkt weiter in  |\n");
				msg.append("    | die Berge! Hopp Hopp!\"                          |\n");
				msg.append("    | ...                                             |\n");
				if (tom) {
					msg.append("    | Eve: \"Du weist ja wirklich alles! Nur noch die  |\n");
					msg.append("    | 'Trials of the Dreams', die schwerste Prüfung.\" |\n");
					msg.append("    | ...                                             |\n");
					if (tod) {
						msg.append("    | Eve: \"Ahhhhh! Du hast es geschafft!!!\"          |\n");
						msg.append("    | (Sie greift in ihr Abendkleid und holt          |\n");
						msg.append("    | einen Gegenstand heraus.)                       |\n");
						msg.append("    | \"Hier, dieses Medallion hast du dir jetzt       |\n");
						msg.append("    | endlich verdient! Jetzt gehörst du wirklich zur |\n");
						msg.append("    | 'School of Pengu'!\"                             |\n");
					}
				}
			}
		}
		msg.append("    |   ______________________________________________|___\n");
		msg.append("    |  /                                                 /\n");
		msg.append("    \\_/_________________________________________________/");
		fail(msg.toString());
	}

}
