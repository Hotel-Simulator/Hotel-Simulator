package pl.agh.edu.actor.slider;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ch.obermuhlner.math.big.BigDecimalMath;
import pl.agh.edu.utils.CustomBigDecimal;

public class MoneySliderComponent extends SliderComponent {
	private CustomBigDecimal A = new CustomBigDecimal(1);
	private BigDecimal a;
	private BigDecimal c;
	private final MathContext coarse = MathContext.DECIMAL64;
	private final MathContext fine = MathContext.DECIMAL128;
	private static final float sliderMax = 200f;
//	private BigDecimal base;
	private BigDecimal base = new BigDecimal("1.2");

	public MoneySliderComponent(String name, BigDecimal min, BigDecimal max, SliderSize sliderSize) {
		super(name, "$", 10f, MoneySliderComponent.sliderMax, 1f, sliderSize);
		setA(min, max);
		setField();
	}

	@Override
	protected void setField() {
		valueLabel.setText(logarithmicMapping(slider.getValue()) + suffix);
	}

	public CustomBigDecimal logarithmicMapping(float valueFromSlider) {

//		BigDecimal linear = c.add(BigDecimal.valueOf(valueFromSlider).multiply(m, coarse));

		CustomBigDecimal result = new CustomBigDecimal(a.multiply(BigDecimalMath.pow(base, Math.round(valueFromSlider), coarse)).add(c).setScale(1, RoundingMode.HALF_UP)).roundToStringValue();

		return result;
	}

	public void setA(BigDecimal min, BigDecimal max) {
//		BigDecimal a = min.multiply(BigDecimalMath.pow(BigDecimal.valueOf(MoneySliderComponent.base), Math.round(MoneySliderComponent.width), fine), fine);
//		BigDecimal b = max.multiply(BigDecimalMath.pow(BigDecimal.valueOf(MoneySliderComponent.base), BigDecimal.valueOf(1), fine), fine);
//		BigDecimal ff = BigDecimalMath.pow(BigDecimal.valueOf(MoneySliderComponent.base), Math.round(MoneySliderComponent.width + 1), fine);
//		BigDecimal tmp = a.subtract(b).divide(ff, fine);
//		m = tmp.divide(BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(MoneySliderComponent.width)), fine);
//		c = min.divide(BigDecimal.valueOf(MoneySliderComponent.base), fine).subtract(m.multiply(BigDecimal.valueOf(1)));
//		System.out.println("m = " + m);
//		System.out.println("c = " + c);

		base =BigDecimal.ONE.add(BigDecimalMath.log10(min,fine).add(BigDecimal.ONE).divide(BigDecimal.valueOf(200),fine));
//		base = max.multiply(BigDecimal.valueOf(2f)).subtract(min).divide(min.subtract(BigDecimal.ONE));
		System.out.println("base = " + base);

		a = (max.subtract(min)).divide(BigDecimalMath.pow(base,BigDecimal.valueOf(MoneySliderComponent.sliderMax),fine).subtract(BigDecimalMath.pow(base, BigDecimal.valueOf(10f), fine)),fine);
		c = min.subtract(a.multiply(BigDecimalMath.pow(base, BigDecimal.valueOf(10f), fine),fine));
		System.out.println("a = " + a);
		System.out.println("c = " + c);



//		base = BigDecimalMath.root(max.divide(min),BigDecimal.valueOf(MoneySliderComponent.sliderMax -1f),fine).floatValue();
	}

}
