package tictactoe.io.gpio;

public class GpioTest {
    private final GpioController controller;

    public GpioTest() {
        controller = new GpioController();
    }


    public static void main(String[] args) {
	try {
            GpioTest test = new GpioTest();
            System.out.println("Starting test now:");
            test.run();
        } catch(Throwable t) {
		System.out.println("Test failed: ");
		t.printStackTrace();
        }
    }

    public void run() {
        blink(GpioPins.GPIO_2);
        blink(GpioPins.GPIO_3);
    }

    public void blink(GpioPins pin) {
	controller.oeffnePin(pin);
        boolean stat = true;
        System.out.println("Blinke mit " + pin + " auf pin " + pin.getGpioNr());
        for(int i = 0; i < 500; i++) {
            controller.setPin(pin,stat);
            stat = !stat;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
               // This will never happen
            }
        }
        controller.aufraumen(pin);
	System.out.println("Kein Fehler, hat gpio geblinkt?");
    }


}
