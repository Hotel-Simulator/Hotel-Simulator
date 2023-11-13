package pl.agh.edu.ui.utils;

public enum SkinFont {
	BODY1("body1"),
	BODY2("body2"),
	BODY3("body3"),
	BUTTON1("button1"),
	BUTTON2("button2"),
	BUTTON3("button3"),
	CONSOLE("console"),
	H1("h1"),
	H2("h2"),
	H3("h3"),
	H4("h4"),
	SUBTITLE1("subtitle1"),
	SUBTITLE2("subtitle2"),
	SUBTITLE3("subtitle3");

	private final String name;

	SkinFont(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getWhiteVariantName() {
		return "white-" + name;
	}
}
