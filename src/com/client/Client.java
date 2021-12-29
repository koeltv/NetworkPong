package com.client;

import java.net.*;
import java.io.*;

public class Client {
	private static final Window fen = new Window();
	private static String move = "2";
	public final static boolean joue = true;

	//Function to simplify writing text in the console
	private static synchronized void println(String str) {
		System.err.println(str);
	}

	//Function to verify if the key is pressed
	static void sendData(String value) {
		move = value;
	}

	//Function to stop the sound
	static void switchSoundState(){
		fen.switchSoundState();
	}

	//fonction qui permet d'envoyer les informations et de les recevoir
	public static class UDPClient implements Runnable {
		private static final int port = 9876;

		@Override
		public void run() {
			try {
				DatagramSocket client = new DatagramSocket();
				while (!Thread.interrupted()) {
					//définition de la valeur à envoyer
					String envoi = move;
					byte[] buffer;
					String example = envoi + "-1";
					buffer = example.getBytes();
					sendData("2");

					//We create a datagram
					InetAddress adresse = InetAddress.getByName("localhost");
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresse, port);
					//We affect the data to send and send it to the server
					packet.setData(buffer);
					client.send(packet);

					if(joue) {
						//We wait for the server answer
						byte[] buffer2 = new byte[128];
						DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, adresse, port);
						client.receive(packet2);
						println("Action : " + envoi + " || Server answer : " + new String(packet2.getData()));

						//We de-format the answer
						String message= new String(packet2.getData());
						String[] cal = message.split("-");

						//We assign the right part to the corresponding variable
						int yJ = Integer.parseInt(cal[0]);
						int yA = Integer.parseInt(cal[1]);
						int xBall = Integer.parseInt(cal[2]);
						int yBall = Integer.parseInt(cal[3]);
						int scoreJ = Integer.parseInt(cal[4]);
						int scoreA = Integer.parseInt(cal[5]);
						fen.getPanneau().setPosYJ(yJ);
						fen.getPanneau().setXBall(xBall);
						fen.getPanneau().setYBall(yBall);
						fen.getPanneau().setPosYA(yA);
						fen.getPanneau().setScoreJ(scoreJ);
						fen.getPanneau().setScoreA(scoreA);
					}
					fen.getPanneau().repaint();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Thread client = new Thread(new UDPClient());
		client.start();
	}
}