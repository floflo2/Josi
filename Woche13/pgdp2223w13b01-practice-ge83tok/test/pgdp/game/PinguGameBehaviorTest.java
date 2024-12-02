package pgdp.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
// thrown by IOTester when trying to read more console input than provided
import java.util.ServiceConfigurationError;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;

import de.tum.in.test.api.dynamic.DynamicClass;
import de.tum.in.test.api.dynamic.DynamicField;
import de.tum.in.test.api.io.IOTester;
import de.tum.in.test.api.jupiter.PublicTest;
import pgdp.game.mock.PinguGameRollDiceMock;
import pgdp.game.oracle.BoardOracle;
import pgdp.game.oracle.BoardUtilityOracle;
import pgdp.game.oracle.PinguGameOracle;

@W13B01
public class PinguGameBehaviorTest {
	// test utils
	private static DynamicClass<?> pinguGameClazz = new DynamicClass<>("pgdp.game.PinguGame");
	private static DynamicClass<?> boardClazz = new DynamicClass<>("pgdp.game.Board");
	private static DynamicClass<?> figureClazz = new DynamicClass<>("pgdp.game.Board$Figure");

	private static DynamicField<?> boardField = pinguGameClazz.field(Board.class, "board");
	private static DynamicField<?> boardFieldsField = boardClazz.field(int[].class, "boardFields");
	private static DynamicField<?> figuresField = boardClazz.field(Object[][].class, "figures");
	private static DynamicField<?> isHomeField = figureClazz.field(boolean.class, "isHome");
	private static DynamicField<?> reachedGoalField = figureClazz.field(boolean.class, "reachedGoal");
	private static DynamicField<?> positionField = figureClazz.field(int.class, "position");

	private static boolean requiredFieldsPresent = false;
	private static List<String> fieldsFailing;

	private static void setIsHome(Object figure, boolean value) {
		try {
			isHomeField.toField().set(figure, Boolean.valueOf(value));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			fail("An exception occured while setting isHome!\n" + e.getLocalizedMessage());
		}
	}

	private static void setReachedGoal(Object figure, boolean value) {
		try {
			reachedGoalField.toField().set(figure, Boolean.valueOf(value));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			fail("An exception occured while setting reachedGoal!\n" + e.getLocalizedMessage());
		}
	}

	private static void setPosition(Object figure, int value) {
		try {
			positionField.toField().set(figure, Integer.valueOf(value));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			fail("An exception occured while setting position!\n" + e.getLocalizedMessage());
		}
	}

	private static Object getBoard(Object pinguGame) {
		return boardField.getOf(pinguGame);
	}

	private static Object[][] getFigures(Object board) {
		return (Object[][]) figuresField.getOf(board);
	}

	private static int[] getBoardFields(Object board) {
		return (int[]) boardFieldsField.getOf(board);
	}

	private static void setupP2WinNextRound(PinguGameRollDiceMock g, PinguGameOracle o) {
		// PinguGameRollDiceMock
		Object gBoard = getBoard(g);
		Object[][] gFigures = getFigures(gBoard);
		for (int i = 0; i < 3; i++) {
			setIsHome(gFigures[1][i], false);
			if (i != 2) {
				setReachedGoal(gFigures[1][i], true);
			} else {
				setPosition(gFigures[1][i], 6);
				(getBoardFields(gBoard))[6] = BoardUtility.getBoardValue(2, i + 1);
			}
		}

		// PinguGameOracle
		BoardOracle oBoard = o.board;
		BoardOracle.Figure[][] oFigures = oBoard.figures;
		for (int i = 0; i < 3; i++) {
			oFigures[1][i].isHome = false;
			if (i != 2) {
				oFigures[1][i].reachedGoal = true;
			} else {
				oFigures[1][i].position = 6;
				oBoard.boardFields[6] = BoardUtilityOracle.getBoardValue(2, i + 1);
			}
		}
	}

	private static String[] convertIntsToString(int... values) {
		return Arrays.stream(values).mapToObj(i -> String.valueOf(i)).toArray(String[]::new);
	}

	private static final int PRINT_LINES_SINGLE = 7; // default: 7

