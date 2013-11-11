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
     * Pin 1
     * 3.3V - Spannung für Geräte
     */
    PIN_1,
    /**
     * Pin 2
     * +5v - Spannung für Geräte
     */
    PIN_2,
    /**
     * Pin 3
     * GPIO
     * SDA / I2C pull-up
     */
    GPIO_2,
    /**
     * Pin 4
     * +5v - Spannung für Geräte
     */
    PIN_4,
    /**
     * Pin 5
     * GPIO
     * SCL / I2C pull-up
     */
    GPIO_3,
    /**
     * Pin 6
     * Ground
     */
    PIN_6,
    /**
     * Pin 7
     * GPIO
     */
    GPIO_4,
    /**
     * Pin 8
     * GPIO / TXD
     * UART
     */
    GPIO_14,
    /**
     * Pin 9
     * Ground
     */
    PIN_9,
    /**
     * Pin 10
     * GPIO / TXD
     * UART
     */
    GPIO_15,
    /**
     * Pin 11
     * GPIO
     * UART-RTS
     */
    GPIO_17,
    /**
     * Pin 12
     * GPIO
     * PWM
     */
    GPIO_18,
    /**
     * Pin 13
     * GPIO
     */
    GPIO_27,
    /**
     * Pin 14
     * Ground
     */
    PIN_14,
    /**
     * Pin 15
     * GPIO
     */
    GPIO_22,
    /**
     * Pin 16
     * GPIO
     */
    GPIO_23,
    /**
     * Pin 17
     * 3.3V - Spannung für Geräte
     */
    PIN_17,
    /**
     * Pin 18
     * GPIO
     */
    GPIO_24,
    /**
     * Pin 19
     * GPIO
     * SPI / MOSI
     */
    GPIO_10,
    /**
     * Pin 20
     * Ground
     */
    PIN_20,
    /**
     * Pin 21
     * GPIO
     * SPI / MISO
     */
    GPIO_9,
    /**
     * Pin 22
     * GPIO
     */
    GPIO_25,
    /**
     * Pin 23
     * GPIO
     * SPI / CLK
     */
    GPIO_11,
    /**
     * Pin 24
     * GPIO
     * SPI / CE0
     */
    GPIO_8,
    /**
     * Pin 25
     * Ground
     */
    PIN_25,
    /**
     * Pin 26
     * GPIO
     * SPI / CE1
     */
    GPIO_7,
}
