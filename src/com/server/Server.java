package com.server;

import java.io.IOException;
import java.net.*;
import javax.swing.*;

public class Server extends JFrame{
	private int yP1 =285, yP2 =285, posXb=700, posYb=355, vYb=6, vXb=7, score1, score2, currentPlayerCount;
	private static String str;
	private static float xSpeedCoefficient =1, ySpeedCoefficient =1;

	private static int port1, port2;
	private static InetAddress player1, player2;

	private DatagramSocket server;

	private Server() {
		this.setVisible(true);
		this.setTitle("Server");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			player1 = InetAddress.getByName("0.0.0.0");
			player2 = InetAddress.getByName("0.0.0.0");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		//Creation of the server side connexion while specifying which port to use
		try {
			server = new DatagramSocket(9876);
			run();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private void moveBall() {
		while (true) {
			try {
				synchronized (this) {
					wait(30);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(currentPlayerCount == 2) {
				//Speed control
				if(vXb < 0) posXb += vXb - xSpeedCoefficient;
				if(vXb >= 0) posXb += vXb + xSpeedCoefficient;
				if(vYb < 0) posYb += vYb - (Math.random() * (4 + 1)) - ySpeedCoefficient;
				if(vYb >= 0) posYb += vYb + (Math.random() *(4 + 1)) + ySpeedCoefficient;

				//If Ball touch Stick from Player 1
				if (posXb <= 45 && posYb+25 >= yP1 && posYb+25 <= yP1 + 180) {
					vXb = -vXb;
					posXb += 10;
				}
				//If Ball touch Stick from Player 2
				if (posXb >= 1390 && posYb+25 >= yP2 && posYb+25 <= yP2 + 180) {
					vXb = -vXb;
					posXb -= 10;
				}
				//Score control
				if (posXb <= 5) {
					score2 += 1;
					xSpeedCoefficient = 1; ySpeedCoefficient = 1;
					posXb = 700; posYb = 355;
					vXb = -vXb;
				} else if (posXb >= 1430) {
					score1 += 1;
					xSpeedCoefficient = 1; ySpeedCoefficient = 1;
					posXb = 700; posYb = 355;
					vXb = -vXb;
				}
				//High and low limits
				if(posYb <= 5) {
					vYb = -vYb;
					posYb = 15;
				}
				if(posYb >= 715) {
					vYb = -vYb;
					posYb = 705;
				}

				str = yP1 + "-" + yP2 + "-" + posXb + "-" + posYb + "-" + score1 + "-" + score2 + "-" + 0;
				xSpeedCoefficient *= 1.002;
				ySpeedCoefficient *= 1.002;
			}
		}
	}

	private void receiveInputs() {
		byte[] buffer = new byte[128];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		while(true) {
			//Get client information (blocking)
			try {
				server.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			//Creating Player1 / Player2
			if (currentPlayerCount == 0) {
				player1 = packet.getAddress();
				port1 = packet.getPort();
				currentPlayerCount++;
				System.out.println("Set player 1 to : " + (packet.getAddress()) + " || players = " + currentPlayerCount);
			} else if (currentPlayerCount == 1 && !packet.getAddress().equals(player1)) {
				player2 = packet.getAddress();
				port2 = packet.getPort();
				currentPlayerCount++;
				System.out.println("Set player 2 to : " + (packet.getAddress()) + " || players = " + currentPlayerCount);
			}

			//Usage of user input
			String[] splitString = new String(packet.getData()).split("-");
			int mv = Integer.parseInt(splitString[0]);

			if (player1.equals(packet.getAddress())) {
				switch (mv) {
					case 0 -> yP1 -= 10;
					case 1 -> yP1 += 10;
				}

				if (yP1 > 575) yP1 = 575;
				else if (yP1 < 10) yP1 = 10;

				port1 = packet.getPort();
			} else if (player2.equals(packet.getAddress())) {
				switch (mv) {
					case 0 -> yP2 -= 10;
					case 1 -> yP2 += 10;
				}

				if (yP2 > 575) yP2 = 575;
				else if (yP2 < 10) yP2 = 10;

				port2 = packet.getPort();
			}
		}
	}

	private void answerPlayers() {
		while(true){
			try {
				synchronized (this) {
					wait(30);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			str = yP1 + "-" + yP2 + "-" + posXb + "-" + posYb + "-" + score1 + "-" + score2 + "-" + 0;
			byte[] buffer2 = (str).getBytes();

			//Réponse joueur 1
			if(currentPlayerCount == 1 || currentPlayerCount == 2) {
				DatagramPacket packet2 = new DatagramPacket(
						buffer2,             //Les donnees
						buffer2.length,      //La taille des donnees
						player1,             //L'adresse de l'émetteur1
						port1     //Le port de l'émetteur
				);
				try {
					server.send(packet2);
					System.out.println("Transmis : " + str + " à " + player1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//Réponse joueur 2
			if(currentPlayerCount == 2) {
				DatagramPacket packet3 = new DatagramPacket(
						buffer2,             //Les donnees
						buffer2.length,      //La taille des donnees
						player2,             //L'adresse de l'émetteur2
						port2    //Le port de l'émetteur
				);
				try { server.send(packet3);
					System.out.println("Transmis : " + str + " à " + player2);
				} catch (IOException e) { e.printStackTrace(); }
			}
		}
	}

	public void run() {
		//Thread for ball position and calculations
		new Thread(this::moveBall).start();
		//Thread for users input
		new Thread(this::receiveInputs).start();
		//Thread to respond to players
		new Thread(this::answerPlayers).start();
	}

	public static void main(String[] args) {
		new Server();
	}
}