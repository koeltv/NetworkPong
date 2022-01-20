package com.server;

import com.Player;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Server extends JFrame{
	public static final int PORT = 9876;

	private int currentPlayerCount;

	private final ServerBall ball = new ServerBall();

	private final ServerPlayer[] players = new ServerPlayer[2];

	private DatagramSocket socket;

	private Server() {
		try {
			this.setTitle("Server");
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JLabel label = new JLabel(String.valueOf(InetAddress.getLocalHost()));
			this.getContentPane().add(label);

			//Creation of the server side connexion while specifying which port to use
			socket = new DatagramSocket(PORT);
			run();

			this.pack();
			this.setVisible(true);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
			dispose();
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

			ball.move();

			if(currentPlayerCount == 2) {
				//If Ball touch Stick from Player 1
				if (ball.x <= 45 && ball.y + ball.radius >= players[0].y && ball.y + ball.radius <= players[0].y + Player.HEIGHT) {
					ball.xSpeed *= -1;
					ball.x += 10;
				//If Ball touch Stick from Player 2
				} else if (ball.x >= 1390 && ball.y + ball.radius >= players[1].y && ball.y + ball.radius <= players[1].y + Player.HEIGHT) {
					ball.xSpeed *= -1;
					ball.x -= 10;
				}

				//Score control
				if (ball.x <= 5) {
					players[1].score++;
					ball.reset();
				} else if (ball.x >= 1430) {
					players[0].score++;
					ball.reset();
				}
				//High and low limits
				if(ball.y <= 5) {
					ball.ySpeed = -ball.ySpeed;
					ball.y = 15;
				}
				if(ball.y >= 715) {
					ball.ySpeed = -ball.ySpeed;
					ball.y = 705;
				}

				updateClients();
			}
		}
	}

	private void receiveInputs() {
		byte[] buffer = new byte[1];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

		while(true) {
			//Get client information (blocking)
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			//Creating Player1 / Player2
			if (currentPlayerCount == 0 || (currentPlayerCount == 1 && !players[1].isAddressIdentical(packet.getAddress()))) {
				players[currentPlayerCount++] = new ServerPlayer(packet.getAddress(), packet.getPort());
				System.out.println("Set player " + currentPlayerCount + " to : " + packet.getAddress() + " || players = " + currentPlayerCount);
			}

			//Usage of user input
			int mouvement = Integer.parseInt(new String(packet.getData()));

			ServerPlayer player = Arrays.stream(players).filter(player3 -> player3.isAddressIdentical(packet.getAddress())).findAny().orElse(null);
			if (player != null) {
				switch (mouvement) {
					case 0 -> player.y -= 10;
					case 1 -> player.y += 10;
				}

				if (player.y > 575) player.y = 575;
				else if (player.y < 10) player.y = 10;

				player.port = packet.getPort();
				updateClients();
			}
		}
	}

	private void updateClients() {
		String str = players[0].y + "-" + players[1].y + "-" + ball.x + "-" + ball.y + "-" + players[0].score + "-" + players[1].score + "-" + 0;
		byte[] buffer = (str).getBytes();

		for (ServerPlayer player : players) {
			DatagramPacket packet = new DatagramPacket(
					buffer,
					buffer.length,
					player.address,
					player.port
			);

			try {
				socket.send(packet);
				System.out.println("Sent : " + str + " to " + player.address);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void run() {
		//Thread for ball position and calculations
		new Thread(this::moveBall).start();
		//Thread for users input
		new Thread(this::receiveInputs).start();
	}

	public static void main(String[] args) {
		new Server();
	}
}