package tictactoe.io.spieler;

import tictactoe.logik.Spiellogik;

/**
 * Ein Spieler-Objekt steuert die Ein- und Ausgabe auf dem Medium, an dem der Spieler spielt.
 *
 * Auf diese weise sind Spiele zwischen Spielern am Computer und am Spielbrett, wie auch Spiele
 * gegen KIs möglich.
 */
public interface SpielerInterface {
	/**
	 * Initialisiert ein Spiel.
	 *
	 * @param logik die verwendete Spiellogik.
	 * @param spieler spieler Nummer wieviel wir sind.
	 */
	public void starteSpiel(Spiellogik logik, int Spieler);

	/**
	 * Lässt den Spieler einen Zug machen.
	 *
	 * @return Die Feldnummer. Oder -1, wenn das Spiel abgebrochen werden soll.
	 * @throws IllegalStateException wenn das Spielfeld voll ist.
	 */
	public int spielzug();

	/**
	 * Zeig den zu des Gegners an.
	 *
	 * @param feld Feldnummer.
	 */
	public void gegenzug(int feld);

	/**
	 * Zeig an, dass der gegeben Spieler gewonnen hat.
	 *
	 * @param spieler der Gewinner (1 oder 2) oder irgendetwas anderes für unentschieden.
	 */
	public void setGewinner(int spieler);

	/**
	 * Gibt die Nummer dieses Spielers zurück.
	 */
	public int getSpielerNr();

	/**
	 * Läst den Spieler eine Auswahl aus einer Liste treffen.
	 *
	 * @param liste die Liste, aus der gewählt wird.
	 * @return den gewählten Index.
	 */
	public int wähle(String [] liste);

	/**
	 * Räumt das Spielfeld ab und setzt die Spielernummer zurück.
	 */
	public void resetSpielfeld();

	/**
	 * Wartet auf eine Eingabe.
	 */
	public void warteAufEingabe();

	/**
	 * Schliesst das Objekt.
	 *
	 * Nach einem Aufruf dieser Methode ist das verhalten anderer Methoden des Objekts undefiniert.
	 */
	public void beenden();

}
