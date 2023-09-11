package pl.agh.edu;

import com.badlogic.gdx.utils.viewport.FitViewport;

import pl.agh.edu.enums.Language;
import pl.agh.edu.enums.Resolution;

public class GameConfig {
	public static Resolution RESOLUTION = Resolution._1366x768;
	public static boolean FULLSCREEN = false;
	public static Language LANGUAGE = Language.ENGLISH;

	public static final FitViewport screenViewport = new FitViewport(RESOLUTION.getWidth(), RESOLUTION.getHeight());

	public static void changeResolution(Resolution resolution) {
		RESOLUTION = resolution;
		screenViewport.setScreenSize(resolution.getWidth(), resolution.getHeight());
	}

}
