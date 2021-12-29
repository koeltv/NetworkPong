package com.client;

import javax.swing.*;

class Window extends JFrame {
    private final Panneau pan = new Panneau();
    private Audio son = new Audio();

    Window() {
        this.addKeyListener(new KeyListenerTester());
        this.setVisible(true);
        this.setTitle("PONG");
        this.setResizable(false);
        this.setSize(1500, 800);
        this.setLocationRelativeTo(null);
        this.setContentPane(pan);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Print the logo on the screen
        pan.logo = true;

        //Start the music
        son.start();
        logo();
    }

    private void logo(){
        int x = pan.getPosXLog(), y = pan.getPosYLog();
        while (y < 501) {
            if (x > 780) {
                x-=1;
                y-=4;
            } else if (x > 630) {
                x-=4;
            } else {
                x-=1;
                y+=4;
            }
            pan.setPosXLog(x);
            pan.setPosYLog(y);
            pan.repaint();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pan.logo = false;
        pan.repaint();
    }

    Panneau getPanneau() {
        return pan;
    }

    //Function to stop the sound
    public void switchSoundState() { // TODO Replace stop() by interrupt()
        if (son == null || !son.isAlive()) {
            son = new Audio();
            son.start();
        } else if (!son.isInterrupted()) {
            son.stop();
            son = null;
        }
    }
}