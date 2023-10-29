package pl.agh.edu.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameSkin extends Skin {
	private static GameSkin instance;

	private GameSkin() {
		super(Gdx.files.internal("skin/skin.json"));
	}

	public static GameSkin getInstance() {
		if (instance == null) {
			instance = new GameSkin();
		}
		return instance;
	}
}
