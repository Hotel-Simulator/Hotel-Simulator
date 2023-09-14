package pl.agh.edu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.*;

import pl.agh.edu.enums.Language;
import pl.agh.edu.enums.Resolution;

public class GameConfig {
	public static Resolution RESOLUTION = Resolution._1920x1080;
	private static boolean fullscreenMode = false;
	public static Language LANGUAGE = Language.ENGLISH;
	public static final Stage stage = new Stage(new FitViewport(RESOLUTION.WIDTH, RESOLUTION.HEIGHT));

	public static void changeResolution(Resolution resolution) {
		RESOLUTION = resolution;
		Viewport viewport = new FitViewport(resolution.WIDTH, resolution.HEIGHT);
		stage.setViewport(viewport);
		if (isFullscreen()) {
			setFullscreenMode(false);
			setFullscreenMode(true);
		} else {
			setFullscreenMode(false);
		}

		viewport.update(resolution.WIDTH, resolution.HEIGHT, true);
		viewport.getCamera().update();
	}

	public static void setFullscreenMode(Boolean value) {
		fullscreenMode = value;
		if (isFullscreen())
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		else
			Gdx.graphics.setWindowedMode(RESOLUTION.WIDTH, RESOLUTION.HEIGHT);
	}

	public static boolean isFullscreen() {
		return fullscreenMode;
	}

}
