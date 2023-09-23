package pl.agh.edu.audio;

import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.enums.Frequency;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.RepeatingTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class MusicController {
	private MusicTrack musicTrack = MusicTrack.parse(Time.getInstance().getPartOfDay());
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final Time time = Time.getInstance();
	private static RepeatingTimeCommand repeatingTimeCommand;

	public MusicController() {
		musicTrack.music.setVolume(AudioConfig.getMusicVolume());
		musicTrack.music.setLooping(true);
		musicTrack.music.play();
		repeatingTimeCommand = new RepeatingTimeCommand(Frequency.EVERY_PART_OF_DAY, this::playNextTrack, time.getNextPartOfDayTime());
		timeCommandExecutor.addCommand(repeatingTimeCommand);
	}

	public void stopBackgroundMusic() {
		musicTrack.music.dispose();
		repeatingTimeCommand.stop();
	}

	public void updateMusicVolume() {
		musicTrack.music.setVolume(AudioConfig.getMusicVolume());
	}

	public void playNextTrack() {
		musicTrack.music.stop();
		musicTrack = MusicTrack.parse(time.getPartOfDay());
		musicTrack.music.setVolume(AudioConfig.getMusicVolume());
		musicTrack.music.setLooping(true);
		musicTrack.music.play();
	}
}