	private static void testOutputs(List<String> pOut, List<String> oOut) {
		assertEquals(oOut.size(), pOut.size(), "Submission printed wrong number of lines!");
		for (int i = 0; i < oOut.size(); i++) {
			if (!oOut.get(i).equals(pOut.get(i))) {
				fail("Submission output wrong!\nexpected:\n< ...\n"
						+ oOut.stream().skip(i > PRINT_LINES_SINGLE ? i - PRINT_LINES_SINGLE : 0)
								.limit(2 * PRINT_LINES_SINGLE + 1).collect(Collectors.joining("\n"))
						+ "... >\nbut got:\n< ...\n"
						+ pOut.stream().skip(i > PRINT_LINES_SINGLE ? i - PRINT_LINES_SINGLE : 0)
								.limit(2 * PRINT_LINES_SINGLE + 1).collect(Collectors.joining("\n"))
						+ "\n... >");
			}
		}
	}

	private static void test(IOTester iot, String[] inputValues, int[] diceValues, PinguGameRollDiceMock pPreped,
			PinguGameOracle oPreped) {
		iot.reset();
		iot.in().addLinesToInput(inputValues);
		pPreped.setDiceValues(diceValues);
		try {
			pPreped.play();
		} catch (ServiceConfigurationError | IllegalStateException e) {
			fail("Your implementation expceted more user input than provided by the tests! This indicates wrong game behavior.\n"
					+ "The most recent console output was (whithout provided inputs):\n"
					+ iot.out().getLinesAsString().stream().skip(Math.max(0, iot.out().getLinesAsString().size() - 12))
							.collect(Collectors.joining("\n")));
		}

		List<String> pOut = iot.out().getLinesAsString();

		iot.reset();
		iot.in().addLinesToInput(inputValues);
		oPreped.values = diceValues;
		oPreped.play();

		List<String> oOut = iot.out().getLinesAsString();

		testOutputs(pOut, oOut);
	}

	// test prep

	@BeforeAll
	public static void testRequiredFieldsPresent() {
		requiredFieldsPresent = false;
		fieldsFailing = new LinkedList<>();
		if (!boardField.exists()) {
			fieldsFailing.add("Board PenguGame.board not found!");
		}
		if (!boardFieldsField.exists()) {
			fieldsFailing.add("int[] Board.boardFields not found!");
		}
		if (!figuresField.exists()) {
			fieldsFailing.add("Figure[][] figures not found!");
		}
		if (!isHomeField.exists()) {
			fieldsFailing.add("Board$Figure.isHome not found!");
		}
		if (!reachedGoalField.exists()) {
			fieldsFailing.add("Board$Figure.reachedGoal not found!");
		}
		if (!positionField.exists()) {
			fieldsFailing.add("Board$Figure.position not found!");
		}
		requiredFieldsPresent = fieldsFailing.size() == 0;
	}

	@PublicTest
	@DisplayName(value = "Test structure")
	@Order(1)
	public void testStructure() {
		if (!requiredFieldsPresent) {
			fail("At least one of the required fields could not be found:\n"
					+ fieldsFailing.stream().collect(Collectors.joining("\n")));
		}
	}

	private void skipTestIfSomethingWrong() {
		if (!requiredFieldsPresent) {
			fail("Skipping test (structure test failed)!");
		}
		if (!startEndWorking) {
			fail("Skipping test (game start or end not working)!");
		}
	}

	private static boolean startEndWorking = false;

	// tests

	// start and end as one to assure other tests will run through!
	// Test win game (request next game with incorrect inputs)
	// Test game start (incl. wrong inputs)
	@PublicTest
	@DisplayName(value = "Test game start and end")
	@Order(1)
	public void testStartEnd(IOTester iot) {
		if (!requiredFieldsPresent) {
			fail("Skipping test (structure test failed)!");
		}

		// set game state - submission
		PinguGameRollDiceMock g = new PinguGameRollDiceMock(true, 100, 0);
		Object gBoard = getBoard(g);
		Object[][] gFigures = getFigures(gBoard);
		setIsHome(gFigures[0][0], false);
		setIsHome(gFigures[0][1], false);
		setIsHome(gFigures[0][2], false);
		setReachedGoal(gFigures[0][0], true);
		setReachedGoal(gFigures[0][1], true);
		setPosition(gFigures[0][2], 30);
		int[] gBoardField = getBoardFields(gBoard);
		gBoardField[30] = BoardUtility.getBoardValue(1, 3);

		// set game params
		g.setDiceValues(new int[] { 4 });
		iot.in().addLinesToInput("5", "4", "3", "nein", "2", "0");

		try {
			g.play();
		} catch (ServiceConfigurationError | IllegalStateException e) {
			fail("Your implementation expceted more user input than provided by the tests! This indicates wrong game behavior.\n"
					+ "The most recent console output was (whithout provided inputs):\n"
					+ iot.out().getLinesAsString().stream().skip(Math.max(0, iot.out().getLinesAsString().size() - 12))
							.collect(Collectors.joining("\n")));
		}
		List<String> gOut = iot.out().getLinesAsString();

		iot.reset();

		// set game state - oracle
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);
		BoardOracle oBoard = o.board;
		BoardOracle.Figure[][] oFigures = oBoard.figures;
		for (int i = 0; i < 3; i++) {
			oFigures[0][i].isHome = false;
			if (i != 2) {
				oFigures[0][i].reachedGoal = true;
			}
		}
		oFigures[0][2].position = 30;
		oBoard.boardFields[30] = BoardUtilityOracle.getBoardValue(1, 3);

