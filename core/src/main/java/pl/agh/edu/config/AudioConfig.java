package pl.agh.edu.config;

import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.ui.audio.MusicController;

public class AudioConfig {
	private static float musicVolume = 0.00f;
	private static float audioVolume = 0.50f;
	private static MusicController musicController = new MusicController(HotelType.CITY);

	public static float getAudioVolume() {
		return audioVolume;
	}

	public static void setAudioVolume(float value) {
		audioVolume = value;
	}

	public static float getMusicVolume() {
		return musicVolume;
	}

	public static void setMusicVolume(float value) {
		musicVolume = value;
		musicController.updateMusicVolume();
	}

	public static void setUpAudioController(HotelType hotelType) {
		musicController = new MusicController(hotelType);
	}

	public static void stopAudioController() {
		musicController.stopBackgroundMusic();
	}
}
