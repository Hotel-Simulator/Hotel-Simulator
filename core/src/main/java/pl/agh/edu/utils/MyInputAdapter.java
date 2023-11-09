package pl.agh.edu.utils;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.config.LanguageConfig;
import pl.agh.edu.ui.language.Language;
import pl.agh.edu.ui.resolution.Resolution;

public class MyInputAdapter extends InputMultiplexer {

	public MyInputAdapter(Stage stage) {
		super(stage);
	}

	@Override
	public boolean keyDown(int keycode) {
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.NUM_1) {
			GraphicConfig.changeResolution(Resolution._1366x768);
		}
		if (keycode == Input.Keys.NUM_2) {
			GraphicConfig.changeResolution(Resolution._1920x1080);
		}
		if (keycode == Input.Keys.NUM_3) {
			GraphicConfig.changeResolution(Resolution._3440x1440);
		}
		if (keycode == Input.Keys.NUM_8) {
			GraphicConfig.changeResolution(Resolution._1440x900);
		}
		if (keycode == Input.Keys.NUM_9) {
			GraphicConfig.changeResolution(Resolution._1600x900);
		}
		if (keycode == Input.Keys.T) {
			GraphicConfig.changeResolution(Resolution._2560x1080);
		}
		if (keycode == Input.Keys.U) {
			GraphicConfig.changeResolution(Resolution._2560x1440);
		}
		if (keycode == Input.Keys.I) {
			GraphicConfig.changeResolution(Resolution._3840x2160);
		}
		if (keycode == Input.Keys.O) {
			GraphicConfig.changeResolution(Resolution._2560x1600);
		}
		if (keycode == Input.Keys.NUM_4) {
			GraphicConfig.setFullscreenMode(true);
		}
		if (keycode == Input.Keys.NUM_5) {
			GraphicConfig.setFullscreenMode(false);
		}
		if (keycode == Input.Keys.NUM_6) {
			LanguageConfig.setLanguage(Language.Polish);
		}
		if (keycode == Input.Keys.NUM_7) {
			LanguageConfig.setLanguage(Language.English);
		}
		return false;
	}
}
