package pgdp.tictactoe;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;

public class MockAI extends PenguAI {

    private Move[] ownMoves;
    private Move[] opponentMoves;
    private boolean firstPlayer;
    private int moveIndex;

    public MockAI(Move[] ownMoves, Move[] opponentMoves, boolean firstPlayer) {
        this.ownMoves = ownMoves;
        this.opponentMoves = opponentMoves;
        this.firstPlayer = firstPlayer;
    }

    @Override
    public Move makeMove(Field[][] board, boolean firstPlayer, boolean[] firstPlayedPieces,
            boolean[] secondPlayedPieces) {
        if (moveIndex >= ownMoves.length) {
            fail("Du hast die KI nach einem weiteren Zug gefragt, obwohl sie keinen mehr zurückgeben müsste. Hast du einen invaliden Zug oder einen Sieg übersehen?");
        }

        replayAndTestGameUntilNow(board, firstPlayer, firstPlayedPieces, secondPlayedPieces);

        return ownMoves[moveIndex++];
    }

    public boolean playedAllMoves() {
        return moveIndex == ownMoves.length;
    }

    private void replayAndTestGameUntilNow(Field[][] board, boolean firstPlayer, boolean[] firstPlayedPieces,
            boolean[] secondPlayedPieces) {
        if (this.firstPlayer != firstPlayer) {
            fail("Du übergibst der KI einen falschen Wert für firstPlayer");
        }

        Field[][] testBoard = new Field[3][3];
        boolean[] testFirstPlayedPieces = new boolean[9];
        boolean[] testSecondPlayedPieces = new boolean[9];
        if (firstPlayer) {
            for (int i = 0; i < moveIndex; i++) {
                playMove(testBoard, ownMoves[i], true, testFirstPlayedPieces, testSecondPlayedPieces);
                playMove(testBoard, opponentMoves[i], false, testFirstPlayedPieces, testSecondPlayedPieces);
            }
        } else {
            for (int i = 0; i < moveIndex; i++) {
                playMove(testBoard, opponentMoves[i], true, testFirstPlayedPieces, testSecondPlayedPieces);
                playMove(testBoard, ownMoves[i], false, testFirstPlayedPieces, testSecondPlayedPieces);
            }
            playMove(testBoard, opponentMoves[moveIndex], true, testFirstPlayedPieces, testSecondPlayedPieces);
        }

        if (!Arrays.deepEquals(board, testBoard)) {
            fail("Wenn der Test das bisherige Spiel nachspielt, sieht das board anders aus als das was du an die KI übergibst.");
        } else if (!Arrays.equals(testFirstPlayedPieces, firstPlayedPieces)) {
            fail("Wenn der Test das bisherige Spiel nachspielt, sieht das Array für die gespielten Steine vom ersten Spieler anders aus als das was du an die KI übergiebst.");
        } else if (!Arrays.equals(testSecondPlayedPieces, secondPlayedPieces)) {
            fail("Wenn der Test das bisherige Spiel nachspielt, sieht das Array für die gespielten Steine vom zweiten Spieler anders aus als das was du an die KI übergiebst.");
        }
    }

    private void playMove(Field[][] board, Move move, boolean firstPlayer, boolean[] firstPlayedPieces,
            boolean[] secondPlayedPieces) {
        board[move.x()][move.y()] = new Field(move.value(), firstPlayer);
        if (firstPlayer) {
            firstPlayedPieces[move.value()] = true;
        } else {
            secondPlayedPieces[move.value()] = true;
        }
    }
}
