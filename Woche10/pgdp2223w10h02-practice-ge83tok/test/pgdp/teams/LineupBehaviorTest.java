package pgdp.teams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;

@W13H02
public class LineupBehaviorTest {

	private static AtomicInteger computeScoreTestsPassed;
	private static AtomicInteger computeOptimalLineupTestsPassed;

	@BeforeAll
	public static void setup() {
		computeScoreTestsPassed = new AtomicInteger();
		computeOptimalLineupTestsPassed = new AtomicInteger();
	}

	// test computeScores
	private void assertLineup(Lineup l, int expectedSkill, int expectedSynergy, int expectedScore) {
		assertEquals(expectedSkill, l.getTeamSkill(), "teamSkill has the wrong value.");
		assertEquals(expectedSynergy, l.getTeamSynergy(), "teamSynergy has the wrong value.");
		assertEquals(expectedScore, l.getTeamScore(), "teamScore has the wrong value.");
	}

	// empty lineup
	@HiddenTest
	@DisplayName(value = "Test computeScores - all empty")
	@Order(1)
	public void testCSEmpty() {
		Lineup l = new Lineup(Set.of(), Set.of(), Set.of());
		assertLineup(l, 0, 0, 0);

		computeScoreTestsPassed.incrementAndGet();
	}

	// test synergy
	@HiddenTest
	@DisplayName(value = "Test computeScores - synergy: intra groups")
	@Order(1)
	public void testCSSynergyGroupOnly() {
		Penguin p1 = new Penguin("p1", 0, 0, 0);
		Penguin p2 = new Penguin("p2", 0, 0, 0);

		// test attackers
		Penguin.setSynergy(p1, p2, 10);
		Lineup l = new Lineup(Set.of(p1, p2), Set.of(), Set.of());
		assertLineup(l, 0, 20, 20);

		// test defenders
		l = new Lineup(Set.of(), Set.of(p1, p2), Set.of());
		assertLineup(l, 0, 20, 20);

		// test supporters
		l = new Lineup(Set.of(), Set.of(), Set.of(p1, p2));
		assertLineup(l, 0, 20, 20);

		// test more complex situation
		Penguin p3 = new Penguin("p3", 0, 0, 0);
		Penguin p4 = new Penguin("p4", 0, 0, 0);
		Penguin p5 = new Penguin("p5", 0, 0, 0);
		Penguin.setSynergy(p4, p5, -5);
		l = new Lineup(Set.of(p1, p2), Set.of(p3), Set.of(p4, p5));
		assertLineup(l, 0, 10, 10);

		computeScoreTestsPassed.incrementAndGet();
	}

	@HiddenTest
	@DisplayName(value = "Test computeScores - synergy: inter groups")
	@Order(1)
	public void testCSSynergyInterGroups() {
		Penguin p1 = new Penguin("p1", 0, 0, 0);
		Penguin p2 = new Penguin("p2", 0, 0, 0);
		Penguin p3 = new Penguin("p3", 0, 0, 0);
		Penguin.setSynergy(p1, p2, 10);
		Lineup l = new Lineup(Set.of(p1), Set.of(p2), Set.of(p3));
		assertLineup(l, 0, 10, 10);

		Penguin.setSynergy(p1, p3, 5);
		l = new Lineup(Set.of(p1), Set.of(p2), Set.of(p3));
		assertLineup(l, 0, 15, 15);

		computeScoreTestsPassed.incrementAndGet();
	}

	@HiddenTest
	@DisplayName(value = "Test computeScores - synergy: combined")
	@Order(1)
	public void testSCSynergyCombined() {
		Penguin p1 = new Penguin("p1", 0, 0, 0);
		Penguin p2 = new Penguin("p2", 0, 0, 0);
		Penguin p3 = new Penguin("p3", 0, 0, 0);
		Penguin p4 = new Penguin("p4", 0, 0, 0);
		Penguin p5 = new Penguin("p5", 0, 0, 0);
		Penguin.setSynergy(p1, p2, 10);
		Penguin.setSynergy(p4, p5, -5);
		Penguin.setSynergy(p1, p3, 5);
		Penguin.setSynergy(p3, p5, -10);
		Lineup l = new Lineup(Set.of(p1, p2), Set.of(p3), Set.of(p4, p5));
		assertLineup(l, 0, 5, 5);

		computeScoreTestsPassed.incrementAndGet();
	}

