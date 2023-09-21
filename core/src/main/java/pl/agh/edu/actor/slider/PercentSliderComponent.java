package pl.agh.edu.actor.slider;

import java.util.function.Function;

public class PercentSliderComponent extends SliderComponent {
	private final Function<Float, Void> stateChangeHandler;

	public PercentSliderComponent(String name, Function<Float, Void> stateChangeHandler) {
		super(name, "%", 0f, 100f, 1f);
		this.stateChangeHandler = stateChangeHandler;
		valueLabel.setText(getPercentageValue() + " " + suffix);
	}

	private float getPercentageValue() {
		return this.getValue();
	}

	@Override
	protected void stateChangeHandler() {
		stateChangeHandler.apply(getPercentageValue());
		valueLabel.setText(getPercentageValue() + " " + suffix);
	}

}
