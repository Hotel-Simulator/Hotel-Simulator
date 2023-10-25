package pl.agh.edu.ui.component.slider;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.MathContext.DECIMAL128;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Function;

import ch.obermuhlner.math.big.BigDecimalMath;
import pl.agh.edu.utils.CustomBigDecimal;

public class MoneySliderComponent extends SliderComponent {
	private static final MathContext fine = DECIMAL128;

	private final BigDecimal maxMoneyValue;
	private final BigDecimal minMoneyValue;
	private final Function<BigDecimal, Void> stateChangeHandler;

	public MoneySliderComponent(String name, BigDecimal minValue, BigDecimal maxValue, Function<BigDecimal, Void> stateChangeHandler) {
		super(name, "$", logarithmicMapping(CustomBigDecimal.getMinValue(minValue)), logarithmicMapping(CustomBigDecimal.getMaxValue(maxValue)), 0.01f);
		this.maxMoneyValue = CustomBigDecimal.getMaxValue(maxValue);
		this.minMoneyValue = CustomBigDecimal.getMinValue(minValue);
		this.stateChangeHandler = stateChangeHandler;
		valueLabel.setText(getSliderValue() + " " + suffix);
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

	private CustomBigDecimal getSliderValue() {
		return new CustomBigDecimal(reverseLogarithmicMapping(this.getValue()));
	}

	@Override
	protected void stateChangeHandler() {
		stateChangeHandler.apply(getSliderValue().getValue());
		valueLabel.setText(getSliderValue() + " " + suffix);
	}
}