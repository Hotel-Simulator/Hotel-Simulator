package pl.agh.edu.ui.utils;

import com.badlogic.gdx.graphics.Color;

public enum SkinSpecialColor implements GameSkinProvider {
	TRANSPARENT("transparent"),
	SHADOW("shadow");

	final String name;

	SkinSpecialColor(String name) {
		this.name = name;
	}

	public Color getColor() {
		return getGameSkin().getColor(this.getName());
	}

	public String getName() {
		return name;
	}
}