	// test skill
	@HiddenTest
	@DisplayName(value = "Test computeScores - skill: single groups")
	@Order(1)
	public void testSCSkillSingleGroups() {
		Penguin p1 = new Penguin("p1", 1, 2, 3);
		Penguin p2 = new Penguin("p2", 10, 20, 30);
		// attackers
		Lineup l = new Lineup(Set.of(p1), Set.of(), Set.of());
		assertLineup(l, 1, 0, 1);
		l = new Lineup(Set.of(p1, p2), Set.of(), Set.of());
		assertLineup(l, 11, 0, 11);
		// defenders
		l = new Lineup(Set.of(), Set.of(p1), Set.of());
		assertLineup(l, 2, 0, 2);
		l = new Lineup(Set.of(), Set.of(p1, p2), Set.of());
		assertLineup(l, 22, 0, 22);
		// supports
		l = new Lineup(Set.of(), Set.of(), Set.of(p1));
		assertLineup(l, 3, 0, 3);
		l = new Lineup(Set.of(), Set.of(), Set.of(p1, p2));
		assertLineup(l, 33, 0, 33);

		computeScoreTestsPassed.incrementAndGet();
	}

	@HiddenTest
	@DisplayName(value = "Test computeScores - skill: combined")
	@Order(1)
	public void testSCSkillCombined() {
		Penguin p1 = new Penguin("p1", 10, -100, -100);
		Penguin p2 = new Penguin("p2", -5, -100, -100);
		Penguin p3 = new Penguin("p3", -100, 10, -100);
		Penguin p4 = new Penguin("p4", -100, -100, 10);
		Penguin p5 = new Penguin("p5", -100, -100, -5);
		Lineup l = new Lineup(Set.of(p1, p2), Set.of(p3), Set.of(p4, p5));
		assertLineup(l, 20, 0, 20);

		computeScoreTestsPassed.incrementAndGet();
	}

	// test synergy and skill combined
	@HiddenTest
	@DisplayName(value = "Test computeScores - skill and synergy")
	@Order(1)
	public void testSCAll() {
		Penguin p1 = new Penguin("p1", 10, -100, -100);
		Penguin p2 = new Penguin("p2", -5, -100, -100);
		Penguin p3 = new Penguin("p3", -100, 10, -100);
		Penguin p4 = new Penguin("p4", -100, -100, 10);
		Penguin p5 = new Penguin("p5", -100, -100, -5);
		Penguin.setSynergy(p1, p2, 10);
		Penguin.setSynergy(p1, p3, -5);
		Penguin.setSynergy(p2, p5, 10);
		Penguin.setSynergy(p4, p5, -1);
		Lineup l = new Lineup(Set.of(p1, p2), Set.of(p3), Set.of(p4, p5));
		assertLineup(l, 20, 23, 43);

		computeScoreTestsPassed.incrementAndGet();
	}

	@PublicTest
	@DisplayName(value = "Test computeScores - small example")
	@Order(1)
	public void testSCExampleSmall() {
		Penguin jonas = new Penguin("Jonas", 10, 0, 0);
		Penguin anatoly = new Penguin("Anatoly", 10, 10, 0);
		Penguin julian = new Penguin("Juilan", 10, 10, 0);
		Penguin simon = new Penguin("Simon", 0, 0, 10);
		Penguin.setSynergy(jonas, anatoly, 10);
		Penguin.setSynergy(jonas, julian, 5);

		Lineup l = new Lineup(Set.of(jonas, anatoly), Set.of(julian), Set.of(simon));
		assertLineup(l, 40, 25, 65);

		computeScoreTestsPassed.incrementAndGet();
	}

