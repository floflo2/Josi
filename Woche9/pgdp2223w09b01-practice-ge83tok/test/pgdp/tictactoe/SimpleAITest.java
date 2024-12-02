package pgdp.tictactoe;

import static org.junit.jupiter.api.Assertions.fail;

import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import pgdp.tictactoe.ai.SimpleAI;

@W09B01
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SimpleAITest {

    private static boolean playsValid;
    private static boolean lostPointDueToValid;

    @PublicTest
    @StrictTimeout(50)
    @Order(1)
    public void testLegalMoves() {
        SimpleAI studentAI = new SimpleAI();
        SimpleAIForTesting testAI = new SimpleAIForTesting();

        for (int i = 0; i < 1000; i++) {
            GameForTesting gameStudentFirst = new GameForTesting(studentAI, testAI);
            GameForTesting gameTestFirst = new GameForTesting(testAI, studentAI);

            gameStudentFirst.playGame();
            gameTestFirst.playGame();
        }
        playsValid = true;
    }

    @PublicTest
    @Order(2)
    public void testWinningMove() {
        SimpleAI studentAI = new SimpleAI();
        for (int i = 0; i < 10; i++) {
            Field[][] board = new Field[][] { { new Field(0, true), new Field(3, true), null },
                    { null, null, new Field(8, false) }, { new Field(0, false), null, null } };

            Move move = studentAI.makeMove(board, true,
                    new boolean[] { true, false, false, true, false, false, false, false, false },
                    new boolean[] { true, false, false, false, false, false, false, false, true });

            if (move.x() != 0 || move.y() != 2 || move.value() < 1 || move.value() == 3 || move.value() > 8) {
                fail("Deine KI spielt nicht einen Zug, der direkt gewinnen würde, bzw. gibt einen invaliden Zug zurück");
            }
        }
    }

    @HiddenTest
    @Order(2)
    public void testWinningMoveOnOpponent() {
        SimpleAI studentAI = new SimpleAI();
        for (int i = 0; i < 10; i++) {
            Field[][] board = new Field[][] { { new Field(0, true), null, null },
                    { null, new Field(3, true), new Field(8, false) }, { null, null, new Field(5, false) } };

            Move move = studentAI.makeMove(board, true,
                    new boolean[] { true, false, false, true, false, false, false, false, false },
                    new boolean[] { false, false, false, false, false, true, false, false, true });

            if (move.x() != 2 || move.y() != 2 || move.value() < 6 || move.value() > 8) {
                fail("Deine KI spielt nicht einen Zug, der direkt gewinnen würde, bzw. gibt einen invaliden Zug zurück");
            }
        }
        checkPointLossDueToValid();
    }

    @HiddenTest
    @Order(2)
    public void testWinningAndBlockingMove() {
        SimpleAI studentAI = new SimpleAI();
        for (int i = 0; i < 10; i++) {
            Field[][] board = new Field[][] { { null, null, new Field(2, false) },
                    { new Field(1, true), null, null },
                    { new Field(7, true), null, new Field(5, false) } };

            Move move = studentAI.makeMove(board, false,
                    new boolean[] { true, true, false, false, true, false, false, true, false },
                    new boolean[] { true, false, true, false, false, true, false, false, false });

            if (move.x() != 1 || move.y() != 2 || move.value() == 0 || move.value() == 2 || move.value() == 5) {
                fail("Deine KI spielt nicht einen Zug, der direkt gewinnen würde, bzw. gibt einen invaliden Zug zurück");
            }
        }
        checkPointLossDueToValid();
    }

    @PublicTest
    @Order(2)
    public void testBlockingMove() {
        SimpleAI studentAI = new SimpleAI();
        for (int i = 0; i < 10; i++) {
            Field[][] board = new Field[][] { { null, new Field(7, true), new Field(8, true) },
                    { null, null, new Field(8, false) }, { null, null, new Field(2, false) } };

            Move move = studentAI.makeMove(board, false,
                    new boolean[] { false, true, false, false, false, false, false, true, true },
                    new boolean[] { false, false, true, false, false, false, false, false, true });

            if (move.x() != 0 || move.y() != 0 || move.value() < 6 || move.value() > 8) {
                fail("Deine KI spielt nicht einen Zug, der das Gewinnen vom Gegner verhindern würde, bzw. gibt einen invaliden Zug zurück");
            }
        }
    }

    @HiddenTest
    @Order(2)
    public void testBlockingMoveOnOpponent() {
        SimpleAI studentAI = new SimpleAI();
        for (int i = 0; i < 10; i++) {
            Field[][] board = new Field[][] { { null, null, new Field(1, false) },
                    { new Field(8, true), null, new Field(7, false) }, { null, new Field(1, true), null } };

            Move move = studentAI.makeMove(board, true,
                    new boolean[] { false, true, false, false, false, false, false, false, true },
                    new boolean[] { false, true, false, false, false, false, false, true, false });

            if (move.x() != 0 || move.y() != 2 || move.value() < 2 || move.value() > 7) {
                fail("Deine KI spielt nicht einen Zug, der das Gewinnen vom Gegner verhindern würde, bzw. gibt einen invaliden Zug zurück");
            }
        }
        checkPointLossDueToValid();
    }

    @HiddenTest
    @Order(2)
    public void testBlockingMoveOnOpponentWhenOpponentCanPlayOnClaimedField() {
        SimpleAI studentAI = new SimpleAI();
        for (int i = 0; i < 10; i++) {
            Field[][] board = new Field[][] { { new Field(5, false), null, null },
                    { null, new Field(8, true), new Field(4, false) }, { null, null, new Field(2, true) } };

            Move move = studentAI.makeMove(board, false,
                    new boolean[] { false, true, true, false, false, false, false, false, true },
                    new boolean[] { false, false, false, false, true, true, false, false, false });

            if (move.x() != 2 || move.y() != 2
                    || (move.value() != 3 && move.value() != 6 && move.value() != 7 && move.value() != 8)) {
                fail("Deine KI spielt nicht einen Zug, der das Gewinnen vom Gegner verhindern würde, bzw. gibt einen invaliden Zug zurück");
            }
        }
        checkPointLossDueToValid();
    }

    private static void checkPointLossDueToValid() {
        if (!lostPointDueToValid && !playsValid) {
            lostPointDueToValid = true;
            fail("Deine KI gibt ungültige Züge zurück, wodurch du einen Punkt verlierst.");
        }
    }
}
