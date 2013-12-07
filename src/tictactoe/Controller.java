package tictactoe;

import java.util.concurrent.TimeUnit;

import tictactoe.logik.*;
import tictactoe.io.IOInterface;
import tictactoe.io.spieler.*;

/**
 * Steuerung und Verbinudung von Spiellogik und Ausgabe.
 *
 * Läuft in einem eigenen Thread um ein sauberes beenden über die
 */
public class Controller {

	/**
	 * Konstuktor.
	 */
	public Controller (IOInterface treiber) {
		beenden = false;
		io = treiber;

		initialisiert = true;
		gedrueckt1 = false;
		gedrueckt2 = false;
	}

	/**
	 * Ob das Objekt noch nicht geschlossen wurde.
	 */
	boolean initialisiert;

	/**
	 * Ob wir alles beenden sollen.
	 *
	 * {@link tictactoe.Controller#run()} gibt dann bei nächster Gelegenheit zurück.
	 */
	private volatile boolean beenden;

	/**
	 * Ob das aktuelle Spiel abgebrochen werden soll.
	 *
	 * Die Kontrolle geht bei nächster Gelegenheit wieder an {@link tictactoe.Controller#run()}.
	 */
	private volatile boolean spielAbbrechen;

	/**
	 * Der IO Treiber.
	 */
	IOInterface io;

	/**
	 * Startet das Programm.
	 *
	 * Wenn diese Methode abgelaufen ist, ist das Objekt "verbraucht" und darf nicht mehr verwendet werden.
	 *
	 * @throws IllegalStateException wenn mehrfach aufgerufen.
	 */
	public void run() {
		if (!initialisiert) {
			throw new IllegalStateException("Bitte ein neues Objekt erzeugen.");
		}

		while (!beenden) {
			spielAbbrechen = false;
			spiel();
		}

		initialisiert = false;
		aufreumen();
	}

	/**
	 * Der Spieler, der gerade am Zug ist.
	 */
	int spieler;

	/**
	 * Die verwendete Instanz der Spiellogik.
	 */
	Spiellogik logik;


	/**
	 * Führt ein Spiel aus.
	 */
	private void spiel() {
		logik = new Spiellogik();
		spieler = 1;

		resetSpielfeld();

		while (!logik.spielFertig()) {
			int naechsterZug = -1;
			while (naechsterZug == -1 || !logik.istGueltig(getX(naechsterZug), getY(naechsterZug))) {
				naechsterZug = getSpielzug();

				if (spielAbbrechen) {
					return;
				}
			}
			logik.setzeFeld(getX(naechsterZug), getY(naechsterZug), spieler);
			io.setFeld(naechsterZug, spieler);

			spieler = (spieler == 1 ? 2 : 1);
		}
		zeigeErgebnis(logik.gewinner());
	}

	/**
	 * Ob Knopf 1 gedruekt ist.
	 */
	private boolean gedrueckt1;

	/**
	 * Ob Knopf 1 im letzten Mainloop-Durchlauf heruntergedrückt wurde (keydown event).
	 */
	private boolean neuGedrueckt1;

	/**
	 * Ob Knopf 1 im letzten Mainloop-Durchlauf losgelassen wurde (keyup event).
	 */
	private boolean losgelassen1;

	/**
	 * Wie viele Mainloop-Runden Knopf 1 schon gedrückt ist.
	 */
	private int gedruecktSeit1;

	/**
	 * Ob Knopf 2 gedruekt ist.
	 */
	private boolean gedrueckt2;

	/**
	 * Ob Knopf 2 im letzten Mainloop-Durchlauf heruntergedrückt wurde (keydown event).
	 */
	private boolean neuGedrueckt2;

	/**
	 * Ob Knopf 2 im letzten Mainloop-Durchlauf losgelassen wurde (keyup event).
	 */
	private boolean losgelassen2;

	/**
	 * Wie viele Mainloop-Runden Knopf 2 schon gedrückt ist.
	 */
	private int gedruecktSeit2;


	/**
	 * Aktualisiert den Zustand der Knöpfe.
	 *
	 * "keydown" wird alle 50 runden wiederholt.
	 */
	private void updateButtons() {
		if (io.istGedrueckt(1)) {
			neuGedrueckt1 = (!gedrueckt1 || gedruecktSeit1 == 50);
			gedrueckt1 = true;
			losgelassen1 = false;

			gedruecktSeit1++;
			gedruecktSeit1 %= 51;
		} else {
			losgelassen1 = gedrueckt1;
			neuGedrueckt1 = false;
			gedrueckt1 = false;
			gedruecktSeit1 = 0;
		}
		if (io.istGedrueckt(2)) {
			neuGedrueckt2 = (!gedrueckt2 || gedruecktSeit2 == 50);
			gedrueckt2 = true;
			losgelassen2 = false;

			gedruecktSeit2++;
			gedruecktSeit2 %= 51;
		} else {
			losgelassen2 = gedrueckt2;
			neuGedrueckt2 = false;
			gedrueckt2 = false;
			gedruecktSeit2 = 0;
		}
	}

