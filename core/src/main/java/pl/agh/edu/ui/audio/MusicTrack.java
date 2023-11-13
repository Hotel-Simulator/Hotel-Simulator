package pl.agh.edu.ui.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.time.Time;

public enum MusicTrack {
	CITY("audio/music/track/city-morning.wav", "audio/music/track/city-day.wav", "audio/music/track/city-evening.wav", "audio/music/track/city-night.wav"),
	RESORT("audio/music/track/resort-morning.wav", "audio/music/track/resort-day.wav", "audio/music/track/resort-evening.wav", "audio/music/track/resort-night.wav"),
	SANATORIUM("audio/music/track/sanatorium-morning.wav", "audio/music/track/sanatorium-day.wav", "audio/music/track/sanatorium-evening.wav", "audio/music/track/sanatorium-night.wav");

	public final Music morningMusic;
	public final Music dayMusic;
	public final Music eveningMusic;
	public final Music nightMusic;

	MusicTrack(String morningMusicPath, String dayMusicPath, String eveningMusicPath, String nightMusicPath) {
		this.morningMusic = getTrack(morningMusicPath);
		this.dayMusic = getTrack(dayMusicPath);
		this.eveningMusic = getTrack(eveningMusicPath);
		this.nightMusic = getTrack(nightMusicPath);
	}

	private Music getTrack() {
		return switch (Time.getInstance().getPartOfDay()) {
			case MORNING -> this.morningMusic;
			case DAY -> this.dayMusic;
			case EVENING -> this.eveningMusic;
			case NIGHT -> this.nightMusic;
		};
	}

	private static Music getTrack(String musicPath) {
		return Gdx.audio.newMusic(Gdx.files.internal(musicPath));
	}

	public static Music getTrack(HotelType hotelType) {
		return switch (hotelType) {
			case CITY -> CITY.getTrack();
			case RESORT -> RESORT.getTrack();
			case SANATORIUM -> SANATORIUM.getTrack();
		};
	}

	public static Music getBackGroundMusic() {
		return getTrack("audio/music/track/menu.wav");
	}
}
