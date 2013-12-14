package tictactoe;

import java.util.concurrent.TimeUnit;

import tictactoe.io.*;

/**
 * Test für den MockIOTreiber.
 */
public class MockIOTest {
	public static void main(String[] args) {
		IOInterface a = new MockIOTreiber();
		int feld[] = new int[9];

		for (int i = 0; i < 9; i++) {
			feld[i] = 0;
		}

		int pos = 1;
		int posf = 8;
		boolean gedruekt1 = false;
		boolean gedruekt2 = false;
		int spieler = 1;
		a.start();
		while (true) {
			boolean s1 = a.istGedrueckt(1);
			boolean s2 = a.istGedrueckt(2);

			if (s1 && !gedruekt1) { // seit neustem gedrückt
				a.setFeld((pos != 9) ? pos + 1 : 1, 3); // neues Feld setzen
				a.setFeld(pos, feld[posf]); // altes zurücksetzen.
				gedruekt1 = true;

				pos %= 9; // Position 9 ist noch gültig 10 und 0 nicht.
				pos++;
				posf++;
				posf %= 9;
			} else if (!s1) { // Nicht mehr gedrückt
				gedruekt1 = false;
			}

			if (s2 && !gedruekt2) {
				feld[posf] = spieler;
				a.setFeld(pos, spieler);
				gedruekt2 = true;

				spieler %= 2;
				spieler++;
			} else if (!s2) {
				gedruekt2 = false;
			}

			if (spieler == 1) {
				a.setFeld(10, 1);
				a.setFeld(11, 0);
			} else {
				a.setFeld(10, 0);
				a.setFeld(11, 1);
			}

			try {
				TimeUnit.MILLISECONDS.sleep(40);
			} catch (InterruptedException e) {
				System.out.print("Interrupted");
			}
		}
	}
}
