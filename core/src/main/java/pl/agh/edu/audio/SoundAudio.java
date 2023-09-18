package pl.agh.edu.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import pl.agh.edu.GameConfig;

public enum SoundAudio {
    CLICK("audio/sound/click1.wav"),
    BUTTON("audio/sound/button1.wav");
    private final Sound sound;
    SoundAudio(String path) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal(path));
    }

    public void play(){
        sound.setVolume(sound.play(), GameConfig.getAudioVolume() / 100f);
    }

}