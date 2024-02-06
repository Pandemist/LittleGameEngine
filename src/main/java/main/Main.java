package main;

import helper.Debugger;

import javax.swing.*;

public class Main {

	public static Debugger debugger = new Debugger();

	public static void main(String[] args) {
		for (String arg : args) {
			if (arg.startsWith("--DEBUG_MODE")) {
				debugger.setDebugMode(true);
				break;
			}
		}

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Pixel Game");

		GameController gameController = new GameController();
		debugger.setGp(gameController);
		window.add(gameController);

		window.pack();

		window.setLocationRelativeTo(null);
		window.setVisible(true);

		gameController.prepareGame();
		gameController.startGameThread();
	}
}
