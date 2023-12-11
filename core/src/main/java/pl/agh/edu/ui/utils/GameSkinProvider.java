package pl.agh.edu.ui.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import pl.agh.edu.ui.GameSkin;

public interface GameSkinProvider {
	default Skin getGameSkin() {
		return GameSkin.getInstance();
	}
}
