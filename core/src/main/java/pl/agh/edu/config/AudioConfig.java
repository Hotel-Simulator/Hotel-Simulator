package pl.agh.edu.config;

import pl.agh.edu.audio.AudioController;

public class AudioConfig {
    private static float musicVolume = 1.00f;
    private static float audioVolume = 1.00f;
    private static AudioController audioController;
    public static void setAudioVolume(float value) {
        audioVolume = value;
    }
    public static void setMusicVolume(float value) {
        musicVolume = value;
        audioController.updateMusicVolume();
    }
    public static float getAudioVolume() {
        return audioVolume;
    }
    public static float getMusicVolume() {
        return musicVolume;
    }

    public static void setUpAudioController() {
        audioController = new AudioController();
    }

    public static void stopAudioController() {
        audioController.stopBackgroundMusic();
    }
}
