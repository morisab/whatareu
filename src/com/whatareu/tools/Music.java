package com.whatareu.tools;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Music {

    private static Clip clip;

    public static void playBackgroundMusic(String musicPath, int fadeInDurationMillis) {
        try {
            File musicFile = new File(musicPath);
            URL url = musicFile.toURI().toURL();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Set volume to 0 initially
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-80.0f);

            clip.start();

            // Fade in
            fadeIn(fadeInDurationMillis);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private static void fadeIn(int durationMillis) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float startVolume = -80.0f;
        float targetVolume = 0.0f;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + durationMillis) {
            float fraction = (float) (System.currentTimeMillis() - startTime) / durationMillis;
            float volume = startVolume + fraction * (targetVolume - startVolume);
            gainControl.setValue(volume);
        }
        // Ensure the volume is set to the target at the end
        gainControl.setValue(targetVolume);
    }

    public static void stopBackgroundMusic(int fadeOutDurationMillis) {
        if (clip != null && clip.isRunning()) {
            // Fade out
            fadeOut(fadeOutDurationMillis);

            clip.stop();
            clip.close();
        }
    }

    private static void fadeOut(int durationMillis) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float startVolume = gainControl.getValue();
        float targetVolume = -80.0f;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < startTime + durationMillis) {
            float fraction = (float) (System.currentTimeMillis() - startTime) / durationMillis;
            float volume = startVolume + fraction * (targetVolume - startVolume);
            gainControl.setValue(volume);
        }
        // Ensure the volume is set to the target at the end
        gainControl.setValue(targetVolume);
    }
}
