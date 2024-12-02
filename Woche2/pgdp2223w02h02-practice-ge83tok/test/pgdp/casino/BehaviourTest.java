package pgdp.casino;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;

import de.tum.in.test.api.TestUtils;
import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicConstructor;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;

@TestClassAnnotation
public class BehaviourTest {

	private final String welcomeMSG = "Welcome to Pengu-BlackJack!";
	private final String startMSG = "(1) Start a game or (2) exit";
	private final String invalidMSG = "What?!";
	private final Function<Integer, String> currentBalanceMSG = (i) -> "Your current balance: " + i;
	private final String askAmountMSG = "How much do you want to bet?";
	private final String playerHeadlineMSG = "Player cards:";
	private final BiFunction<Integer, Integer, String> cardMSG = (i, j) -> i + " : " + j;
	private final Function<Integer, String> currentStandingMSG = (i) -> "Current standing: " + i;
	private final String drawNextMSG = "(1) draw another card or (2) stay";
	private final Function<Integer, String> lostMSG = (i) -> "You lost " + i + " tokens.";
	private final Function<Integer, String> blackJackMSG = (i) -> "Blackjack! You won " + i + " tokens.";
	private final String dealerHeadlineMSG = "Dealer cards:";
	private final Function<Integer, String> dealerFinalMSG = (i) -> "Dealer: " + i;
	private final Function<Integer, String> playerWinMSG = (i) -> "You won " + i + " tokens.";
	private final Function<Integer, String> playerLossMSG = (i) -> "Dealer wins. You lost " + i + " tokens.";
	private final String brokeMSG = "Sorry, you are broke. Better Luck next time.";
	private final Function<Integer, String> finalBalanceMSG = (i) -> "Your final balance: " + i;
	private final String lossMSG = "That's very very sad :(";
	private final String winMSG = "Wohooo! Ez profit! :D";
	private final String thanksMSG = "Thank you for playing. See you next time.";

	@PublicTest
	@DisplayName(value = "Player closes game immediately")
	public void testNoGamePlayedInstantExit(IOTester iot) {
		runTest(iot, 1337, new String[] { "2" },
				new String[] { welcomeMSG, startMSG, finalBalanceMSG.apply(1000), lossMSG, thanksMSG });
	}

	@PublicTest
	@DisplayName(value = "Invalid Inputs at start of game")
	public void testNoGamePlayedInvalidInput(IOTester iot) {
		runTest(iot, 1337, new String[] { "0", "-1", "3", "2" },
				new String[] { welcomeMSG, startMSG, invalidMSG, startMSG, invalidMSG, startMSG, invalidMSG, startMSG,
						finalBalanceMSG.apply(1000), lossMSG, thanksMSG });
	}

	@HiddenTest
	@DisplayName(value = "Ask user for amount")
	public void testAskAmount(IOTester iot) {
		runTest(iot, 1337, 2, new String[] { "1", "69" },
				new String[] { currentBalanceMSG.apply(1000), askAmountMSG, playerHeadlineMSG });
	}

	@HiddenTest
	@DisplayName(value = "Ask user for amount - invalid input")
	public void testAskAmountInvalidInput(IOTester iot) {
		runTest(iot, 1337, 2, new String[] { "1", "-1", "1001", "69" }, new String[] { currentBalanceMSG.apply(1000),
				askAmountMSG, askAmountMSG, askAmountMSG, playerHeadlineMSG });
	}

	@HiddenTest
	@DisplayName(value = "Draw cards until limit reached")
	public void testDrawCardsAutoExit(IOTester iot) {
		runTest(iot, 1337, 4, new String[] { "1", "69", "1", "1" },
				new String[] { playerHeadlineMSG, cardMSG.apply(1, 9), cardMSG.apply(2, 2),
						currentStandingMSG.apply(11), drawNextMSG, cardMSG.apply(3, 5), currentStandingMSG.apply(16),
						drawNextMSG, cardMSG.apply(4, 9), currentStandingMSG.apply(25), lostMSG.apply(69) });
	}

	@HiddenTest
	@DisplayName(value = "Draw cards until user stays")
	public void testDrawCardsStay(IOTester iot) {
		runTest(iot, 1337, 4, new String[] { "1", "69", "1", "2" },
				new String[] { playerHeadlineMSG, cardMSG.apply(1, 9), cardMSG.apply(2, 2),
						currentStandingMSG.apply(11), drawNextMSG, cardMSG.apply(3, 5), currentStandingMSG.apply(16),
						drawNextMSG, dealerHeadlineMSG });
	}

