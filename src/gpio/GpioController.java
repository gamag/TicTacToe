package gpio;

/**
 * @author Tim
 * @since 11.11.13 22:45
 */
public class GpioController {

    /**
     * Aktiviert oder deaktiviert ein GPIO Pin
     * @param pin der Pin, muss ein GPIO Pin sein
     * @param wert true - pin wird aktiviert, false - pin wird deaktiviert
     */
    public void setPin(GpioPins pin, boolean wert) {

    }

    /**
     * Gibt den status eines passiven GPIO Pinns zurück
     * @param pin der Pin, muss ein GPIO Pin sein
     * @return den Status des Pinns
     */
    public boolean getEnabled(GpioPins pin) {
        return false;
    }

}
