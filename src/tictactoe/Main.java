package tictactoe;

import tictactoe.Controller;
import tictactoe.io.spieler.*;
import java.io.*;

/**
 * Tic Tac Toe Spiel.
 */
public class Main {
	public static void main(String[] args) {
		SpielerInterface spieler = null;
		if (args.length > 0) {
			if (args[0].equals("--help") || args[0].equals("-h")) {
				System.out.println("GPIO Tictactoe Steuerungsprogramm.");
				System.out.println("Folgenden Komandozeilenargumente werden unterstützt:");
				System.out.println("--help, -h\tZeigt diese Hilfe an.");
				System.out.println("--text, -t\tStartet das Programm mit der Textkonsole als Master Spieler.");
				return;
			}
			if (args[0].equals("--text") || args[0].equals("-t")) {
				spieler = new KonsolenSpieler();
			} else {
				System.out.println("Ungültige Komandozeilenargumente, --help für Hilfe");
				return;
			}
		}
		if (spieler == null) {
			spieler = new GpioSpieler();
		}
		Controller spiel = new Controller(spieler);
		spiel.run();
		return;
	}
}
