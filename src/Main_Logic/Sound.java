package Main_Logic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {
    private Clip clip;

    public void playSound(String soundFilePath) {
        try {
            // Load the sound file (WAV format)
            File soundFile = new File(soundFilePath); // Replace with your sound file path
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            // Get a clip to play the sound
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Play the sound
            clip.start();

            // Wait until the clip finishes playing
            while (clip.isRunning()) {
                // Busy-wait until the sound finishes
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
