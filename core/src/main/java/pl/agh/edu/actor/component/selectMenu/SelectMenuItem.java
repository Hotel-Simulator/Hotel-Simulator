package pl.agh.edu.actor.component.selectMenu;

import java.util.function.Supplier;

import pl.agh.edu.config.GraphicConfig;

public abstract class SelectMenuItem {
	protected final Supplier<String> getStringFunction;
	public final String name;

	public SelectMenuItem(String name, Supplier<String> getStringFunction) {
		this.name = name;
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
