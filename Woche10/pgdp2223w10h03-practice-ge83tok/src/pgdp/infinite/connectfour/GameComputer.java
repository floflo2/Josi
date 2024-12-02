package pgdp.infinite.connectfour;

import pgdp.infinite.tree.InfiniteTree;
import pgdp.infinite.tree.Optimizable;

import java.util.*;
import java.util.function.Function;

import static java.util.Collections.emptyIterator;

public class GameComputer {

    private static final ConnectFour.Tile PLAYER = ConnectFour.Tile.RED_TILE;
    private static final ConnectFour.Tile COMPUTER = ConnectFour.Tile.YELLOW_TILE;

    private final ConnectFour game;
    // the depth to which the game tree is searched. Higher numbers mean longer waiting times, but better moves.
    private final int depth;
    // a value which is good enough for the minimax approach that we can stop the search for better moves.
    private final int sufficientToUse;
    // a tree with the moves from the current game, where moves are represented by the played column as bytes.
    private final InfiniteTree<byte[]> gameTree;

    private GameComputer(int depth, int sufficientToUse) {
        this.depth = depth;
        this.sufficientToUse = sufficientToUse;
        // the player is always red and starts, while the computer is always yellow
        this.game = new ConnectFour(PLAYER);

        // The iterator generating the new moves from a certain series of moves.
        Function<byte[], Iterator<byte[]>> childrenGenerator = moves -> {
            game.move(moves);
            // if the play could win in the next move, we assume he will do so and stop the search of this part of the
            // tree.
            Iterator<byte[]> iter = couldPlayerWinInNextMove() ? emptyIterator() : getLegalMoves(moves);
            game.revertMoves(moves.length);
            return iter;
        };
        this.gameTree = new InfiniteTree<>(childrenGenerator);
    }

    public static void main(String[] args) {
        GameComputer computer = new GameComputer(7, 300);
        computer.playAgainstComputer();
    }

    /**
     * @return an Optimizable object that can be used to find the best move for the computer, using
     * a simple minimax approach.
     */
    private Optimizable<byte[]> getMiniMaxOptimizable() {
        return new Optimizable<>() {
            byte[] bestMoves = null;
            int bestScore = Integer.MIN_VALUE;

            @Override
            public boolean process(byte[] moves) {
                if (bestScore > sufficientToUse) {
                    return true;
                }

                game.move(moves);

                int computerScore = game.evaluateBoard(COMPUTER);
                int playerScore = game.evaluateBoard(PLAYER);

                int score = computerScore - playerScore;
                if (score > bestScore) {
                    bestScore = score;
                    bestMoves = moves;
                }

                game.revertMoves(moves.length);

                return score > sufficientToUse;
            }

            @Override
            public byte[] getOptimum() {
                return bestMoves;
            }
        };
    }

    private void makePlayersMove(int column) {
        if (!game.canPlayMove(column)) {
            throw new IllegalArgumentException("Invalid move!");
        }
        game.move(column);
    }

    private boolean couldPlayerWinInNextMove() {
        for (int i = 0; i < 7; i++) {
            if (game.canPlayMove(i)) {
                game.move(i);
                if (game.isWon()) {
                    game.revertMove();
                    return true;
                }
                game.revertMove();
            }
        }
        return false;
    }

    /**
     * @return the legal winning move that the computer could make to win, else -1.
     */
    private int couldComputerWinInNextMove() {
        for (int i = 0; i < 7; i++) {
            if (game.canPlayMove(i)) {
                game.move(i);
                if (game.getWinner() == COMPUTER) {
                    game.revertMove();
                    return i;
                }
                game.revertMove();
            }
        }
        return -1;
    }

    private int getRandomLegalMove() {
        for (int i = 0; i < 7; i++) {
            if (game.canPlayMove(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Advises the computers next move, if the game can be won in the next move, the computer will play it,
     * otherwise it will play the best move it can find, using the (game) InfiniteTree.
     *
     * @return the computers next move.
     */
    private int adviseMove() {
        int winningMove = couldComputerWinInNextMove();
        if (winningMove != -1) {
            return winningMove;
        }
        byte[] bestOutcome = gameTree.find(new byte[0], depth, getMiniMaxOptimizable());
        return bestOutcome != null ? bestOutcome[0] : getRandomLegalMove();
    }

    /**
     * @param moves The moves that have been made so far in addition to the current state of the game.
     * @return An iterator over all legal moves that can be made after the given moves each appended to
     * the given moves.
     */
    private Iterator<byte[]> getLegalMoves(byte[] moves) {
        List<byte[]> legalMoves = new ArrayList<>(7);
        for (byte i = 0; i < 7; i++) {
            if (game.canPlayMove(i)) {
                byte[] newMove = Arrays.copyOf(moves, moves.length + 1);
                newMove[moves.length] = i;
                legalMoves.add(newMove);
            }
        }
        return legalMoves.iterator();
    }

    private void readAndPlayNextMove() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("[!] Please enter your next move: ");
                makePlayersMove(scanner.nextInt());
                break;
            } catch (InputMismatchException e) {
                // input was not an integer
                scanner.nextLine(); // discard the invalid input
                System.out.println("[e] The input could not be read! Try again.");
            } catch (IllegalArgumentException e) {
                // input was an integer but not a valid move
                System.out.println("[e] This column is already full! Try again.");
            }
        }
    }

    public void playAgainstComputer() {

        // Run as long as the game is not won and there are still moves to be made.
        while (game.getWinner() == ConnectFour.Tile.NO_TILE && game.moveCount < 42) {
            System.out.println("[i] Current board: \n" + game);
            readAndPlayNextMove();

            // This move was winning, stop the game.
            if (game.getWinner() != ConnectFour.Tile.NO_TILE) {
                break;
            }

            // Does the computer side of the game.
            System.out.println("[i] Calculating computers move, please wait ...");
            int advisedMove = adviseMove();
            System.out.println("[i] Computer plays column: " + advisedMove);
            game.move(advisedMove);
        }

        ConnectFour.Tile winner = game.getWinner();
        if (winner == ConnectFour.Tile.NO_TILE) {
            System.out.println("[i] The game ended in a draw.");
        } else if (winner == PLAYER) {
            System.out.println("[i] You won! Congratulations!");
        } else {
            System.out.println("[i] The computer won!");
        }

        System.out.println("[i] Final board:\n" + game);
    }
}
