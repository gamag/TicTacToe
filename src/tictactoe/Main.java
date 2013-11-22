package tictactoe;

import tictactoe.Controller;
import tictactoe.io.gpio.*;
import tictactoe.io.*;

/**
 * Tic Tac Toe Spiel.
 */
public class Main {
	public static void main(String[] args) {
		Controller spiel = new Controller(new MockIOTreiber());
		spiel.run();
	}
}
