package gpio;

/**
 * @author Tim
 * @since 13.11.13 19:31
 */
public enum GpioMode {
    INPUT("in"),
    OUTPUT("out");

    private String value;

    private GpioMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
