/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author mnieves.domnav
 */
public class Sound {

    private Clip clip;
    private volatile boolean running, looping;

    public Sound(String location) {
        looping = running = false;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(location));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void play() {
        running = true;
        clip.setFramePosition(0);
        new Thread(new Runnable() {
            public void run() {
                clip.start();
                while (true) {
                    if (clip.getMicrosecondPosition() == clip.getMicrosecondLength()) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
            }
        }).start();
    }

    public void pause() {
        running = false;
        clip.stop();
    }

    public void resume() {
        running = true;
        new Thread(new Runnable() {
            public void run() {
                clip.start();
                while (true) {
                    if (clip.getMicrosecondPosition() == clip.getMicrosecondLength()) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
        looping = false;
        clip.setFramePosition(0);
    }

    public void loop() {
        looping = true;
        new Thread(new Runnable() {
            public void run() {
                clip.start();
                while (looping) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        }).start();
    }

    public boolean isResumed() {
        if (clip.getMicrosecondPosition() > 0) {
            return true;
        }
        return false;
    }

    public static void sfx(String location) {
        new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Sound.class.getResource(location));
                Clip effectClip = AudioSystem.getClip();
                effectClip.open(audioInputStream);
                effectClip.start();
            } catch (Exception ex) {
                Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
