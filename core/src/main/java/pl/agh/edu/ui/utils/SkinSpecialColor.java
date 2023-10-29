package pl.agh.edu.ui.utils;

import com.badlogic.gdx.graphics.Color;

import pl.agh.edu.ui.GameSkin;

public enum SkinSpecialColor {
	TRANSPARENT("transparent"),
	SHADOW("shadow");

	final String name;

	SkinSpecialColor(String name) {
		this.name = name;
	}

	public Color getColor() {
		return GameSkin.getInstance().getColor(this.getName());
	}

	public String getName() {
		return name;
	}
}
