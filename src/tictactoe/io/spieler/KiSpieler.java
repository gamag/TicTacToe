package tictactoe.io.spieler;

import tictactoe.logik.Spiellogik;
import tictactoe.logik.KI3T;
import tictactoe.Controller;

/**
 * Spielerklasse zur tictactoe.logik.KI
 */
public class KiSpieler implements SpielerInterface {
	/**
	 * Die Spiellogik.
	 */
	private Spiellogik logik;

	/**
	 * Das KI Objekt.
	 */
	private KI3T ki;

	/**
	 * Die Spielernummer.
	 */
	private int spielerNr;

	/**
	 * Ob dieser Spieler den ersten Zug hat.
	 */
	private boolean startet;

	/**
	 * Ob die Ki gestartet ist.
	 */
	private boolean gestartet;

	/**
	 * Konstruktor.
	 */
	public KiSpieler() {
		ki = null;
		logik = null;
		spielerNr = -1;
	}

	/**
	 * Initialisiert ein Spiel.
	 *
	 * @param logik die verwendete Spiellogik.
	 * @param spieler spieler Nummer wieviel wir sind.
	 */
	public void starteSpiel(Spiellogik logik, int Spieler) {
		this.logik = logik;
		spielerNr = Spieler;
		startet = true;
		gestartet = false;
		ki = new KI3T(false);
	}

	/**
	 * Gibt den Zug der KI zurück.
	 *
	 * @return Die Feldnummer. Oder -1, wenn das Spiel abgebrochen werden soll.
	 * @throws IllegalStateException wenn das Spielfeld voll ist.
	 */
	public int spielzug() {
		if (startet && !gestartet) {
			gestartet = true;
			ki.start();
		}
		logik.setzeFeld(ki.getLastX(), ki.getLastY(), spielerNr);
		// Wenn das Spiel läuft, macht die KI ihre Züge automatisch in gegenzug();
		return ki.getLastY() * 3 + ki.getLastX() + 1;
	}

	/**
	 * Teilt der KI den Zug des Gegeners mit und lässt sie eine eigenen machen.
	 *
	 * @param feld Feldnummer.
	 */
	public void gegenzug(int feld) {
		startet = false;
		ki.set(Controller.getX(feld), Controller.getY(feld), false);
	}

	/**
	 * Tut nichts.
	 *
	 * @param spieler der Gewinner (1 oder 2) oder irgendetwas anderes für unentschieden.
	 */
	public void setGewinner(int spieler) {
	}

	/**
	 * Gibt die Nummer dieses Spielers zurück.
	 */
	public int getSpielerNr() {
		return spielerNr;
	}

	/**
	 * Läst den Spieler eine Auswahl aus einer Liste treffen.
	 * Führt zu einer Ausnahme - IllegalStateException.
	 *
	 * @param liste die Liste, aus der gewählt wird.
	 * @return den gewählten Index.
	 */
	public int wähle(String [] liste) {
		throw new IllegalStateException("Die KI darf nie Master Spieler sein, sie kann nicht denken oder entscheiden.");
	}

	/**
	 * Räumt das Spielfeld ab und setzt die Spielernummer zurück.
	 */
	public void resetSpielfeld() {
		ki = null;
		spielerNr = -1;
	}

	/**
	 * Tut nichts.
	 */
	public void warteAufEingabe() {
	}

	/**
	 * Schliesst das Objekt.
	 *
	 * Nach einem Aufruf dieser Methode ist das verhalten anderer Methoden des Objekts undefiniert.
	 */
	public void beenden() {
		ki = null;
	}

}
