package pgdp.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

/**
 * This is the class to configure and start a game
 * 
 * You may add public and private attributes and/or methods if needed.
 *
 * See task description for all explanations and restrictions!
 */
public class PinguGame {
	private final boolean renderBW;
	private final Random random;
	private final BufferedReader reader;
	private final int renderDelay;

	private Board board;

	public PinguGame() {
		this(false);
	}

	public PinguGame(boolean renderBW) {
		this(renderBW, Long.MIN_VALUE, 500);
	}

	public PinguGame(boolean renderBW, long randomSeed, int renderDelay) {
		this.renderBW = renderBW;
		if (randomSeed == Long.MIN_VALUE) {
			random = new Random();
		} else {
			random = new Random(randomSeed);
		}
		reader = new BufferedReader(new InputStreamReader(System.in));
		this.renderDelay = renderDelay;
		board = new Board();
	}

	public void play() {
		// TODO
	}

	// here are some helper methods to help you with the implementation:

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
		return random.nextInt(6) + 1;
	}

	/**
	 * prints the board using the selected render mode and waits for
	 * renderDelay milliseconds if its value is larger than 0
	 */
	private void render() {
		if (renderBW) {
			BoardUtility.printBoardBW(board);
		} else {
			BoardUtility.printBoard(board);
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

	public static void main(String[] args) {
		// default options (you may change them for testing or playing)
		boolean bw = false;
		int seed = 420;
		int delay = 500;
		// very basic args check
		if (args.length >= 1) {
			bw = Boolean.parseBoolean(args[0]);
		}
		if (args.length >= 2) {
			seed = Integer.parseInt(args[1]);
		}
		if (args.length >= 3) {
			delay = Integer.parseInt(args[2]);
		}
		// init game and start playing
		PinguGame p = new PinguGame(bw, seed, delay);
		p.play();
	}
}
