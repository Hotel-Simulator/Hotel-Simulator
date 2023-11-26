package pl.agh.edu.ui.component.selectMenu;

import java.util.function.Supplier;

public abstract class SelectMenuItem {
	public final String name;
	protected final Supplier<String> getStringFunction;

	public SelectMenuItem(String name, Supplier<String> getStringFunction) {
		this.name = name;
		this.getStringFunction = getStringFunction;
	}

	@Override
	public String toString() {
		int maxLength = 16;
		return (getStringFunction.get().length() <= maxLength) ? getStringFunction.get() : getStringFunction.get().substring(0, maxLength) + "...";
	}
}