	@HiddenTest
	@DisplayName(value = "Draw cards invalid input")
	public void testDrawCardsInvalidInput(IOTester iot) {
		runTest(iot, 1337, 8, new String[] { "1", "69", "-1", "0", "3" }, new String[] { drawNextMSG, invalidMSG,
				drawNextMSG, invalidMSG, drawNextMSG, invalidMSG, drawNextMSG });
	}

	@HiddenTest
	@DisplayName(value = "Message after limit surpassed")
	public void testInstantLoss(IOTester iot) {
		runTest(iot, 1337, 14, new String[] { "1", "69", "1", "1" }, new String[] { lostMSG.apply(69), startMSG });
	}

	@HiddenTest
	@DisplayName(value = "Message after Black Jack")
	public void testBlackJack(IOTester iot) {
		runTest(iot, 8, 8, new String[] { "1", "69" }, new String[] { blackJackMSG.apply(138), startMSG });
	}

	@PublicTest
	@DisplayName(value = "Dealer plays")
	public void testDealerCards(IOTester iot) {
		runTest(iot, 1337, 12, new String[] { "1", "69", "1", "2" },
				new String[] { dealerHeadlineMSG, cardMSG.apply(1, 9), cardMSG.apply(2, 3), cardMSG.apply(3, 1),
						cardMSG.apply(4, 5), dealerFinalMSG.apply(18) });
	}

	@HiddenTest
	@DisplayName(value = "Message after win vs dealer")
	public void testWinAfterDealer(IOTester iot) {
		runTest(iot, 420, 21, new String[] { "1", "69", "1", "1", "2" },
				new String[] { playerWinMSG.apply(69), startMSG });
	}

	@HiddenTest
	@DisplayName(value = "Message after loss vs dealer")
	public void testLossAfterDealer(IOTester iot) {
		runTest(iot, 1337, 18, new String[] { "1", "69", "1", "2" },
				new String[] { playerLossMSG.apply(69), startMSG });
	}

	@HiddenTest
	@DisplayName(value = "Bankruptcy message")
	public void testBankruptcy(IOTester iot) {
		runTest(iot, 1337, 15, new String[] { "1", "1000", "1", "1" },
				new String[] { brokeMSG, finalBalanceMSG.apply(0) });
	}

	@HiddenTest
	@DisplayName(value = "Final loss message")
	public void testFinalMSGLoss(IOTester iot) {
		runTest(iot, 1337, 16, new String[] { "1", "69", "1", "1", "2" },
				new String[] { finalBalanceMSG.apply(931), lossMSG, thanksMSG });
	}

	@HiddenTest
	@DisplayName(value = "Final win message")
	public void testFinalMSGWin(IOTester iot) {
		runTest(iot, 420, 23, new String[] { "1", "69", "1", "1", "2", "2" },
				new String[] { finalBalanceMSG.apply(1069), winMSG, thanksMSG });
	}

	@HiddenTest
	@DisplayName(value = "Series of games played")
	public void testGameSeries(IOTester iot) {
		// round 1 : { "1", "69", "1", "1", "2" } player wins
		// round 2 : { "1", "420", "1" } black jack
		// round 3 : { "1", "1337", "1", "1" } player loss
		// { "2" } end game
		runTest(iot, 420, new String[] { "1", "69", "1", "1", "2", "1", "420", "1", "1", "1337", "1", "1", "2" },
				new String[] { welcomeMSG, startMSG, currentBalanceMSG.apply(1000), askAmountMSG, playerHeadlineMSG,
						cardMSG.apply(1, 1), cardMSG.apply(2, 8), currentStandingMSG.apply(9), drawNextMSG,
						cardMSG.apply(3, 3), currentStandingMSG.apply(12), drawNextMSG, cardMSG.apply(4, 8),
						currentStandingMSG.apply(20), drawNextMSG, dealerHeadlineMSG, cardMSG.apply(1, 4),
						cardMSG.apply(2, 2), cardMSG.apply(3, 8), cardMSG.apply(4, 8), dealerFinalMSG.apply(22),
						playerWinMSG.apply(69), startMSG, currentBalanceMSG.apply(1069), askAmountMSG,
						playerHeadlineMSG, cardMSG.apply(1, 9), cardMSG.apply(2, 5), currentStandingMSG.apply(14),
						drawNextMSG, cardMSG.apply(3, 7), currentStandingMSG.apply(21), blackJackMSG.apply(840),
						startMSG, currentBalanceMSG.apply(1909), askAmountMSG, playerHeadlineMSG, cardMSG.apply(1, 3),
						cardMSG.apply(2, 3), currentStandingMSG.apply(6), drawNextMSG, cardMSG.apply(3, 7),
						currentStandingMSG.apply(13), drawNextMSG, cardMSG.apply(4, 10), currentStandingMSG.apply(23),
						lostMSG.apply(1337), startMSG, finalBalanceMSG.apply(572), lossMSG, thanksMSG });
	}