	@PublicTest
	@DisplayName(value = "Test computeScores - large example")
	@Order(1)
	public void testSCExampleLarge() {
		Penguin eve = new Penguin("Eve", 9151, 5, 11);
		Penguin enrico = new Penguin("Enrico", 97, 103, 3499);
		Penguin hanna = new Penguin("Hanna", 6367, 331, 337);
		Penguin sachmi = new Penguin("Sachmi", 103, 5701, 109);
		Penguin jasmine = new Penguin("Jasmine", 233, 5737, 239);
		Penguin jakob = new Penguin("Jakob", 307, 313, 3559);

		Penguin.setSynergy(eve, hanna, 30);
		Penguin.setSynergy(enrico, jakob, 77);
		Penguin.setSynergy(sachmi, jasmine, 121);
		Penguin.setSynergy(jasmine, jakob, 34);
		Penguin.setSynergy(eve, sachmi, 1);

		Lineup l = new Lineup(Set.of(eve, hanna), Set.of(sachmi, jasmine), Set.of(enrico, jakob));
		assertLineup(l, 34014, 491, 34505);

		computeScoreTestsPassed.incrementAndGet();
	}

	// test computeOptimalLineup
	private static DynamicClass<?> lineupClazz = new DynamicClass<>("pgdp.teams.Lineup");
	private static DynamicField<?> attackersField = lineupClazz.field(Set.class, "attackers");
	private static DynamicField<?> defendersField = lineupClazz.field(Set.class, "defenders");
	private static DynamicField<?> supportersField = lineupClazz.field(Set.class, "supporters");

	@SafeVarargs
	@SuppressWarnings("unchecked")
	private void computeScoresForLineupAndAssert(Lineup l, int expectedSkill, int expectedSynergy, int expectedScore,
			Set<Penguin>... expectedSets) {
		Set<Penguin> attackers = (Set<Penguin>) attackersField.getOf(l);
		Set<Penguin> defenders = (Set<Penguin>) defendersField.getOf(l);
		Set<Penguin> supporters = (Set<Penguin>) supportersField.getOf(l);

		assertNotNull(attackers, "Lineup.attackers should never be null!");
		assertNotNull(defenders, "Lineup.defenders should never be null!");
		assertNotNull(supporters, "Lineup.supporters should never be null!");

		// computeScores to test independent from assignments computeScores-implementation
		int teamSkill, teamSynergy, teamScore;
		teamSkill = attackers.stream().mapToInt(p -> p.attack).sum() + defenders.stream().mapToInt(p -> p.defence).sum()
				+ supporters.stream().mapToInt(p -> p.support).sum();
		teamSynergy = 0;
		for (Penguin p : attackers) {
			teamSynergy += 2 * attackers.stream().mapToInt(q -> p.getSynergy(q)).sum();
			teamSynergy += defenders.stream().mapToInt(q -> p.getSynergy(q)).sum();
			teamSynergy += supporters.stream().mapToInt(q -> p.getSynergy(q)).sum();
		}
		for (Penguin p : defenders) {
			teamSynergy += attackers.stream().mapToInt(q -> p.getSynergy(q)).sum();
			teamSynergy += 2 * defenders.stream().mapToInt(q -> p.getSynergy(q)).sum();
			teamSynergy += supporters.stream().mapToInt(q -> p.getSynergy(q)).sum();
		}
		for (Penguin p : supporters) {
			teamSynergy += attackers.stream().mapToInt(q -> p.getSynergy(q)).sum();
			teamSynergy += defenders.stream().mapToInt(q -> p.getSynergy(q)).sum();
			teamSynergy += 2 * supporters.stream().mapToInt(q -> p.getSynergy(q)).sum();
		}
		teamSynergy /= 2;
		teamScore = teamSkill + teamSynergy;

		assertEquals(expectedSkill, teamSkill, "teamSkill differs from optimal solution.");
		assertEquals(expectedSynergy, teamSynergy, "teamSynergy differs from optimal solution.");
		assertEquals(expectedScore, teamScore, "teamScore differs from optimal solution.");

		// for unambiguous solutions test set equality
		if (expectedSets.length == 3) {
			assertEquals(expectedSets[0], attackers,
					"attackers contains wrong penguins for unambiguous optimal solution.");
			assertEquals(expectedSets[1], defenders,
					"defenders contains wrong penguins for unambiguous optimal solution.");
			assertEquals(expectedSets[2], supporters,
					"supporters contains wrong penguins for unambiguous optimal solution.");
		}
	}

