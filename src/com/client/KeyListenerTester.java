package com.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListenerTester implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> Client.sendData("0");
            case KeyEvent.VK_DOWN -> Client.sendData("1");
            case KeyEvent.VK_M -> Client.switchSoundState();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}