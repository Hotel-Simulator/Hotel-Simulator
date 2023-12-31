package pl.agh.edu.ui.component.selection;

import java.util.List;
import java.util.function.Consumer;

import pl.agh.edu.ui.component.label.CustomLabel;

public abstract class BaseInfinitySelection<T> extends BaseSelection<T> {
	private final List<T> values;
	private int index = 0;

	public BaseInfinitySelection(List<T> values, CustomLabel label, Consumer<T> action) {
		super(values.get(0), label, action);
		if (values.isEmpty())
			throw new IllegalArgumentException("Values cannot be empty");
		this.values = values;
	}

	protected void nextButtonHandler() {
		index = this.getIndex(index + 1);
		setValue(values.get(index));
	}

	protected void previousButtonHandler() {
		index = this.getIndex(index - 1);
		setValue(values.get(index));
	}

	private int getIndex(int index) {
		return (index + values.size()) % values.size();
	}
}
