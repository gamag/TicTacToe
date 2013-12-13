package tictactoe.io.spieler;

import tictactoe.io.gpio.GpioIOTreiber;
/**
 * Spieler Klasse, die die MockGpio-Ausgabe verwendet.
 * Nur zum Testen geeignet.
 * ACHTUNG: Diese Klasse darf nicht mit GpioSpieler als Gegenspieler
 * verwendet werden. Ansonsten entsteht eine grosses ducheinander mit den
 * io-Objkten.
 * Genaugenommen wird sie sich wie ein GpioSpieler Spieler verhalten, wenn
 * er als Gegenspieler zuerst instanziiert wird, und umgekehrt.
 */
public class MockGpioSpieler extends GpioSpieler {
	/**
	 * Konstuktor.
	 * Wenn wir hier den Mock-Treiber laden, merkt der GpioSpieler nichts davon,
	 * egal ob er Superklasse oder Gegenspieler ist.
	 */
	public MockGpioSpieler() {
		if (GpioSpieler.referenzen <= 0) {
			GpioSpieler.io = new GpioIOTreiber();
			GpioSpieler.io.run();
		}
		GpioSpieler.referenzen++;
	}
}
