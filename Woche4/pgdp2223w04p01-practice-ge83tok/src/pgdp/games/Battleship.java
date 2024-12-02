package pgdp.games;

import static pgdp.PinguLib.*;

public class Battleship {

	// Main-Methode für Experimente
	public static void main(String[] args) {
		battleship();
	}

	/** Methode, die das Spiel 'Schiffe Versenken' auf der Konsole ausführt.
	 * 	Solange noch nicht alle Schiffe getroffen sind, wird das aktuelle Spielbrett geprintet,
	 * 	danach wird der Spieler nach einer Koordinate gefragt, auf die er schießen will.
	 */
	public static void battleship() {

	}

	/** Fordert vom Nutzer solange eine Integer-Eingabe,
	 *  bis diese zwischen den angegebenen Grenzen liegt.
	 *
	 * @param min Beliebiger Integer
	 * @param max Beliebiger Integer (wenn max < min ist,
	 *               ist diese Methode eine Endlosschleife von Nutzereingaben)
	 * @return Die erste vom Nutzer eingegebene Zahl x, sodass min <= x <= max
	 */
	public static int readNumberInInterval(int min, int max, String question) {
		return 0;
	}

	/** Fordert vom Nutzer solange eine Character-Eingabe,
	 *  bis diese zwischen den angegebenen Grenzen liegt.
	 *
	 * @param min Beliebiger Character
	 * @param max Beliebiger Character (wenn max < min ist,
	 *               ist diese Methode eine Endlosschleife von Nutzereingaben)
	 * @return Der erste vom Nutzer eingegebene Charakter c, sodass min <= c <= max
	 */
	public static char readCharacterInInterval(char min, char max, String question) {
		return (char) 0;
	}

	/** Ändert den Zustand der übergebenen Koordinate,
	 * 	sodass dieses Feld nun frei liegt.
	 * 	Wenn sich davor ein Schiff darauf befand, befindet sich dort nun ein 'Hit',
	 * 	wenn sich davor kein Schiff darauf befand, befindet sich dort nun ein 'Miss'.
	 * 	Wenn das Feld bereits beschossen wurde, ändert sich nichts und es wird 'false' zurückgegeben.
	 *
	 * 	Die Methode nimmt ohne Weiteres an, dass sich die übergebene Koordinate
	 * 	innerhalb des Spielfelds befindet und dieses noch nicht beschossen wurde.
	 *
	 * @param row Eine Spaltennummer innerhalb der Spielfeldgrenzen
	 * @param col Eine Zeilennummer innerhalb der Spielfeldgrenzen
	 * @param gameBoard Ein 2D-Integer-Array mit Einträgen zwischen 0 und 3,
	 * 	                welches das Spielfeld repräsentiert.
	 */
	public static void shootAt(int row, int col, int[][] gameBoard) {

	}

	/** Überprüft, ob die übergebene Koordinate bereits beschossen wurde.
	 *
	 * 	Die Methode nimmt ohne Weiteres an, dass sich die übergebene Koordinate
	 * 	innerhalb des Spielfelds befindet.
	 *
	 * @param row Eine Spaltennummer innerhalb der Spielfeldgrenzen
	 * @param col Eine Zeilennummer innerhalb der Spielfeldgrenzen
	 * @param gameBoard Ein 2D-Integer-Array mit Einträgen zwischen 0 und 3,
	 * 	                welches das Spielfeld repräsentiert.
	 * @return 'true' gdw. das Feld bereits beschossen wurde.
	 */
	public static boolean hasBeenShotAt(int row, int col, int[][] gameBoard) {
		return false;
	}

	/** Überprüft, ob noch Schiffe auf dem Spielfeld übrig sind.
	 *
	 * @param board Ein 2D-Integer-Array mit Einträgen zwischen 0 und 3,
	 *              welches das Spielfeld repräsentiert.
	 * @return 'true' gdw. noch Schiffe auf dem Spielfeld übrig sind.
	 */
	public static boolean shipsLeft(int[][] board) {
		return false;
	}

	/** Printet das übergebene Spielfeld auf die Konsole.
	 *
	 * @param board Ein 2D-Integer-Array mit Einträgen zwischen 0 und 3,
	 * 	            welches das Spielfeld repräsentiert.
	 */
	public static void printBoard(int[][] board) {

	}
}