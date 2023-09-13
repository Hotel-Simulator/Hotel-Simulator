package pl.agh.edu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import pl.agh.edu.enums.Language;
import pl.agh.edu.enums.Resolution;

public class GameConfig {
	public static Resolution RESOLUTION = Resolution._1366x768;
	public static Language LANGUAGE = Language.ENGLISH;
	public static final Stage stage = new Stage(new FitViewport(RESOLUTION.WIDTH, RESOLUTION.HEIGHT));

	public static void changeResolution(Resolution resolution) {
		RESOLUTION = resolution;
		stage.setViewport(new FitViewport(resolution.WIDTH, resolution.HEIGHT));
		if (isFullscreen()) setFullscreenMode();
		else setWindowedMode();
	}

	public static void setFullscreenMode() {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
	}

	public static void setWindowedMode() {
		Gdx.graphics.setWindowedMode(RESOLUTION.WIDTH, RESOLUTION.HEIGHT);
	}

	public static boolean isFullscreen() {
		return Gdx.graphics.isFullscreen();
	}

}
