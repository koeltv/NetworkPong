package com.client;

import javax.swing.*;

/**
 * The graphical Window.
 */
class Window extends JFrame {
	/**
	 * The Panel.
	 */
	final Panel panel = new Panel();

	/**
	 * The Audio.
	 */
	Audio audio = new Audio();

	/**
	 * Instantiates a new Window.
	 */
	Window() {
		super();
		this.setTitle("PONG");
		this.setResizable(false);
		this.setSize(1500, 800);
		this.setLocationRelativeTo(null);
		this.setContentPane(panel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Switch sound state between playing and stopped.
	 */
	public void switchSoundState() { // TODO Replace stop() by interrupt()
		if (audio == null || !audio.isAlive()) {
			audio = new Audio();
			audio.start();
		} else if (!audio.isInterrupted()) {
			audio.stop();
			audio = null;
		}
	}
}