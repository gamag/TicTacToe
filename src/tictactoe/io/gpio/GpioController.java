package tictactoe.io.gpio;

import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tim
 * @since 11.11.13 22:45
 */
public class GpioController {

	/**
	 * Der Pfad für die Datei, welche die zu öffnenden pins entgegennimmt.
	 */
	public static final String GPIO_EXPORT_FILE = "/sys/class/gpio/export";
	/**
	 * Der Pfad für die Datei, welche den Modus für den pins entgegennimmt.
	 */
	public static final String GPIO_DIRECTION_FILE = "/sys/class/gpio/gpio${nr}/direction";
	/**
	 * Der Pfad für die Datei welche den Wert des pinns bestimmt: an / aus
	 */
	public static final String GPIO_VALUE_FILE = "/sys/class/gpio/gpio${nr}/value";
	/**
	 * Der Pfad über den an und aus für pins vertauscht werden kann.
	 */
	public static final String GPIO_ACTIVE_LOW_FILE = "/sys/class/gpio/gpio${nr}/active_low";
	/**
	 * Der Pfad für die Datei welche pins aufgeräumt werden
	 */
	public static final String GPIO_UNEXPORT_FILE = "/sys/class/gpio/unexport";

	/**
	 * Value-Files für input Pins.
	 */
	private RandomAccessFile[] gpioValueIn;

	/**
	 * Lesebuffer.
	 */
	private byte[] buffer;

	/**
	 * Value-Files für Ausgabepins.
	 */
	public FileWriter[] gpioValueOut;

	/**
	 * Liste der exportierten pins, zum automatischen aufräumen, und zum werfen von
	 * Ausnahmen bei Lese oder Schreibzugriff auf ungeöffnete pins.
	 * Speichert lese / schreibzugriff.
	 */
	private volatile GpioPins[] exportiertePins;

	public GpioController() {
		gpioValueIn = new RandomAccessFile[27];  // das ist zwar zuviel, aber man spart sich so einige rechnerei.
		gpioValueOut = new FileWriter[27]; // und da wir Java programmieren, sagen wir ja, dass uns Speicher egal ist.

		exportiertePins = new GpioPins[27];
		for (int i = 0; i < 27; i++) {
			exportiertePins[i] = null;
		}

		buffer = new byte[5];
	}

	/**
	 * Öffnet einen Pin, muss einmalig vor der ersten Benützung dieses Pins erfolgen
	 * @param pin der zu öffnende Pin
	 */
	public void oeffnePin(GpioPins pin) {
		if (vertifizierePin(pin)) {
			throw new IllegalStateException(pin + " is already opened");
		}
		writeToFile(GPIO_EXPORT_FILE, String.valueOf(pin.getGpioNr()));
		exportiertePins[pin.getPinNr()] = pin;
		setPinModus(pin, GpioMode.OUTPUT);
	}

	/**
	 * Aktiviert oder deaktiviert ein GPIO Pin
	 *
	 * @param pin  der Pin, muss ein GPIO Pin sein
	 * @param wert true - pin wird aktiviert, false - pin wird deaktiviert
	 */
	public void setPin(GpioPins pin, boolean wert) {
		if (!vertifizierePin(pin)) {
			throw new IllegalStateException(pin + " has not been opened yet! hint: call oeffnePin() first");
		}
		try {
			gpioValueOut[pin.getPinNr()].write(wert ? "1" : "0");
			gpioValueOut[pin.getPinNr()].flush();
		} catch (IOException e) {
		}
	}

	/**
	 * Gibt den status eines passiven GPIO Pinns zurück
	 *
	 * @param pin der Pin, muss ein GPIO Pin sein
	 * @return den Status des Pinns
	 */
	public boolean getEnabled(GpioPins pin) {
		if (!vertifizierePin(pin)) {
			throw new IllegalStateException(pin + " has not been opened yet! hint: call oeffnePin() first");
		}
		try {
			gpioValueIn[pin.getPinNr()].seek(0);
			gpioValueIn[pin.getPinNr()].read(buffer);
			String val = new String(buffer);
			if (val != null) {
				return val.startsWith("1");
			}
		} catch (IOException e) {
		}
		return false;
	}

