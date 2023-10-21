package pl.agh.edu.actor.utils;

import com.badlogic.gdx.graphics.Color;

import pl.agh.edu.actor.GameSkin;

class SkinColor {
	private Color color;

	SkinColor(ColorCategory colorCategory, ColorLevel colorLevel) {
		if (colorCategory == ColorCategory.TRANSPARENT || colorCategory == ColorCategory.SHADOW)
			color = GameSkin.getInstance().getColor(colorCategory.value);
		else
			color = GameSkin.getInstance().getColor(colorCategory.value + colorLevel.value);
	}

	enum ColorCategory {
		PRIMARY("primary"),
		SECONDARY("secondary"),
		ALERT("alert"),
		SUCCESS("success"),
		GRAY("gray"),
		WARNING("warning"),
		TRANSPARENT("transparent"),
		SHADOW("shadow");

		String value;

		ColorCategory(String s) {
			value = s;
		}
	}

	enum ColorLevel {
		_000("-000"),
		_100("-100"),
		_300("-300"),
		_500("-500"),
		_700("-700"),
		_900("-900");

		public final String value;

		ColorLevel(String s) {
			value = s;
		}
	}
}
