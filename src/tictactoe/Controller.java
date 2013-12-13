package tictactoe;

import java.util.concurrent.TimeUnit;

import tictactoe.logik.*;
import tictactoe.io.spieler.*;

/**
 * Steuerung und Verbinudung von Spiellogik und Ausgabe.
 *
 * Läuft in einem eigenen Thread um ein sauberes beenden über die
 */
public class Controller {

	/**
	 * Konstuktor.
	 *
	 * @param master der spieler, der die Entscheidungen über das nächste Spiel trifft.
	 */
	public Controller (SpielerInterface master) {
		spieler =  new SpielerInterface[2];
		initialisiert = true;
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
	 * Der zwischen den Spielen Kontrolle habende Spieler.
	 */
	SpielerInterface master;

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

		while (true) {
			spielAbbrechen = false;

			String typen [] = {
				"Gegen Gpio-Spielbrett.",
				"Beenden."
			};

			int spieltyp = master.wähle(typen);
			boolean beenden = false;

			switch (spieltyp) {
				case 1:
				case -1:
					spieltyp = -1;
					break;
				case 0:
				default:
					spieler[0] = master;
					spieler[1] = new GpioSpieler();
			}

			if (spieltyp == -1) {
				break;
			}

			spiel();

			if (master.getSpielerNr() != 1) {
				spieler[0].beenden();
			}

			if (master.getSpielerNr() != 2) {
				spieler[1].beenden();
			}

			master.resetSpielfeld();
		}

		initialisiert = false;
		aufreumen();
	}

	/**
	 * Die beiden spielenden Spieler.
	 * Nur während eines Spieles gültig.
	 */
	SpielerInterface spieler[];

	/**
	 * Der Spieler, der gerade am Zug ist (Index).
	 */
	int spielerAmZug;

	/**
	 * Die verwendete Instanz der Spiellogik.
	 */
	Spiellogik logik;


	/**
	 * Führt ein Spiel aus.
	 */
	private void spiel() {
		logik = new Spiellogik();
		spieler[0].starteSpiel(logik, 1);
		spieler[1].starteSpiel(logik, 2);

		spielerAmZug = 0;

		while (!logik.spielFertig()) {
			int naechsterZug = spieler[spielerAmZug].spielzug();
			if (naechsterZug == -1) {
				beenden();
				return;
			}
			logik.setzeFeld(getX(naechsterZug), getY(naechsterZug), spielerAmZug + 1);

			spielerAmZug %= 1;

			spieler[spielerAmZug].gegenzug(naechsterZug);
		}

		int gewinner = logik.gewinner();
		spieler[0].setGewinner(gewinner);
		spieler[1].setGewinner(gewinner);

		master.warteAufEingabe();
	}

	/**
	 * Schliesst das Ausgabeobjekt.
	 */
	private void aufreumen() {

	}

	/**
	 * Lässt run() so schnell wie möglich zurückgeben.
	 *
	 * Zum sauberen herunterfahren, threadsave.
	 */
	public void beenden() {
		beenden = true;
	}

	/**
	 * Gibt die x-Koordinate des angegebenen Feldes zurück.
	 * @param feld Feldnummer (1-9).
	 */
	public static int getX(int feld) {
		return ((feld - 1) % 3);
	}

	/**
	 * Gibt die y-Koordinate des angegebenen Feldes zurück.
	 * @param feld Feldnummer (1-9).
	 */
	public static int getY(int feld) {
		return ((feld-1) / 3);
	}

}
