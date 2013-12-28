package tictactoe.io.gpio;

import java.util.concurrent.TimeUnit;

/**
 * GPIO Ein-/Ausgabe für das Tic Tac Toe Spiel.
 */
public class GpioIOTreiber extends Thread {

	/**
	 * Gpio Steuerungsobjekt.
	 */
	private GpioController gpioSteuerung;

	/**
	 * Pin des Knopf 1
	 */
	private static final GpioPins KNOPF_1 = GpioPins.GPIO_27; // pin 13

	/**
	 * Pin des Knopf 2
	 */
	private static final GpioPins KNOPF_2 = GpioPins.GPIO_22; // pin 15

	/**
	 * Thread, der die realen LEDs steuert.
	 */
	private GpioMatrixThread ledThread;

	/**
	 * Spielfeld - aus, an, blinken langsam, blinken schnell
	 */
	private int feld[] = new int[12];

	/**
	 * Konstruktor, initialisiere das Spielfeld.
	 */
	public GpioIOTreiber() {
		for (int i = 0; i < 12; i++) {
			feld[i] = 0;
		}

		gpioSteuerung = new GpioController();
		gpioSteuerung.oeffnePin(KNOPF_1);
		gpioSteuerung.setPinModus(KNOPF_1, GpioMode.INPUT);
		gpioSteuerung.oeffnePin(KNOPF_2);
		gpioSteuerung.setPinModus(KNOPF_2, GpioMode.INPUT);

		schnellBlinken = -1;
		schellBlinkenAn = false;
		langsamBlinken = -1;
		langsamBlinkenAn = false;

		ledThread = new GpioMatrixThread();
		ledThread.start();

		gedrueckt1 = false;
		gedrueckt2 = false;
		neuGedrueckt1 = false;
		neuGedrueckt2 = false;
		rundenGerdrueckt1 = 0;
		rundenGerdrueckt2 = 0;
	}

	/**
	 * Setzt ein Feld auf einen Zustand.
	 * @param feldnummer die Nummer des zu setzenden Feldes (1-10)
	 * @param zustand der Zustand, auf den das Feld gesetzt werden soll (0 = Aus, 1 = leuchten, 2 = Langsam Blinken, 3 = schnell Blinken)
	 */
	public void setFeld(int feldnummer, int zustand) {
		if (feldnummer > 11 || feldnummer < 1) {
			throw new IllegalArgumentException("Feldnummer ungültig.");
		}
		if (zustand < 0 || zustand > 3) {
			throw new IllegalArgumentException("Zustand ungültig.");
		}

		feld[feldnummer-1] = zustand;
	}

	/**
	 * Testet ob ein Knopf gedrückt ist.
	 * @param knopf die Knopfnummer (1 oder 2)
	 * @return true wenn der Knopf gedrückt ist.
	 */
	public boolean istGedrueckt(int knopf) {
		return (knopf == 1) ? gedrueckt1 : gedrueckt2;
	}

	/**
	 * Testet, ob der Knopf seit dem letzten Aufruf dieser Methode gedrückt wurde.
	 * @param knopf die Knopfnummer (1 oder 2)
	 * @return true, wenn der Knopf neu gedrückt ist.
	 */
	public boolean istNeuGedrueckt(int knopf) {
		if (knopf == 1 && neuGedrueckt1) {
			neuGedrueckt1 = false;
			return true;
		} else if (knopf == 2 && neuGedrueckt2) {
			neuGedrueckt2 = false;
			return true;
		}
		return false;
	}

	/**
	 * True, wenn Knopf1 gedrückt ist.
	 */
	boolean gedrueckt1;

	/**
	 * True, wenn Knopf1 in der letzten Runde gedrückt wurde. (keydown event)
	 */
	boolean neuGedrueckt1;

	/**
	 * Wie lange ist Knopf1 schon gedrückt?
	 * Erst bei drei wird auch gedrueckt1 und neuGedruekt1 gesetzt.
	 */
	int rundenGerdrueckt1;

	/**
	 * True, wenn Knopf2 gedrückt ist.
	 */
	boolean gedrueckt2;

	/**
	 * True, wenn Knopf2 in der letzten Runde gedrückt wurde. (keydown event)
	 */
	boolean neuGedrueckt2;

	/**
	 * Wie lange ist Knopf2 schon gedrückt?
	 * Erst bei drei wird auch gedrueckt2 und neuGedruekt2 gesetzt.
	 */
	int rundenGerdrueckt2;

	private void updateInput() {
		if (gpioSteuerung.getEnabled(KNOPF_1)) {
			rundenGerdrueckt1++;
			if (rundenGerdrueckt1 >= 3) {
				if (!gedrueckt1) {
					neuGedrueckt1 = true;
				}
				gedrueckt1 = true;
			}
		} else {
			rundenGerdrueckt1 = 0;
			gedrueckt1 = false;
		}

		if (gpioSteuerung.getEnabled(KNOPF_2)) {
			rundenGerdrueckt2++;
			if (rundenGerdrueckt2 >= 3) {
				if (!gedrueckt2) {
					neuGedrueckt2 = true;
				}
				gedrueckt2 = true;
			}
		} else {
			rundenGerdrueckt2 = 0;
			gedrueckt2 = false;
		}
	}

	/**
	 * "Rundenzähler" für schnelles Blinken.
	 */
	private int schnellBlinken;

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
	 * True wenn beendet werden soll.
	 */
	private volatile boolean sollBeenden;


	/**
	 * Startet die Ausgabe. 
	 * Zum starten des Thread bitte die start() Methode benutzen.
	 */
	public void run() {
		sollBeenden = false;
		while (!sollBeenden) {
			try {
				TimeUnit.MILLISECONDS.sleep(40);
			} catch (InterruptedException e) {
			}

			schnellBlinken++;
			langsamBlinken++;
			schnellBlinken %= 3;
			langsamBlinken %= 20;

			updateInput();
			

			if (schnellBlinken != 0 && langsamBlinken != 0) {
				continue; // Wir brauchen nicht so oft zu aktuallisieren.
			}

			// Bei 0 LEDs umschalten:
			schellBlinkenAn = ((schnellBlinken == 0) ? !schellBlinkenAn : schellBlinkenAn);
			langsamBlinkenAn = ((langsamBlinken == 0) ? !langsamBlinkenAn : langsamBlinkenAn);

			for (int i = 0; i < 12; i++) {
				switch (feld[i]) {
					case 0:
						ledThread.setLed(i, false);
						break;
					case 1:
						ledThread.setLed(i, true);
						break;
					case 2:
						ledThread.setLed(i, langsamBlinkenAn);
						break;
					case 3:
						ledThread.setLed(i, schellBlinkenAn);
						break;
					default:
						ledThread.setLed(i, false);
				}
			}
		}
		
		gpioSteuerung.allesAufraumen();
		ledThread.beenden(); 
	}

	/**
	 * Lässt den Thread so schnell als möglich beenden.
	 * Wartet, bis er beendet ist.
	 */
	public void beenden() {
		sollBeenden = true;
		try {
			this.join();
		} catch (InterruptedException e) {
		} 
	}
}
