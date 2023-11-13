package pl.agh.edu.ui.audio;

import static pl.agh.edu.engine.time.Frequency.EVERY_PART_OF_DAY;

import com.badlogic.gdx.audio.Music;

import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;

public class MusicController {
	private static RepeatingTimeCommand repeatingTimeCommand;
	private Music musicTrack;

	public MusicController(HotelType hotelType) {
		this.musicTrack = MusicTrack.getTrack(hotelType);

		musicTrack.setVolume(AudioConfig.getMusicVolume());
		musicTrack.setLooping(true);
		musicTrack.play();

		Runnable playNextTrack = () -> {
			musicTrack.stop();
			musicTrack = MusicTrack.getTrack(hotelType);
			musicTrack.setVolume(AudioConfig.getMusicVolume());
			musicTrack.setLooping(true);
			musicTrack.play();
		};
		repeatingTimeCommand = new RepeatingTimeCommand(EVERY_PART_OF_DAY, playNextTrack, Time.getInstance().getNextPartOfDayTime());
		TimeCommandExecutor.getInstance().addCommand(repeatingTimeCommand);
	}

	public MusicController() {
		this.musicTrack = MusicTrack.getBackGroundMusic();
		musicTrack.setVolume(AudioConfig.getMusicVolume());
		musicTrack.setLooping(true);
		musicTrack.play();
	}

	public void stopBackgroundMusic() {
		musicTrack.dispose();
		repeatingTimeCommand.stop();
	}

	public void updateMusicVolume() {
		musicTrack.setVolume(AudioConfig.getMusicVolume());
	}

}
