package pl.agh.edu.actor.component.selectMenu;

import pl.agh.edu.config.GraphicConfig;

public abstract class SelectMenuItem {
	protected final String text;

	public SelectMenuItem(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		int maxLength = switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> 12;
			case MEDIUM -> 15;
			case LARGE -> 18;
		};
		return (text.length() <= maxLength) ? text : text.substring(0, maxLength) + "...";
	}
}
