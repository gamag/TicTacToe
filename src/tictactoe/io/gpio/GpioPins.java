package tictactoe.io.gpio;

/**
 * @author Tim
 * @since 11.11.13 22:52
 */
public enum GpioPins {
    /**
     * Pin 1
     * 3.3V - Spannung für Geräte
     */
    PIN_1(false, -1, 1),
    /**
     * Pin 2
     * +5v - Spannung für Geräte
     */
    PIN_2(false, -1, 2),
    /**
     * Pin 3
     * GPIO
     * SDA / I2C pull-up
     */
    GPIO_2(true, 2, 3),
    /**
     * Pin 4
     * +5v - Spannung für Geräte
     */
    PIN_4(false, -1, 4),
    /**
     * Pin 5
     * GPIO
     * SCL / I2C pull-up
     */
    GPIO_3(true, 3, 5),
    /**
     * Pin 6
     * Ground
     */
    PIN_6(false, -1, 6),
    /**
     * Pin 7
     * GPIO
     */
    GPIO_4(true, 4, 7),
    /**
     * Pin 8
     * GPIO / TXD
     * UART
     */
    GPIO_14(true, 14, 8),
    /**
     * Pin 9
     * Ground
     */
    PIN_9(false, -1, 9),
    /**
     * Pin 10
     * GPIO / TXD
     * UART
     */
    GPIO_15(true, 15, 10),
    /**
     * Pin 11
     * GPIO
     * UART-RTS
     */
    GPIO_17(true, 17, 11),
    /**
     * Pin 12
     * GPIO
     * PWM
     */
    GPIO_18(true, 18, 12),
    /**
     * Pin 13
     * GPIO
     */
    GPIO_27(true, 27, 13),
    /**
     * Pin 14
     * Ground
     */
    PIN_14(false, -1, 14),
    /**
     * Pin 15
     * GPIO
     */
    GPIO_22(true, 22, 15),
    /**
     * Pin 16
     * GPIO
     */
    GPIO_23(true, 23, 16),
    /**
     * Pin 17
     * 3.3V - Spannung für Geräte
     */
    PIN_17(false, -1, 17),
    /**
     * Pin 18
     * GPIO
     */
    GPIO_24(true, 24, 18),
    /**
     * Pin 19
     * GPIO
     * SPI / MOSI
     */
    GPIO_10(true, 10, 19),
    /**
     * Pin 20
     * Ground
     */
    PIN_20(false, -1, 20),
    /**
     * Pin 21
     * GPIO
     * SPI / MISO
     */
    GPIO_9(true, 9, 21),
    /**
     * Pin 22
     * GPIO
     */
    GPIO_25(true, 25, 22),
    /**
     * Pin 23
     * GPIO
     * SPI / CLK
     */
    GPIO_11(true, 11, 23),
    /**
     * Pin 24
     * GPIO
     * SPI / CE0
     */
    GPIO_8(true, 8, 24),
    /**
     * Pin 25
     * Ground
     */
    PIN_25(false, -1, 25),
    /**
     * Pin 26
     * GPIO
     * SPI / CE1
     */
    GPIO_7(true, 7, 26);

    private boolean isGPIO;
    private int gpioNr;
	private int pinNr;

    private GpioPins(boolean isGPIO, int gpioNr, int pinNr) {
        this.isGPIO = isGPIO;
        if (!isGPIO) {
            this.gpioNr = -1;
        } else {
            this.gpioNr = gpioNr;
        }
    }


    public int getGpioNr() {
        return gpioNr;
    }

    public boolean isGPIO() {
        return isGPIO;
    }
	
	public int getPinNr() {
		return pinNr;
	}
}