	@HiddenTest
	@DisplayName(value = "Test computeOptimalLineup - empty")
	@Order(1)
	public void testCOLEmpty() {
		Lineup l = Lineup.computeOptimalLineup(Set.of(), 0, 0, 0);
		computeScoresForLineupAndAssert(l, 0, 0, 0, Set.of(), Set.of(), Set.of());

		computeOptimalLineupTestsPassed.incrementAndGet();
	}

	@HiddenTest
	@DisplayName(value = "Test computeOptimalLineup - all in single group")
	@Order(1)
	public void testCOLSingleGroupOnlyExact() {
		Penguin p = new Penguin("p", 5, 0, -5);

		Lineup l = Lineup.computeOptimalLineup(Set.of(p), 1, 0, 0);
		computeScoresForLineupAndAssert(l, 5, 0, 5, Set.of(p), Set.of(), Set.of());

		l = Lineup.computeOptimalLineup(Set.of(p), 0, 1, 0);
		computeScoresForLineupAndAssert(l, 0, 0, 0, Set.of(), Set.of(p), Set.of());

		l = Lineup.computeOptimalLineup(Set.of(p), 0, 0, 1);
		computeScoresForLineupAndAssert(l, -5, 0, -5, Set.of(), Set.of(), Set.of(p));

		computeOptimalLineupTestsPassed.incrementAndGet();
	}

	@HiddenTest
	@DisplayName(value = "Test computeOptimalLineup - single group with additional penguins")
	@Order(1)
	public void testCOLSingleGroupOnlyMorePenguins() {
		Penguin a = new Penguin("a", 100, 10, -100);
		Penguin d = new Penguin("d", -100, 100, 10);
		Penguin s = new Penguin("s", 10, -100, 100);
		Penguin p = new Penguin("p", 90, 90, 90);
		Penguin.setSynergy(a, d, -100);
		Penguin.setSynergy(a, s, -100);
		Penguin.setSynergy(d, s, -100);

		Lineup l = Lineup.computeOptimalLineup(Set.of(a, d, s, p), 1, 0, 0);
		computeScoresForLineupAndAssert(l, 100, 0, 100, Set.of(a), Set.of(), Set.of());

		l = Lineup.computeOptimalLineup(Set.of(a, d, s, p), 0, 1, 0);
		computeScoresForLineupAndAssert(l, 100, 0, 100, Set.of(), Set.of(d), Set.of());

		l = Lineup.computeOptimalLineup(Set.of(a, d, s, p), 0, 0, 1);
		computeScoresForLineupAndAssert(l, 100, 0, 100, Set.of(), Set.of(), Set.of(s));

		l = Lineup.computeOptimalLineup(Set.of(a, d, s, p), 2, 0, 0);
		computeScoresForLineupAndAssert(l, 190, 0, 190, Set.of(a, p), Set.of(), Set.of());

		computeOptimalLineupTestsPassed.incrementAndGet();
	}

	@HiddenTest
	@DisplayName(value = "Test computeOptimalLineup - all Penguins used in all groups")
	@Order(1)
	public void testCOLAllGroupsExact() {
		// synergies have to be considered to pass this test
		Penguin a1 = new Penguin("a1", 100, 0, 0);
		Penguin a2 = new Penguin("a2", 100, 0, 0);
		Penguin a3 = new Penguin("a3", 90, 90, 0);
		Penguin d1 = new Penguin("d1", 160, 100, 0);
		Penguin d2 = new Penguin("d2", 0, 100, 0);
		Penguin d3 = new Penguin("d3", 0, 90, 0);
		Penguin s1 = new Penguin("s1", 0, 100, 100);
		Penguin s2 = new Penguin("s2", 0, 0, 100);
		Penguin s3 = new Penguin("s3", 0, 0, -10);
		Penguin.setSynergy(a2, a3, 10);
		Penguin.setSynergy(a1, d1, -100);
		Penguin.setSynergy(s1, d2, 10);
		Penguin.setSynergy(s1, s3, 20);

		Lineup l = Lineup.computeOptimalLineup(Set.of(a1, a2, a3, d1, d2, d3, s1, s2, s3), 3, 3, 3);
		computeScoresForLineupAndAssert(l, 770, -30, 740, Set.of(a1, a2, a3), Set.of(d1, d2, d3), Set.of(s1, s2, s3));

		computeOptimalLineupTestsPassed.incrementAndGet();
	}