		// set game params
		o.values = new int[] { 4 };
		iot.in().addLinesToInput("5", "4", "3", "nein", "2", "0");
		o.play();
		List<String> oOut = iot.out().getLinesAsString();

		// test submission
		testOutputs(gOut, oOut);

		startEndWorking = true;
	}

	// Test move out of house dice 3x (normal, throw, move throw, move less, move less throw, move more, move more throw)
	@PublicTest
	@DisplayName(value = "Test move out of house")
	@Order(2)
	public void testMoveOutOfHouse(IOTester iot) {
		skipTestIfSomethingWrong();

		// roll twice, leave home, free start
		testMOOHNormal(iot);
		// roll three times, throw on start, free start
		testMOOHThrow(iot);
		// roll three times, leave home, free start and throw
		testMOOHMoveThrow(iot);
		// roll once, leave home, move less to free start
		testMOOHMoveLess(iot);
		// roll once, leave home, move less and throw
		testMOOHMoveLessThrow(iot);
		// roll once, leave home, move more
		testMOOHMoveMore(iot);
		// roll once, leave home, move more and throw
		testMOOHMoveMoreThrow(iot);
		// roll three times, no 6
		testMOOHNoMove(iot);
	}

	private void testMOOHNormal(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);

		String[] inputValues = convertIntsToString(4, 1, 3, 0);
		int[] diceValues = { 1, 6, 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMOOHThrow(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 0);
		pBoardFields[0] = BoardUtility.getBoardValue(3, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 0;
		oBoard.boardFields[0] = BoardUtilityOracle.getBoardValue(3, 2);

		String[] inputValues = convertIntsToString(4, 1, 3, 0);
		int[] diceValues = { 1, 2, 6, 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMOOHMoveThrow(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 2);
		pBoardFields[2] = BoardUtility.getBoardValue(3, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 2;
		oBoard.boardFields[2] = BoardUtilityOracle.getBoardValue(3, 2);

		String[] inputValues = convertIntsToString(4, 1, 3, 0);
		int[] diceValues = { 1, 2, 6, 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMOOHMoveLess(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 2);
		pBoardFields[2] = BoardUtility.getBoardValue(1, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 2;
		oBoard.boardFields[2] = BoardUtilityOracle.getBoardValue(1, 2);

		String[] inputValues = convertIntsToString(4, 1, 3, 0);
		int[] diceValues = { 6, 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMOOHMoveLessThrow(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 2);
		pBoardFields[2] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 1);
		pBoardFields[1] = BoardUtility.getBoardValue(3, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 2;
		oBoard.boardFields[2] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 1;
		oBoard.boardFields[1] = BoardUtilityOracle.getBoardValue(3, 2);

		String[] inputValues = convertIntsToString(4, 1, 3, 0);
		int[] diceValues = { 6, 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMOOHMoveMore(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 1);
		pBoardFields[1] = BoardUtility.getBoardValue(1, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 1;
		oBoard.boardFields[1] = BoardUtilityOracle.getBoardValue(1, 2);

		String[] inputValues = convertIntsToString(4, 1, 3, 0);
		int[] diceValues = { 6, 1, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMOOHMoveMoreThrow(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 1);
		pBoardFields[1] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 2);
		pBoardFields[2] = BoardUtility.getBoardValue(3, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 1;
		oBoard.boardFields[1] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 2;
		oBoard.boardFields[2] = BoardUtilityOracle.getBoardValue(3, 2);

		String[] inputValues = convertIntsToString(4, 1, 3, 0);
		int[] diceValues = { 6, 1, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMOOHNoMove(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);

		String[] inputValues = convertIntsToString(4, 3, 0);
		int[] diceValues = { 1, 1, 1, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	// Test move throw (normal, multiple figures possible, multiple figures on field (only one throw possible))
	@PublicTest
	@DisplayName(value = "Test move and throw")
	@Order(2)
	public void testMoveAndThrow(IOTester iot) {
		skipTestIfSomethingWrong();

		// roll, move and throw (1 fig on field)
		testMATNormal(iot);
		// roll, select and move (2 fig on field, 2 can throw)
		testMATMultipleFigures(iot);
		// roll, select wrong, select right and move (2 fig on field, 2 can throw)
		testMATSingleFigurePossible(iot);
	}

	private void testMATNormal(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 2);
		pBoardFields[2] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 4);
		pBoardFields[4] = BoardUtility.getBoardValue(3, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 2;
		oBoard.boardFields[2] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 4;
		oBoard.boardFields[4] = BoardUtilityOracle.getBoardValue(3, 2);

		String[] inputValues = convertIntsToString(4, 2, 3, 0);
		int[] diceValues = { 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMATMultipleFigures(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 2);
		pBoardFields[2] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 4);
		pBoardFields[4] = BoardUtility.getBoardValue(3, 2);
		setIsHome(pFigures[0][2], false);
		setPosition(pFigures[0][2], 3);
		pBoardFields[3] = BoardUtility.getBoardValue(1, 3);
		setIsHome(pFigures[2][2], false);
		setPosition(pFigures[2][2], 5);
		pBoardFields[5] = BoardUtility.getBoardValue(3, 3);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 2;
		oBoard.boardFields[2] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 4;
		oBoard.boardFields[4] = BoardUtilityOracle.getBoardValue(3, 2);
		oBoard.figures[0][2].isHome = false;
		oBoard.figures[0][2].position = 3;
		oBoard.boardFields[3] = BoardUtilityOracle.getBoardValue(1, 3);
		oBoard.figures[2][2].isHome = false;
		oBoard.figures[2][2].position = 5;
		oBoard.boardFields[5] = BoardUtilityOracle.getBoardValue(3, 3);

		String[] inputValues = convertIntsToString(4, 2, 3, 0);
		int[] diceValues = { 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMATSingleFigurePossible(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 2);
		pBoardFields[2] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 4);
		pBoardFields[4] = BoardUtility.getBoardValue(3, 2);
		setIsHome(pFigures[0][2], false);
		setPosition(pFigures[0][2], 3);
		pBoardFields[3] = BoardUtility.getBoardValue(1, 3);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 2;
		oBoard.boardFields[2] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 4;
		oBoard.boardFields[4] = BoardUtilityOracle.getBoardValue(3, 2);
		oBoard.figures[0][2].isHome = false;
		oBoard.figures[0][2].position = 3;
		oBoard.boardFields[3] = BoardUtilityOracle.getBoardValue(1, 3);

		String[] inputValues = convertIntsToString(4, 3, 2, 3, 0);
		int[] diceValues = { 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	// Test move normal (normal, multiple possible)
	@PublicTest
	@DisplayName(value = "Test move")
	@Order(2)
	public void testMove(IOTester iot) {
		skipTestIfSomethingWrong();

		// roll, invalid choice, move
		testMNormal(iot);
		// roll, invalid choice, move (2 figs on field)
		testMMultiplePossible(iot);
	}

	private void testMNormal(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 1);
		pBoardFields[1] = BoardUtility.getBoardValue(1, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 1;
		oBoard.boardFields[1] = BoardUtilityOracle.getBoardValue(1, 2);

		String[] inputValues = convertIntsToString(4, 1, 2, 3, 0);
		int[] diceValues = { 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMMultiplePossible(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 1);
		pBoardFields[1] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[0][2], false);
		setPosition(pFigures[0][2], 2);
		pBoardFields[2] = BoardUtility.getBoardValue(1, 3);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 1;
		oBoard.boardFields[1] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[0][2].isHome = false;
		oBoard.figures[0][2].position = 2;
		oBoard.boardFields[2] = BoardUtilityOracle.getBoardValue(1, 3);

		String[] inputValues = convertIntsToString(4, 1, 3, 3, 0);
		int[] diceValues = { 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	// Test move goal (normal, throw, not exact)
	@PublicTest
	@DisplayName(value = "Test move to goal")
	@Order(2)
	public void testMoveToGoal(IOTester iot) {
		skipTestIfSomethingWrong();

		//normal(exact, not exact) covered by other tests "on the fly"
		// move to goal (exact) and throw
		testMTGThrowExact(iot);
		// move to goal (overflow) and throw
		testMTGThrowOverflow(iot);
	}

	private void testMTGThrowExact(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 30);
		pBoardFields[30] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 31);
		pBoardFields[31] = BoardUtility.getBoardValue(3, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 30;
		oBoard.boardFields[30] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 31;
		oBoard.boardFields[31] = BoardUtilityOracle.getBoardValue(3, 2);

		String[] inputValues = convertIntsToString(4, 2, 3, 0);
		int[] diceValues = { 1, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testMTGThrowOverflow(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 30);
		pBoardFields[30] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 31);
		pBoardFields[31] = BoardUtility.getBoardValue(3, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 30;
		oBoard.boardFields[30] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 31;
		oBoard.boardFields[31] = BoardUtilityOracle.getBoardValue(3, 2);

		String[] inputValues = convertIntsToString(4, 2, 3, 0);
		int[] diceValues = { 5, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	// Test 6 (move goal no figure on field, throw, normal move (exit house, end turn))
	@PublicTest
	@DisplayName(value = "Test 6 special cases")
	@Order(2)
	public void testRollSixSpecialCases(IOTester iot) {
		skipTestIfSomethingWrong();

		// move to goal with 6, no 6 (no fig on field) -> no more tries
		testRSSCGoalNoFigOnField(iot);
		// throw with 6, move with 6, move
		testRSSCThrow(iot);
		// no figs, roll 6, leave home, roll 6 -> end turn
		testRSSCMoveOutOfHouse(iot);
	}

	private void testRSSCGoalNoFigOnField(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 30);
		pBoardFields[30] = BoardUtility.getBoardValue(1, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 30;
		oBoard.boardFields[30] = BoardUtilityOracle.getBoardValue(1, 2);

		String[] inputValues = convertIntsToString(4, 2, 3, 0);
		int[] diceValues = { 6, 2, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testRSSCThrow(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[0][1], false);
		setPosition(pFigures[0][1], 3);
		pBoardFields[3] = BoardUtility.getBoardValue(1, 2);
		setIsHome(pFigures[2][1], false);
		setPosition(pFigures[2][1], 9);
		pBoardFields[9] = BoardUtility.getBoardValue(3, 2);

		BoardOracle oBoard = o.board;
		oBoard.figures[0][1].isHome = false;
		oBoard.figures[0][1].position = 3;
		oBoard.boardFields[3] = BoardUtilityOracle.getBoardValue(1, 2);
		oBoard.figures[2][1].isHome = false;
		oBoard.figures[2][1].position = 9;
		oBoard.boardFields[9] = BoardUtilityOracle.getBoardValue(3, 2);

		String[] inputValues = convertIntsToString(4, 2, 2, 2, 3, 0);
		int[] diceValues = { 6, 6, 1, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	private void testRSSCMoveOutOfHouse(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[1][2], false);
		setPosition(pFigures[1][2], 5);
		pBoardFields[5] = BoardUtility.getBoardValue(2, 3);
		pBoardFields[6] = Board.EMPTY;

		BoardOracle oBoard = o.board;
		oBoard.figures[1][2].isHome = false;
		oBoard.figures[1][2].position = 5;
		oBoard.boardFields[5] = BoardUtilityOracle.getBoardValue(2, 3);
		oBoard.boardFields[6] = BoardOracle.EMPTY;

		String[] inputValues = convertIntsToString(4, 2, 2, 3, 0);
		int[] diceValues = { 6, 6, 2 };

		test(iot, inputValues, diceValues, p, o);
	}

	// Test AI (full, with humans)
	@PublicTest
	@DisplayName(value = "Test ai")
	@Order(2)
	public void testAI(IOTester iot) {
		skipTestIfSomethingWrong();

		// full AI game covered by full game test (AI only)
		// test 2 humans, 2 AI, 1 round
		testAIHumanCombined(iot);
	}

	private void testAIHumanCombined(IOTester iot) {
		PinguGameRollDiceMock p = new PinguGameRollDiceMock(true, 100, 0);
		PinguGameOracle o = new PinguGameOracle(true, 100, 0);

		setupP2WinNextRound(p, o);
		Object pBoard = getBoard(p);
		Object[][] pFigures = getFigures(pBoard);
		int[] pBoardFields = getBoardFields(pBoard);
		setIsHome(pFigures[1][2], false);
		setPosition(pFigures[1][2], 5);
		pBoardFields[5] = BoardUtility.getBoardValue(2, 3);
		pBoardFields[6] = Board.EMPTY;

		BoardOracle oBoard = o.board;
		oBoard.figures[1][2].isHome = false;
		oBoard.figures[1][2].position = 5;
		oBoard.boardFields[5] = BoardUtilityOracle.getBoardValue(2, 3);
		oBoard.boardFields[6] = BoardOracle.EMPTY;

		String[] inputValues = convertIntsToString(2, 3, 3, 3, 0);
		int[] diceValues = { 1, 1, 1, 1, 6, 5, 1, 2, 6, 1, 6, 1, 1 };

		test(iot, inputValues, diceValues, p, o);
	}

	// Test full game (AI, humans, with prev game)
	@PublicTest
	@DisplayName(value = "Test full games")
	@Order(2)
	public void testFullGames(IOTester iot) {
		skipTestIfSomethingWrong();

		// one full game round with only human players (i.e. Penguins!!!)
		testFGHumansOnly(iot);
		// two subsequent rounds with only ai players
		testFGAIOnly(iot);
	}

	private void testFGHumansOnly(IOTester iot) {
		PinguGame p = new PinguGame(true, 42, 0);
		PinguGameOracle o = new PinguGameOracle(true, 42, 0);

		String[] inputValues = convertIntsToString(4, 3, 2, 3, 2, 2, 3, 3, 2, 2, 3, 2, 1, 1, 3, 2, 1, 3, 3, 3, 2, 2, 3,
				3, 2, 2, 3, 3, 2, 2, 3, 3, 2, 2, 1, 2, 2, 3, 1, 2, 2, 3, 3, 1, 1, 2, 3, 2, 2, 2, 3, 2, 2, 2, 1, 1, 2, 2,
				1, 2, 2, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 3, 3, 2, 2, 2, 3, 3, 3, 3,
				2, 2, 3, 3, 2, 3, 3, 2, 2, 3, 3, 2, 2, 3, 2, 2, 0);

		iot.reset();
		iot.in().addLinesToInput(inputValues);
		try {
			p.play();
		} catch (ServiceConfigurationError | IllegalStateException e) {
			fail("Your implementation expceted more user input than provided by the tests! This indicates wrong game behavior.\n"
					+ "The most recent console output was (whithout provided inputs):\n"
					+ iot.out().getLinesAsString().stream().skip(Math.max(0, iot.out().getLinesAsString().size() - 12))
							.collect(Collectors.joining("\n")));
		}

		List<String> pOut = iot.out().getLinesAsString();

		iot.reset();
		iot.in().addLinesToInput(inputValues);
		o.play();

		List<String> oOut = iot.out().getLinesAsString();

		testOutputs(pOut, oOut);
	}

	private void testFGAIOnly(IOTester iot) {
		PinguGame p = new PinguGame(true, 21, 0);
		PinguGameOracle o = new PinguGameOracle(true, 21, 0);

		String[] inputValues = convertIntsToString(0, 1, 0, 0);

		iot.reset();
		iot.in().addLinesToInput(inputValues);
		try {
			p.play();
		} catch (ServiceConfigurationError | IllegalStateException e) {
			fail("Your implementation expceted more user input than provided by the tests! This indicates wrong game behavior.\n"
					+ "The most recent console output was (whithout provided inputs):\n"
					+ iot.out().getLinesAsString().stream().skip(Math.max(0, iot.out().getLinesAsString().size() - 12))
							.collect(Collectors.joining("\n")));
		}

		List<String> pOut = iot.out().getLinesAsString();

		iot.reset();
		iot.in().addLinesToInput(inputValues);
		o.play();

		List<String> oOut = iot.out().getLinesAsString();

		testOutputs(pOut, oOut);
	}
}
