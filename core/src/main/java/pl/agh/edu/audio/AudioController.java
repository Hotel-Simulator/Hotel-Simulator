package pl.agh.edu.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class AudioController {
    private Music backgroundMusic;
    private float backgroundMusicVolume;
    private float soundVolume;

    public AudioController() {
        backgroundMusicVolume = 0.5f;
        soundVolume = 0.5f;
    }

    public void playBackgroundMusic(String musicPath) {
        stopBackgroundMusic();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(musicPath));
        backgroundMusic.setVolume(backgroundMusicVolume);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }

    public void playSoundEffect(String soundPath) {
        FileHandle soundFile = Gdx.files.internal(soundPath);
        Sound sound = Gdx.audio.newSound(soundFile);
        sound.play(soundVolume);
        sound.dispose(); // Dispose of the sound after playing it
    }

    public void setBackgroundMusicVolume(float volume) {
        backgroundMusicVolume = volume;
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(volume);
        }
    }

    public void setSoundVolume(float volume) {
        soundVolume = volume;
    }
}
