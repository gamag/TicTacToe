package tictactoe.io.spieler;

import tictactoe.logik.Spiellogik;
import tictactoe.io.gpio.GpioIOTreiber;
import tictactoe.Controller;

import java.util.concurrent.TimeUnit;

/**
 * Der am GPIO-Spielbrett spielende Spieler.
 */
public class GpioSpieler implements SpielerInterface {
	
	/**
	 * Das IO Objekt.
	 */
	protected static GpioIOTreiber io;

	/**
	 * Das Logik Objekt.
	 */
	private Spiellogik logik;

	/**
	 * Wie viele instanzen greifen noch auf io zu?
	 */
	protected static int referenzen = 0;

	/**
	 * Welcher Spieler wir sind.
	 */
	protected int spielerNr;


	/**
	 * Konstuktor
	 */
	public GpioSpieler() {
		spielerNr = 0;
		if (referenzen <= 0) {
			io = new GpioIOTreiber();
			io.start();
		}
		referenzen++;
	}

	/**
	 * Initalisiert ein Spiel.
	 */
	public void starteSpiel(Spiellogik spl, int spieler) {
		resetSpielfeld();
		spielerNr = spieler;
		logik = spl;
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
			if (logik.istGueltig(Controller.getX(i), Controller.getY(i))) {
				return i;
			}
			i %= 9;
			i++;
		}

		if (feld != 0 && logik.istGueltig(Controller.getX(feld), Controller.getY(feld))) {
			return feld;
		}

		return -1;
	}

	/**
	 * Lässt den Spieler einen Zug machen.
	 *
	 * @return Die Feldnummer, oder -1, wenn abgebrochen werden soll.
	 * @throws IllegalStateException wenn das Spielfeld voll ist.
	 */
	public int spielzug() {
		io.setFeld(10, spielerNr);

		int aktuellesFeld = findeMoeglichenZug(0);
		io.setFeld(aktuellesFeld, 3);

		while (true) {
			if (io.istGedrueckt(1) && io.istGedrueckt(2)) {
				return -1;
			} else if (io.istNeuGedrueckt(1)) {
				io.setFeld(aktuellesFeld, 0);

				aktuellesFeld = findeMoeglichenZug(aktuellesFeld);

				if (aktuellesFeld == -1) {
					throw new IllegalStateException("Unerwartetes Spielende");
				}

				io.setFeld(aktuellesFeld, 3);
			} else if (io.istNeuGedrueckt(2)) {
				io.setFeld(aktuellesFeld, spielerNr);
				logik.setzeFeld(Controller.getX(aktuellesFeld), Controller.getY(aktuellesFeld), spielerNr);

				return aktuellesFeld;
			}
			try {
				TimeUnit.MILLISECONDS.sleep(40);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Zeig den zu des Gegners an.
	 *
	 * @param feld Feldnummer.
	 */
	public void gegenzug(int feld) {
		io.setFeld(feld, (spielerNr % 2) + 1);
	}

	/**
	 * Zeig an, dass der gegeben Spieler gewonnen hat.
	 *
	 * @param spieler der Gewinner.
	 */
	public void setGewinner(int spieler) {
		if (spieler == 1) {
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
		} else if (spieler == 2) {
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
	}

	/**
	 * Gibt die Spielernummer zurück.
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
		resetSpielfeld();
		for (int i = 1; i <= liste.length; i++) {
			io.setFeld(i, 1);
		}
		int aktuell = 1;
		io.setFeld(1, 3);
		while (!io.istNeuGedrueckt(2)) {
			if (io.istNeuGedrueckt(1)) {
				io.setFeld(aktuell, 1);
				aktuell %= liste.length;
				aktuell++;
				io.setFeld(aktuell, 3);
			}
		}
		io.setFeld(aktuell, 2);
		return aktuell-1;
	}

	/**
	 * "Räumt das Spielfeld ab".
	 */
	public void resetSpielfeld() {
		spielerNr = 0;
		for (int i = 1; i <= 10; i++) {
			io.setFeld(i, 0);
		}
	}

	/**
	 * Wartet auf eine Eingabe.
	 */
	public void warteAufEingabe() {
		while (!(io.istNeuGedrueckt(1) || io.istNeuGedrueckt(2))) {
			try {
				TimeUnit.MILLISECONDS.sleep(40);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Beenden.
	 */
	public void beenden() {
		referenzen --;
		if (referenzen <= 0) {
			io.beenden();
		}
	}
}