	/**
	 * Lässt den aktuellen Spieler seinen Spielzug eingeben.
	 *
	 * Nimmt nur die Eingabe entgegen, übernimmt die Mainloop.
	 *
	 * @return Die Nummer des gewählten Feldes
	 *
	 * @throws IllegalStateException Wenn das Spielfeld voll ist.
	 */
	private int getSpielzug() {
		io.setFeld(10, spieler);

		int aktuellesFeld = findeMoeglichenZug(0);
		io.setFeld(aktuellesFeld, 3);

		while (!spielAbbrechen) {

			mainLoop();

			if (gedrueckt1 && gedrueckt2) {
				spielAbbrechen = true;
				return 0; // Das ist zwar Unsinn, aber wir wollen auch nicht behaupten, abbrechen sei ein Fehler (-1).
			} else if (neuGedrueckt1) {
				io.setFeld(aktuellesFeld, 0);

				aktuellesFeld = findeMoeglichenZug(aktuellesFeld);

				if (aktuellesFeld == -1) {
					throw new IllegalStateException("Unerwartetes Spielende");
				}

				io.setFeld(aktuellesFeld, 3);
			} else if (neuGedrueckt2) {
				io.setFeld(aktuellesFeld, 0);

				return aktuellesFeld;
			}
		}

		return -1;
	}

	/**
	 * Findet das nächste freie Feld nach dem angegebenen.
	 *
	 * @param feld Das Feld, ab dem gesucht wird, 0 um alle zu durchsuchen.
	 * @return Die gefundene Feldnummer oder -1.
	 */
	private int findeMoeglichenZug(int feld) {
		int i = feld;
		i %= 9;
		i++;

		while (i != feld) {
			if (logik.istGueltig(getX(i), getY(i))) {
				return i;
			}
			i %= 9;
			i++;
		}

		if (feld != 0 && logik.istGueltig(getX(feld), getY(feld))) {
			return feld;
		}

		return -1;
	}

	/**
	 * Gibt auf geeignete Weise aus, wer gewonnen hat.
	 *
	 * Wartet, bis beide Köpfe gedrückt sind. Übernimmt die Mainloop.
	 *
	 * @param gewinner der Gewinner.
	 */
	private void zeigeErgebnis(int gewinner) {
		if (gewinner == 1) {
			// Wir zeigen einen Kreis an.
			resetSpielfeld();
			io.setFeld(1, 1);
			io.setFeld(2, 1);
			io.setFeld(3, 1);
			io.setFeld(4, 1);
			io.setFeld(6, 1);
			io.setFeld(7, 1);
			io.setFeld(8, 1);
			io.setFeld(9, 1);

			io.setFeld(10, 1);
		} else if (gewinner == 2) {
			// wir zeigen eine X an.
			resetSpielfeld();
			io.setFeld(1, 2);
			io.setFeld(3, 2);
			io.setFeld(5, 2);
			io.setFeld(7, 2);
			io.setFeld(9, 2);

			io.setFeld(10, 2);
		} else {
			// Wir lassen alles schnell Blinken.
			resetSpielfeld();
			io.setFeld(1, 3);
			io.setFeld(2, 3);
			io.setFeld(3, 3);
			io.setFeld(4, 3);
			io.setFeld(5, 3);
			io.setFeld(6, 3);
			io.setFeld(7, 3);
			io.setFeld(8, 3);
			io.setFeld(9, 3);

			io.setFeld(10, 3);
		}

		while (!(gedrueckt1 && gedrueckt2)) {
			mainLoop();

		}
	}

	/**
	 * Gibt die x-Koordinate des angegebenen Feldes zurück.
	 * @param feld Feldnummer (1-9).
	 */
	private int getX(int feld) {
		return ((feld - 1) % 3);
	}

	/**
	 * Gibt die y-Koordinate des angegebenen Feldes zurück.
	 * @param feld Feldnummer (1-9).
	 */
	private int getY(int feld) {
		return ((feld-1) / 3);
	}

	/**
	 * Inhalt der Mainloop.
	 *
	 * Wird von der Methode aufgerufen, die gerade das Spiel kontrolliert.
	 *
	 * Wartet einige Zeit.
	 */
	private void mainLoop() {
		io.update();
		updateButtons();

	}

	/**
	 * Schliesst das Ausgabeobjekt.
	 */
	private void aufreumen() {
		io.beenden();
	}

	/**
	 * Lässt run() so schnell wie möglich zurückgeben.
	 *
	 * Zum sauberen herunterfahren, threadsave.
	 */
	public void beenden() {
		spielAbbrechen = true;
		beenden = true;
	}
}
