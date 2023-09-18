package pl.agh.edu.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public enum MusicAudio {
    CLICK("assets/audio/music/hotel.wav"),
    HOVER("assets/audio/music/city_night.wav");
    private Music music;
    MusicAudio(String path) {
        this.music = Gdx.audio.newMusic(Gdx.files.internal(path));
    }

    public void play(){
        music.play();
    }
    public Music getSound(){
        return music;
    }

}