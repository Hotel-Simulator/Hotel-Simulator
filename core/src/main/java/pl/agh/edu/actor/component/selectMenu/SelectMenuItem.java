package pl.agh.edu.actor.component.selectMenu;

import pl.agh.edu.config.GraphicConfig;

import java.util.function.Supplier;

public abstract class SelectMenuItem {
	protected final Supplier<String> getStringFunction;

	public SelectMenuItem(Supplier<String> getStringFunction) {
		this.getStringFunction = getStringFunction;
	}

	@Override
	public String toString() {
		int maxLength = switch (GraphicConfig.getResolution().SIZE) {
			case SMALL -> 12;
			case MEDIUM -> 15;
			case LARGE -> 18;
		};
		return (getStringFunction.get().length() <= maxLength) ? getStringFunction.get() : getStringFunction.get().substring(0, maxLength) + "...";
	}
}
