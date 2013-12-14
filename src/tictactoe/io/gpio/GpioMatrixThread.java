package tictactoe.io.gpio;

import java.util.concurrent.TimeUnit;

/**
 * Thread, der die konkrete LED-Ausgabe durch schelles Blinken handhabt.
 *
 * Wir haben folgende Matrix (LED-Index und pin Name):
 *
 *	                  1 - wenn eine LED leuchten soll
 *                  gpio18  gpio23  gpio24      gpio25
 *
 * 0 - wenn   gpio4    0      1       2
 * eine LED
 * leuchten   gpio17   3      4       5
 * soll.
 *            gpio14   6      7       8           9
 *
 * Knopf 1 hängt an +3.3V und gpio27
 * Knopf 2 hängt an +3.3V und gpio22
 *
 */
public class GpioMatrixThread extends Thread {

	public GpioMatrixThread() {
		super();

		for (int i = 0; i < 12; i++) {
			feld[i] = false;
		}
	}

	/**
	 * True, wenn bei nächster Gelegenheit beendet werden soll.
	 *
	 * @see tictactoe.io.gpio.GpioMatrixThread#beenden()
	 */
	private volatile boolean beenden = false;

	/**
	 * Der GPIO Controller.
	 * Da die GpioController nicht sicher Threadsave ist, haben wir unsere eigene
	 * Instanz.
	 */
	private GpioController gpio;

	/**
	 * Der Soll-Zustand der einzelnen Felder.
	 *
	 * Wir arbeiten (noch) wie das IOInterface mit int.
	 */
	private volatile boolean feld[] = new boolean[12];

	/**
	 * Die zu ändernden Ausgabepins, wenn auf ein Feld geschrieben werden soll.
	 */
	private final GpioPins pins[][] = {
		{GpioPins.GPIO_4, GpioPins.GPIO_18},
		{GpioPins.GPIO_4, GpioPins.GPIO_23},
		{GpioPins.GPIO_4, GpioPins.GPIO_24},
		{GpioPins.GPIO_17, GpioPins.GPIO_18},
		{GpioPins.GPIO_17, GpioPins.GPIO_23},
		{GpioPins.GPIO_17, GpioPins.GPIO_24},
		{GpioPins.GPIO_14, GpioPins.GPIO_18},
		{GpioPins.GPIO_14, GpioPins.GPIO_23},
		{GpioPins.GPIO_14, GpioPins.GPIO_24},
		{GpioPins.GPIO_14, GpioPins.GPIO_25}
	};

	/**
	 * Wie lange eine LED braucht bis sie aus ist (ms).
	 *
	 * Wie Lange ein Pin braucht bis er von 3.3 V auf 0 V kommt.
	 */
	private static final int LED_GEHT_AUS = 100;

	/**
	 * Wie lange eine LED leuchten darf (ms).
	 *
	 * Inklusive der Zeit, die der Pin von 0 V bis 3.3 V braucht.
	 */
	private static final int LED_LEUCHTET = 600;

	/**
	 * Lässt den Thread laufen.
	 */
	public void run() {
		initGpio();

		try {
			beenden = false;
			int i = 0;
			while (!beenden) {
				if (feld[i]) {
					gpio.setPin(pins[i][0], true);
					gpio.setPin(pins[i][1], true);

					TimeUnit.NANOSECONDS.sleep(LED_LEUCHTET);
				}
				gpio.setPin(pins[i][0], false);
				gpio.setPin(pins[i][1], false);

				TimeUnit.NANOSECONDS.sleep(LED_GEHT_AUS);

				i++;
				i %= 10;
			}
		} catch (InterruptedException e) {
			// handle like beenden == true
		}

		schliesseGpio();
	}

	/**
	 * Initialisiert die Gpio-Ausgabe.
	 */
	private void initGpio() {
		gpio = new GpioController();

		gpio.oeffnePin(GpioPins.GPIO_4);
		gpio.setPinModus(GpioPins.GPIO_4, GpioMode.OUTPUT);
		gpio.setActiveLow(GpioPins.GPIO_4, true);

		gpio.oeffnePin(GpioPins.GPIO_17);
		gpio.setPinModus(GpioPins.GPIO_17, GpioMode.OUTPUT);
		gpio.setActiveLow(GpioPins.GPIO_17, true);

		gpio.oeffnePin(GpioPins.GPIO_14);
		gpio.setPinModus(GpioPins.GPIO_14, GpioMode.OUTPUT);
		gpio.setActiveLow(GpioPins.GPIO_14, true);

		gpio.oeffnePin(GpioPins.GPIO_18);
		gpio.setPinModus(GpioPins.GPIO_18, GpioMode.OUTPUT);
		gpio.setActiveLow(GpioPins.GPIO_18, false);

		gpio.oeffnePin(GpioPins.GPIO_23);
		gpio.setPinModus(GpioPins.GPIO_23, GpioMode.OUTPUT);
		gpio.setActiveLow(GpioPins.GPIO_23, false);

		gpio.oeffnePin(GpioPins.GPIO_24);
		gpio.setPinModus(GpioPins.GPIO_24, GpioMode.OUTPUT);
		gpio.setActiveLow(GpioPins.GPIO_24, false);

		gpio.oeffnePin(GpioPins.GPIO_25);
		gpio.setPinModus(GpioPins.GPIO_25, GpioMode.OUTPUT);
		gpio.setActiveLow(GpioPins.GPIO_25, false);
	}

	/**
	 * Schliesst die Gpio-Ausgabe.
	 */
	private void schliesseGpio() {
		gpio.allesAufraumen();
	}

	/**
	 * Lässt den Thread bei nächster Gelegenheit beenden.
	 *
	 * Wartet bis der Thread steht.
	 */
	public void beenden() {
		beenden = true;
		try {
		this.join();
		} catch (InterruptedException e) {
			throw new UnsupportedOperationException("Ok, ok, we are terminateing, but please, don't do this again!", e);
		}
	}

	/**
	 * Setzt ein LED auf einen bestimmten Wert.
	 *
	 * @param ledIndex LED-Index (Nummer - 1)
	 * @param an Ob das LED leuchten soll
	 */
	public void setLed(int ledIndex, boolean an) {
		feld[ledIndex] = an;
	}
}
