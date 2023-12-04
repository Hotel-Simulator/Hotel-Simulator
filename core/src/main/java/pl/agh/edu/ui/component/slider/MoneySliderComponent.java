package pl.agh.edu.ui.component.slider;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.MathContext.DECIMAL128;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Function;

import ch.obermuhlner.math.big.BigDecimalMath;
import pl.agh.edu.ui.language.LanguageManager;
import pl.agh.edu.utils.CustomBigDecimal;
import pl.agh.edu.utils.LanguageString;

public class MoneySliderComponent extends SliderComponent {
	private static final MathContext fine = DECIMAL128;
	private final BigDecimal maxMoneyValue;
	private final BigDecimal minMoneyValue;
	private final Function<BigDecimal, Void> stateChangeHandler;

	public MoneySliderComponent(LanguageString languageString, BigDecimal minValue, BigDecimal maxValue, Function<BigDecimal, Void> stateChangeHandler, BigDecimal startValue) {
		super(languageString, new LanguageString("common.dollar"), logarithmicMapping(CustomBigDecimal.getMinValue(minValue)), logarithmicMapping(CustomBigDecimal.getMaxValue(maxValue)), 0.001f,stateChangeHandler);
		this.maxMoneyValue = CustomBigDecimal.getMaxValue(maxValue);
		this.minMoneyValue = CustomBigDecimal.getMinValue(minValue);
		this.stateChangeHandler = stateChangeHandler;
		this.setValue(logarithmicMapping(startValue));
		valueLabel.setText(getSliderValue() + " " + LanguageManager.get(suffix));
	}

	private static float logarithmicMapping(BigDecimal value) {
		return BigDecimalMath.log10(value.add(ONE), fine).floatValue();
	}

	private BigDecimal reverseLogarithmicMapping(float valueFromSlider) {
		if (isSliderAtMax(valueFromSlider)) {
			return maxMoneyValue;
		}
		if (isSliderAtMin(valueFromSlider)) {
			return minMoneyValue;
		}
		return calculateMappedValue(valueFromSlider);
	}

	private boolean isSliderAtMax(float valueFromSlider) {
		return this.getMaxValue() == valueFromSlider;
	}

	private boolean isSliderAtMin(float valueFromSlider) {
		return this.getMinValue() == valueFromSlider;
	}

	private BigDecimal calculateMappedValue(float valueFromSlider) {
		BigDecimal exponent = BigDecimal.valueOf(valueFromSlider);
		return BigDecimalMath.pow(TEN, exponent, fine).subtract(ONE);
	}

	public CustomBigDecimal getSliderValue() {
		return new CustomBigDecimal(reverseLogarithmicMapping(this.getValue()));
	}

	public void setValue(BigDecimal value) {
		super.setValue(logarithmicMapping(value));
	}

	@Override
	protected void stateChangeHandler() {
		stateChangeHandler.apply(getSliderValue().getValue());
		valueLabel.setText(getSliderValue() + " " + LanguageManager.get(suffix));
	}
}
