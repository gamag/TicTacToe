package tictactoe.io;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Simuliert das GPIO Spielfeld über Tastatur und Bildschirm.
 *
 * Zum Testen des TicTacToe Spieles wenn die Hardware nicht verfügbar ist. Keine brauchbare Benutzeroberfläche.
 */
public class MockIOTreiber extends Thread implements IOInterface {

	/**
	 * LEDs - theoretisch an oder aus.
	 */
	private boolean leds[] = new boolean[12];

	/**
	 * Spielfeld - aus, an, blinken langsam, blinken schnell
	 */
	private volatile int feld[] = new int[12];

	/**
	 * Konstruktor, initialisiere das Spielfeld.
	 */
	public MockIOTreiber() {
		for (int i = 0; i < 12; i++) {
			leds[i] = false;
			feld[i] = 0;
		}

		schnellBinken = -1;
		schellBlinkenAn = false;
		langsamBlinken = -1;
		langsamBlinkenAn = false;
		gedruekt1 = false;
		gedruekt2 = false;
		stdin = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Wilkommen im TicTacToe Spielbrettsimulator!");
		System.out.println("Zur Steuerung werden die Tasten '1' und '2' verwendet.");
		System.out.println("'1<Enter>' setzt Taste 1 auf gedrückt. Ein erneutes '1<Enter>' \"lässt sie wieder los\".");
		System.out.println("Entsprechendes gilt für Taste 2.");
		System.out.println("Dieses Programm ist zum Testen der I/O Steuerung, nicht der Spiellogik gedacht.");
	}

	/**
	 * Siehe IOInterface
	 */
	public void setFeld(int feldnummer, int zustand) {
		if (feldnummer > 12 || feldnummer < 1) {
			throw new IllegalArgumentException("feldnummer ungültig.");
		}
		if (zustand < 0 || zustand > 3) {
			throw new IllegalArgumentException("zustand ungültig.");
		}

		feld[feldnummer-1] = zustand;
	}

	/**
	 * Eingabebuffer
	 */
	private BufferedReader stdin;

	/**
	 * Eingabe lesen und interpretieren.
	 */
	private void updateInput() {
		try {
			if (System.in.available() > 0) {
				String st = stdin.readLine();
				int l = st.length();
				for (int i = 0; i < l; i++) {
					char c = st.charAt(i);
					if (c == '1') {
						gedruekt1 = !gedruekt1;
						neuGedruekt1 = gedruekt1;
					}
					if (c == '2') {
						gedruekt2 = !gedruekt2;
						neuGedruekt2 = !gedruekt2;
					}
				}
			}
		} catch (IOException e) {
		}
	}

	/**
	 * Knopf 1 gedrückt?
	 */
	private volatile boolean gedruekt1;

	/**
	 * Knopf 2 gedrückt?
	 */
	private volatile boolean gedruekt2;

	/**
	 * Knopf 1 geht gerade runter ?
	 */
	private volatile boolean neuGedruekt1;

	/**
	 * Knopf 2 geht gerade runter ?
	 */
	private volatile boolean neuGedruekt2;

	/**
	 * Testet ob 1 oder 2 eingegeben wurde.
	 */
	public boolean istGedrueckt(int knopf) {
		return (knopf == 1) ? gedruekt1 : gedruekt2;
	}

	/**
	 * Testet ob 1 oder 2 gerade eingegeben wurde.
	 */
	public boolean istNeuGedrueckt(int knopf) {
		boolean ret;
		if (knopf == 1) {
			ret = neuGedruekt1;
			neuGedruekt1 = false;
		} else {
			ret = neuGedruekt2;
			neuGedruekt2 = false;
		}
		return ret;
	}

	/**
	 * Schreibt den aktuellen Spielstand nach stdout.
	 */
	private void updateUI() {
		System.out.print("\r||");

		for (int i = 0; i < 9; i++) {
			System.out.print( (leds[i] ? " #" : " _") + " |");
		}

		System.out.print("| e1: " + (leds[9] ? "#" : "_") + " e2: " + (leds[10] ? "#" : "_") + " e3: " + (leds[11] ? "#" : "_"));
		System.out.print(" b1: " + (gedruekt1 ? "↓" : "^") + " b2: " +(gedruekt2 ? "↓" : "^") + " > ");
	}

	/**
	 * "Rundenzähler" für schnelles Blinken.
	 */
	private int schnellBinken;

	/**
	 * Sind schnelle Blinker gerade an?
	 */
	private boolean schellBlinkenAn;

	/**
	 * "Rundenzähler" für langsames Blinken.
	 */
	private int langsamBlinken;

	/**
	 * Sind langsamen Blinker gerade am leuchten?
	 */
	private boolean langsamBlinkenAn;

	/**
	 * Wenn beendet werden soll.
	 */
	private volatile boolean stop;


	/**
	 * Update - Ausgabe aktualisieren.
	 *
	 * Die Standartausgabe ist zu langsam um immer nur eine LED anzuzeigen, wie es mit der Matrix nötig wird,
	 * daher machen wir hier nur langsames blinken.
	 */
	public void run() {
		stop = false;
		while (!stop) {
			try {
				TimeUnit.MILLISECONDS.sleep(40);
			} catch (InterruptedException e) {
			}

			schnellBinken++;
			langsamBlinken++;
			schnellBinken %= 5;
			langsamBlinken %= 20;

			if (schnellBinken != 0 && langsamBlinken != 0) {
				continue; // Wir brauchen nicht so oft zu aktuallisieren.
			}

			// Bei 0 LEDs umschalten:
			schellBlinkenAn = ((schnellBinken == 0) ? !schellBlinkenAn : schellBlinkenAn);
			langsamBlinkenAn = ((langsamBlinken == 0) ? !langsamBlinkenAn : langsamBlinkenAn);

			for (int i = 0; i < 12; i++) {
				switch (feld[i]) {
					case 0:
						leds[i] = false;
						break;
					case 1:
						leds[i] = true;
						break;
					case 2:
						leds[i] = langsamBlinkenAn;
						break;
					case 3:
						leds[i] = schellBlinkenAn;
						break;
					default:
						leds[i] = false;
				}
			}

			updateInput();
			updateUI();
		}
	}

	/**
	 * Anhalten.
	 */
	public void beenden() {
		stop = true;
	}
}
