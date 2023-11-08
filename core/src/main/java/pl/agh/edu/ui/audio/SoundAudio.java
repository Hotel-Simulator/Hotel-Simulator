package pl.agh.edu.ui.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import pl.agh.edu.config.AudioConfig;
import pl.agh.edu.utils.RandomUtils;

public enum SoundAudio {
	BLOCK("audio/sound/track/block1.wav", "audio/sound/track/block2.wav", "audio/sound/track/block3.wav"),
	BOMB("audio/sound/track/bomb1.wav", "audio/sound/track/bomb2.wav", "audio/sound/track/bomb3.wav"),
	BUMP("audio/sound/track/bump1.wav", "audio/sound/track/bump2.wav", "audio/sound/track/bump3.wav"),
	CLICK("audio/sound/track/click1.wav", "audio/sound/track/click2.wav", "audio/sound/track/click3.wav"),
	DING("audio/sound/track/ding1.wav", "audio/sound/track/ding2.wav", "audio/sound/track/ding3.wav"),
	FIZ("audio/sound/track/fiz1.wav", "audio/sound/track/fiz2.wav", "audio/sound/track/fiz3.wav"),
	PING("audio/sound/track/ping1.wav", "audio/sound/track/ping2.wav", "audio/sound/track/ping3.wav"),
	PIP("audio/sound/track/pip1.wav", "audio/sound/track/pip2.wav", "audio/sound/track/pip3.wav"),
	PONG("audio/sound/track/pong1.wav", "audio/sound/track/pong2.wav", "audio/sound/track/pong3.wav"),
	POP("audio/sound/track/pop1.wav", "audio/sound/track/pop2.wav", "audio/sound/track/pop3.wav"),
	RIPPLE("audio/sound/track/ripple1.wav", "audio/sound/track/ripple2.wav", "audio/sound/track/ripple3.wav"),
	TIK("audio/sound/track/tik1.wav", "audio/sound/track/tik2.wav", "audio/sound/track/tik3.wav");

	private final Sound lowSound;
	private final Sound mediumSound;
	private final Sound highSound;

	SoundAudio(String pathLowSound, String pathMediumSound, String pathHighSound) {
		this.lowSound = Gdx.audio.newSound(Gdx.files.internal(pathLowSound));
		this.mediumSound = Gdx.audio.newSound(Gdx.files.internal(pathMediumSound));
		this.highSound = Gdx.audio.newSound(Gdx.files.internal(pathHighSound));
	}

	private Sound getRandomSound() {
		return getSound(RandomUtils.randomEnumElement(SoundType.class));
	}

	public void playSound() {
		play(getRandomSound());
	}

	public void playSound(SoundType soundType) {
		play(getSound(soundType));
	}

	private Sound getSound(SoundType soundType) {
		return switch (soundType) {
			case LOW -> lowSound;
			case MEDIUM -> mediumSound;
			case HIGH -> highSound;
		};
	}

	private void play(Sound sound) {
		sound.setVolume(sound.play(), AudioConfig.getAudioVolume());
	}

	public enum SoundType {
		LOW,
		MEDIUM,
		HIGH
	}
}
