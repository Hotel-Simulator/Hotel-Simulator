package pl.agh.edu.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;

import ch.obermuhlner.math.big.BigDecimalMath;

public class CustomBigDecimal {
	private final BigDecimal value;
	private final Prefix prefix;
	private static final MathContext MC = MathContext.DECIMAL128;
	public CustomBigDecimal(BigDecimal bigDecimal) {
		this.prefix = Prefix.getAdequatePrefixByValue(bigDecimal);
		this.value = roundValue(bigDecimal,prefix);
	}
	@Override
	public String toString() {
		System.out.println(value);
		return value.toPlainString() + " " + prefix.code;
	}

	private BigDecimal roundValue(BigDecimal value,Prefix prefix) {
		if(prefix == Prefix.n) return value.setScale(0,RoundingMode.FLOOR);
		BigDecimal valueWithOffsetPrecision = value.movePointLeft(BigDecimalMath.log10(prefix.value,MC).intValue() - 3).setScale(0,RoundingMode.FLOOR);
		int scale = 5 - BigDecimalMath.log10(valueWithOffsetPrecision,MC).intValue();
		return valueWithOffsetPrecision.movePointLeft(3).setScale(scale,RoundingMode.FLOOR);
	}
	public BigDecimal getValue() {
		return value;
	}

	public static BigDecimal getMaxValue(BigDecimal value) {
		return value.min(Prefix.EXCESS.value.subtract(BigDecimal.ONE));
	}
	public static BigDecimal getMinValue(BigDecimal value) {
		return value.max(BigDecimal.ZERO);
	}

	public enum Prefix {
		n(new BigDecimal("1"),""),
		k(new BigDecimal("1E3"),"k"),
		M(new BigDecimal("1E6"),"M"),
		B(new BigDecimal("1E9"),"B"),
		T(new BigDecimal("1E12"),"T"),
		Q(new BigDecimal("1E15"),"Q"),
		EXCESS(new BigDecimal("1E18"),"ERROR");
		public final BigDecimal value;
		public final String code;

		Prefix(BigDecimal value,String code) {
			this.code = code;
			this.value = value;
		}

		public static Prefix getAdequatePrefixByValue(BigDecimal value) {
			return Arrays.stream(Prefix.values())
                    .sorted()
                    .takeWhile(prefix -> value.compareTo(prefix.value) >= 0)
					.max(Comparator.naturalOrder())
					.orElse(Prefix.n);
		}
	}
}
