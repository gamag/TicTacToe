package tictactoe;

import tictactoe.Controller;
import tictactoe.io.spieler.*;

/**
 * Tic Tac Toe Spiel.
 */
public class Main {
	public static void main(String[] args) {
		Controller spiel = new Controller(new GpioSpieler());
		spiel.run();
	}
}