	@HiddenTest
	@DisplayName(value = "Grading - Outer loop")
	public void testGradingOuterLoop(IOTester iot) {
		try {
			testNoGamePlayedInstantExit(iot);
			testNoGamePlayedInvalidInput(iot);
			testGameSeries(iot);
		} catch (AssertionError e) {
			fail("At least one of the required test cases to get this point failed.");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - Balance and Bet")
	public void testGradingBalanceAndBet(IOTester iot) {
		try {
			testAskAmount(iot);
			testAskAmountInvalidInput(iot);
		} catch (AssertionError e) {
			fail("At least one of the required test cases to get this point failed.");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - Player")
	public void testGradingPlayer(IOTester iot) {
		try {
			testDrawCardsAutoExit(iot);
			testDrawCardsStay(iot);
			testDrawCardsInvalidInput(iot);
		} catch (AssertionError e) {
			fail("At least one of the required test cases to get this point failed.");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - Decision")
	public void testGradingDecision(IOTester iot) {
		try {
			testInstantLoss(iot);
			testBlackJack(iot);
		} catch (AssertionError e) {
			fail("At least one of the required test cases to get this point failed.");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - Dealer")
	public void testGradingDealer(IOTester iot) {
		try {
			testDealerCards(iot);
			testWinAfterDealer(iot);
			testLossAfterDealer(iot);
		} catch (AssertionError e) {
			fail("At least one of the required test cases to get this point failed.");
		}
	}

	@HiddenTest
	@DisplayName(value = "Grading - Final Balance")
	public void testGradingFinalBalance(IOTester iot) {
		try {
			testBankruptcy(iot);
			testFinalMSGLoss(iot);
			testFinalMSGWin(iot);
		} catch (AssertionError e) {
			fail("At least one of the required test cases to get this point failed.");
		}
	}

	private void runTest(IOTester iot, int seed, String[] in, String[] out) {
		setCardDeck(seed);
		iot.reset();
		iot.in().addLinesToInput(in);
		Casino.penguBlackJack();
		iot.out().assertLinesMatch("Test Failed", out);
	}

	private void runTest(IOTester iot, int seed, int from, String[] in, String[] out) {
		setCardDeck(seed);
		iot.reset();
		iot.in().addLinesToInput(in);
		try {
			Casino.penguBlackJack();
		} catch (IllegalStateException e) {
			// this is expected to happen
		} catch (Exception e) {
			throw e;
		}
		String[] lazyOut = new String[Math.max(iot.out().getLines().size(), from + out.length)];
		IntStream.range(0, lazyOut.length).parallel().forEach(i -> {
			if (i < from || from + out.length - 1 < i)
				lazyOut[i] = "||.*||";
			else
				lazyOut[i] = out[i - from];
		});
		iot.out().assertLinesMatch("Test Failed", lazyOut);
	}

	/**
	 * Method to set the seed of the Random instance used in the CardDeck
	 * 
	 * @param seed
	 */
	private static void setCardDeck(int seed) {
		try {
			DynamicClass<?> clazz = new DynamicClass<>("pgdp.casino.CardDeck");
			DynamicConstructor<?> constr = clazz.constructor(int.class);
			DynamicField<?> field = clazz.field(CardDeck.class, "deck");
			var deck = constr.newInstance(seed);
			field.toField().set(deck, deck);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			TestUtils.privilegedFail("The test itself failed! Please contanct a tutor.");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			TestUtils.privilegedFail("The test itself failed! Please contanct a tutor.");
		}
	}

}
