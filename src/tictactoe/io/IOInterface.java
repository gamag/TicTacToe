package tictactoe.io;

/**
 * Interface zum Vereinheitlichen verschiedener Ein/Ausgabe Möglichkeiten wie GPIO oder Tastatur.
 *
 * Wir bieten nur Funktionen zum setzen der Spielfelder und der Benutzermarkierungen sowie zum lesen des
 * aktuellen  Tastenzustands. Eine Änderung des Tastendruckes wird nicht bemerkt. Der Zustand der
 * Spielfelder/LEDs kann nicht abgefragt werden.
 * Änderungen werden spätestens beim nächsten {@link tictactoe.io.IOInterface#update()} Aufruf übernommen.
 */
public interface IOInterface {

	/**
	 * Setzt das angegeben Feld des Spielfeldes auf den angegeben Zustand.
	 *
	 * @param feldnummer Nummer des zu ändernden Feldes vom 1 (oben Links) bis 9 (unten Rechts) und 10 =
	 * Extra 1 (Spieler), 11 = Extra 2, 12 Extra 3.
	 * @param zustand Der Zustand, auf den das Feld gesetzt werden soll: 0 = leer, 1 = Spieler1 (an), 2 =
	 * Spieler2, 3 = Provisorische Auswahl.
	 */
	public void setFeld(int feldnummer, int zustand);

	/**
	 * Testet ob der angegeben Knopfes gedrückt ist.
	 *
	 * @param knopf Nummer des Knopfes (1 = Auswahl, 2 = Bestätigen)
	 *
	 * @return true wenn der Knopf gedrückt ist.
	 */
	public boolean istGerdrueckt(int knopf);

	/**
	 * Aktualisiert die Ausgabe.
	 */
	public void update();

	/**
	 * Beendet die Aus-/Eingabe.
	 *
	 * sollte einmal aufgerufen werden, wenn das Object nichtmehr benötigt wird, damit ev offene Dateien, Pipes oder
	 * GPIO Verbindungen geschlossen werden können.
	 */
	public void beenden();
}
