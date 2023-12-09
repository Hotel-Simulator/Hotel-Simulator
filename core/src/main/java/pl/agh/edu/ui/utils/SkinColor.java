package pl.agh.edu.ui.utils;

import com.badlogic.gdx.graphics.Color;

public enum SkinColor implements GameSkinProvider {
	PRIMARY("primary"),
	SECONDARY("secondary"),
	ALERT("alert"),
	SUCCESS("success"),
	GRAY("gray"),
	WARNING("warning");

	final String name;

	SkinColor(String name) {
		this.name = name;
	}

	public Color getColor(ColorLevel level) {
		return getGameSkin().getColor(this.getName(level));
	}

	public String getName(ColorLevel level) {
		return name + "-" + level.value;
	}

	public enum ColorLevel {
		_000("000"),
		_100("100"),
		_300("300"),
		_500("500"),
		_700("700"),
		_900("900");

		public final String value;

		ColorLevel(String value) {
			this.value = value;
		}
	}
}