	@HiddenTest
	@DisplayName(value = "Test computeOptimalLineup - all groups with additional penguins and one solution")
	@Order(1)
	public void testCOLAllGroupsMorePenguinsSingleSolution() {
		Penguin a1 = new Penguin("a1", 100, 0, 0);
		Penguin a2 = new Penguin("a2", 100, 0, 0);
		Penguin a3 = new Penguin("a3", 90, 90, 0);
		Penguin d1 = new Penguin("d1", 100, 100, 0);
		Penguin d2 = new Penguin("d2", 0, 100, 0);
		Penguin d3 = new Penguin("d3", 0, 90, 0);
		Penguin s1 = new Penguin("s1", 0, 100, 100);
		Penguin s2 = new Penguin("s2", 0, 0, 100);
		Penguin s3 = new Penguin("s3", 0, 0, 90);
		Penguin.setSynergy(a1, a3, 4);
		Penguin.setSynergy(s1, s2, -4);

		Lineup l = Lineup.computeOptimalLineup(Set.of(a1, a2, a3, d1, d2, d3, s1, s2, s3), 2, 2, 2);
		computeScoresForLineupAndAssert(l, 600, -8, 592, Set.of(a1, a2), Set.of(d1, d2), Set.of(s1, s2));

		computeOptimalLineupTestsPassed.incrementAndGet();
	}

	@HiddenTest
	@DisplayName(value = "Test computeOptimalLineup - all gropus with additional penguins and multiple solutions")
	@Order(1)
	public void testCOLAllGroupsMorePenguinsMultipleSolutions() {
		// all negative solution has to be possible to pass this test
		Penguin a1 = new Penguin("a1", -10, -100, -100);
		Penguin a2 = new Penguin("a2", -10, -100, -100);
		Penguin a3 = new Penguin("a3", -10, -100, -100);
		Penguin d1 = new Penguin("d1", -100, -10, -100);
		Penguin d2 = new Penguin("d2", -100, -10, -100);
		Penguin d3 = new Penguin("d3", -100, -10, -100);
		Penguin s1 = new Penguin("s1", -100, -100, -10);
		Penguin s2 = new Penguin("s2", -100, -100, -10);
		Penguin s3 = new Penguin("s3", -100, -100, -10);
		Penguin.setSynergy(a1, a2, -20);
		Penguin.setSynergy(a1, a3, -20);
		Penguin.setSynergy(a2, a3, -20);
		Penguin.setSynergy(s1, s2, 10);
		Penguin.setSynergy(s1, s3, 10);
		Penguin.setSynergy(s2, s3, 10);

		Lineup l = Lineup.computeOptimalLineup(Set.of(a1, a2, a3, d1, d2, d3, s1, s2, s3), 2, 2, 2);
		computeScoresForLineupAndAssert(l, -60, -20, -80);

		computeOptimalLineupTestsPassed.incrementAndGet();
	}

