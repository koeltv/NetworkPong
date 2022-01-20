package com.client;

import com.server.Server;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client extends Window implements Runnable {
	public static final boolean joue = true;

	private final InetAddress localhost;

	//Function to verify if the key is pressed
	void sendData(String value) {
		try {
			DatagramSocket client = new DatagramSocket();
			byte[] buffer = value.getBytes();

			//We create a datagram
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, localhost, Server.PORT);
			//We affect the data to send and send it to the server
			System.out.println("Sending \"" + value + "\"");
			client.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	Client() {
		super();

		InetAddress localhost1;
		try {
			localhost1 = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			localhost1 = null;
			e.printStackTrace();
		}
		localhost = localhost1;

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
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				if (joue) {
					DatagramSocket client = new DatagramSocket();
					//We wait for the server answer
					byte[] buffer2 = new byte[128];
					DatagramPacket packet = new DatagramPacket(buffer2, buffer2.length, localhost, Server.PORT);
					client.receive(packet);
					System.err.println("Server answer : " + new String(packet.getData()));

					//We de-format the answer
					String message= new String(packet.getData());
					String[] cal = message.split("-");

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
		Thread client = new Thread(new Client());
		client.start();
	}
}