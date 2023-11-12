package pl.agh.edu.ui.audio;

import static pl.agh.edu.engine.time.Frequency.EVERY_PART_OF_DAY;

import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.engine.time.command.SerializableRunnable;

public class MusicController {
	private static RepeatingTimeCommand repeatingTimeCommand;
	private final Time time = Time.getInstance();
	private MusicTrack musicTrack = MusicTrack.parse(Time.getInstance().getPartOfDay());

	public MusicController() {
		musicTrack.music.setVolume(AudioConfig.getMusicVolume());
		musicTrack.music.setLooping(true);
		musicTrack.music.play();
		repeatingTimeCommand = new RepeatingTimeCommand(EVERY_PART_OF_DAY, (SerializableRunnable)this::playNextTrack, time.getNextPartOfDayTime());
		TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
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
