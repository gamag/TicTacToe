package gpio;

/**
 * TODO: Finde bedeutung der Abkürzungen
 * Quelle: http://developer-blog.net/wp-content/uploads/2013/09/raspberry-pi-rev2-gpio-pinout.jpg
 * Quelle: http://developer-blog.net/hardware/raspberry-pi-gpio-schnittstelle-teil-1/
 * @author Tim
 * @since 11.11.13 22:52
 */
public enum GpioPins {
    /**
     * 3.3V - Spannung für Geräte
     */
    PIN_1,
    /**
     * +5v - Spannung für Geräte
     */
    PIN_2,
    /**
     * GPIO / SDA
     * I2C pull-up
     */
    PIN_3,
    /**
     * +5v - Spannung für Geräte
     */
    PIN_4,
    /**
     * GPIO / SCL
     * I2C pull-up
     */
    PIN_5,
    /**
     * Ground
     */
    PIN_6,
    /**
     * GPIO
     */
    PIN_7,
    /**
     * GPIO / TXD
     * UART
     */
    PIN_8,
    /**
     * Ground
     */
    PIN_9,
    /**
     * GPIO / TXD
     * UART
     */
    PIN_10,
    /**
     * GPIO
     * UART-RTS
     */
    PIN_11,
    /**
     * GPIO
     * PWM
     */
    PIN_12,
    /**
     * GPIO
     */
    PIN_13,
    /**
     * Ground
     */
    PIN_14,
    /**
     * GPIO
     */
    PIN_15,
    /**
     * GPIO
     */
    PIN_16,
    /**
     * 3.3V - Spannung für Geräte
     */
    PIN_17,
    /**
     * GPIO
     */
    PIN_18,
    /**
     * GPIO
     * SPI / MOSI
     */
    PIN_19,
    /**
     * Ground
     */
    PIN_20,
    /**
     * GPIO
     * SPI / MISO
     */
    PIN_21,
    /**
     * GPIO
     */
    PIN_22,
    /**
     * GPIO
     * SPI / CLK
     */
    PIN_23,
    /**
     * GPIO
     * SPI / CE0
     */
    PIN_24,
    /**
     * Ground
     */
    PIN_25,
    /**
     * GPIO
     * SPI / CE1
     */
    PIN_26,
}
