package pl.agh.edu.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import pl.agh.edu.enums.PartOfDay;

public enum MusicTrack {
	CITY_DAY("audio/music/city_day.wav"),
	CITY_NIGHT("audio/music/city_night.wav");

	public final Music music;

	MusicTrack(String path) {
		this.music = Gdx.audio.newMusic(Gdx.files.internal(path));
	}

	public static MusicTrack parse(PartOfDay partOfDay) {
		return switch (partOfDay) {
			case MORNING -> CITY_DAY;
			case DAY -> CITY_DAY;
			case EVENING -> CITY_NIGHT;
			case NIGHT -> CITY_NIGHT;
		};
	}
}
