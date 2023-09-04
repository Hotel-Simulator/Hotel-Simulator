package pl.agh.edu.actor.slider;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.obermuhlner.math.big.BigDecimalMath;
import pl.agh.edu.utils.CustomBigDecimal;

public class MoneySliderComponent extends SliderComponent {
	private CustomBigDecimal A = new CustomBigDecimal(1);
	private BigDecimal m;
	private BigDecimal c;
	private final MathContext mc = new MathContext(1000, RoundingMode.HALF_UP);
	private final MathContext init = new MathContext(10000, RoundingMode.HALF_UP);
	private static final float width = 100f;
	private static final float base = 1.08f;

	public MoneySliderComponent(String name, BigDecimal min, BigDecimal max, SliderSize sliderSize) {
		super(name, "$", 1f, MoneySliderComponent.width, 1f, sliderSize);
		setA(min, max);
		setField();
	}

	@Override
	protected void setField() {
		valueLabel.setText(logarithmicMapping(slider.getValue()) + suffix);
	}

	public CustomBigDecimal logarithmicMapping(float valueFromSlider) {

		BigDecimal linear = c.add(BigDecimal.valueOf(valueFromSlider).multiply(m, init));

		CustomBigDecimal result = new CustomBigDecimal(linear.multiply(BigDecimalMath.pow(BigDecimal.valueOf(MoneySliderComponent.base), BigDecimal.valueOf(valueFromSlider), mc))
				.setScale(1, RoundingMode.HALF_UP)).roundToStringValue();

		return result;
	}

	public void setA(BigDecimal min, BigDecimal max) {
		BigDecimal a = min.multiply(BigDecimalMath.pow(BigDecimal.valueOf(MoneySliderComponent.base), BigDecimal.valueOf(MoneySliderComponent.width), init), init);
		BigDecimal b = max.multiply(BigDecimalMath.pow(BigDecimal.valueOf(MoneySliderComponent.base), BigDecimal.valueOf(1), init), init);
		BigDecimal ff = BigDecimalMath.pow(BigDecimal.valueOf(MoneySliderComponent.base), BigDecimal.valueOf(MoneySliderComponent.width + 1), init);
		BigDecimal tmp = a.subtract(b).divide(ff, init);
		m = tmp.divide(BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(MoneySliderComponent.width)), init);
		c = min.divide(BigDecimal.valueOf(MoneySliderComponent.base), init).subtract(m.multiply(BigDecimal.valueOf(1)));

	}

}
