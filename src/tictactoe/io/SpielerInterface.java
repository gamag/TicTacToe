package tictactoe.io;

/**
 * Ein Spieler-Objekt steuert die Ein- und Ausgabe auf dem Medium, an dem der Spieler spielt.
 *
 * Auf diese weise sind Spiele zwischen Spielern am Computer und am Spielbrett, wie auch Spiele
 * gegen KIs möglich.
 */
public interface SpielerInterface {
	/**
	 * Lässt den Spieler einen Zug machen.
	 *
	 * @return Die Feldnummer.
	 * @throws IllegalStateException wenn das Spielfeld voll ist.
	 */
	public int getSpielzug();

	/**
	 * Stetzt das gegebene Feld auf die gegebene Spielernummer.
	 *
	 * @param feld Feldnummer.
	 * @param spieler Spielernummer.
	 */
	public void setFeld(int feld, int spieler);
}
