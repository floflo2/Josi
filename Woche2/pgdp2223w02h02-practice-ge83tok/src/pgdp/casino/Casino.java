package pgdp.casino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Casino {

	public static void penguBlackJack() {

		// Here is a card deck for your games :)
		// Remember for testing you can use seeds:
		// CardDeck deck = CardDeck.getDeck(420);
		CardDeck deck = CardDeck.getDeck();

		// TODO
	}

	public static String readString() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int readInt() {
		while (true) {
			String input = readString();
			try {
				return Integer.parseInt(input);
			} catch (Exception e) {
				System.out.println("This was not a valid number, please try again.");
			}
		}
	}

	public static void main(String[] args) {
		penguBlackJack();
	}

}
