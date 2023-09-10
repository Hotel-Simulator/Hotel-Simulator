package pl.agh.edu.actor.component.selectMenu;

import pl.agh.edu.actor.utils.Size;

public abstract class SelectMenuItem {
	protected final Size size;
	protected final String text;

	public SelectMenuItem(String text, Size size) {
		this.size = size;
		this.text = text;
	}

	@Override
	public String toString() {
		int maxLength = switch (this.size) {
		case SMALL -> 12;
		case MEDIUM -> 15;
		case LARGE -> 18;
		};
		return (text.length() <= maxLength) ? text : text.substring(0, maxLength) + "...";
	}
}
