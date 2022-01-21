package com.client;

import com.server.Server;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * The graphical Client.
 */
public class Client extends Window implements Runnable {
	/**
	 * The Server address.
	 */
	private final InetAddress serverAddress;

	/**
	 * Instantiates a new Client.
	 *
	 * @throws UnknownHostException if the given IP is not correct
	 */
	Client() throws UnknownHostException {
		super();

		String input = JOptionPane.showInputDialog(
				"Enter server IP address",
				"Server IP"
		);
		if (input == null) System.exit(0);
		serverAddress = InetAddress.getByName(input);

		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP -> sendData((byte) 0);
					case KeyEvent.VK_DOWN -> sendData((byte) 1);
					case KeyEvent.VK_M -> switchSoundState();
				}
			}
		});

		//Start the music and display the logo animation
		audio.start();
		panel.displayLogo();
	}

	/**
	 * Send data to the server.
	 *
	 * @param data the value to send
	 */
	void sendData(byte data) {
		try {
			DatagramSocket socket = new DatagramSocket();

			//We create a datagram
			DatagramPacket packet = new DatagramPacket(new byte[]{data}, 1, serverAddress, Server.PORT);
			//We affect the data to send and send it to the server
			System.out.println("Sending \"" + data + "\"");
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				DatagramSocket socket = new DatagramSocket();
				//We wait for the server answer
				byte[] buffer = new byte[128];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, Server.PORT);
				socket.receive(packet);
				System.err.println("Server answer : " + new String(packet.getData()));

				//We de-format the answer
				int[] cal = Arrays.stream(new String(packet.getData()).split("-"))
						.mapToInt(Integer::parseInt)
						.toArray();

				//We assign the right part to the corresponding variable
				panel.updatePlayer1(cal[0], cal[4]);
				panel.updatePlayer2(cal[1], cal[5]);
				panel.updateBall(cal[2], cal[3]);

				panel.repaint();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		try {
			Thread client = new Thread(new Client());
			client.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}