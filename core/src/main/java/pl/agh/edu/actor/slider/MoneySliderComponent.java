package pl.agh.edu.actor.slider;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.obermuhlner.math.big.BigDecimalMath;
import pl.agh.edu.actor.utils.Size;
import pl.agh.edu.utils.CustomBigDecimal;

public class MoneySliderComponent extends SliderComponent {
	private BigDecimal coefficient;
	private BigDecimal offset;
	private final MathContext coarse = MathContext.DECIMAL64;
	private final MathContext fine = MathContext.DECIMAL128;
	private static final float sliderMax = 200f;
	private final BigDecimal base;

	public MoneySliderComponent(String name, BigDecimal minValue, BigDecimal maxValue, Size size) {
		super(name, "$", 1f, MoneySliderComponent.sliderMax, 1f, size);
		base = BigDecimal.ONE.add(BigDecimalMath.log10(minValue, fine).add(BigDecimal.ONE).divide(BigDecimal.valueOf(200), fine));
		setup(minValue, maxValue);
		setField();
	}

	@Override
	protected void setField() {
		valueLabel.setText(logarithmicMapping(slider.getValue()) + suffix);
	}

	public CustomBigDecimal logarithmicMapping(float valueFromSlider) {
		return new CustomBigDecimal(coefficient.multiply(BigDecimalMath.pow(base, Math.round(valueFromSlider), coarse)).add(offset).setScale(1, RoundingMode.HALF_UP))
				.roundToStringValue();
	}

	public void setup(BigDecimal minValue, BigDecimal maxValue) {
		coefficient = (maxValue.subtract(minValue)).divide(BigDecimalMath.pow(base, BigDecimal.valueOf(MoneySliderComponent.sliderMax), fine).subtract(BigDecimalMath.pow(base, BigDecimal.ONE,
				fine)), fine);
		offset = minValue.subtract(coefficient.multiply(BigDecimalMath.pow(base, BigDecimal.ONE, fine), fine));
	}

}
