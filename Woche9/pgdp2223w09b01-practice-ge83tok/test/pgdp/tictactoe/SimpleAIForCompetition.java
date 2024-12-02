package pgdp.tictactoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleAIForCompetition extends PenguAI {

    @Override
    public Move makeMove(Field[][] board, boolean firstPlayer, boolean[] firstPlayedPieces,
            boolean[] secondPlayedPieces) {
        boolean[] playedPieces = firstPlayer ? firstPlayedPieces : secondPlayedPieces;
        List<Move> possibleMoves = getAllMoves(firstPlayer, playedPieces, board);

        Collections.shuffle(possibleMoves);

        Move winningMove = getWinningMove(possibleMoves, firstPlayer, board, playedPieces);
        if (winningMove != null) {
            return winningMove;
        }

        Move potentiallyBlockingMove = getPotentiallyBlockingMove(possibleMoves, firstPlayer, board, playedPieces,
                firstPlayer ? secondPlayedPieces : firstPlayedPieces);

        return potentiallyBlockingMove != null ? potentiallyBlockingMove : possibleMoves.get(0);
    }

    private List<Move> getAllMoves(boolean firstPlayer, boolean[] playedPieces, Field[][] board) {
        List<Move> possibleMoves = new ArrayList<>();

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                addAllMovesForField(x, y, board[x][y], firstPlayer, playedPieces, possibleMoves);
            }
        }

        return possibleMoves;
    }

    private void addAllMovesForField(int x, int y, Field field, boolean firstPlayer, boolean[] playedPieces,
            List<Move> possibleMoves) {
        if (field == null) {
            for (int i = 0; i < playedPieces.length; i++) {
                if (!playedPieces[i]) {
                    possibleMoves.add(new Move(x, y, i));
                }
            }
        } else if (field.firstPlayer() != firstPlayer) {
            for (int i = field.value() + 1; i < playedPieces.length; i++) {
                if (!playedPieces[i]) {
                    possibleMoves.add(new Move(x, y, i));
                }
            }
        }
    }

    private Move getWinningMove(List<Move> possibleMoves, boolean firstPlayer, Field[][] board,
            boolean[] playedPieces) {
        for (Move move : possibleMoves) {
            Field initialField = board[move.x()][move.y()];
            playMove(move, firstPlayer, playedPieces, board);
            if (wonGame(move, board)) {
                wonGame(move, board);
                undoMove(move, initialField, playedPieces, board);
                return move;
            }
            undoMove(move, initialField, playedPieces, board);
        }

        return null;
    }

    private void playMove(Move move, boolean firstPlayer, boolean[] playedPieces, Field[][] board) {
        board[move.x()][move.y()] = new Field(move.value(), firstPlayer);
        playedPieces[move.value()] = true;
    }

    private void undoMove(Move move, Field initialField, boolean[] playedPieces, Field[][] board) {
        board[move.x()][move.y()] = initialField;
        playedPieces[move.value()] = false;
    }

    private boolean wonGame(Move move, Field[][] board) {
        return wonRow(move, board) || wonColumn(move, board) || wonDiagonalRight(move, board)
                || wonDiagonalLeft(move, board);
    }

    private boolean wonRow(Move move, Field[][] board) {
        int rowIndex = move.y();
        boolean firstPlayer = board[move.x()][move.y()].firstPlayer();

        for (int x = 0; x < board.length; x++) {
            if (board[x][rowIndex] == null || board[x][rowIndex].firstPlayer() != firstPlayer) {
                return false;
            }
        }

        return true;
    }

    private boolean wonColumn(Move move, Field[][] board) {
        int columnIndex = move.x();
        boolean firstPlayer = board[move.x()][move.y()].firstPlayer();

        for (int y = 0; y < board.length; y++) {
            if (board[columnIndex][y] == null || board[columnIndex][y].firstPlayer() != firstPlayer) {
                return false;
            }
        }

        return true;
    }

    private boolean wonDiagonalRight(Move move, Field[][] board) {
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

    private boolean wonDiagonalLeft(Move move, Field[][] board) {
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

    private Move getPotentiallyBlockingMove(List<Move> possibleMoves, boolean firstPlayer, Field[][] board,
            boolean[] playedPieces, boolean[] opponentPlayedPieces) {
        return possibleMoves.stream().filter(move -> {
            Field initialField = board[move.x()][move.y()];
            playMove(move, firstPlayer, playedPieces, board);
            boolean opponentCantWin = getWinningMove(getAllMoves(!firstPlayer, opponentPlayedPieces, board),
                    !firstPlayer, board, opponentPlayedPieces) == null;
            undoMove(move, initialField, playedPieces, board);
            return opponentCantWin;
        }).findFirst().orElse(null);
    }
}
