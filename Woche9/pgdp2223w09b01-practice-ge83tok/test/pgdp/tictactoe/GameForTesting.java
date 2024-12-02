package pgdp.tictactoe;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Objects;

public class GameForTesting {

    private Field[][] board;
    private PenguAI first;
    private PenguAI second;
    private boolean[] firstPlayedPieces;
    private boolean[] secondPlayedPieces;

    private PenguAI winner;

    public GameForTesting(PenguAI first, PenguAI second) {
        this(first, second, 9);
    }

    public GameForTesting(PenguAI first, PenguAI second, int numberOfPieces) {
        int fieldLength = 3;

        board = new Field[fieldLength][fieldLength];

        firstPlayedPieces = new boolean[numberOfPieces];
        secondPlayedPieces = new boolean[numberOfPieces];

        this.first = first;
        this.second = second;
    }

    public PenguAI getWinner() {
        return winner;
    }

    public void playGame() {
        PenguAI currentPlayer = first;

        for (int i = 0; i < firstPlayedPieces.length * 2; i++) {
            boolean firstPlayerTurn = currentPlayer == first;

            if (noMovePossible(firstPlayerTurn)) {
                winner = firstPlayerTurn ? second : first;
                return;
            }

            Field[][] copiedField = Arrays.stream(board).map(column -> Arrays.copyOf(column, column.length))
                    .toArray(Field[][]::new);

            Move move = currentPlayer.makeMove(copiedField, firstPlayerTurn,
                    Arrays.copyOf(firstPlayedPieces, firstPlayedPieces.length),
                    Arrays.copyOf(secondPlayedPieces, secondPlayedPieces.length));

            if (invalidMove(move, firstPlayerTurn)) {
                winner = firstPlayerTurn ? second : first;
                fail("Deine KI hat einen Zug zurückgegeben, der nicht valide war.");
            }

            playMove(move, firstPlayerTurn);

            if (wonGame(move)) {
                winner = currentPlayer;
                return;
            }

            currentPlayer = firstPlayerTurn ? second : first;
        }

        int boardSum = Arrays.stream(board).flatMap(Arrays::stream).filter(Objects::nonNull)
                .mapToInt(field -> (field.firstPlayer() ? +1 : -1) * field.value()).sum();

        if (boardSum > 0) {
            winner = second;
        } else if (boardSum < 0) {
            winner = first;
        }
    }

    private boolean noMovePossible(boolean firstPlayer) {
        int highestUnplayedPiece = 0;
        boolean[] playedPieces = firstPlayer ? firstPlayedPieces : secondPlayedPieces;

        for (int i = playedPieces.length - 1; i >= 0; i--) {
            if (!playedPieces[i]) {
                highestUnplayedPiece = i;
                break;
            }
        }

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                if (board[x][y] == null
                        || (board[x][y].firstPlayer() != firstPlayer && board[x][y].value() < highestUnplayedPiece)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean invalidMove(Move move, boolean firstPlayer) {
        if (move == null) {
            return true;
        } else if (move.x() < 0 || move.x() >= board.length || move.y() < 0 || move.y() >= board.length) {
            return true;
        } else if (move.value() < 0 || move.value() >= firstPlayedPieces.length) {
            return true;
        } else if (firstPlayer ? firstPlayedPieces[move.value()] : secondPlayedPieces[move.value()]) {
            return true;
        }
        Field selectedField = board[move.x()][move.y()];
        return selectedField != null
                && (selectedField.firstPlayer() == firstPlayer || selectedField.value() >= move.value());
    }

    private boolean wonGame(Move move) {
        return wonRow(move) || wonColumn(move) || wonDiagonalRight(move) || wonDiagonalLeft(move);
    }

    private boolean wonRow(Move move) {
        int rowIndex = move.y();
        boolean firstPlayer = board[move.x()][move.y()].firstPlayer();

        for (int x = 0; x < board.length; x++) {
            if (board[x][rowIndex] == null || board[x][rowIndex].firstPlayer() != firstPlayer) {
                return false;
            }
        }

        return true;
    }

    private boolean wonColumn(Move move) {
        int columnIndex = move.x();
        boolean firstPlayer = board[move.x()][move.y()].firstPlayer();

        for (int y = 0; y < board.length; y++) {
            if (board[columnIndex][y] == null || board[columnIndex][y].firstPlayer() != firstPlayer) {
                return false;
            }
        }

        return true;
    }

    private boolean wonDiagonalRight(Move move) {
        if (move.x() != move.y()) {
            return false;
        }

        boolean firstPlayer = board[move.x()][move.y()].firstPlayer();

        for (int i = 0; i < board.length; i++) {
            if (board[i][i] == null || board[i][i].firstPlayer() != firstPlayer) {
                return false;
            }
        }

        return true;
    }

    private boolean wonDiagonalLeft(Move move) {
        if (move.x() + move.y() != board.length - 1) {
            return false;
        }

        boolean firstPlayer = board[move.x()][move.y()].firstPlayer();

        for (int i = 0; i < board.length; i++) {
            if (board[i][board.length - 1 - i] == null || board[i][board.length - 1 - i].firstPlayer() != firstPlayer) {
                return false;
            }
        }

        return true;
    }

    private void playMove(Move move, boolean firstPlayer) {
        board[move.x()][move.y()] = new Field(move.value(), firstPlayer);
        if (firstPlayer) {
            firstPlayedPieces[move.value()] = true;
        } else {
            secondPlayedPieces[move.value()] = true;
        }
    }
}