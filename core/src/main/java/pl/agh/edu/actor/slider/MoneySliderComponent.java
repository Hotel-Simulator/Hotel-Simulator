package pl.agh.edu.actor.slider;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigDecimalMath;
import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.utils.CustomBigDecimal;

public class MoneySliderComponent extends SliderComponent {
	private final static MathContext fine = MathContext.DECIMAL128;

	private final BigDecimal maxMoneyValue;
	private final BigDecimal minMoneyValue;

	public MoneySliderComponent(String name, BigDecimal minValue, BigDecimal maxValue, Size size) {
		super(name, "$", logarithmicMapping(CustomBigDecimal.getMinValue(minValue)), logarithmicMapping(CustomBigDecimal.getMaxValue(maxValue)), 0.01f, size);
		maxMoneyValue = CustomBigDecimal.getMaxValue(maxValue);
		minMoneyValue = CustomBigDecimal.getMinValue(minValue);
		setField();
	}
	private static float logarithmicMapping(BigDecimal value) {
		return BigDecimalMath.log10(value.add(BigDecimal.ONE), fine).floatValue();
	}

	private BigDecimal reverseLogarithmicMapping(float valueFromSlider) {
		if(this.getMaxValue() == valueFromSlider)
			return maxMoneyValue;
		if(this.getMinValue() == valueFromSlider)
			return minMoneyValue;
		return BigDecimalMath.pow(BigDecimal.TEN, BigDecimal.valueOf(valueFromSlider), fine).subtract(BigDecimal.ONE);
	}

	private CustomBigDecimal getSliderValue() {
		return new CustomBigDecimal(reverseLogarithmicMapping(this.getValue()));
	}


	@Override
	protected void setField() {
		valueLabel.setText(getSliderValue() + " " + suffix);
	}
}
