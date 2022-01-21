package com.client;

import javax.swing.*;

class Window extends JFrame {
    private final Panel panel = new Panel();
    private Audio audio = new Audio();

    Window() {
        super();
        this.setTitle("PONG");
        this.setResizable(false);
        this.setSize(1500, 800);
        this.setLocationRelativeTo(null);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //Start the music
        audio.start();
        displayLogo();
    }

    private void displayLogo(){
        int x = panel.getPosXLog(), y = panel.getPosYLog();
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
            panel.setPosXLog(x);
            panel.setPosYLog(y);
            panel.repaint();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        panel.logo = false;
        panel.repaint();
    }

    Panel getPanneau() {
        return panel;
    }

    //Function to stop/play the sound
    public void switchSoundState() { // TODO Replace stop() by interrupt()
        if (audio == null || !audio.isAlive()) {
            audio = new Audio();
            audio.start();
        } else if (!audio.isInterrupted()) {
            audio.stop();
            audio = null;
        }
    }
}