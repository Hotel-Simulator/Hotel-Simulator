package pl.agh.edu.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import pl.agh.edu.actor.utils.ResolutionManager;
import pl.agh.edu.enums.Resolution;

public class GraphicConfig {
	private static Resolution resolution = Resolution._1920x1080;
	private static boolean fullscreenMode = false;
	public static final Stage stage = new Stage();

	public static void changeResolution(Resolution newResolution) {
		resolution = newResolution;
		ResolutionManager.notifyListeners();
		Viewport viewport = new FitViewport(newResolution.WIDTH, newResolution.HEIGHT);
		stage.setViewport(viewport);

		setFullscreenMode(isFullscreen());
	}

	public static void setFullscreenMode(Boolean value) {
		fullscreenMode = value;
		if (isFullscreen()) {
			Gdx.graphics.setWindowedMode(resolution.WIDTH, resolution.HEIGHT);
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		else{
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			Gdx.graphics.setWindowedMode(resolution.WIDTH, resolution.HEIGHT);
		}
	}

	public static boolean isFullscreen() {
		return fullscreenMode;
	}

	public static Resolution getResolution() {
		return resolution;
	}
}