	@PublicTest
	@DisplayName(value = "Test computeOptimalLineup - small example")
	@Order(1)
	public void testCOLExampleSmall() {
		Penguin eric = new Penguin("Eric", 10, 0, 0);
		Penguin nils = new Penguin("Nils", 10, 10, 0);
		Penguin felix = new Penguin("Felix", 10, 10, 0);
		Penguin thomas = new Penguin("Thomas", 0, 0, 10);

		Penguin.setSynergy(eric, nils, 20);
		Penguin.setSynergy(eric, felix, 5);

		Lineup l = Lineup.computeOptimalLineup(Set.of(eric, nils, felix, thomas), 2, 1, 1);
		computeScoresForLineupAndAssert(l, 40, 45, 85, Set.of(nils, eric), Set.of(felix), Set.of(thomas));

		computeOptimalLineupTestsPassed.incrementAndGet();
	}

	@PublicTest
	@DisplayName(value = "Test computeOptimalLineup - large example")
	@Order(1)
	public void testCOLExampleLarge() {
		Penguin jan = new Penguin("Jan", -101, 177013, 777);
		Penguin georg = new Penguin("Georg", 9001, -25984, 66);
		Penguin anton = new Penguin("Anton", 300, 5180, -20000);
		Penguin johannes = new Penguin("Johannes", 0, 314, 2792);
		Penguin konrad = new Penguin("Konrad", 420, 8008, 911);
		Penguin max = new Penguin("Max", 1337, -161, 69);
		Penguin oliver = new Penguin("Oliver", 1, 271, 2319);
		Penguin robin = new Penguin("Robin", 13, 34, 666);
		Penguin laura = new Penguin("Laura", -37, 577, 1459);
		Penguin lukas = new Penguin("Lukas", -79, 549, 1123);

		Penguin.setSynergy(georg, max, 1137);
		Penguin.setSynergy(max, oliver, 33);
		Penguin.setSynergy(max, konrad, 9);
		Penguin.setSynergy(georg, anton, 2187);
		Penguin.setSynergy(oliver, anton, 1138);
		Penguin.setSynergy(jan, lukas, 883);
		Penguin.setSynergy(jan, laura, 787);
		Penguin.setSynergy(johannes, oliver, 420);
		Penguin.setSynergy(johannes, jan, 69);

		Lineup l = Lineup.computeOptimalLineup(
				Set.of(jan, georg, anton, johannes, konrad, max, oliver, robin, laura, lukas), 2, 3, 5);
		computeScoresForLineupAndAssert(l, 208898, 8220, 217118, Set.of(max, georg), Set.of(jan, anton, konrad),
				Set.of(lukas, laura, johannes, oliver, robin));
		computeOptimalLineupTestsPassed.incrementAndGet();
	}

	// grading
	private static int points = 0;

	private static void calculatePoints() {
		int passedTestsCS = computeScoreTestsPassed.intValue();
		int passedTestsCOL = computeOptimalLineupTestsPassed.intValue();

		points = 0;
		// computeScore: 9 Tests
		// 2 points: 1 for 4, 2 for all 9 tests
		if (passedTestsCS >= 4) {
			points += 1;
		}
		if (passedTestsCS == 9) {
			points += 1;
		}

		// computeOptimalLineup: 8 Tests
		// 1 point for 2 tests
		if (passedTestsCOL >= 2) {
			points += 1;
		}
		if (passedTestsCOL >= 4) {
			points += 1;
		}
		if (passedTestsCOL >= 6) {
			points += 1;
		}
		if (passedTestsCOL >= 8) {
			points += 1;
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - 1st point")
	@Order(2)
	public void testGradingPoint1() {
		calculatePoints();
		if (points < 1) {
			fail("Not enough points to pass this test");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - 2nd point")
	@Order(3)
	public void testGradingPoint2() {
		if (points < 2) {
			fail("Not enough points to pass this test");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - 3rd point")
	@Order(3)
	public void testGradingPoint3() {
		if (points < 3) {
			fail("Not enough points to pass this test");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - 4th point")
	@Order(3)
	public void testGradingPoint4() {
		if (points < 4) {
			fail("Not enough points to pass this test");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - 5th point")
	@Order(3)
	public void testGradingPoint5() {
		if (points < 5) {
			fail("Not enough points to pass this test");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - 6th point")
	@Order(3)
	public void testGradingPoint6() {
		if (points < 6) {
			fail("Not enough points to pass this test");
		}
	}
}
