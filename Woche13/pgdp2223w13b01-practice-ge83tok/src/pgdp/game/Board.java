package pgdp.game;

import java.util.Arrays;

/**
 * class representing the playing board
 * NOTE: IDs and indices of players and figures are off by one (1-based vs 0-based)
 * 
 * you may add any public or private attributes and/or methods to this class
 * if you need them to solve the exercise
 * 
 * you may also rewrite this class if you prefer to solve the exercise
 * with a different approach (see task description for more details)
 */
public class Board {
	public static final int EMPTY = 0;
	private static final int[] START_POSITIONS = { 0, 8, 16, 24 };
	private static final int[] GOAL_POSITIONS = { 31, 7, 15, 23 };

	private int[] boardFields;
	private Figure[][] figures;

	public Board() {
		boardFields = new int[32];
		Arrays.fill(boardFields, EMPTY);
		figures = new Figure[4][3];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				figures[i][j] = new Figure(j);
			}
		}
	}

	/**
	 * returns the boards current value at given position
	 * @param position the position
	 * @return the boards value at position
	 */
	public int getBoardAt(int position) {
		if (position < 0 || position >= boardFields.length) {
			throw new IllegalArgumentException("Invalid value for parameter position: " + position);
		}
		return boardFields[position];
	}

	/**
	 * returns the board position of figure from player
	 * @param player the player [1,4]
	 * @param figure the figure [1,3]
	 * @return the figures position
	 */
	public int getFigurePosition(int player, int figure) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		if (figure < 1 || figure > 3) {
			throw new IllegalArgumentException("Invalid value for parameter figure: " + figure);
		}
		return figures[player - 1][figure - 1].position;
	}

	/**
	 * returns whether or not the figure of player is at home
	 * @param player the player [1,4]
	 * @param figure the figure [1,3]
	 * @return whether or not the figure is at home
	 */
	public boolean isFigureAtHome(int player, int figure) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		if (figure < 1 || figure > 3) {
			throw new IllegalArgumentException("Invalid value for parameter figure: " + figure);
		}
		return figures[player - 1][figure - 1].isHome;
	}

	/**
	 * returns whether or not the figure of player reached to goal
	 * @param player the player [1,4]
	 * @param figure the figure [1,3]
	 * @return whether or not the figure is at home
	 */
	public boolean isFigureAtGoal(int player, int figure) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		if (figure < 1 || figure > 3) {
			throw new IllegalArgumentException("Invalid value for parameter figure: " + figure);
		}
		return figures[player - 1][figure - 1].reachedGoal;
	}

	/**
	 * returns players start position
	 * @param player the player [1,4]
	 * @return the players start position
	 */
	public static int getPlayerStartPosition(int player) {
		if (player <= 0 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		return START_POSITIONS[player - 1];
	}

	/**
	 * returns players goal position
	 * @param player the player [1,4]
	 * @return the players goal position
	 */
	public static int getPlayerGoalPosition(int player) {
		if (player <= 0 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		return GOAL_POSITIONS[player - 1];
	}

	/**
	 * private class to store information about a figure
	 */
	private static class Figure {
		boolean isHome;
		boolean reachedGoal;
		int position;

		Figure(int homePosition) {
			position = homePosition;
			isHome = true;
			reachedGoal = false;
		}
	}
}
