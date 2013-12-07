package tictactoe.io.gpio;

import tictactoe.io.*;

/**
 * GPIO Ein-/Ausgabe für das Tic Tac Toe Spiel.
 */
public class GpioIOTreiber implements IOInterface {

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

		schnellBinken = -1;
		schellBlinkenAn = false;
		langsamBlinken = -1;
		langsamBlinkenAn = false;

		ledThread = new GpioMatrixThread();
		ledThread.run();
	}

	/**
	 * @see tictactoe.io.IOInterface
	 */
	public void setFeld(int feldnummer, int zustand) {
		if (feldnummer > 12 || feldnummer < 1) {
			throw new IllegalArgumentException("Feldnummer ungültig.");
		}
		if (zustand < 0 || zustand > 3) {
			throw new IllegalArgumentException("Zustand ungültig.");
		}

		feld[feldnummer-1] = zustand;
	}

	/**
	 * @see tictactoe.io.IOInterface
	 */
	public boolean istGedrueckt(int knopf) {
		return (knopf == 1) ? gpioSteuerung.getEnabled(KNOPF_1) : gpioSteuerung.getEnabled(KNOPF_2);
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
	 * @see tictactoe.io.IOInterface
	 */
	public void update() {
		schnellBinken++;
		langsamBlinken++;
		schnellBinken %= 5;
		langsamBlinken %= 20;

		if (schnellBinken != 0 && langsamBlinken != 0) {
			return; // Wir brauchen nicht so oft zu aktuallisieren.
		}

		// Bei 0 LEDs umschalten:
		schellBlinkenAn = ((schnellBinken == 0) ? !schellBlinkenAn : schellBlinkenAn);
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

	/**
	 * @see tictactoe.io.IOInterface
	 */
	public void beenden() {
		gpioSteuerung.allesAufraumen();
		ledThread.beenden();
	}
}
