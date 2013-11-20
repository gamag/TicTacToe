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
	 * @return Die Feldnummer.
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
	 * @param spieler der Gewinner.
	 */
	public void setGewinner(int spieler);

	/**
	 * Läst den Spieler eine Auswahl aus einer Liste treffen.
	 *
	 * @param liste die Liste, aus der gewählt wird.
	 * @return den gewählten Index.
	 */
	public int wähle(String [] liste);

	/**
	 * Räumt das Spielfeld ab.
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
