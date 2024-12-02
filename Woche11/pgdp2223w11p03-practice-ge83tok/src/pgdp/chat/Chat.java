package pgdp.chat;

import java.net.Socket;

import static pgdp.PinguLib.*;

public class Chat {
	public static void main(String[] args) {
		Socket sock = null;

		while (true) {
			write("Gib <port> an, um den Chatserver zu starten oder "
					+ "<host>:<port>, um dich mit einem laufenden Server zu verbinden.");
			write("Gib zum Beenden exit ein");
			String input = readString("");
			if (input.equals("exit")) {
				System.out.println("Beenden.");
				return;
			}

			// TODO
		}

		// TODO
	}
}
