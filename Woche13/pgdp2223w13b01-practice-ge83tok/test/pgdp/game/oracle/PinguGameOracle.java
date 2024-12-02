package pgdp.game.oracle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PinguGameOracle {
	private final boolean renderBW;
	private final Random random;
	private final BufferedReader reader;
	private final int renderDelay;

	public BoardOracle board;
	private int pinguPlayers;

	public PinguGameOracle() {
		this(false);
	}

	public PinguGameOracle(boolean renderBW) {
		this(renderBW, Long.MIN_VALUE, 500);
	}

	public PinguGameOracle(boolean renderBW, long randomSeed, int renderDelay) {
		this.renderBW = renderBW;
		if (randomSeed == Long.MIN_VALUE) {
			random = new Random();
		} else {
			random = new Random(randomSeed);
		}
		reader = new BufferedReader(new InputStreamReader(System.in));
		this.renderDelay = renderDelay;
		initNewGame();
	}

	private void initNewGame() {
		board = new BoardOracle();
	}

	public void play() {
		startSequence();

		int currentPlayer = 1;
		OUTER: while (true) {
			printString(4, currentPlayer);
			// figure on field?
			if (board.hasFigureOnField(currentPlayer)) {
				// yes ->
				//		6 ->
				//			reach goal, throw, normal move, check win and redice
				//			or move to start and one dice away, no further redice
				//		1-5 ->
				//			reach goal if possible
				//			throw if possible
				//			normal move
				//			both: check win
				int roll;
				do {
					roll = rollDice();
					printString(5, currentPlayer, roll);
					Set<Integer> onField = board.getFiguresOnField(currentPlayer);
					Set<Integer> canThrow = new HashSet<>();
					Set<Integer> canMove = new HashSet<>();
					Set<Integer> canReachGoal = new HashSet<>();
					Set<Integer> inHome = board.getFiguresInHome(currentPlayer);
					for (int i : onField) {
						if (board.canReachGoal(currentPlayer, i, roll)) {
							canReachGoal.add(i);
							continue;
						}
						int boardValue = board.getBoardAt((board.getFigurePosition(currentPlayer, i) + roll) % 32);
						if (boardValue != BoardOracle.EMPTY) {
							if (BoardUtilityOracle.getPlayerFromMaskOrBoard(boardValue) != currentPlayer) {
								canThrow.add(i);
							}
						} else {
							canMove.add(i);
						}
					}
					if (roll == 6) {
						canMove.addAll(inHome);
						int boardValue = board.getBoardAt(BoardOracle.getPlayerStartPosition(currentPlayer));
						if (boardValue != BoardOracle.EMPTY
								&& BoardUtilityOracle.getPlayerFromMaskOrBoard(boardValue) != currentPlayer) {
							canThrow.addAll(inHome);
						}
					}

					if (!canReachGoal.isEmpty()) {
						// force to move figure into goal
						printString(7, canReachGoal.stream().mapToInt(i -> i).toArray());
						int figureToMove;
						if (currentPlayer <= pinguPlayers) {
							do {
								figureToMove = readInt();
							} while (!canReachGoal.contains(figureToMove));
						} else {
							figureToMove = canReachGoal.stream().min(Integer::compare).get();
							printString(6, figureToMove);
						}

						// check, if enemy figure is on goal entrance
						int boardVal;
						if ((boardVal = board
								.getBoardAt(BoardOracle.getPlayerGoalPosition(currentPlayer))) != BoardOracle.EMPTY) {
							int otherPlayer = BoardUtilityOracle.getPlayerFromMaskOrBoard(boardVal);
							int otherFigure = BoardUtilityOracle.getFigureFromMaskOrBoard(boardVal);
							printString(8, otherFigure, otherPlayer);
							board.throwFigure(otherPlayer, otherFigure);
						}
						board.moveFigureToGoal(currentPlayer, figureToMove);
						render();
						// check for win
						if (board.hasWon(currentPlayer)) {
							break OUTER;
						}
					} else if (!canThrow.isEmpty()) {
						// force to throw other player
						printString(9, canThrow.stream().mapToInt(i -> i).toArray());
						int figureToMove;
						if (currentPlayer <= pinguPlayers) {
							do {
								figureToMove = readInt();
							} while (!canThrow.contains(figureToMove));
						} else {
							figureToMove = canThrow.stream().min(Integer::compare).get();
							printString(6, figureToMove);
						}

						if (inHome.contains(figureToMove)) {
							moveOutOfHouse(currentPlayer, figureToMove);
							break;
						} else {
							throwFigure(currentPlayer, figureToMove, roll);
						}
					} else if (!canMove.isEmpty()) {
						// select move
						printString(10, canMove.stream().mapToInt(i -> i).toArray());
						int figureToMove;
						if (currentPlayer <= pinguPlayers) {
							do {
								figureToMove = readInt();
							} while (!canMove.contains(figureToMove));
						} else {
							figureToMove = canMove.stream().min(Integer::compare).get();
							printString(6, figureToMove);
						}

						if (!inHome.contains(figureToMove)) {
							board.moveFigure(currentPlayer, figureToMove, roll);
							render();
						} else {
							// special, if move out of house
							moveOutOfHouse(currentPlayer, figureToMove);
							break;
						}
					} else {
						printString(11, currentPlayer, roll);
						render();
					}
				} while (roll == 6);
			} else {
				// no -> 3 tries
				// 		no 6 -> next player
				// 		6 -> move away from home; always possible, no further redice
				printString(12, currentPlayer);
				int roll;
				int count = 0;
				do {
					roll = rollDice();
					printString(5, currentPlayer, roll);
					count++;
				} while (roll != 6 && count < 3);

				if (roll != 6) {
					printString(13);
					render();
				} else {
					int figureToMove;
					Set<Integer> inHome = board.getFiguresInHome(currentPlayer);
					printString(14, inHome.stream().mapToInt(i -> i).toArray());
					if (currentPlayer <= pinguPlayers) {
						do {
							figureToMove = readInt();
						} while (!inHome.contains(figureToMove));
					} else {
						figureToMove = inHome.stream().min(Integer::compare).get();
						printString(6, figureToMove);
					}
					moveOutOfHouse(currentPlayer, figureToMove);
				}
			}
			// continue with next player, if not won
			currentPlayer = currentPlayer % 4 + 1;
		}

		printString(22, currentPlayer);
		printString(23);
		// ask to restart the game
		int restart;
		do {
			restart = readInt();
		} while (restart != 1 && restart != 0);

		if (restart == 1) {
			initNewGame();
			play();
		}
	}

	private void startSequence() {
		printString(1);
		printString(2);
		do {
			pinguPlayers = readInt();
		} while (pinguPlayers < 0 || pinguPlayers > 4);
		printString(3, pinguPlayers);
		render();
	}

	/**
	 * reads a single integer from console and prints the lines required by the task description
	 * @return the number or Integer.MIN_VALUE if input is invalid
	 */
	private int readInt() {
		System.out.print("> ");
		try {
			String in = reader.readLine();
			return Integer.parseInt(in);
		} catch (IOException e) {
			System.out.println("An Exception occured: " + e.getLocalizedMessage());
			System.exit(1);
		} catch (NumberFormatException n) {
			System.out.println("Keine gültige Zahl!");
			return Integer.MIN_VALUE;
		}
		return Integer.MIN_VALUE;
	}

	/**
	 * rolls a dice
	 * @return an integer in [1,6]
	 */
	protected int rollDice() {
		// do not change method (will be overridden for testing)
		if (values == null || idx >= values.length) {
			return random.nextInt(6) + 1;
		} else {
			return values[idx++];
		}
	}

	public int[] values;
	public int idx = 0;

	private void moveOutOfHouse(int player, int figure) {
		// check if home is free and throw if not
		int housePosition = BoardOracle.getPlayerStartPosition(player);
		int boardValue;
		if ((boardValue = board.getBoardAt(housePosition)) != BoardOracle.EMPTY) {
			int otherFigure = BoardUtilityOracle.getFigureFromMaskOrBoard(boardValue);
			int otherPlayer = BoardUtilityOracle.getPlayerFromMaskOrBoard(boardValue);
			printString(15, otherFigure, otherPlayer);
			board.throwFigure(otherPlayer, otherFigure);
		}
		// move and render
		board.moveOutOfHome(player, figure);
		render();
		// roll again and move
		printString(16, player);
		int roll = rollDice();
		printString(5, player, roll);
		if ((boardValue = board.getBoardAt((housePosition + roll) % 32)) == BoardOracle.EMPTY) {
			board.moveFigure(player, figure, roll);
			render();
		} else if (BoardUtilityOracle.getPlayerFromMaskOrBoard(boardValue) != player) {
			throwFigure(player, figure, roll);
		} else {
			for (int i = roll - 1; i > 0; i--) {
				if ((boardValue = board.getBoardAt((housePosition + i) % 32)) == BoardOracle.EMPTY) {
					printString(17, i);
					board.moveFigure(player, figure, i);
					render();
					return;
				} else if (BoardUtilityOracle.getPlayerFromMaskOrBoard(boardValue) != player) {
					printString(18, i);
					throwFigure(player, figure, i);
					return;
				}
			}
			for (int i = roll + 1; i < 32; i++) {
				if ((boardValue = board.getBoardAt((housePosition + i) % 32)) == BoardOracle.EMPTY) {
					printString(19, i);
					board.moveFigure(player, figure, i);
					render();
					return;
				} else if (BoardUtilityOracle.getPlayerFromMaskOrBoard(boardValue) != player) {
					printString(20, i);
					throwFigure(player, figure, i);
					return;
				}
			}
		}
	}

	private void throwFigure(int player, int figure, int moveDistance) {
		// throw figure at position
		int targetPosition = (board.getFigurePosition(player, figure) + moveDistance) % 32;
		int boardValue = board.getBoardAt(targetPosition);
		int otherPlayer = BoardUtilityOracle.getPlayerFromMaskOrBoard(boardValue);
		int otherFigure = BoardUtilityOracle.getFigureFromMaskOrBoard(boardValue);
		printString(21, otherFigure, otherPlayer);
		board.throwFigure(otherPlayer, otherFigure);
		// move figure to position
		board.moveFigure(player, figure, moveDistance);
		render();
	}

	/**
	 * prints the board using the selected render mode and waits for
	 * renderDelay milliseconds if its value is larger than 0
	 */
	private void render() {
		if (renderBW) {
			BoardUtilityOracle.printBoardBW(board);
		} else {
			BoardUtilityOracle.printBoard(board);
		}
		if (renderDelay > 0) {
			try {
				Thread.sleep(renderDelay);
			} catch (InterruptedException e) {
				System.out.println("Eine Exception ist aufgetreten: " + e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Prints the String defined by id (see task description)
	 * @param id the Strings id
	 * @param params the required parameters for the string in order of appearance 
	 * 		(for outputs with variable number of figures the order is sorted automatically)
	 */
	private static void printString(int id, int... params) {
		switch (id) {
		case 1:
			// #1: start new Game
			System.out.println("Willkommen zu \"Pingu ärgere dich nicht\"!");
			break;
		case 2:
			// #2: input request for number of players
			System.out.println("Wie viele Pinguine wollen spielen?");
			System.out.println("Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!");
			break;
		case 3:
			// #3: game start with selected number of players
			System.out
					.println("Starte Spiel mit " + params[0] + " \"echten\" und " + (4 - params[0]) + " KI Pinguinen.");
			break;
		case 4:
			// #4: first line of a single turn
			System.out.println("Pinguin " + params[0] + " ist am Zug.");
			break;
		case 5:
			// #5: dice roll
			System.out.println("Pinguin " + params[0] + " hat eine " + params[1] + " gewürfelt.");
			break;
		case 6:
			// #6: AI chooses figure
			System.out.println("KI wählt Figur " + params[0] + ".");
			break;
		case 7:
			// #7: figures can reach goal
			System.out.print("Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): ");
			Arrays.sort(params);
			for (int i = 0; i < params.length; i++) {
				System.out.print(params[i]);
				if (i != params.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
			break;
		case 8:
			// #8: throw figure while reaching goal
			System.out.println("Beim Erreichen des Ziels wurde Figur " + params[0] + " des Pinguins " + params[1]
					+ " geschlagen.");
			break;
		case 9:
			// #9: figure can throw
			System.out.print("Eine der folgenden Figuren kann eine gegnerische Figur schlagen (bitte auswählen): ");
			Arrays.sort(params);
			for (int i = 0; i < params.length; i++) {
				System.out.print(params[i]);
				if (i != params.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
			break;
		case 10:
			// #10: figure can move
			System.out.print("Eine der folgenden Figuren kann bewegt werden (bitte auswählen): ");
			Arrays.sort(params);
			for (int i = 0; i < params.length; i++) {
				System.out.print(params[i]);
				if (i != params.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
			break;
		case 11:
			// #11: no move possible
			System.out.println(
					"Keine Figur von Pinguin " + params[0] + " kann mit einer " + params[1] + " bewegt werden.");
			break;
		case 12:
			// #12: no figure on field
			System.out.println("Pinguin " + params[0]
					+ " hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.");
			break;
		case 13:
			// #13: no 6 this round
			System.out.println("Schade, keine 6. Mehr Glück nächste Runde!");
			break;
		case 14:
			// #14: choose figure to move to field
			System.out.print("Welche Figur möchtest du aufs Spielfeld ziehen? Bitte wählen: ");
			Arrays.sort(params);
			for (int i = 0; i < params.length; i++) {
				System.out.print(params[i]);
				if (i != params.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println();
			break;
		case 15:
			// #15: throw while moving to field
			System.out.println("Die Figur " + params[0] + " von Pinguin " + params[1]
					+ " wurde beim Verlassen des Hauses geschlagen.");
			break;
		case 16:
			// #16: clear start field
			System.out.println("Pinguin " + params[0] + " muss das Startfeld räumen.");
			break;
		case 17:
			// #17: clear start field - special case: move less
			System.out.println("Feld bereits belegt, es kann/können nur " + params[0] + " Feld(er) gegangen werden.");
			break;
		case 18:
			// #18: clear start field - special case: move less + throw
			System.out.println("Feld bereits belegt, es kann/können nur " + params[0]
					+ " Feld(er) gegangen werden und dabei wird eine andere Figur geschlagen.");
			break;
		case 19:
			// #19: clear start field - special case: move more
			System.out.println("Feld bereits belegt, es dürfen sogar " + params[0] + " Felder gegangen werden.");
			break;
		case 20:
			// #20: clear start field - special case: move more + throw
			System.out.println("Feld bereits belegt, es dürfen sogar " + params[0]
					+ " Felder gegangen werden und dabei wird eine andere Figur geschlagen.");
			break;
		case 21:
			// #21: figure thrown
			System.out.println("Figur " + params[0] + " von Pinguin " + params[1] + " wurde geschlagen.");
			break;
		case 22:
			// #22: player won
			System.out.println("Herzlichen Glückwunsch Pinguin " + params[0] + ", du hast gewonnen!!!");
			break;
		case 23:
			// #23: restart game
			System.out.println("Soll ein neues Spiel gestartet werden? 1 - Ja, 0 - Nein");
			break;
		default:
			System.out.println("Unkown id!");
			break;
		}
	}
}
