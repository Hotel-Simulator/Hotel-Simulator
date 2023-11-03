package pl.agh.edu.ui.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import pl.agh.edu.config.AudioConfig;

public enum SoundAudio {
	CLICK_1("audio/sound/click1.wav"),
	CLICK_2("audio/sound/click2.wav"),
	BUTTON_1("audio/sound/button1.wav"),
	BUTTON_2("audio/sound/button2.wav"),
	BUTTON_3("audio/sound/button3.wav"),
	KNOCK_1("audio/sound/knock1.wav"),
	PIP_1("audio/sound/pip1.wav"),
	POP_1("audio/sound/pop1.wav");

	private final Sound sound;

	SoundAudio(String path) {
		this.sound = Gdx.audio.newSound(Gdx.files.internal(path));
	}

	public void playAudio() {
		sound.setVolume(sound.play(), AudioConfig.getAudioVolume());
	}

}
