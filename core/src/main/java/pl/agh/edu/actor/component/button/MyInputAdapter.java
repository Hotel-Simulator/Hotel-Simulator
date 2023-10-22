package pl.agh.edu.actor.component.button;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;

import pl.agh.edu.config.GraphicConfig;
import pl.agh.edu.enums.Resolution;

public class MyInputAdapter extends InputMultiplexer {
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
		if (keycode == Input.Keys.NUM_4) {
			GraphicConfig.setFullscreenMode(true);
		}
		if (keycode == Input.Keys.NUM_5) {
			GraphicConfig.setFullscreenMode(false);
		}
		return false;
	}
}
