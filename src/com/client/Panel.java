package com.client;

import com.Ball;
import com.Player;
import com.server.Server;

import java.awt.*;
import javax.swing.JPanel;

/**
 * The type Panel.
 */
public class Panel extends JPanel {
	/**
	 * The font used for the score.
	 */
	private static final Font font = new Font("Courier", Font.BOLD, 40);

	/**
	 * The wolf image used for the logo animation.
	 */
	private final Image wolf;

	/**
	 * The 2 players.
	 */
	private final Player[] players = new Player[2];

	/**
	 * The ball.
	 */
	private final Ball ball = new Ball();

	/**
	 * The x position of the ball in the logo animation.
	 */
	private int xLogo = 830;

	/**
	 * The y position of the ball in the logo animation.
	 */
	private int yLogo = 500;

	/**
	 * The color of the ball in the logo animation.
	 */
	private final Color lightYellow = new Color(226, 222, 50);

	/**
	 * whether to display the logo animation or not.
	 */
	boolean logo = true;

	/**
	 * Instantiates a new Panel.
	 */
	Panel() {
		players[0] = new Player();
		players[1] = new Player();
		wolf = getToolkit().getImage("data/loup.png");
	}

	/**
	 * Display logo animation.
	 */
	void displayLogo() {
		int x = xLogo, y = yLogo;
		while (y < 501) {
			if (x > 780) {
				x -= 1;
				y -= 4;
			} else if (x > 630) {
				x -= 4;
			} else {
				x -= 1;
				y += 4;
			}
			xLogo = x;
			yLogo = y;
			repaint();
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logo = false;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		if (logo) {
			super.paintComponent(g);
			if (wolf != null) g.drawImage(wolf, 600, 350, this);
			g.setColor(lightYellow);
			g.fillOval(xLogo, yLogo, 25, 25);
		} else {
			super.paintComponent(g);

			g.setColor(Color.white);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			g.setColor(Color.red);
			g.fillOval(ball.x, ball.y, 50, 50);

			g.setColor(Color.black);
			g.fillRect(20, players[0].y, Player.WIDTH, Player.HEIGHT);
			g.fillRect(1440, players[1].y, Player.WIDTH, Player.HEIGHT);

			g.setFont(font);
			g.drawString(players[0].score + " : " + players[1].score, 680, 30);

			if ((players[0].score >= Server.MAX_SCORE) || (players[1].score >= Server.MAX_SCORE)) {
				g.setColor(Color.white);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setFont(font);
				g.setColor(Color.black);
				g.drawString("Congratulation, you played well!", 455, 375);
				g.drawString(players[0].score + " : " + players[1].score, 680, 30);
			}
		}
	}

	/**
	 * Update ball coordinates.
	 *
	 * @param x the x
	 * @param y the y
	 */
	void updateBall(int x, int y) {
		ball.x = x;
		ball.y = y;
	}

	/**
	 * Update player 1.
	 *
	 * @param y     the y
	 * @param score the score
	 */
	void updatePlayer1(int y, int score) {
		players[0].y = y;
		players[0].score = score;
	}

	/**
	 * Update player 2.
	 *
	 * @param y     the y
	 * @param score the score
	 */
	void updatePlayer2(int y, int score) {
		players[1].y = y;
		players[1].score = score;
	}
}