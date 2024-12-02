package pgdp.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleAIForTesting extends PenguAI {
    private Random random;

    public SimpleAIForTesting() {
        random = new Random();
    }

    @Override
    public Move makeMove(Field[][] board, boolean firstPlayer, boolean[] firstPlayedPieces,
            boolean[] secondPlayedPieces) {
        boolean[] playedPieces = firstPlayer ? firstPlayedPieces : secondPlayedPieces;
        List<Move> possibleMoves = getAllMoves(firstPlayer, playedPieces, board);

        return possibleMoves.get(random.nextInt(possibleMoves.size()));
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
}