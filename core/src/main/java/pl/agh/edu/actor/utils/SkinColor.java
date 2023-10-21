package pl.agh.edu.actor.utils;

import com.badlogic.gdx.graphics.Color;

import pl.agh.edu.actor.GameSkin;

class SkinColor {
	public final Color color;
	public final String colorName;

	SkinColor(ColorCategory colorCategory, ColorLevel colorLevel) {
		if (colorCategory == ColorCategory.TRANSPARENT || colorCategory == ColorCategory.SHADOW)
			colorName = colorCategory.value;
		else
			colorName = colorCategory.value + colorLevel.value;
		color = GameSkin.getInstance().getColor(colorName);
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
