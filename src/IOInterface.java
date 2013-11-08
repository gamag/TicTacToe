/**
 * Interface zum Vereinheitlichen verschiedener Ein/Ausgabe Möglichkeiten wie GPIO oder Tastatur.
 *
 * Wir bieten nur Funktionen zum setzen der Spielfelder und der Benutzermarkierungen sowie zum lesen des
 * aktuellen  Tastenzustands. Eine Änderung des Tastendruckes wird nicht bemerkt. Der Zustand der
 * Spielfelder/LEDs kann nicht abgefragt werden.
 * Änderungen werden spätestens beim nächsten Update() Aufruf übernommen.
 * Die Update Funktion sollte mindestens 24 mal pro Sekunde aufgerufen werden, da die GPIO Ausgabe mit Matrix
 * auf schnellem Blinken beruht.
 */
interface IOInterface {

	/**
	 * Setzt das angegeben Feld des Spielfeldes auf den angegeben Zustand.
	 *
	 * @param feldnummer Nummer des zu ändernden Feldes vom 1 (oben Links) bis 9 (unten Rechts) und 10 =
	 * Spieler1, 11 = Spieler2
	 * @param zustand Der Zustand, auf den das Feld gesetzt werden soll: 0 = leer, 1 = Spieler1 (an), 2 =
	 * Spieler2, 3 = Provisorische Auswahl.
	 */
	void setFeld(int feldnummer, int zustand);

	/**
	 * Testet ob der angegeben Knopfes gedrückt ist.
	 *
	 * @param knopf Nummer des Knopfes (1 = Auswahl, 2 = Bestätigen)
	 *
	 * @return true wenn der Knopf gedrückt ist.
	 */
	boolean istGerdrueckt(int knopf);

	/**
	 * Aktualisiert die Ausgabe.
	 *
	 * Mind. 24 mal pro Sekunde aufrufen.
	 */
	void update();
}
