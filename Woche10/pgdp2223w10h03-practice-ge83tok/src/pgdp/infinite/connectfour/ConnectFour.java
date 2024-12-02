package pgdp.infinite.connectfour;

import java.util.Arrays;

public class ConnectFour {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String BOARD = """
              ____________________________
             /   /   /   /   /   /   /   /|
            |---|---|---|---|---|---|---| |
            | %s | %s | %s | %s | %s | %s | %s | |
            |---|---|---|---|---|---|---| |
            | %s | %s | %s | %s | %s | %s | %s | |
            |---|---|---|---|---|---|---| |
            | %s | %s | %s | %s | %s | %s | %s | |
            |---|---|---|---|---|---|---| |
            | %s | %s | %s | %s | %s | %s | %s | |
            |---|---|---|---|---|---|---| |
            | %s | %s | %s | %s | %s | %s | %s | |
            |---|---|---|---|---|---|---| |
            | %s | %s | %s | %s | %s | %s | %s | |
            |---|---|---|---|---|---|---| /
            \\_0___1___2___3___4___5___6_/
            """;
    // A classic Connect Four game with a 6 rows times 7 columns board.
    private final Tile[][] board;
    // saves the moves made by both players.
    private final int[] moves = new int[42];
    public int moveCount;
    private Tile currentPlayer;
    public ConnectFour(Tile playerStarts) {
        this.currentPlayer = playerStarts;
        this.board = new Tile[6][7];
        for (Tile[] row : board) {
            Arrays.fill(row, Tile.NO_TILE);
        }
    }

    public static Tile oppositePlayer(Tile player) {
        return player == Tile.RED_TILE ? Tile.YELLOW_TILE : Tile.RED_TILE;
    }

    /**
     * Calculates the points a player gets for a certain row/column/diagonal.
     *
     * @param tiles  the tiles in the row/column/diagonal.
     * @param player the player for which the points are calculated.
     * @return the points the player gets for the row/column/diagonal.
     */
    private int evaluateForFields(Tile[] tiles, Tile player) {
        // if the row/column/diagonal cannot be finished anymore, it is not interesting for the evaluation.
        for (Tile tile : tiles) {
            if (tile == oppositePlayer(player)) {
                return 0;
            }
        }

        // count the number of tiles of the player has in the row/column/diagonal.
        int myTiles = 0;
        for (Tile tile : tiles) {
            if (tile == player) {
                myTiles++;
            }
        }

        // assigns a value to the number of tiles the player has in the row/column/diagonal.
        return switch (myTiles) {
            case 4 -> 16;
            case 3 -> 6;
            case 2 -> 1;
            default -> 0;
        };
    }

    public enum Tile {
        RED_TILE, YELLOW_TILE, NO_TILE
    }

    /**
     * Calculates the points a player gets for the setup of the board.
     *
     * @param player the player for which the points are calculated.
     * @return the points the player gets for the setup of the board.
     */
    public int evaluateBoard(Tile player) {
        int score = 0;
        // Check rows
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                score += evaluateForFields(new Tile[]{board[i][j], board[i][j + 1], board[i][j + 2],
                        board[i][j + 3]}, player);
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 7; j++) {
                score += evaluateForFields(new Tile[]{board[i][j], board[i + 1][j], board[i + 2][j],
                        board[i + 3][j]}, player);
            }
        }

        // Check diagonals
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                score += evaluateForFields(new Tile[]{board[i][j], board[i + 1][j + 1], board[i + 2][j + 2],
                        board[i + 3][j + 3]}, player);
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 3; j < 7; j++) {
                score += evaluateForFields(new Tile[]{board[i][j], board[i + 1][j - 1], board[i + 2][j - 2],
                        board[i + 3][j - 3]}, player);
            }
        }

        return score;
    }


    public void move(int column) {
        for (int i = 0; i < 6; i++) {
            if (board[i][column] == Tile.NO_TILE) {
                board[i][column] = currentPlayer;
                currentPlayer = oppositePlayer(currentPlayer);
                moves[moveCount++] = column;
                return;
            }
        }
    }

    public void move(byte[] moves) {
        for (byte move : moves) {
            move(move);
        }
    }

    public void revertMove() {
        int column = moves[--moveCount];
        for (int i = 5; i >= 0; i--) {
            if (board[i][column] != Tile.NO_TILE) {
                board[i][column] = Tile.NO_TILE;
                currentPlayer = currentPlayer == Tile.RED_TILE ? Tile.YELLOW_TILE : Tile.RED_TILE;
                return;
            }
        }
    }

    public void revertMoves(int count) {
        for (int i = 0; i < count; i++) {
            revertMove();
        }
    }

    public boolean canPlayMove(int column) {
        return board[5][column] == Tile.NO_TILE;
    }

    /**
     * Checks if the game is over. For simplicity, it only checks if the game was won after the last move,
     * if the game was one in moves before that the method does not work correctly.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isWon() {
        if (moveCount < 4) {
            return false;
        }

        int lastColumn = moves[moveCount - 1];
        int lastRow = 6;
        for (int i = 5; i >= 0; i--) {
            if (board[i][lastColumn] != Tile.NO_TILE) {
                lastRow = i;
                break;
            }
        }

        Tile lastPlayer = oppositePlayer(currentPlayer);

        // Check if in the row of the last move are four connected.
        int connected = 0;
        for (int i = 0; i < 7; i++) {
            if (board[lastRow][i] == lastPlayer) {
                connected++;
                if (connected == 4) {
                    return true;
                }
            } else {
                connected = 0;
            }
        }

        // Check if int the column of the last move are four connected.
        connected = 0;
        for (int i = 0; i < 6; i++) {
            if (board[i][lastColumn] == lastPlayer) {
                connected++;
                if (connected == 4) {
                    return true;
                }
            } else {
                connected = 0;
            }
        }

        // Check lower left to upper right diagonal.
        int min = Math.min(lastRow, lastColumn);
        int lowestRow = lastRow - min;
        int lowestColumn = lastColumn - min;
        connected = 0;
        while (lowestRow < 6 && lowestColumn < 7) {
            if (board[lowestRow++][lowestColumn++] == lastPlayer) {
                connected++;
                if (connected == 4) {
                    return true;
                }
            } else {
                connected = 0;
            }
        }

        // Check lower right to upper left diagonal.
        min = Math.min(lastRow, 6 - lastColumn);
        lowestRow = lastRow - min;
        lowestColumn = lastColumn + min;
        connected = 0;
        while (lowestRow < 6 && lowestColumn >= 0) {
            if (board[lowestRow++][lowestColumn--] == lastPlayer) {
                connected++;
                if (connected == 4) {
                    return true;
                }
            } else {
                connected = 0;
            }
        }

        return false;
    }

    public Tile getWinner() {
        return isWon() ? oppositePlayer(currentPlayer) : Tile.NO_TILE;
    }

    @Override
    public String toString() {
        String[] boardString = new String[42];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                boardString[(35 - i * 7) + j] = switch (board[i][j]) {
                    case RED_TILE -> ANSI_BOLD + ANSI_RED + "O" + ANSI_RESET;
                    case YELLOW_TILE -> ANSI_BOLD + ANSI_YELLOW + "O" + ANSI_RESET;
                    case NO_TILE -> " ";
                };
            }
        }
        return String.format(BOARD, (Object[]) boardString);
    }
}
