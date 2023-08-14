package pl.agh.edu.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class HotelSkin extends Skin {
	private static HotelSkin instance;

	private HotelSkin() {
		super(Gdx.files.internal("skin/skin.json"));
	}

	public static HotelSkin getInstance() {
		if (instance == null) {
			instance = new HotelSkin();
		}
		return instance;
	}
}
