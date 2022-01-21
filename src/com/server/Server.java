package com.server;

import com.Player;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * The type Server.
 */
public class Server extends JFrame{
	/**
	 * The port used by the server.
	 */
	public static final int PORT = 9876;

	/**
	 * The score required to win.
	 */
	public static final int MAX_SCORE = 10;

	/**
	 * The current player count.
	 */
	private int playerCount;

	/**
	 * The Ball.
	 */
	private final ServerBall ball = new ServerBall();

	/**
	 * The Players.
	 */
	private final ServerPlayer[] players = new ServerPlayer[2];

	/**
	 * The Socket.
	 */
	private final DatagramSocket socket;

	/**
	 * Instantiates a new Server.
	 *
	 * @throws IOException if the port couldn't be used
	 */
	private Server() throws IOException {
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
	}

	/**
	 * Move ball.
	 */
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

			if(playerCount == 2) {
				//If Ball touch Stick from Player
				if (ball.x <= 45 && ball.y + ball.radius >= players[0].y && ball.y + ball.radius <= players[0].y + Player.HEIGHT) {
					ball.xSpeed *= -1;
					ball.x += 10;
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

	/**
	 * Receive inputs.
	 */
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
			if (playerCount == 0 || (playerCount == 1 && !players[1].isAddressIdentical(packet.getAddress()))) {
				players[playerCount++] = new ServerPlayer(packet.getAddress(), packet.getPort());
				System.out.println("Set player " + playerCount + " to : " + packet.getAddress() + " || players = " + playerCount);

				//Thread for ball position and calculations
				if (playerCount == 2) new Thread(this::moveBall).start();
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

	/**
	 * Update clients.
	 */
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

	/**
	 * Start listening for user inputs.
	 */
	public void run() {
		new Thread(this::receiveInputs).start();
	}

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		try {
			new Server();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}