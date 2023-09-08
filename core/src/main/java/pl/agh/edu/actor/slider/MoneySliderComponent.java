package pl.agh.edu.actor.slider;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.obermuhlner.math.big.BigDecimalMath;
import pl.agh.edu.utils.CustomBigDecimal;

public class MoneySliderComponent extends SliderComponent {
	private BigDecimal a;
	private BigDecimal c;
	private final MathContext coarse = MathContext.DECIMAL64;
	private final MathContext fine = MathContext.DECIMAL128;
	private static final float sliderMax = 200f;
	private BigDecimal base = new BigDecimal("1.2");

	public MoneySliderComponent(String name, BigDecimal min, BigDecimal max, SliderSize sliderSize) {
		super(name, "$", 1f, MoneySliderComponent.sliderMax, 1f, sliderSize);
		setA(min, max);
		setField();
	}

	@Override
	protected void setField() {
		valueLabel.setText(logarithmicMapping(slider.getValue()) + suffix);
	}

	public CustomBigDecimal logarithmicMapping(float valueFromSlider) {
		System.out.println("");
		CustomBigDecimal result = new CustomBigDecimal(a.multiply(BigDecimalMath.pow(base, Math.round(valueFromSlider), coarse)).add(c).setScale(1, RoundingMode.HALF_UP))
				.roundToStringValue();
		System.out.println("result = " + result);
		System.out.println("");
		return result;
	}

	public void setA(BigDecimal min, BigDecimal max) {

		base = BigDecimal.ONE.add(BigDecimalMath.log10(min, fine).add(BigDecimal.ONE).divide(BigDecimal.valueOf(200), fine));

		a = (max.subtract(min)).divide(BigDecimalMath.pow(base, BigDecimal.valueOf(MoneySliderComponent.sliderMax), fine).subtract(BigDecimalMath.pow(base, BigDecimal.valueOf(1f),
				fine)), fine);
		c = min.subtract(a.multiply(BigDecimalMath.pow(base, BigDecimal.valueOf(1f), fine), fine));
	}

}
