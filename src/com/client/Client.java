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

public class Client extends Window implements Runnable {
	public static final boolean play = true;

	private final InetAddress serverAddress;

	//Function to verify if the key is pressed
	void sendData(String value) {
		try {
			DatagramSocket socket = new DatagramSocket();
			byte[] buffer = value.getBytes();

			//We create a datagram
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, Server.PORT);
			//We affect the data to send and send it to the server
			System.out.println("Sending \"" + value + "\"");
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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
					case KeyEvent.VK_UP -> sendData("0");
					case KeyEvent.VK_DOWN -> sendData("1");
					case KeyEvent.VK_M -> switchSoundState();
				}
			}
		});

		//Start the music and display the logo animation
		audio.start();
		displayLogo();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				if (play) {
					DatagramSocket socket = new DatagramSocket();
					//We wait for the server answer
					byte[] buffer = new byte[128];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, Server.PORT);
					socket.receive(packet);
					System.err.println("Server answer : " + new String(packet.getData()));

					//We de-format the answer
					String[] cal = new String(packet.getData()).split("-");

					//We assign the right part to the corresponding variable
					getPanneau().setPosYJ(Integer.parseInt(cal[0]));
					getPanneau().setPosYA(Integer.parseInt(cal[1]));
					getPanneau().setXBall(Integer.parseInt(cal[2]));
					getPanneau().setYBall(Integer.parseInt(cal[3]));
					getPanneau().setScoreJ(Integer.parseInt(cal[4]));
					getPanneau().setScoreA(Integer.parseInt(cal[5]));
				}
				getPanneau().repaint();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			Thread client = new Thread(new Client());
			client.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}