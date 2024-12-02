package pgdp.game.oracle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * class representing the playing board
 * NOTE: IDs and indices of players and figures are off by one (1-based vs 0-based)
 */
public class BoardOracle {
	public static final int EMPTY = 0;
	private static final int[] START_POSITIONS = { 0, 8, 16, 24 };
	private static final int[] GOAL_POSITIONS = { 31, 7, 15, 23 };

	public int[] boardFields;
	public Figure[][] figures;

	public BoardOracle() {
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

	public boolean hasFigureOnField(int player) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		for (int i = 0; i < 3; i++) {
			if (!figures[player - 1][i].isHome && !figures[player - 1][i].reachedGoal) {
				return true;
			}
		}
		return false;
	}

	public boolean hasWon(int player) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (figures[player - 1][i].reachedGoal) {
				count++;
			}
		}
		return count == 3;
	}

	public Set<Integer> getFiguresOnField(int player) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		Set<Integer> ret = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			if (!figures[player - 1][i].isHome && !figures[player - 1][i].reachedGoal) {
				ret.add(i + 1);
			}
		}
		return ret;
	}

	public Set<Integer> getFiguresInHome(int player) {
		if (player < 1 || player > 4) {
			throw new IllegalArgumentException("Invalid value for parameter player: " + player);
		}
		Set<Integer> ret = new HashSet<>();
		for (int i = 0; i < 3; i++) {
			if (figures[player - 1][i].isHome) {
				ret.add(i + 1);
			}
		}
		return ret;
	}

	public boolean canReachGoal(int player, int figure, int distance) {
		int reachablePos = (figures[player - 1][figure - 1].position + distance) % 32;
		if (figures[player - 1][figure - 1].position > reachablePos) {
			// overflow -> only player 1 can reach goal in this area
			return player == 1;
		} else {
			return figures[player - 1][figure - 1].position < GOAL_POSITIONS[player - 1]
					&& GOAL_POSITIONS[player - 1] <= reachablePos;
		}
	}

	public void moveFigureToGoal(int player, int figure) {
		Figure fig = figures[player - 1][figure - 1];
		boardFields[fig.position] = EMPTY;

		fig.reachedGoal = true;
		fig.position = figure;
	}

	public void throwFigure(int player, int figure) {
		Figure fig = figures[player - 1][figure - 1];
		boardFields[fig.position] = EMPTY;

		fig.isHome = true;
		fig.position = figure;
	}

	public void moveFigure(int player, int figure, int amount) {
		Figure fig = figures[player - 1][figure - 1];
		boardFields[fig.position] = EMPTY;
		fig.position = (fig.position + amount) % 32;
		boardFields[fig.position] = BoardUtilityOracle.getBoardValue(player, figure);
		fig.isHome = false;
	}

	public void moveOutOfHome(int player, int figure) {
		Figure fig = figures[player - 1][figure - 1];
		fig.position = START_POSITIONS[player - 1];
		fig.isHome = false;
		boardFields[fig.position] = BoardUtilityOracle.getBoardValue(player, figure);
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
	public static class Figure {
		public boolean isHome;
		public boolean reachedGoal;
		public int position;

		Figure(int homePosition) {
			position = homePosition;
			isHome = true;
			reachedGoal = false;
		}
	}
}