	/**
	 * Ein GPIO Pin kann nur entweder input oder output sein.
	 * Diese Funktion bestimmt den Modus
	 *
	 * @param modus der Modus der vom Pin angenommen werden soll
	 */
	public void setPinModus(GpioPins pin, GpioMode modus) {
		if (!vertifizierePin(pin)) {
			throw new IllegalStateException(pin + " has not been opened yet! hint: call oeffnePin() first");
		}
		writeToFile(GPIO_DIRECTION_FILE.replace("${nr}", String.valueOf(pin.getGpioNr())), modus.getValue());
		try {
			if (modus == GpioMode.INPUT) {
				gpioValueOut[pin.getPinNr()] = null;
				gpioValueIn[pin.getPinNr()] = new RandomAccessFile(GPIO_VALUE_FILE.replace("${nr}", String.valueOf(pin.getGpioNr())), "r");
			} else {
				gpioValueOut[pin.getPinNr()] = new FileWriter(GPIO_VALUE_FILE.replace("${nr}", String.valueOf(pin.getGpioNr())));
				gpioValueIn[pin.getPinNr()] = null;
			}
		} catch (IOException e) {
			allesAufraumen();
			throw new IllegalStateException(pin + " can not be opened. We can't continue like this.");
		}

		exportiertePins[pin.getPinNr()] = pin;
	}

	/**
	 * Stellt ein, ob der angegebene Pin eigentlich an sein soll, wenn er auf aus gesetzt ist und ungekehrt.
	 *
	 * @param pin Der zu ändernde Pin
	 * @param an Ob die Funktion aktiviert werden soll
	 * @throws IllegalStateException wenn der Pin  nicht geöffnet wurde
	 * @throws IllegalArgumentException wenn der Pin kein GPIO Pin ist
	 */
	public void setActiveLow(GpioPins pin, boolean an) {
		if (!vertifizierePin(pin)) {
			throw new IllegalStateException(pin + " has not been opened yet.");
		}
		writeToFile(GPIO_ACTIVE_LOW_FILE.replace("${nr}", String.valueOf(pin.getGpioNr())), an ? "1" : "0");
	}

	/**
	 * Räumt alle offenen Pins auf
	 * @see tictactoe.io.gpio.GpioController#aufraumen(GpioPins)
	 */
	public void allesAufraumen() {
		for (int i = 0; i < 27; i++) {
			if (exportiertePins[i] != null) {
				aufraumen(exportiertePins[i]);
			}
		}
	}

	/**
	 * Löscht die betriebsystem kopplung an den GPIO Port, sollte für jeden GPIO Pin durchgeführt werden
	 * Um alles aufzuräument kann auch {@link gpio.GpioController#allesAufraumen()} verwendet werden.
	 * aufgerufen werden.
	 * @param pin Pin zum aufräumen
	 */
	public void aufraumen(GpioPins pin) {
		try {
			if (gpioValueOut[pin.getPinNr()] != null) {
				gpioValueOut[pin.getPinNr()].close();
			}
			if (gpioValueIn[pin.getPinNr()] != null) {
				gpioValueIn[pin.getPinNr()].close();
			}
		} catch (IOException e) {
		}
		writeToFile(GPIO_UNEXPORT_FILE, String.valueOf(pin.getGpioNr()));
		exportiertePins[pin.getPinNr()] = null;
	}

	/**
	 * Überprüft ob der gegebene Pin wirklich ein GPIO Pin ist, und gibt zurück ob der Pin registriert wurde.
	 *
	 * @param pin Der zu prüfende Pin
	 * @return true wenn der Pin schon geöffnet wurde
	 * @throws IllegalArgumentException wenn der Pin kein GPIO Pin ist
	 */
	private boolean vertifizierePin(GpioPins pin) {
		if (!pin.isGPIO()) {
			throw new IllegalArgumentException(pin + " is not a legal gpio pin!");
		}
		return exportiertePins[pin.getPinNr()] != null;
	}

	/**
	 * Schreibt ein String in eine Datei
	 *
	 * @param file  die Datei zum reinschreiben
	 * @param value der String zum reinschreiben
	 */
	private void writeToFile(String file, String value) {
		try {
			FileWriter w = new FileWriter(file);
			w.write(value);
			w.flush();
			w.close();
		} catch (IOException e) {
			throw new RuntimeException("Somehow can't write to file " + file, e);
		}
	}
}
