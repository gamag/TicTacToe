package tictactoe.io.spieler;

import java.io.*;

import tictactoe.Controller;
import tictactoe.logik.Spiellogik;

/**
 * Spielerklasse für Spieler am Bildschirm, in der Textkonsole.
 */
public class KonsolenSpieler implements SpielerInterface {
	/**
	 * Die Spielernummer
	 */
	private int spielerNr;

	/**
	 * Eingabebuffer
	 */
	private BufferedReader in;

	/**
	 * Das Spielfeld.
	 */
	private char[][] spielfeld;

	/**
	 * Wie viele Objekte dieser Klasse gibt es gerade?
	 */
	private static int bildschirmspiele;

	/**
	 * Die Spiellogik.
	 */
	private Spiellogik logik;

	/**
	 * Konstruktor
	 */
	public KonsolenSpieler() {
		spielerNr = -1;
		bildschirmspiele++;
		in = new BufferedReader(new InputStreamReader(System.in));
		spielfeld = new char[3][3];
	}

	/**
	 * Gibt das Spielfeld aus.
	 */
	private void printSpielfeld() {
		System.out.println("    0   1   2");
		System.out.println("  ╔═══╤═══╤═══╗");
		for (int i = 0; i < 3; i++) {
			if (i > 0) {
				System.out.println("  ╟───┼───┼───╢");
			}
			System.out.print(i + " ║");
			for (int x = 0; x < 3; x++) {
				System.out.print(" " + spielfeld[x][i] +
						" " + ((x < 2) ? "│" : "║"));
			}
			System.out.println("");
		}
		System.out.println("  ╚═══╧═══╧═══╝");
	}

	/**
	 * Initialisiert ein Spiel.
	 *
	 * @param logik die verwendete Spiellogik.
	 * @param spieler spieler Nummer wieviel wir sind.
	 */
	public void starteSpiel(Spiellogik logik, int spieler) {
		resetSpielfeld();
		this.logik = logik;
		spielerNr = spieler;
	}

	/**
	 * Lässt den Spieler einen Zug machen.
	 *
	 * @return Die Feldnummer. Oder -1, wenn das Spiel abgebrochen werden soll.
	 * @throws IllegalStateException wenn das Spielfeld voll ist.
	 */
	public int spielzug() {
		printSpielfeld();
		System.out.println("Spieler " + (spielerNr == 1 ? "O" : "X") + " ist am Zug.");
		int zug = -1;
		do {
			System.out.print("Koordinaten Ihres Zugs (Spalte Zeile, m zum Abbrechen) >");
			String s = "";
			try {
				s = in.readLine();
			} catch (IOException e) {
				System.out.println("Lesefehler: " + e);
				continue;
			}
			if (s.length() < 0) {
				continue;
			}
			if (s.equals("m")) {
				return -1;
			}
			String[] c = s.split("[ ,|)(\t]+");
			int x = 0;
			int y = 0;
			if (c.length != 2) {
				System.out.println("Bitte geben Sie zwei Zahlen ein.");
				continue;
			}
			try {
				x = Integer.parseInt(c[0]);
				y = Integer.parseInt(c[1]);
			} catch (NumberFormatException e) {
				System.out.println("Bitte geben Sie zwei Zahlen ein.");
				continue;
			}
			if (logik.istGueltig(x, y)) {
				spielfeld[x][y] = (spielerNr == 1 ? 'O' : 'X');
				logik.setzeFeld(x, y, spielerNr);
				zug = (y * 3 + x + 1);
			} else {
				System.out.println("Spielzug ungültig, bitte geben Sie einen Erlaubten Spielzug an.");
			}
		} while (zug < 0);
		return zug;
	}

	/**
	 * Zeig den zu des Gegners an.
	 *
	 * @param feld Feldnummer.
	 */
	public void gegenzug(int feld) {
		spielfeld[Controller.getX(feld)][Controller.getY(feld)] = (spielerNr == 1 ? 'X' : 'O');
		if (bildschirmspiele < 2) {
			printSpielfeld();
			System.out.println("Spieler " + (spielerNr == 1 ? 'O' : 'X') + "hat auf (" 
					+ Controller.getX(feld) + "|" + Controller.getY(feld) + ") gesetzt." );
		}
	}

	/**
	 * Zeig an, dass der gegeben Spieler gewonnen hat.
	 *
	 * @param spieler der Gewinner (1 oder 2) oder irgendetwas anderes für unentschieden.
	 */
	public void setGewinner(int spieler) {
		if (bildschirmspiele < 2 || spielerNr == 1) {
			printSpielfeld();
			System.out.println("=========================================");
			if (spieler != 1 && spieler != 2) {
				System.out.println("Das Spiel endet unentschieden.");
			} else {
				System.out.println("Spieler " + (spieler == 1 ? "O" : "X") + " hat gewonnen!");
			}
			System.out.println("=========================================");
		}
	}

	/**
	 * Gibt die Nummer dieses Spielers zurück.
	 */
	public int getSpielerNr() {
		return spielerNr;
	}

	/**
	 * Läst den Spieler eine Auswahl aus einer Liste treffen.
	 *
	 * @param liste die Liste, aus der gewählt wird.
	 * @return den gewählten Index.
	 */
	public int wähle(String [] liste) {
		System.out.println("Bitte wählen Sie aus dem folgenden Menü:");
		while (true) {
			for (int i = 0; i < liste.length; i++) {
				System.out.println("(" + i + ") " + liste[i]);
			}
			System.out.print("Menüeintrag (Nummer) >");
			String s = "";
			try {
				s = in.readLine();
			} catch (IOException e) {
				System.out.println("Lesefehler: " + e);
				continue;
			}
			if (s.length() < 0) {
				continue;
			}
			int nr = -1;
			try {
				nr = Integer.parseInt(s);
			} catch (NumberFormatException e) {
				System.out.println("Bitte geben Sie eine gültige Zahl ein.");
				continue;
			}
			if (nr < 0 || nr >= liste.length) {
				System.out.println("Ungültiges Element. Bitte wählen Sie aus der folgenden Liste:");
				continue;
			} else {
				return nr;
			}
		}
	}

	/**
	 * Räumt das Spielfeld ab und setzt die Spielernummer zurück.
	 */
	public void resetSpielfeld() {
		spielerNr = -1;
		for (int x = 0; x < 3; x++){
			for (int y = 0; y < 3; y++) {
				spielfeld[x][y] = ' ';
			}
		}

	}

	/**
	 * Wartet auf eine Eingabe.
	 */
	public void warteAufEingabe() {
		// Da wir nie "Clear" oder ähnliches verwenden und keine langen Listen ausgeben, ist dashier
		// überflüssig.
	}

	/**
	 * Schliesst das Objekt.
	 *
	 * Nach einem Aufruf dieser Methode ist das verhalten anderer Methoden des Objekts undefiniert.
	 */
	public void beenden() {
		bildschirmspiele--;
	}
}
