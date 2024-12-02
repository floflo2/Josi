package pgdp.tictactoe;

import de.tum.in.test.api.jupiter.HiddenTest;
import de.tum.in.test.api.jupiter.PublicTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@W09B01
public class GameTest {

    @PublicTest
    public void firstWins() {
        firstWinsHidden();
    }

    public void firstWinsHidden() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 1), new Move(1, 0, 2), new Move(2, 0, 3) };
        Move[] secondMoves = new Move[] { new Move(0, 1, 1), new Move(1, 1, 2) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, first);
    }

    @PublicTest
    public void draw() {
        drawHidden();
    }

    public void drawHidden() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 0), new Move(0, 0, 2), new Move(0, 0, 4), new Move(0, 0, 6),
                new Move(0, 0, 8), new Move(1, 1, 1), new Move(1, 1, 3), new Move(1, 1, 5), new Move(1, 1, 7) };
        Move[] secondMoves = new Move[] { new Move(0, 0, 1), new Move(0, 0, 3), new Move(0, 0, 5), new Move(0, 0, 7),
                new Move(1, 1, 0), new Move(1, 1, 2), new Move(1, 1, 4), new Move(1, 1, 6), new Move(1, 1, 8) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, null);
    }

    public void secondWins() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 1), new Move(1, 0, 2), new Move(2, 0, 3) };
        Move[] secondMoves = new Move[] { new Move(0, 0, 2), new Move(1, 1, 3), new Move(2, 2, 1) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, second);
    }

    @HiddenTest
    public void winTest() {
        firstWinsHidden();
        drawHidden();
        secondWins();
    }

    private List<String> getIllegalErrorMessages() {
        List<String> errorMessages = new ArrayList<>();
        try {
            illegalMoveOutOfBounds();
        } catch(Throwable t) {
            errorMessages.add(t.getMessage());
        }
        try {
            illegalMoveSameValueAgain();
        } catch(Throwable t) {
            errorMessages.add(t.getMessage());
        }
        try {
            illegalMoveOnOwnPiece();
        } catch(Throwable t) {
            errorMessages.add(t.getMessage());
        }
        try {
            illegalMoveLowerValue();
        } catch(Throwable t) {
            errorMessages.add(t.getMessage());
        }
        try {
            illegalIllegalValue();
        } catch(Throwable t) {
            errorMessages.add(t.getMessage());
        }
        return errorMessages;
    }

    @HiddenTest
    public void testIllegalMoveOnePoint() {
        List<String> errorMessages = getIllegalErrorMessages();
        assertTrue(errorMessages.size() <= 3, "Du fängst nicht genug illegale Züge ab:\n" +
                String.join("\n", errorMessages));
    }

    @HiddenTest
    public void testIllegalMoveTwoPoints() {
        List<String> errorMessages = getIllegalErrorMessages();
        assertTrue(errorMessages.isEmpty(), "Du fängst nicht alle illegale Züge ab:\n" +
                String.join("\n", errorMessages));
    }

    public void illegalMoveOutOfBounds() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 0), new Move(0, 3, 2) };
        Move[] secondMoves = new Move[] { new Move(1, 1, 1) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, second);

        firstMoves = new Move[] { new Move(0, 0, 0), new Move(0, 1, 2) };
        secondMoves = new Move[] { new Move(1, 1, 1), new Move(-1, 2, 3) };
        first = new MockAI(firstMoves, secondMoves, true);
        second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, first);
    }

    public void illegalMoveSameValueAgain() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 0), new Move(0, 1, 2), new Move(2, 2, 2) };
        Move[] secondMoves = new Move[] { new Move(1, 1, 1), new Move(0, 2, 3) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, second);
    }

    public void illegalMoveOnOwnPiece() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 0), new Move(0, 1, 2) };
        Move[] secondMoves = new Move[] { new Move(1, 1, 1), new Move(1, 1, 3) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, first);
    }

    public void illegalMoveLowerValue() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 5), new Move(0, 1, 2) };
        Move[] secondMoves = new Move[] { new Move(1, 1, 1), new Move(0, 0, 1) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, first);
    }

    public void illegalIllegalValue() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 5), new Move(0, 1, 9) };
        Move[] secondMoves = new Move[] { new Move(1, 1, 1) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, second);
    }

    @HiddenTest
    public void winThroughBlock() {
        Move[] firstMoves = new Move[] { new Move(0, 0, 8), new Move(1, 2, 7), new Move(2, 0, 6), new Move(0, 1, 5),
                new Move(1, 1, 4) };
        Move[] secondMoves = new Move[] { new Move(0, 2, 8), new Move(1, 0, 7), new Move(2, 2, 6), new Move(2, 1, 5) };
        MockAI first = new MockAI(firstMoves, secondMoves, true);
        MockAI second = new MockAI(secondMoves, firstMoves, false);

        playGame(first, second, first);
    }

    private void playGame(MockAI first, MockAI second, MockAI expectedWinner) {
        Game game = new Game(first, second);
        game.playGame();

        if (!first.playedAllMoves() || !second.playedAllMoves()) {
            fail("Dein Game hat früher abgebrochen, obwohl noch kein Sieger feststand.");
        }

        assertSame(expectedWinner, game.getWinner(),
                "Dein Game gibt nach dem Spiel bei getWinner nicht das Richtige zurück.");
    }
}
