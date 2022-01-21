package com.client;

import com.Ball;
import com.Player;
import com.server.Server;

import java.awt.*;
import javax.swing.JPanel;

public class Panel extends JPanel {
    //Defining base position of components
    private static final Font font = new Font("Courier", Font.BOLD, 40);

    private final Player[] players = new Player[2];
    private final Ball ball = new Ball();

    private int posXLog = 830, posYLog = 500;
    private final Color lightYellow = new Color(226, 222, 50);
    boolean logo = true;

    Panel() {
        players[0] = new Player();
        players[1] = new Player();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (logo) {
            super.paintComponent(g);
            Image image = getToolkit().getImage("data/loup.png");
            if (image != null) g.drawImage(image, 600, 350, this);
            g.setColor(lightYellow);
            g.fillOval(posXLog, posYLog, 25, 25);
        } else if (Client.joue) {
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
        }
        if ((players[0].score >= Server.MAX_SCORE) || (players[1].score >= Server.MAX_SCORE)) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setFont(font);
            g.setColor(Color.black);
            g.drawString("Congratulation, you played well!", 455, 375);
            g.drawString(players[0].score + " : " + players[1].score, 680, 30);
        }
    }

    void setPosYJ(int posYJ) {
        this.players[0].y = Math.max(posYJ, 0);
    }

    void setXBall(int xBall) {
        this.ball.x = xBall;
    }

    void setYBall(int yBall) {
        this.ball.y = yBall;
    }

    void setPosYA(int posYA) {
        this.players[1].y = posYA;
    }

    void setScoreJ(int scoreJ) {
        this.players[0].score = scoreJ;
    }

    void setScoreA(int scoreA) {
        this.players[1].score = scoreA;
    }

    int getPosXLog() {
        return posXLog;
    }

    void setPosXLog(int posXLog) {
        this.posXLog = posXLog;
    }

    int getPosYLog() {
        return posYLog;
    }

    void setPosYLog(int posYLog) {
        this.posYLog = posYLog;
    }
}