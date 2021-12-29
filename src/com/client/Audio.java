package com.client;

import  java.io.*;
import javax.sound.sampled.*;

public class Audio extends Thread{

    @Override
    public void run(){
        while(!this.isInterrupted()) {
            File file = new File("data/pong.wav");
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(file);

                AudioFormat format = stream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                byte[] bytes = new byte[1024];
                int bytesRead;
                while ((bytesRead = stream.read(bytes, 0, bytes.length)) != -1) {
                    line.write(bytes, 0, bytesRead);
                }
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}