package pl.agh.edu.actor.component.selectMenu;

import pl.agh.edu.actor.utils.Size;

public abstract class SelectMenuItem {
	private final Size size;

	protected SelectMenuItem(Size size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return truncatedToString();
	}

	public String truncatedToString() {
		int maxLength = switch (this.size) {
		case SMALL -> 10;
		case MEDIUM -> 20;
		case LARGE -> 30;
		};
		String fullString = super.toString();
		return (fullString.length() <= maxLength) ? fullString : fullString.substring(0, maxLength) + "...";
	}
}
