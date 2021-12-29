package com.client;

import java.awt.*;
import javax.swing.JPanel;

public class Panneau extends JPanel {
    //Defining base position of components
    final Font font = new Font("Courier", Font.BOLD, 40);
    private int posYJ = 287, xBall = 700, yBall = 375, posYA = 287, scoreJ = 0, scoreA = 0, posXLog = 830, posYLog = 500;
    private final Color lightYellow = new Color(226, 222, 50);
    boolean logo = true;

    @Override
    public void paintComponent(Graphics g) {
        int scoreMax = 10;

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
            g.setColor(Color.black);
            g.fillRect(20, posYJ, 25, 180);
            g.fillRect(1440, posYA, 25, 180);
            g.setColor(Color.red);
            g.fillOval(xBall, yBall, 50, 50);
            g.setFont(font);
            g.setColor(Color.black);

            g.drawString(scoreJ + " : " + scoreA, 680, 30);
        }
        if ((scoreA >= scoreMax) || (scoreJ >= scoreMax)) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setFont(font);
            g.setColor(Color.black);
            g.drawString("Bravo, vous avez bien joué!", 455, 375);
            g.drawString(scoreJ + " : " + scoreA, 680, 30);
        }
    }

    void setPosYJ(int posYJ) {
        this.posYJ = Math.max(posYJ, 0);
    }

    void setXBall(int xBall) {
        this.xBall = xBall;
    }

    void setYBall(int yBall) {
        this.yBall = yBall;
    }

    void setPosYA(int posYA) {
        this.posYA = posYA;
    }

    void setScoreJ(int scoreJ) {
        this.scoreJ = scoreJ;
    }

    void setScoreA(int scoreA) {
        this.scoreA = scoreA;
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