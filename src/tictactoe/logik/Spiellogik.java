package tictactoe.logik;
/**
 * Tic Tac Toe Logik.
 */
public class Spiellogik {

	/**
	 * Unser Spielfeld.
	 * 0 = frei, 1 = Spieler 1, 2 = Spieler 2
	 */
	private int spielfeld[][];

	/**
	 * Konstruktor.
	 *
	 * Initialisiert das Spielfeld.
	 */
	public Spiellogik() {
		spielfeld = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				spielfeld[i][j] = 0;
			}
		}
	}

	/**
	 * Prüft ob das Feld frei ist.
	 *
	 * @param x x-Koordinate des zu prüfenden Feldes.
	 * @param y y-Koordinate des zu prüfenden Feldes.
	 */
	public boolean istGueltig(int x, int y) {
		if (x < 0 || x > 2 || y < 0 || y > 2) {
			return false;
		}
		return (spielfeld[x][y] == 0);
	}

	/**
	 * Prüft ob alle Felder belegt sind oder jemand gewonnen hat.
	 */
	public boolean spielFertig() {
		if (gewinner() == 0) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (spielfeld[i][j] == 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Gibt den Gewinner oder 0 zurück.
	 */
	public int gewinner() {
		if (spielfeld[0][0] == spielfeld[0][1] && spielfeld[0][0] == spielfeld[0][2] && spielfeld[0][0] != 0) {
			return spielfeld[0][0];
		}
		if (spielfeld[0][0] == spielfeld[1][0] && spielfeld[0][0] == spielfeld[2][0] && spielfeld[0][0] != 0) {
			return spielfeld[0][0];
		}
		if (spielfeld[0][0] == spielfeld[1][1] && spielfeld[0][0] == spielfeld[2][2] && spielfeld[0][0] != 0) {
			return spielfeld[0][0];
		}
		if (spielfeld[1][0] == spielfeld[1][1] && spielfeld[1][0] == spielfeld[1][2] && spielfeld[1][0] != 0) {
			return spielfeld[1][0];
		}
		if (spielfeld[2][0] == spielfeld[2][1] && spielfeld[2][0] == spielfeld[2][2] && spielfeld[2][0] != 0) {
			return spielfeld[2][0];
		}
		if (spielfeld[2][0] == spielfeld[1][1] && spielfeld[2][0] == spielfeld[0][2] && spielfeld[2][0] != 0) {
			return spielfeld[2][0];
		}
		if (spielfeld[0][1] == spielfeld[1][1] && spielfeld[0][1] == spielfeld[2][1] && spielfeld[0][1] != 0) {
			return spielfeld[0][1];
		}
		if (spielfeld[0][2] == spielfeld[1][2] && spielfeld[0][2] == spielfeld[2][2] && spielfeld[0][2] != 0) {
			return spielfeld[0][2];
		}
		return 0;
	}

	/**
	 * Macht einen Zug.
	 *
	 * @param x x-Koordinate des zu setzenden Feldes.
	 * @param y y-Koordinate des zu setzenden Feldes.
	 * @param wert die Nummer des Spielers.
	 */
	public void setzeFeld(int x, int y, int wert) {
		if (!istGueltig(x, y)) {
			throw new IllegalArgumentException("Ungültiger Zug: " + x + " " + y);
		}
		spielfeld[x][y] = wert;
	}
}
