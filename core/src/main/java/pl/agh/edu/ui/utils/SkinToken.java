package pl.agh.edu.ui.utils;

public enum SkinToken {
	EASE("{EASE}"),
	HANG("{HANG}"),
	JUMP("{JUMP}"),
	SHAKE("{ENDSHAKE}"),
	SICK("{SICK}"),
	SLIDE("{SLIDE}"),
	WAVE("{WAVE}"),
	WIND("{WIND}"),
	BLINK("{BLINK}"),
	FADE("{FADE}"),
	GRADIENT("{GRADIENT}"),
	RAINBOW("{RAINBOW}");

	private final String name;

	SkinToken(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
