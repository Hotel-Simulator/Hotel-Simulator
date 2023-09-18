package pl.agh.edu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.*;

import pl.agh.edu.enums.Language;
import pl.agh.edu.enums.Resolution;

public class GameConfig {
	private static Resolution resolution = Resolution._1920x1080;
	private static boolean fullscreenMode = false;
	private static Language language = Language.ENGLISH;
	private static long musicVolume = 100;
	private static long audioVolume = 100;
	public static final Stage stage = new Stage(new FitViewport(resolution.WIDTH, resolution.HEIGHT));

	public static void changeResolution(Resolution newResolution) {
		resolution = newResolution;
		Viewport viewport = new FitViewport(newResolution.WIDTH, newResolution.HEIGHT);
		stage.setViewport(viewport);
		if (isFullscreen()) {
			setFullscreenMode(false);
			setFullscreenMode(true);
		} else {
			setFullscreenMode(false);
		}

		viewport.update(newResolution.WIDTH, newResolution.HEIGHT, true);
		viewport.getCamera().update();
	}

	public static void setFullscreenMode(Boolean value) {
		fullscreenMode = value;
		if (isFullscreen())
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		else
			Gdx.graphics.setWindowedMode(resolution.WIDTH, resolution.HEIGHT);
	}

	public static boolean isFullscreen() {
		return fullscreenMode;
	}

	public static void setAudioVolume(long value) {
		audioVolume = value;
	}
	public static void setMusicVolume(long value) {
		musicVolume = value;
	}
	public static long getAudioVolume() {
		return audioVolume;
	}
	public static long getMusicVolume() {
		return musicVolume;
	}

	public static Resolution getResolution() {
		return resolution;
	}
}
