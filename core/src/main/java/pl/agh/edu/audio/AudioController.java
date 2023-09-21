package pl.agh.edu.audio;

import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.enums.Frequency;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.RepeatingTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class AudioController {
    private MusicTrack musicTrack = MusicTrack.CITY_DAY;
    private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
    private final Time time = Time.getInstance();
    private static RepeatingTimeCommand repeatingTimeCommand;
    public AudioController() {
        musicTrack.music.setVolume(AudioConfig.getMusicVolume());
        musicTrack.music.setLooping(true);
        musicTrack.music.play();
        repeatingTimeCommand = new RepeatingTimeCommand(Frequency.EVERY_PART_OF_DAY, this::playNextTrack,time.getStartOfTheDayPart());
        timeCommandExecutor.addCommand(repeatingTimeCommand);
    }
    public void stopBackgroundMusic() {
        musicTrack.music.stop();
        repeatingTimeCommand.stop();
    }
    public void updateMusicVolume() {
        System.out.println(AudioConfig.getMusicVolume());
        musicTrack.music.setVolume(AudioConfig.getMusicVolume()/100f);
    }
    public void playNextTrack() {
        musicTrack.music.stop();
        musicTrack = MusicTrack.parse(time.getPartOfDay());
        musicTrack.music.setVolume(AudioConfig.getMusicVolume());
        musicTrack.music.setLooping(true);
        musicTrack.music.play();
    }
}
