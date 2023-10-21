package pl.agh.edu.actor.utils;

import com.badlogic.gdx.graphics.Color;

import pl.agh.edu.actor.GameSkin;

public enum SkinColor {
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
		return GameSkin.getInstance().getColor(this.getName(level));
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
