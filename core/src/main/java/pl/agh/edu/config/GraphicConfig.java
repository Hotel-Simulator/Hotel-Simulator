package pl.agh.edu.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

import pl.agh.edu.actor.utils.resolution.ResolutionManager;
import pl.agh.edu.enums.Resolution;

public class GraphicConfig {
	private static Resolution resolution = Resolution._1920x1080;
	private static boolean fullscreenMode = false;
	private static final OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	private static final FitViewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

	public static void changeResolution(Resolution newResolution) {
		resolution = newResolution;

		ResolutionManager.notifyListeners();
		viewport.setWorldSize(resolution.WIDTH, resolution.HEIGHT);
		viewport.update(newResolution.WIDTH, newResolution.HEIGHT, true);
		setFullscreenMode(isFullscreen());
		ResolutionManager.notifyListeners();
	}

	public static void setFullscreenMode(Boolean value) {
		fullscreenMode = value;
		if (isFullscreen()) {
			Gdx.graphics.setWindowedMode(resolution.WIDTH, resolution.HEIGHT);
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		} else {
			Gdx.graphics.setWindowedMode(resolution.WIDTH, resolution.HEIGHT);
		}
	}

	public static boolean isFullscreen() {
		return fullscreenMode;
	}

	public static Resolution getResolution() {
		return resolution;
	}

	public static FitViewport getViewport() {
		return viewport;
	}

}
