package pl.agh.edu.ui.component.slider;

import java.util.function.Function;

import pl.agh.edu.utils.LanguageString;

public class PercentSliderComponent extends SliderComponent {
	private final Function<Float, Void> stateChangeHandler;

	public PercentSliderComponent(LanguageString languageString, Function<Float, Void> stateChangeHandler) {
		super(languageString, "%", 0f, 100f, 1f);
		this.stateChangeHandler = stateChangeHandler;
		valueLabel.setText(getPercentageValue() + " " + suffix);
	}

	private float getPercentageValue() {
		return this.getValue() / 100f;
	}

	@Override
	protected void stateChangeHandler() {
		stateChangeHandler.apply(getPercentageValue());
		valueLabel.setText(getValue() + " " + suffix);
	}

	@Override
	public void setValue(float value) {
		super.setValue(value * 100f);
	}

}
