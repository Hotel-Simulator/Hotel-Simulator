package pl.agh.edu.ui.component.slider;

import java.util.function.Function;

import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.utils.LanguageString;

public class PercentSliderComponent extends SliderComponent {


	public PercentSliderComponent(LanguageString languageString, Function<Float, Void> stateChangeHandler) {
		super(languageString, new LanguageString("common.percent"), 0f, 100f, 1f,stateChangeHandler);
		valueLabel.setText(getValue() + " " + LanguageManager.get(suffix));
	}

	private Float getPercentageValue() {
		return this.getValue() / 100f;
	}

	@Override
	protected void stateChangeHandler() {
		stateChangeHandler.apply(getPercentageValue());
		valueLabel.setText(getValue() + " " + LanguageManager.get(suffix));
	}

	@Override
	public void setValue(float value) {
		super.setValue(value * 100f);
	}

}
