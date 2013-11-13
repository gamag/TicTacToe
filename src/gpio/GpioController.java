package gpio;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim
 * @since 11.11.13 22:45
 */
public class GpioController {

    //TODO: Pfade bestätigen
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
     * Der Pfad für die Datei welche pins aufgeräumt werden
     */
    public static final String GPIO_UNEXPORT_FILE = "/sys/class/gpio/unexport";

    /**
     * Liste der exportierten pins, zum automatischen aufräumen, und zum werfen von
     * Ausnahmen bei Lese oder Schreibzugriff auf ungeöffnete pins.
     */
    private final List<GpioPins> exportiertePins;

    public GpioController() {
        exportiertePins = new ArrayList<GpioPins>();
    }

    public void oeffnePin(GpioPins pin) {
        if (vertifizierePin(pin)) {
            throw new IllegalStateException(pin + " is already opened");
        }
        //TODO: Öffne pin :)
        exportiertePins.add(pin);
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

    }

    /**
     * Überprüft ob der gegebene Pin wirklich ein GPIO Pin ist, und gibt zurück ob der Pin registriert wurde.
     * @param pin Der zu prüfende Pin
     * @return true wenn der Pin schon geöffnet wurde
     * @throws IllegalArgumentException wenn der Pin kein GPIO Pin ist
     */
    private boolean vertifizierePin(GpioPins pin) {
        if (!pin.isGPIO()) {
            throw new IllegalArgumentException(pin + " is not a legal gpio pin!");
        }
        return exportiertePins.contains(pin);
    }

}
